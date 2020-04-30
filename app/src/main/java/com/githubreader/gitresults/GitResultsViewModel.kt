package com.githubreader.gitresults

import androidx.databinding.ObservableField
import androidx.lifecycle.*
import com.currencytrackingapp.utils.SingleLiveEvent
import com.githubreader.utils.network.InternetConnectionManager
import com.githubreader.data.models.RepoObject
import com.githubreader.data.source.GitHubResultsRepository
import kotlinx.coroutines.launch
import com.githubreader.data.models.*
import com.githubreader.data.source.remote.api.APIConstants.Companion.PAGE_ENTRIES

/**
 * @author Tomislav Curis
 */
class GitResultsViewModel(private val repository: GitHubResultsRepository,
                          private val internetConnectionManager: InternetConnectionManager
) : ViewModel() {

    val _currentPage = ObservableField<Int>(1)
    val _currentSearch = ObservableField<String>("a")

    private val _snackbarText = SingleLiveEvent<Int>()

    val isDataLoadingError = MutableLiveData<Boolean>(false)

    val _itemClicked = SingleLiveEvent<RepoObject>()
    val itemClicked: LiveData<RepoObject> = _itemClicked

    protected val _error = SingleLiveEvent<Throwable>()
    val error: LiveData<Throwable> get() = _error

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _forceUpdate = MutableLiveData<Boolean>(false)

    private val _items: LiveData<List<RepoObject>> =
        _forceUpdate.switchMap { forceUpdate ->
        if (forceUpdate && internetConnectionManager.hasInternetConnection()) { //f
            viewModelScope.launch {
                handleResponseWithError(repository.getGitHubResults(forceUpdate, _currentSearch.get()!!, _currentPage.get()!!, PAGE_ENTRIES))
                _dataLoading.value = false
            }
        }
            repository.observeRepos(_currentSearch.get()!!).switchMap {
                handleResponseWithError(it)
            }

    }

    val items: LiveData<List<RepoObject>> = _items

    // This LiveData depends on another so we can use a transformation.
    val empty: LiveData<Boolean> = Transformations.map(_items) {
        it.isEmpty()
    }

    fun refresh(refresh: Boolean) {
        _dataLoading.value = internetConnectionManager.hasInternetConnection() && !isDataLoadingError.value!! && refresh
        _forceUpdate.value = refresh
    }

    fun onSearchTextChanged(query: String){
        _currentSearch.set(query)
        items.value
        _forceUpdate.value = false
    }

    fun onItemClick(repoObject: RepoObject){
        _itemClicked.postValue(repoObject)
    }

    private fun showSnackbarMessage(message: Int) {
        _snackbarText.postValue(message)
    }

    protected fun handleResponseWithError(response: Result<List<RepoObject>>): LiveData<List<RepoObject>> {
        return when (response) {
            is Result.Success -> {
                isDataLoadingError.value = false
                MutableLiveData(
                    response.data) as LiveData<List<RepoObject>>
            }
            is Result.Error -> {
                isDataLoadingError.value = true
                _error.postValue(response.exception)
                MutableLiveData( response.exception) as LiveData<List<RepoObject>>
            }
            is Result.Loading -> MutableLiveData( null)
        }
    }
}