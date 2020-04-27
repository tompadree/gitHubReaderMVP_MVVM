package com.githubreader.gitresults

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.githubreader.databinding.FragmentGitResultsBinding
import com.githubreader.base.BindingFragment
import com.githubreader.R
import com.githubreader.data.models.RepoObject
import com.githubreader.splash.SplashFragmentDirections
import com.githubreader.utils.helpers.observe
import kotlinx.android.synthetic.main.fragment_git_results.*


class GitResultsFragment : BindingFragment<FragmentGitResultsBinding>() {

    override val layoutId: Int = R.layout.fragment_git_results

    private val viewModel: GitResultsViewModel by viewModel()

    private lateinit var gitHubResultsAdapter : GitHubResultsAdapter
    private var emptyShimmerCheck = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this.viewLifecycleOwner


//        shimmerViewContainer.startShimmer()
        setupObservers()
        setupRv()
        viewModel.refresh(true)
    }

    private fun setupObservers(){

        observeError(viewModel.error)

        viewModel.empty.observe(this) {

            if(it == true) {
                emptyShimmerCheck = true
                shimmerViewContainer.startShimmer()
            } else if(it == false) { // && emptyShimmerCheck) {
                shimmerViewContainer.stopShimmer()
                shimmerViewContainer.visibility = View.GONE
                emptyShimmerCheck = false
            }
        }

        viewModel.isDataLoadingError.observe(this) {
            it?.let {
                gitResultFragSwipeLayout.isEnabled = it
                gitResultFragSwipeLayout.isRefreshing = it
            }
        }

        viewModel.itemClicked.observe(this){
              it?.let {  navigateToGitREsultsSubscribers(it) }
        }
    }

    private fun navigateToGitREsultsSubscribers(repoObject: RepoObject){
        val nc = NavHostFragment.findNavController(this)
        nc.navigate(GitResultsFragmentDirections.actionGitResultsFragmentToGitResultsDetailsFragment(repoObject))
    }

    private fun setupRv() {
        gitHubResultsAdapter = GitHubResultsAdapter(viewModel)

        with(gitResultFragRv) {
            layoutManager = LinearLayoutManager(context)
            adapter = gitHubResultsAdapter
            (this.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

            // width and height don't change
//            setHasFixedSize(true)

            // Set the number of offscreen views to retain before adding them
            // to the potentially shared recycled view pool
            setItemViewCacheSize(100)

                        // Scroll to first item after change
//            gitHubResultsAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
//                override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
//                    super.onItemRangeMoved(fromPosition, toPosition, itemCount)
//                    (layoutManager as LinearLayoutManager).scrollToPositionWithOffset(0, 0)
//                }
//            })
        }
    }
}


