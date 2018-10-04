package com.githubreader.view.activities.GitResults

import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.githubreader.R
import com.githubreader.data.models.OwnerObject
import com.githubreader.data.models.RepoObject
import com.githubreader.di.component.DaggerGitResultComponent
import com.githubreader.di.module.GitResultModule
import com.githubreader.domain.listeners.OnInternetConnected
import com.githubreader.presenter.GithubResultsPresenter
import com.githubreader.utils.AppConstants.Companion.REPO_ID
import com.githubreader.utils.AppConstants.Companion.REPO_NAME
import com.githubreader.utils.AppConstants.Companion.SUBSCRIBERS
import com.githubreader.utils.AppConstants.Companion.USER_NAME
import com.githubreader.utils.helpers.NetworkHelper
import com.githubreader.view.activities.BaseActivity
import com.githubreader.view.adapters.GitResultDetailsAdapter
import com.githubreader.view.views.GitResultsView
import kotlinx.android.synthetic.main.activity_git_result_details.*
import javax.inject.Inject

class GitResultDetailsActivity : BaseActivity(), GitResultsView, OnInternetConnected {

    @Inject
    lateinit var githubResultsPresenter: GithubResultsPresenter

    lateinit var internetReceiver: BroadcastReceiver
    lateinit var gitResultDetailsAdapter: GitResultDetailsAdapter
    lateinit var localSubscribers: ArrayList<OwnerObject>
    var repoName = ""
    var userName = ""
    var subscribersCt = 0
    var repoId = 0
    var lastText = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_git_result_details)

        // showLoading()

        DaggerGitResultComponent.builder()
                .appComponent(getApplicationComponent())
                .gitResultModule(GitResultModule(this))
                .build().inject(this)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        intent?.let {
            repoName = it.getStringExtra(REPO_NAME)
            if (repoName != "")
                supportActionBar?.title = repoName

            userName = it.getStringExtra(USER_NAME)
            repoId = it.getIntExtra(REPO_ID, 0)
            subscribersCt = it.getIntExtra(SUBSCRIBERS, 0)
        }
        localSubscribers = ArrayList()

        githubResultsPresenter.fetchRepoSubscribers(repoId, repoName)
    }


    override fun onResume() {
        super.onResume()

        isInternetAvailable()
    }

    override fun onPause() {
        super.onPause()

    }

    override fun onDestroy() {
        super.onDestroy()

        unregisterReceiver(internetReceiver)
        githubResultsPresenter.destroy()
    }

    override fun onBackPressed() {
        finish()
    }

    override fun showLoading() {
        repo_detail_ProgressBar?.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        repo_detail_ProgressBar?.visibility = View.GONE
    }

    override fun showLoadingFooter() {
        gitResultDetailsAdapter.addLoadingFooter()
    }

    override fun hideLoadingFooter() {
        gitResultDetailsAdapter.removeLoadingFooter()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }


    override fun repoSubscribersFetched(subscribers: ArrayList<OwnerObject>) {
        if (localSubscribers.size == 0) {
            localSubscribers = subscribers
            setupRecyclerView()
        } else {
            localSubscribers.addAll(subscribers)
            repo_detail_subscribers_rv.adapter.notifyDataSetChanged()
        }
    }

    override fun onInternetConnected() {

        if (localSubscribers.size == 0 && repo_detail_ProgressBar?.visibility != View.VISIBLE) {
            githubResultsPresenter.fetchRepoSubscribers(repoId, repoName)
            showLoading()
        }
    }

    override fun showError(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()

        hideLoading()

    }

    internal fun isInternetAvailable() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        internetReceiver = NetworkHelper.isInternetAvailable(findViewById(android.R.id.content), getString(R.string.no_internet_message), this)
        registerReceiver(internetReceiver, intentFilter)
    }

    fun setupRecyclerView() {

        repo_detail_info_layout.visibility = View.VISIBLE
        repo_detail_name_text_view.text = repoName
        repo_detail_subscribers_number.text = subscribersCt.toString()

        gitResultDetailsAdapter = GitResultDetailsAdapter(this, localSubscribers)

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        repo_detail_subscribers_rv.layoutManager = layoutManager
        repo_detail_subscribers_rv.adapter = gitResultDetailsAdapter

        repo_detail_subscribers_rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val totalItemCount = layoutManager.itemCount
                val myTotalCount = totalItemCount - 14
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (dy > 0) { //dy scrolling down
                    if ((firstVisibleItemPosition >= myTotalCount) && firstVisibleItemPosition > 0
                            && myTotalCount > 0 && localSubscribers.size <= totalItemCount)
                        githubResultsPresenter.fetchNextPage()
                }
            }
        })

        hideLoading()

    }


    override fun showRefreshLoading() {}

    override fun hideRefreshLoading() {}

    override fun updateList(repos: ArrayList<RepoObject>) {}
}