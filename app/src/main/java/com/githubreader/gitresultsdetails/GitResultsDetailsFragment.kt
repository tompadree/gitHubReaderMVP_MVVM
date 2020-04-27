package com.githubreader.gitresultsdetails


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator

import com.githubreader.R
import com.githubreader.base.BindingFragment
import com.githubreader.data.models.RepoObject
import com.githubreader.databinding.FragmentGitResultsDetailsBinding
import com.githubreader.utils.AppConstants.Companion.REPO_OBJECT
import com.githubreader.utils.helpers.observe
import kotlinx.android.synthetic.main.fragment_git_results_details.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 */
class GitResultsDetailsFragment : BindingFragment<FragmentGitResultsDetailsBinding>() {

    override val layoutId: Int = R.layout.fragment_git_results_details

//    private val viewModel: GitResultsDetailsViewModel by viewModel()
//
//    private lateinit var gitHubResultsDetailsAdapter : GitHubResultsDetailsAdapter
//    private var emptyShimmerCheck = true
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        binding.viewModel = viewModel
//        binding.lifecycleOwner = this.viewLifecycleOwner
//
//
////        shimmerViewContainer.startShimmer()
//        setupObservers()
//        setupRv()
//        viewModel._parentRepoObject.set(arguments?.getSerializable(REPO_OBJECT) as RepoObject)
//        viewModel.refresh(true)
//    }
//
//    private fun setupObservers(){
//
//        observeError(viewModel.error)
//
//        viewModel.isDataLoadingError.observe(this) {
//            it?.let {
//                gitResultDetailsFragSwipeLayout.isEnabled = it
//                gitResultDetailsFragSwipeLayout.isRefreshing = it
//            }
//        }
//    }
//
//    private fun setupRv() {
//        gitHubResultsDetailsAdapter = GitHubResultsDetailsAdapter(viewModel)
//
////        with(repo_detail_subscribers_rv) {
////            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
////            adapter = gitHubResultsDetailsAdapter
////            (this.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
////
////            // width and height don't change
////            setHasFixedSize(true)
////
////            // Set the number of offscreen views to retain before adding them
////            // to the potentially shared recycled view pool
////            setItemViewCacheSize(100)
////        }
//    }
}

