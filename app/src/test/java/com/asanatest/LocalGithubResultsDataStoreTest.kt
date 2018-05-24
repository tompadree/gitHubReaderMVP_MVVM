package com.asanatest

import com.asanatest.data.db.GitHubDAO
import com.asanatest.data.models.OwnerObject
import com.asanatest.data.models.RepoObject
import com.asanatest.data.models.ReposModel
import io.reactivex.Flowable
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when` as whenever
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import io.reactivex.SingleEmitter
import org.mockito.Mockito.`when`

/**
 * Created by Tomislav on 24,May,2018
 */
class LocalGithubResultsDataStoreTest {

    @Mock
    lateinit var gitHubDAO: GitHubDAO

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    private fun saveGetGitHubResults() {

        val saveRepos = Single.fromCallable {
            gitHubDAO.saveGitHubResults(ArrayList(listOf(
                    RepoObject(2, "BTest", 185, 715, "23:4", false, OwnerObject()),
                    RepoObject(4, "ATest", 145, 155, "YESTERDAY", false, OwnerObject()),
                    RepoObject(),
                    RepoObject(1, "Test2", 99919, 992, "12:34", true, OwnerObject())
            )))
        }.toFlowable().test()

        saveRepos.assertNoErrors()
        saveRepos.assertValue { t: LongArray ->
            t.size == 4
        }

        var reposModel = ReposModel()
        val getRepos = Single.fromCallable { ArrayList(gitHubDAO.getGitHubResults("es", 1, 50)) }.toFlowable()
                .map {
                    reposModel.items = it
                    reposModel
                }.test()
        getRepos.assertNoErrors()
        getRepos.assertValue { reposModel: ReposModel ->
            reposModel.items.size == 4
        }

    }

    @Test
    private fun saveGetGitHubResultSubscribers(repoName: String, emitter: SingleEmitter<ArrayList<OwnerObject>>) {

        val saveSubs = Single.fromCallable {
            gitHubDAO.saveGitHubResultSubscribers(ArrayList(listOf(
                    OwnerObject("user5", "www.avatar.com/223.jpeg", "usetype", "Admin", "JakeWHarton"),
                    OwnerObject("user6", "www.avatar.com/212.jpeg", "usetype", "Admin", "JakeWHarton"),
                    OwnerObject("user7", "www.avatar.com/123.jpeg", "usetype", "Admin", "JakeWHarton"),
                    OwnerObject(),
                    OwnerObject("user8", "www.avatar.com/213.jpeg", "usetype", "Admin", "JakeWHarton")
            )))
        }.toFlowable().test()

        saveSubs.assertNoErrors()
        saveSubs.assertValue { t: LongArray ->
            t.size == 5
        }

        val getSubs = Single.fromCallable { ArrayList(gitHubDAO.getGitHubResultSubscribers("JakeWHarton", 1, 50)) }
                .toFlowable().test()

        getSubs.assertNoErrors()
        getSubs.assertValue { owners: ArrayList<OwnerObject> ->
            owners.size == 5
        }

    }

}