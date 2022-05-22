package com.example.github.repositories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.github.repositories.data.LocalDataStore
import com.example.github.repositories.data.OwnerDTO
import com.example.github.repositories.data.RepositoryDTO
import com.example.github.repositories.databinding.FragmentDetailBinding

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
    private var binding: FragmentDetailBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initArgs()
        setDataInUI()
    }

    private fun initArgs() {
        repository = arguments?.getParcelable(DATA_KEY)
    }

    private fun setDataInUI() {
        repository?.let { repo ->
            binding?.let { mBinding ->
                mBinding.title.text = repo.name
                mBinding.description.text = repo.description
                mBinding.url.text = repo.html_url

                mBinding.image.let {
                    it.setImageResource(
                        if (LocalDataStore.instance.getBookmarks().contains(repo))
                            R.drawable.baseline_bookmark_black_24
                        else
                            R.drawable.baseline_bookmark_border_black_24
                    )
                    it.setOnClickListener {
                        val isBookmarked = LocalDataStore.instance.getBookmarks().contains(repo)
                        LocalDataStore.instance.bookmarkRepo(repo, !isBookmarked)
                        mBinding.image.setImageResource(if (!isBookmarked) R.drawable.baseline_bookmark_black_24 else R.drawable.baseline_bookmark_border_black_24)
                    }
                }
                mBinding.detail.let {
                    it.text = getString(R.string.repo_created, repo.owner?.login, repo.created_at)
                    it.setOnClickListener { repo.owner?.let { ownerDTO -> openUserFragment(ownerDTO) } }
                }
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

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}