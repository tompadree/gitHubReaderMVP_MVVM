package com.asanatest

import android.content.Context
import com.asanatest.data.models.OwnerObject
import com.asanatest.data.models.RepoObject
import com.asanatest.presenter.GithubResultsPresenter
import com.asanatest.view.views.GitResultsView
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.ArgumentMatchers.eq
import org.mockito.BDDMockito.given
import org.mockito.Mockito.verify
import org.reactivestreams.Subscriber

/**
 * Created by Tom on 23.5.2018..
 */
@RunWith(MockitoJUnitRunner::class)
class GithubResultsPresenterUnitTest {

    private var githubResultsPresenter: GithubResultsPresenter? = null
    private val DUMMY_SEARCH = "a"

    @Mock
    private val mockContext: Context? = null
    @Mock
    private val gitResultsView: GitResultsView? = null
    @Mock
    private val mockRepos: ArrayList<RepoObject>? = null
    @Mock
    private val mockOwners: ArrayList<OwnerObject>? = null

    @Before
    fun setUp() {

        MockitoAnnotations.initMocks(this)

        mockRepos?.let { gitResultsView?.updateList(it) }
        mockOwners?.let { gitResultsView?.repoSubscribersFetched(it) }

    }

    @Test
    fun testUserListPresenterSearch() {

        given(gitResultsView!!).willReturn(gitResultsView)

        verify(githubResultsPresenter!!).fetchRepos(DUMMY_SEARCH)

        verify<GitResultsView>(gitResultsView).showLoading()

        verify(githubResultsPresenter!!).fetchRepoSubscribers(eq<Int>(1),eq<String>(DUMMY_SEARCH))
    }

}