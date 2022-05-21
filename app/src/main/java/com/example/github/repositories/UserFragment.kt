package com.example.github.repositories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.github.repositories.common.loadImage
import com.example.github.repositories.common.showToast
import com.example.github.repositories.data.ApiState
import com.example.github.repositories.data.OwnerDTO
import com.example.github.repositories.data.RepositoryDTO
import com.example.github.repositories.common.visibility
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserFragment : Fragment() {

    companion object {
        private const val DATA_KEY = "DetailFragment"
        fun getInstance(user: OwnerDTO): UserFragment {
            return UserFragment().apply {
                arguments = Bundle().apply { putParcelable(DATA_KEY, user) }
            }
        }
    }

    private var repositoryAdapter: RepositoryAdapter? = null
    private val viewModel: UserViewModel by viewModels()
    private var user: OwnerDTO? = null
    private var title: TextView? = null
    private var image: ImageView? = null
    private var detail: TextView? = null
    private var url: TextView? = null
    private var list: RecyclerView? = null
    private var progress: ProgressBar? = null
    private var retry: AppCompatButton? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initArgs()
        initViews(view)
        handleViewItemClick()
        initObservers()
        fetchUserData()
        setDataInUI()
    }

    private fun initArgs() {
        user = arguments?.getParcelable(DATA_KEY)
    }

    private fun initViews(view: View) {
        title = view.findViewById(R.id.title)
        image = view.findViewById(R.id.image)
        detail = view.findViewById(R.id.detail)
        url = view.findViewById(R.id.url)
        list = view.findViewById(R.id.list)
        progress = view.findViewById(R.id.progress)
        retry = view.findViewById(R.id.btn_retry)
    }

    private fun handleViewItemClick() {
        retry?.let { button ->
            button.setOnClickListener {
                button.visibility(false)
                fetchUserData()
            }
        }
    }

    private fun initObservers() {
        viewModel.user.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ApiState.Success -> {
                    detail?.text =
                        getString(R.string.twitter_handle, state.data.twitter_username ?: "")
                }
                is ApiState.Failure -> {
                    state.failure.showToast(context)
                    handleErrorScenario()
                }
            }
        }

        viewModel.repositories.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ApiState.Success -> handleRepoList(state.data)
                is ApiState.Loading -> progress?.visibility(state.isLoading)
                is ApiState.Failure -> {
                    state.failure.showToast(context)
                    handleErrorScenario()
                }
            }

        }
    }

    private fun fetchUserData() {
        user?.let { viewModel.fetchUser(it.login) }
    }

    private fun setDataInUI() {
        user?.let { user ->
            title?.text = user.login
            user.avatar_url.let { image?.loadImage(it) }
        }
    }

    private fun handleRepoList(listRepos: List<RepositoryDTO>) {
        repositoryAdapter
            ?.let { it.updateList(listRepos) }
            ?: kotlin.run {
                repositoryAdapter = RepositoryAdapter(listRepos) { handleRepoItemClick(it) }
                list?.adapter = repositoryAdapter
            }
    }

    private fun handleRepoItemClick(item: RepositoryDTO) {
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(android.R.id.content, DetailFragment.getInstance(item))
            .addToBackStack("DetailFragment")
            .commit()
    }

    private fun handleErrorScenario() {
        retry?.visibility(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        repositoryAdapter = null
    }
}