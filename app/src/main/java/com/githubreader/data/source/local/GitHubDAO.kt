package com.githubreader.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.githubreader.data.models.OwnerObject
import com.githubreader.data.models.RepoObject


/**
 * Created by Tom on 22.5.2018..
 */
@Dao
interface GitHubDAO {

    /**
     * Observes list of repos.
     *
     * @return all repos.
     */
    @Query("SELECT * FROM repos")
    fun observeTasks(): LiveData<List<RepoObject>>

    /**
     * Delete all repos.
     */
    @Query("DELETE FROM repos")
    suspend fun deleteRepos()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveGitHubResults(githubResults: List<RepoObject>) //: LongArray

    @Query("SELECT * FROM repos WHERE repoName LIKE :repoName LIMIT :page , :per_page") //" ORDER BY repoName ASC
    fun getGitHubResults(repoName: String, page: Int, per_page: Int): List<RepoObject>

    /**
     * Delete all owners.
     */
    @Query("DELETE FROM owners")
    suspend fun deleteOwners()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveGitHubResultSubscribers(subscribers: List<OwnerObject>) //: LongArray

    @Query("SELECT * FROM owners WHERE parentRepo LIKE :repoName LIMIT :page , :per_page") // ORDER BY userName ASC LIMIT (:page * :per_page) , :per_page")
    fun getGitHubResultSubscribers(repoName: String, page: Int, per_page: Int): List<OwnerObject>

}