package com.githubreader.gitresultsdetails

import androidx.lifecycle.ViewModel
import com.githubreader.utils.network.InternetConnectionManager
import com.githubreader.data.source.GitHubResultsRepository

/**
 * @author Tomislav Curis
 */
class GitResultsDetailsViewModel (private val repository: GitHubResultsRepository,
                                  private val internetConnectionManager: InternetConnectionManager
) : ViewModel() {
}