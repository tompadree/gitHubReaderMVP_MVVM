package com.githubreader

import com.githubreader.data.api.APIConstants
import com.githubreader.data.models.OwnerObject
import com.githubreader.data.models.RepoObject
import com.githubreader.data.models.ReposModel
import com.githubreader.data.repositories.githubresults.LocalGithubResultsDataStore
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations


/**
 * Created by Tomislav on 24,May,2018
 */

class GithubResultsInteractorTest {

    @Mock
    lateinit var localGithubResultsDataStore: LocalGithubResultsDataStore

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun saveGetGitHubResults() {

        val saveResults = localGithubResultsDataStore.saveGitHubResultsDB("", ArrayList(listOf(
                RepoObject(2, "BTest", 185, 715, "23:4", false, OwnerObject()),
                RepoObject(4, "ATest", 145, 155, "YESTERDAY", false, OwnerObject()),
                RepoObject(),
                RepoObject(1, "Test2", 99919, 992, "12:34", true, OwnerObject())
        ))).test()

        saveResults.assertNoErrors()
        saveResults.assertValue { t: LongArray -> t.size == 4 }


        val getResults = localGithubResultsDataStore.getGitHubResults("es", 1, APIConstants.PAGE_ENTRIES).test()


        getResults.assertNoErrors()
        getResults.assertValue { repoModel: ReposModel -> repoModel.items.size == 4 }
        getResults.assertValue { repoModel: ReposModel -> repoModel.items[0].repoName == "BTest" }
    }


    @Test
    fun saveGetGitHubResultSubscribers() {
        val saveSubs = localGithubResultsDataStore.saveGitHubResultSubscribersDB("JakeWHarton", ArrayList(listOf(
                OwnerObject("user5", "www.avatar.com/223.jpeg", "usetype", "Admin", "JakeWHarton"),
                OwnerObject("user6", "www.avatar.com/212.jpeg", "usetype", "Admin", "JakeWHarton"),
                OwnerObject("user7", "www.avatar.com/123.jpeg", "usetype", "Admin", "JakeWHarton"),
                OwnerObject(),
                OwnerObject("user8", "www.avatar.com/213.jpeg", "usetype", "Admin", "JakeWHarton")
        ))).test()

        saveSubs.assertNoErrors()
        saveSubs.assertValue { t: LongArray ->
            t.size == 5
        }


        val getSubs = localGithubResultsDataStore.getGitHubResultSubscribers(1, "JakeWHarton", 1, 50).test()

        getSubs.assertNoErrors()
        getSubs.assertValue { owners: ArrayList<OwnerObject> -> owners.size == 5 }
        getSubs.assertValue { owners: ArrayList<OwnerObject> -> owners[0].userName == "user5" }
    }

}

