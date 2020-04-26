package com.githubreader.gitresults

import androidx.databinding.ObservableField
import androidx.lifecycle.*
import com.currencytrackingapp.utils.SingleLiveEvent
import com.githubreader.utils.network.InternetConnectionManager
import com.githubreader.data.models.RepoObject
import com.githubreader.data.source.GitHubResultsRepository
import kotlinx.coroutines.launch
import com.githubreader.data.models.*

/**
 * @author Tomislav Curis
 */
class GitResultsViewModel(private val repository: GitHubResultsRepository,
                          private val internetConnectionManager: InternetConnectionManager
) : ViewModel() {

    val _currentPage = ObservableField<Int>(1)

    private val _snackbarText = SingleLiveEvent<Int>()

    val isDataLoadingError = MutableLiveData<Boolean>(false)

    protected val _error = SingleLiveEvent<Throwable>()
    val error: LiveData<Throwable> get() = _error

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _forceUpdate = MutableLiveData<Boolean>(false)

    private val _items: LiveData<List<RepoObject>> = _forceUpdate.switchMap { forceUpdate ->
        if (forceUpdate && internetConnectionManager.hasInternetConnection()) {
            viewModelScope.launch {
//                handleResponseWithError(
                    repository.getGitHubResults(forceUpdate, "", _currentPage.get()!!, 30)
                _dataLoading.value = false
            }
        }


        repository.observeRepos().map { handleResponseWithError(it)!! }
    }

    val items: LiveData<List<RepoObject>> = _items

    // This LiveData depends on another so we can use a transformation.
    val empty: LiveData<Boolean> = Transformations.map(_items) {
        it.isEmpty()
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