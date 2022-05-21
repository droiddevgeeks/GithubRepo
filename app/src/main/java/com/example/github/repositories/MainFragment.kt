package com.example.github.repositories

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.github.repositories.common.showToast
import com.example.github.repositories.common.visibility
import com.example.github.repositories.data.ApiState
import com.example.github.repositories.data.RepositoryDTO
import com.example.github.repositories.data.Response
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()
    private var swipeRefresh: SwipeRefreshLayout? = null
    private var recyclerview: RecyclerView? = null
    private var retry: AppCompatButton? = null

    private var repositoryAdapter: RepositoryAdapter? = null

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.fetchItems()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        initObservers()
        setAdapter()
        setSwipeListener()
        handleViewItemClick()
    }

    private fun handleViewItemClick() {
        retry?.let { button ->
            button.setOnClickListener {
                button.visibility(false)
                viewModel.fetchItems()
            }
        }
    }

    private fun initViews(view: View) {
        swipeRefresh = view.findViewById(R.id.swipe_refresh)
        recyclerview = view.findViewById(R.id.news_list)
        retry = view.findViewById(R.id.btn_retry)
    }

    private fun initObservers() {
        viewModel.repositories.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ApiState.Success -> handleRepoList(state.data)
                is ApiState.Loading -> swipeRefresh?.isRefreshing = state.isLoading
                is ApiState.Failure -> {
                    state.failure.showToast(context)
                    handleErrorScenario()
                }
            }
        }
    }

    private fun handleErrorScenario() {
        retry?.visibility(true)
    }

    private fun handleRepoList(response: Response) {
        val data = response.items.take(20).toMutableList()
        repositoryAdapter
            ?.let { it.updateList(data) }
            ?: kotlin.run {
                repositoryAdapter = RepositoryAdapter(data) { handleRepoItemClick(it) }
                recyclerview?.adapter = repositoryAdapter
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
        recyclerview?.let {
            it.layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setSwipeListener() {
        swipeRefresh?.setOnRefreshListener { viewModel.refresh() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        repositoryAdapter = null
    }
}