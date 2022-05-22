package com.example.github.repositories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.github.repositories.common.loadImage
import com.example.github.repositories.common.showToast
import com.example.github.repositories.data.ApiState
import com.example.github.repositories.data.OwnerDTO
import com.example.github.repositories.data.RepositoryDTO
import com.example.github.repositories.common.visibility
import com.example.github.repositories.databinding.FragmentUserBinding
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

    private var user: OwnerDTO? = null
    private var binding: FragmentUserBinding? = null
    private val viewModel: UserViewModel by viewModels()
    private var repositoryAdapter: RepositoryAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initArgs()
        handleViewItemClick()
        initObservers()
        fetchUserData()
        setDataInUI()
    }

    private fun initArgs() {
        user = arguments?.getParcelable(DATA_KEY)
    }

    private fun handleViewItemClick() {
        binding?.btnRetry?.let { button ->
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
                    binding?.detail?.text =
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
                is ApiState.Loading -> binding?.progress?.visibility(state.isLoading)
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
            binding?.title?.text = user.login
            user.avatar_url.let { binding?.image?.loadImage(it) }
        }
    }

    private fun handleRepoList(listRepos: List<RepositoryDTO>) {
        repositoryAdapter?.updateList(listRepos)
            ?: kotlin.run {
                repositoryAdapter = RepositoryAdapter(listRepos) { handleRepoItemClick(it) }
                binding?.list?.adapter = repositoryAdapter
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
        binding?.btnRetry?.visibility(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        repositoryAdapter = null
    }
}