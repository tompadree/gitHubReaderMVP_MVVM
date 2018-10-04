package com.githubreader.data.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.githubreader.data.models.OwnerObject
import com.githubreader.data.models.RepoObject

/**
 * Created by Tom on 22.5.2018..
 */

@Database(entities = [RepoObject::class, OwnerObject::class], version = 1)
abstract class GitHubDatabase : RoomDatabase() {
    abstract fun getGitHubDao(): GitHubDAO
}