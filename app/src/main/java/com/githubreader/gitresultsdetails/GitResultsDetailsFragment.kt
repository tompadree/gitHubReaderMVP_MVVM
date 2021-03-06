package com.githubreader.gitresultsdetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.view.WindowManager
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.SimpleItemAnimator
import com.githubreader.R
import com.githubreader.base.BindingFragment
import com.githubreader.data.models.RepoObject
import com.githubreader.databinding.FragmentGitResultsDetailsBinding
import com.githubreader.utils.AppConstants.Companion.REPO_OBJECT
import com.githubreader.utils.helpers.observe
import kotlinx.android.synthetic.main.fragment_git_results_details.*
import kotlinx.android.synthetic.main.layout_repo_details.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class GitResultsDetailsFragment : BindingFragment<FragmentGitResultsDetailsBinding>() {

    override val layoutId: Int = R.layout.fragment_git_results_details

    private val viewModel: GitResultsDetailsViewModel by viewModel()

    private val args: GitResultsDetailsFragmentArgs by navArgs()

    private lateinit var gitHubResultsDetailsAdapter : GitHubResultsDetailsAdapter
    private var emptyShimmerCheck = true
    private lateinit var repoObject: RepoObject

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this.viewLifecycleOwner

        repoObject = args.repoObject

        setupToolbar()
        setupObservers()
        setupDetails()
        setupRv()
        initVM()
    }

    private fun initVM(){
        viewModel._parentRepoObject.set(repoObject)
        viewModel.refresh(true)
    }

    private fun setupToolbar() {
        gitResultDetailsToolbar.title = repoObject.repoName
        gitResultDetailsToolbar.setNavigationOnClickListener { activity?.onBackPressed() }

        // Hide keyboard
        activity?.window?.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }

    private fun setupObservers(){

        observeError(viewModel.error)
        viewModel.empty.observe(this) {

            if(it == true) {
                emptyShimmerCheck = true
                shimmerViewContainerDetails.startShimmer()
            } else if(it == false) { // && emptyShimmerCheck) {
                shimmerViewContainerDetails.stopShimmer()
                shimmerViewContainerDetails.visibility = View.GONE
                emptyShimmerCheck = false
            }
        }
    }

    private fun setupRv() {
        gitHubResultsDetailsAdapter = GitHubResultsDetailsAdapter(viewModel)

        with(repo_detail_subscribers_rv) {
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
            adapter = gitHubResultsDetailsAdapter
            (this.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

            // Set the number of offscreen views to retain before adding them
            // to the potentially shared recycled view pool
            setItemViewCacheSize(100)

        }
    }

    fun setupDetails() {

        val fmt = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        val outputTime = SimpleDateFormat("H:mm  M.d.yyyy", Locale.US)

        if (repoObject.language != null)
            layout_repo_details_tv_language.text = repoObject.language
        else
            layout_repo_details_language_layout.visibility = View.GONE

        if (repoObject.createdAt != null)
            layout_repo_details_tv_created.text = outputTime.format(fmt.parse(repoObject.createdAt))
        else
            layout_repo_details_created_layout.visibility = View.GONE

        if (repoObject.modified != null)
            layout_repo_details_tv_modified.text = outputTime.format(fmt.parse(repoObject.modified))
        else
            layout_repo_details_modified_layout.visibility = View.GONE

        layout_repo_details_tv_watchers.text = repoObject.watchers_count.toString()

        layout_repo_details_tv_forks.text = repoObject.forks_count.toString()

        layout_repo_details_tv_subscriptions.text = repoObject.subscribers_count.toString()

        if (repoObject.owner!!.userType != null)
            layout_repo_details_tv_typeUser.text = repoObject.owner!!.userType
        else
            layout_repo_details_typeUser_layout.visibility = View.GONE

        if (repoObject.owner!!.siteAdmin != null)
            layout_repo_details_tv_siteAdmin.text = repoObject.owner!!.siteAdmin
        else
            layout_repo_details_siteAdmin_layout.visibility = View.GONE

        layout_repo_details_tv_issues.text = repoObject.open_issues_count.toString()
    }
}

