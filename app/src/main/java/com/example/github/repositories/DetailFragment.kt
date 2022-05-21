package com.example.github.repositories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.github.repositories.data.LocalDataStore
import com.example.github.repositories.data.OwnerDTO
import com.example.github.repositories.data.RepositoryDTO

class DetailFragment : Fragment() {
    companion object {
        private const val DATA_KEY = "DetailFragment"
        fun getInstance(repository: RepositoryDTO): DetailFragment {
            return DetailFragment().apply {
                arguments = Bundle().apply { putParcelable(DATA_KEY, repository) }
            }
        }
    }

    private var repository: RepositoryDTO? = null
    private var title: TextView? = null
    private var image: ImageView? = null
    private var detail: TextView? = null
    private var description: TextView? = null
    private var url: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initArgs()
        initViews(view)
        setDataInUI()
    }

    private fun initArgs() {
        repository = arguments?.getParcelable(DATA_KEY)
    }

    private fun initViews(view: View) {
        title = view.findViewById(R.id.title)
        image = view.findViewById(R.id.image)
        detail = view.findViewById(R.id.detail)
        description = view.findViewById(R.id.description)
        url = view.findViewById(R.id.url)
    }

    private fun setDataInUI() {
        repository?.let { repo ->
            title?.text = repo.name
            description?.text = repo.description
            url?.text = repo.html_url

            image?.let {
                it.setImageResource(
                    if (LocalDataStore.instance.getBookmarks().contains(repo))
                        R.drawable.baseline_bookmark_black_24
                    else
                        R.drawable.baseline_bookmark_border_black_24
                )
                it.setOnClickListener {
                    val isBookmarked = LocalDataStore.instance.getBookmarks().contains(repo)
                    LocalDataStore.instance.bookmarkRepo(repo, !isBookmarked)
                    image?.setImageResource(if (!isBookmarked) R.drawable.baseline_bookmark_black_24 else R.drawable.baseline_bookmark_border_black_24)
                }
            }
            detail?.let {
                it.text = getString(R.string.repo_created, repo.owner?.login, repo.created_at)
                it.setOnClickListener { repo.owner?.let { ownerDTO -> openUserFragment(ownerDTO) } }
            }
        }
    }

    private fun openUserFragment(user: OwnerDTO) {
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(android.R.id.content, UserFragment.getInstance(user))
            .addToBackStack("UserFragment")
            .commit()
    }
}