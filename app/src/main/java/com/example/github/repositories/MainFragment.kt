package com.example.github.repositories

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.github.repositories.common.showToast
import com.example.github.repositories.common.visibility
import com.example.github.repositories.data.ApiState
import com.example.github.repositories.data.RepositoryDTO
import com.example.github.repositories.data.Response
import com.example.github.repositories.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()

    private var repositoryAdapter: RepositoryAdapter? = null
    private var binding: FragmentMainBinding? = null

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.fetchItems()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        setAdapter()
        setSwipeListener()
        handleViewItemClick()
    }

    private fun handleViewItemClick() {
        binding?.btnRetry?.let { button ->
            button.setOnClickListener {
                button.visibility(false)
                viewModel.fetchItems()
            }
        }
    }

    private fun initObservers() {
        viewModel.repositories.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ApiState.Success -> handleRepoList(state.data)
                is ApiState.Loading -> binding?.swipeRefresh?.isRefreshing = state.isLoading
                is ApiState.Failure -> {
                    state.failure.showToast(context)
                    handleErrorScenario()
                }
            }
        }
    }

    private fun handleErrorScenario() {
        binding?.btnRetry?.visibility(true)
    }

    private fun handleRepoList(response: Response) {
        val data = response.items.take(20).toMutableList()
        repositoryAdapter?.updateList(data)
            ?: kotlin.run {
                repositoryAdapter = RepositoryAdapter(data) { handleRepoItemClick(it) }
                binding?.newsListRecycler?.adapter = repositoryAdapter
            }
    }

    private fun handleRepoItemClick(item: RepositoryDTO) {
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(android.R.id.content, DetailFragment.getInstance(item))
            .addToBackStack("DetailFragment")
            .commit()
    }

    private fun setAdapter() {
        binding?.newsListRecycler?.let {
            it.layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setSwipeListener() {
        binding?.swipeRefresh?.setOnRefreshListener { viewModel.refresh() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        repositoryAdapter = null
    }
}