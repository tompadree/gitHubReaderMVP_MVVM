package com.githubreader.base

import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.githubreader.utils.network.InternetConnectionManager
import com.githubreader.R
import com.githubreader.data.api.APIConstants
import org.koin.android.ext.android.inject


class GitHubActivity : AppCompatActivity() {

    private val onDestinationChangedListener = this@GitHubActivity::onDestinationChanged
    private lateinit var appBarConfiguration: AppBarConfiguration

    private val internetConnectionManager: InternetConnectionManager by inject()
    private lateinit var internetReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_github)

        showSnackbar(findViewById(android.R.id.content))
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.mainNavHost).navigateUp(appBarConfiguration) ||
                super.onSupportNavigateUp()
    }

    override fun onPause() {
        val navHostFragment = (supportFragmentManager.primaryNavigationFragment as NavHostFragment?)!!
        navHostFragment.navController.removeOnDestinationChangedListener(onDestinationChangedListener)
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        val navHostFragment = (supportFragmentManager.primaryNavigationFragment as NavHostFragment?)!!
        navHostFragment.navController.addOnDestinationChangedListener(onDestinationChangedListener)
    }

    private fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?) {

    }

    private fun showSnackbar(parentLayout: View){

        val intentFilter = IntentFilter()
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        internetReceiver = internetConnectionManager.isInternetAvailable(findViewById(android.R.id.content))
        registerReceiver(internetReceiver, intentFilter)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.git_results_menu, menu)

        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {

//                if (newText != lastText) {
//
//                    showLoading()
//                    localRepos.clear()
//                    gitResultsAdapter?.clear()
//                    localRepos = ArrayList()
//                    gitResultsAdapter?.notifyDataSetChanged()
//                    if (newText == "")
//                        githubResultsPresenter.fetchRepos(APIConstants.DUMMY_SEARCH)
//                    else
//                        githubResultsPresenter.fetchRepos(newText)
//                    lastText = newText
//                }

                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {

//                if (!query.isEmpty()) {
//                    showLoading()
//                    localRepos.clear()
//                    gitResultsAdapter?.clear()
//                    localRepos = ArrayList()
//                    gitResultsAdapter?.notifyDataSetChanged()
//                    if (query == "")
//                        githubResultsPresenter.fetchRepos(APIConstants.DUMMY_SEARCH)
//                    else
//                        githubResultsPresenter.fetchRepos(query)
//                }

                return false
            }

        })

        return true
    }
}
