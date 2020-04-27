package com.githubreader.gitresultsdetails

import androidx.lifecycle.ViewModel
import com.githubreader.utils.network.InternetConnectionManager
import com.githubreader.data.source.GitHubResultsRepository
import androidx.databinding.ObservableField
import androidx.lifecycle.*
import com.currencytrackingapp.utils.SingleLiveEvent
import com.githubreader.data.models.OwnerObject
import kotlinx.coroutines.launch
import com.githubreader.data.models.*
import com.githubreader.data.source.remote.api.APIConstants.Companion.DUMMY_SEARCH

/**
 * @author Tomislav Curis
 */
class GitResultsDetailsViewModel(private val repository: GitHubResultsRepository,
                          private val internetConnectionManager: InternetConnectionManager
) : ViewModel() {

    val _currentPage = ObservableField<Int>(1)
    val _parentRepoObject = ObservableField<RepoObject>()

    private val _snackbarText = SingleLiveEvent<Int>()

    val isDataLoadingError = MutableLiveData<Boolean>(false)

    protected val _error = SingleLiveEvent<Throwable>()
    val error: LiveData<Throwable> get() = _error

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _forceUpdate = MutableLiveData<Boolean>(false)

    private val _items: LiveData<List<OwnerObject>> =
        _forceUpdate.switchMap { forceUpdate ->
            if (forceUpdate && internetConnectionManager.hasInternetConnection()) {
                viewModelScope.launch {
                    handleResponseWithError( repository.getGitHubResultSubscribers(forceUpdate, _parentRepoObject.get()!!.repoName, _currentPage.get()!!, 30))
                    _dataLoading.value = false
                }
            }

            repository.observeSubscribers().map {
                handleResponseWithError(it)!! }
        }

    val items: LiveData<List<OwnerObject>> = _items

    // This LiveData depends on another so we can use a transformation.
    val empty: LiveData<Boolean> = Transformations.map(_items) {
        it.isEmpty()
    }

    fun refresh(refresh: Boolean) {
        _forceUpdate.value = refresh
    }

    private fun showSnackbarMessage(message: Int) {
        _snackbarText.postValue(message)
    }

    protected fun <T> handleResponseWithError(response: Result<T>): T? {
        return when (response) {
            is Result.Success -> {
                isDataLoadingError.value = false
                response.data
            }
            is Result.Error -> {
                isDataLoadingError.value = true
                _error.postValue(response.exception)
                null
            }
            is Result.Loading -> null
        }
    }
}