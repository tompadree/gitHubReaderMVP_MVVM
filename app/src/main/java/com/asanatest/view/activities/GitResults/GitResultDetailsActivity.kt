package com.asanatest.view.activities.GitResults

import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast
import butterknife.Unbinder
import com.asanatest.R
import com.asanatest.data.models.RepoObject
import com.asanatest.di.component.DaggerGitResultComponent
import com.asanatest.di.module.GitResultModule
import com.asanatest.presenter.GithubResultsPresenter
import com.asanatest.utils.helpers.NetworkHelper
import com.asanatest.view.activities.BaseActivity
import com.asanatest.view.adapters.GitResultDetailsAdapter
import com.asanatest.view.adapters.GitResultsAdapter
import com.asanatest.view.views.GitResultsView
import kotlinx.android.synthetic.main.activity_git_result_details.*
import javax.inject.Inject

class GitResultDetailsActivity : BaseActivity(), GitResultsView {

    @Inject
    lateinit var githubResultsPresenter: GithubResultsPresenter

    lateinit var internetReceiver: BroadcastReceiver
    lateinit var gitResultDetailsAdapter: GitResultDetailsAdapter
    lateinit var localSubscribers: ArrayList<RepoObject.Owner>
    var repoName = ""
    var subscribersCt = 0
    var repoId = 0
    var lastText = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_git_result_details)

        DaggerGitResultComponent.builder()
                .appComponent(getApplicationComponent())
                .gitResultModule(GitResultModule(this))
                .build().inject(this)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        intent?.let {
            repoName = it.getStringExtra("repoName")
            repoId = it.getIntExtra("repoId", 0)
            subscribersCt = it.getIntExtra("subscribers", 0)
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

//    override fun onBackPressed() {
//
//    }

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

    override fun showRefreshLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideRefreshLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateList(repos: ArrayList<RepoObject>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun repoSubscribersFetched(subscribers: ArrayList<RepoObject.Owner>) {
        if (localSubscribers.size == 0) {
            localSubscribers = subscribers
            setupRecyclerView()
        } else {
            localSubscribers.addAll(subscribers)
            repo_detail_subscribers_rv.adapter.notifyDataSetChanged()
        }
    }


    override fun showError(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()

//        if (gitResultDetailsAdapter == null)
//            setupRecyclerView()
    }

    internal fun isInternetAvailable() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        internetReceiver = NetworkHelper.isInternetAvailable(findViewById(android.R.id.content), getString(R.string.no_internet_message))
        registerReceiver(internetReceiver, intentFilter)
    }

    fun setupRecyclerView() {

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

        hideRefreshLoading()
        hideLoading()
        //hideLoadingFooter()

    }


}
