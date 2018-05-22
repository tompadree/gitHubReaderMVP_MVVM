package com.asanatest.data.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.asanatest.data.models.RepoObject

/**
 * Created by Tom on 22.5.2018..
 */

@Database(entities = [RepoObject::class], version = 1)
abstract class GitHubDatabase : RoomDatabase() {
    abstract fun getGitHubDao(): GitHubDAO
}