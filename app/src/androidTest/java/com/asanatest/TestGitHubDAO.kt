package com.asanatest

import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.asanatest.data.db.GitHubDAO
import com.asanatest.data.db.GitHubDatabase
import com.asanatest.data.models.OwnerObject
import com.asanatest.data.models.RepoObject
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by Tomislav on 24,May,2018
 */


@RunWith(AndroidJUnit4::class)
class TestGitHubDAO {

    lateinit var userDao: GitHubDAO
    lateinit var database: GitHubDatabase

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getTargetContext()
        database = Room.inMemoryDatabaseBuilder(context, GitHubDatabase::class.java).build()
        userDao = database.getGitHubDao()
    }

    @After
    fun tearDown() {
        database.close()
    }


    @Test
    fun testSaveGetGitHubResults() {
        val githubResults = listOf(
                RepoObject(2, "Test", 15, 15, "TODAY", false, OwnerObject()),
                RepoObject(2, "Test", 15, 15, "YESTERDAY", false, OwnerObject()),
                RepoObject(),
                RepoObject(1, "Test2", 9999, 99, "12:34", true, OwnerObject())
        )

        val githubResults2 = listOf(
                RepoObject(2, "BTest", 185, 715, "23:4", false, OwnerObject()),
                RepoObject(4, "ATest", 145, 155, "YESTERDAY", false, OwnerObject()),
                RepoObject(),
                RepoObject(1, "Test2", 99919, 992, "12:34", true, OwnerObject())
        )


        userDao.saveGitHubResults(ArrayList(githubResults))
        userDao.saveGitHubResults(ArrayList(githubResults2))

        val getResults = userDao.getGitHubResults("T", 1, 50)


        val expectedResult = listOf(
                RepoObject(2, "Test", 15, 15, "TODAY", false, OwnerObject()),
                RepoObject(2, "Test", 15, 15, "YESTERDAY", false, OwnerObject()),
                RepoObject(),
                RepoObject(1, "Test2", 9999, 99, "12:34", true, OwnerObject()),
                RepoObject(2, "BTest", 185, 715, "23:4", false, OwnerObject()),
                RepoObject(4, "ATest", 145, 155, "YESTERDAY", false, OwnerObject()),
                RepoObject(),
                RepoObject(1, "Test23", 99919, 992, "12:34", true, OwnerObject())

        )

        Assert.assertEquals(expectedResult, getResults)
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

        Assert.assertEquals(expectedResult, getResults)
    }

}