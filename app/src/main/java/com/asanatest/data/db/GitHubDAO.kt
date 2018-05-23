package com.asanatest.data.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.asanatest.data.models.OwnerObject
import com.asanatest.data.models.RepoObject
import com.asanatest.data.models.ReposModel
import io.reactivex.Flowable
import io.reactivex.Single
import android.arch.paging.LivePagedListProvider


/**
 * Created by Tom on 22.5.2018..
 */
@Dao
interface GitHubDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveGitHubResults(githubResults: ArrayList<RepoObject>): LongArray

    @Query("SELECT * FROM repos WHERE repoName LIKE :repoName") // ORDER BY repoName ASC LIMIT :page , :per_page") //" AND RowNum >= 1 AND RowNum < 20")
    fun getGitHubResults(repoName: String): List<RepoObject>

    @Query("SELECT * FROM repos WHERE repoName LIKE :repoName") // ORDER BY repoName ASC LIMIT :page , :per_page") //" AND RowNum >= 1 AND RowNum < 20")
    fun getGitHubResults1(repoName: String): RepoObject

//    @Query("SELECT * FROM repos ORDER BY repoName ASC")
//    fun results(): LivePagedListProvider<Int, RepoObject>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveGitHubResultSubscribers(subscribers: ArrayList<OwnerObject>): LongArray

    @Query("SELECT * FROM owners WHERE parentRepo LIKE :repoName") // ORDER BY userName ASC LIMIT (:page * :per_page) , :per_page")
    fun getGitHubResultSubscribers(repoName: String): List<OwnerObject>

}