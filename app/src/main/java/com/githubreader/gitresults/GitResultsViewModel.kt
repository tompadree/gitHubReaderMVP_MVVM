package com.githubreader.gitresults

import androidx.databinding.ObservableField
import androidx.lifecycle.*
import com.currencytrackingapp.utils.SingleLiveEvent
import com.githubreader.R
import com.githubreader.utils.network.InternetConnectionManager
import com.githubreader.data.models.RepoObject
import com.githubreader.data.source.GitHubResultsRepository
import kotlinx.coroutines.launch
import com.githubreader.data.models.*
import com.githubreader.data.source.remote.api.APIConstants.Companion.DUMMY_SEARCH

/**
 * @author Tomislav Curis
 */
class GitResultsViewModel(private val repository: GitHubResultsRepository,
                          private val internetConnectionManager: InternetConnectionManager
) : ViewModel() {

    val _currentPage = ObservableField<Int>(1)
    val _currentSearch = ObservableField<String>("a")

    private val _snackbarText = SingleLiveEvent<Int>()

    val isDataLoadingError = MutableLiveData<Boolean>(true)

    val _itemClicked = SingleLiveEvent<RepoObject>()
    val itemClicked: LiveData<RepoObject> = _itemClicked

    protected val _error = SingleLiveEvent<Throwable>()
    val error: LiveData<Throwable> get() = _error

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _forceUpdate = MutableLiveData<Boolean>(false)

    private val _items: LiveData<List<RepoObject>> =
        _forceUpdate.switchMap { forceUpdate ->
        if (forceUpdate && internetConnectionManager.hasInternetConnection()) {
            viewModelScope.launch {
                handleResponseWithError(
                    repository.getGitHubResults(forceUpdate, _currentSearch.get()!!, _currentPage.get()!!, 30)  )
                _dataLoading.value = false
            }
        }

            repository.observeRepos().map { handleResponseWithError(it)!! }

//        repository.observeRepos().switchMap {
////            handleResponseWithError(it)
//            filterRates(it)
//        }
    }

    val items: LiveData<List<RepoObject>> = _items

    // This LiveData depends on another so we can use a transformation.
    val empty: LiveData<Boolean> = Transformations.map(_items) {
        it.isEmpty()
    }

    private fun filterRates(ratesResult: Result<List<RepoObject>>): LiveData<List<RepoObject>> {
        // TODO: This is a good case for liveData builder. Replace when stable.
        val result = MutableLiveData<List<RepoObject>>()

        if (ratesResult is Result.Success) {
            viewModelScope.launch {
                result.value = ratesResult.data
                showSnackbarMessage(R.string.error_default_db)
            }
        } else {
            result.value = emptyList()
            showSnackbarMessage(R.string.error_default_db)
        }
        return result
    }

    fun refresh(refresh: Boolean) {
        _forceUpdate.value = refresh
    }

    fun onItemClick(repoObject: RepoObject){
        _itemClicked.postValue(repoObject)
    }

    private fun showSnackbarMessage(message: Int) {
        _snackbarText.postValue(message)
    }

//    protected fun handleResponseWithError(response: Result<List<RepoObject>>): LiveData<List<RepoObject>> {
    protected fun <T> handleResponseWithError(response: Result<T>): T? {
        return when (response) {
            is Result.Success -> {
                isDataLoadingError.value = false
//                MutableLiveData(
                    response.data
            }
            is Result.Error -> {
                isDataLoadingError.value = true
                _error.postValue(response.exception)
//                MutableLiveData( emptyList()
                null
            }
            is Result.Loading -> null
        }
    }
}