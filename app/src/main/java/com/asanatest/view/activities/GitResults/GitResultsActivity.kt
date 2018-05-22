package com.asanatest.view.activities.GitResults

import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.Menu
import android.widget.Toast
import butterknife.Unbinder
import com.asanatest.R
import com.asanatest.data.models.RepoObject
import com.asanatest.di.component.DaggerGitResultComponent
import com.asanatest.di.module.GitResultModule
import com.asanatest.domain.listeners.OnResultItemClicked
import com.asanatest.presenter.GithubResultsPresenter
import com.asanatest.utils.helpers.NetworkHelper
import com.asanatest.view.activities.BaseActivity
import com.asanatest.view.adapters.GitResultsAdapter
import com.asanatest.view.views.GitResultsView
import kotlinx.android.synthetic.main.activity_git_result.*
import javax.inject.Inject

class GitResultsActivity : BaseActivity(), GitResultsView, OnResultItemClicked, SwipeRefreshLayout.OnRefreshListener {

    @Inject
    lateinit var githubResultsPresenter: GithubResultsPresenter

    lateinit var internetReceiver: BroadcastReceiver
    lateinit var gitResultsAdapter: GitResultsAdapter
    lateinit var localRepos: ArrayList<RepoObject>
    var lastText = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_git_result)

        DaggerGitResultComponent.builder()
                .appComponent(getApplicationComponent())
                .gitResultModule(GitResultModule(this))
                .build().inject(this)

//        supportActionBar?.title = "GithubRepos"
        localRepos = ArrayList()
//        gitResultsAdapter = GitResultsAdapter(this, localRepos, this)
        githubResultsPresenter.fetchRepos("a")

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
        //  homeActivityProgressBar?.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        //  homeActivityProgressBar?.visibility = View.GONE
    }

    override fun showLoadingFooter() {
        gitResultsAdapter.addLoadingFooter()
    }

    override fun hideLoadingFooter() {
        gitResultsAdapter.removeLoadingFooter()
    }

    override fun showRefreshLoading() {
        gitResultActivitySwipeLayout.isRefreshing = true
        gitResultActivitySwipeLayout.isEnabled = false
    }

    override fun hideRefreshLoading() {
        gitResultActivitySwipeLayout.isRefreshing = false
        gitResultActivitySwipeLayout.isEnabled = true
    }

    override fun onRefresh() {
        localRepos = ArrayList()
        githubResultsPresenter.fetchRepos("a")
    }

    override fun updateList(repos: ArrayList<RepoObject>) {
        if (localRepos.size == 0) {
            localRepos = repos
            setupRecyclerView()
        } else {
            localRepos.addAll(repos)
            gitResultActivityRv.adapter.notifyDataSetChanged()
        }
    }

    override fun repoSubscribersFetched(subscribers: ArrayList<RepoObject.Owner>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemClicked(position: Int) {
        var intent = Intent(this, GitResultDetailsActivity::class.java)
        intent.putExtra("repoName", localRepos[position].reponame)
        intent.putExtra("repoId", localRepos[position].repoId)
        intent.putExtra("subscribers", localRepos[position].watchers_count)
        startActivity(intent)
//        finish()
    }

    override fun showError(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()

        if(gitResultsAdapter == null)
            setupRecyclerView()
    }

    internal fun isInternetAvailable() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        internetReceiver = NetworkHelper.isInternetAvailable(findViewById(android.R.id.content), getString(R.string.no_internet_message))
        registerReceiver(internetReceiver, intentFilter)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.git_results_menu, menu)

        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {

                if (newText != lastText) {
                    localRepos.clear()
                    gitResultsAdapter.clear()
                    localRepos = ArrayList()
                    gitResultsAdapter.notifyDataSetChanged()
                    if(newText == "")
                        githubResultsPresenter.fetchRepos("a")
                    else
                        githubResultsPresenter.fetchRepos(newText)
                    lastText = newText
                }

                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {

                if (!query.isEmpty()) {
                    localRepos.clear()
                    gitResultsAdapter.clear()
                    localRepos = ArrayList()
                    gitResultsAdapter.notifyDataSetChanged()
                    if(query == "")
                        githubResultsPresenter.fetchRepos("a")
                    else
                        githubResultsPresenter.fetchRepos(query)
                }

                return false
            }

        })

        return true
    }

    fun setupRecyclerView() {
        gitResultsAdapter = GitResultsAdapter(this, localRepos, this)

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        gitResultActivityRv.layoutManager = layoutManager
        gitResultActivityRv.adapter = gitResultsAdapter

        gitResultActivityRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val totalItemCount = layoutManager.itemCount
                val myTotalCount = totalItemCount - 14
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (dy > 0) { //dy scrolling down
                    if ((firstVisibleItemPosition >= myTotalCount) && firstVisibleItemPosition > 0
                            && myTotalCount > 0 && localRepos.size <= totalItemCount)
                        githubResultsPresenter.fetchNextPage()
                }
            }
        })

        hideRefreshLoading()
        hideLoading()
        //hideLoadingFooter()


    }
}
