package com.githubreader

import androidx.room.Room
import androidx.test.InstrumentationRegistry.getTargetContext
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.githubreader.data.db.GitHubDAO
import com.githubreader.data.db.GitHubDatabase
import com.githubreader.data.models.OwnerObject
import com.githubreader.data.models.RepoObject
import io.reactivex.Single
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by Tomislav on 24,May,2018
 */


@RunWith(AndroidJUnit4::class)
class GitHubDAOUnitTest {

    lateinit var userDao: GitHubDAO
    lateinit var database: GitHubDatabase

    @Before
    fun setup() {
//        val context = InstrumentationRegistry.getTargetContext()
//        database = Room.inMemoryDatabaseBuilder(context, GitHubDatabase::class.java).build()
        userDao = database.getGitHubDao()
    }

    @After
    fun tearDown() {
        database.close()
    }


    @Test
    fun testSaveGetGitHubResults() {
        val githubResults = listOf(
                RepoObject(1, "repo/Testino", 15, 15, "TODAY", false, OwnerObject()),
                RepoObject(2, "repo/Tester", 15, 15, "YESTERDAY", false, OwnerObject()),
              //  RepoObject(),
                RepoObject(4, "repo/Test2", 9999, 99, "12:34", true, OwnerObject()))

        val githubResults2 = listOf(
                RepoObject(5, "repo/BTest", 185, 715, "23:4", false, OwnerObject()),
                RepoObject(6, "repo/ATest", 145, 155, "YESTERDAY", false, OwnerObject()),
               // RepoObject(),
                RepoObject(8, "repo/QTest2", 99919, 992, "12:34", true, OwnerObject()))

        userDao.saveGitHubResults(ArrayList(githubResults))
        userDao.saveGitHubResults(ArrayList(githubResults2))

        val getResultsSingle = Single.fromCallable {userDao.getGitHubResults("%Test%", 0, 50) }
                .subscribe({

                    var t = it
                    var test = t
                },{

                    var t : Throwable
                    t = it

                })


        val getResults = userDao.getGitHubResults("%Test%", 0, 50)
        val expectedResult = listOf(
                RepoObject(1, "repo/Testino", 15, 15, "TODAY", false, OwnerObject()),
                RepoObject(2, "repo/Tester", 15, 15, "YESTERDAY", false, OwnerObject()),
                //  RepoObject(),
                RepoObject(4, "repo/Test2", 9999, 99, "12:34", true, OwnerObject()),
                RepoObject(5, "repo/BTest", 185, 715, "23:4", false, OwnerObject()),
                RepoObject(6, "repo/ATest", 145, 155, "YESTERDAY", false, OwnerObject()),
                // RepoObject(),
                RepoObject(8, "repo/QTest2", 99919, 992, "12:34", true, OwnerObject()))

        for(i in 0 until getResults.size){
            var tst = getResults[i]
            var tst2 = expectedResult[i]

            if(tst.repoName == tst2.repoName)
                tst = tst2

        }



        if(getResults.equals(expectedResult))
            return

//        Assert.assertEquals(getResults, expectedResult)
    }

    @Test
    fun testSaveGetGitHubResultSubscribers() {
        val subscribers = listOf(
                OwnerObject("user1", "www.avatar.com/2124523.jpeg", "usetype", "Admin", "JakeWHarton"),
                OwnerObject("user2", "www.avatar.com/215463.jpeg", "usetype", "Admin", "JakeWHarton"),
                OwnerObject("user3", "www.avatar.com/212423.jpeg", "usetype", "Admin", "JakeWHarton"),
                OwnerObject(),
                OwnerObject("user4", "www.avatar.com/2123.jpeg", "usetype", "Admin", "JakeWHarton")
        )

        val subscribers2 = listOf(
                OwnerObject("user5", "www.avatar.com/223.jpeg", "usetype", "Admin", "JakeWHarton"),
                OwnerObject("user6", "www.avatar.com/212.jpeg", "usetype", "Admin", "JakeWHarton"),
                OwnerObject("user7", "www.avatar.com/123.jpeg", "usetype", "Admin", "JakeWHarton"),
                OwnerObject(),
                OwnerObject("user8", "www.avatar.com/213.jpeg", "usetype", "Admin", "JakeWHarton")
        )


        userDao.saveGitHubResultSubscribers(ArrayList(subscribers))
        userDao.saveGitHubResultSubscribers(ArrayList(subscribers2))

        val getResults = userDao.getGitHubResultSubscribers("u", 1, 50)


        val expectedResult = listOf(
                OwnerObject("user1", "www.avatar.com/2124523.jpeg", "usetype", "Admin", "JakeWHarton"),
                OwnerObject("user2", "www.avatar.com/215463.jpeg", "usetype", "Admin", "JakeWHarton"),
                OwnerObject("user3", "www.avatar.com/212423.jpeg", "usetype", "Admin", "JakeWHarton"),
                OwnerObject(),
                OwnerObject("user4", "www.avatar.com/2123.jpeg", "usetype", "Admin", "JakeWHarton"),
                OwnerObject("user5", "www.avatar.com/223.jpeg", "usetype", "Admin", "JakeWHarton"),
                OwnerObject("user6", "www.avatar.com/212.jpeg", "usetype", "Admin", "JakeWHarton"),
                OwnerObject("user7", "www.avatar.com/123.jpeg", "usetype", "Admin", "JakeWHarton"),
                OwnerObject(),
                OwnerObject("user8", "www.avatar.com/213.jpeg", "usetype", "Admin", "JakeWHarton")
        )

      //  Assert.assertEquals(expectedResult, getResults)
    }

}