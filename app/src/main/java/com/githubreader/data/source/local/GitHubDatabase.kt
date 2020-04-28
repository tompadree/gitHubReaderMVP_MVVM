package com.githubreader.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.githubreader.data.models.*

/**
 * Created by Tom on 22.5.2018..
 */

@Database(entities = [RepoObject::class, OwnerObject::class], version = 1, exportSchema = false)
@TypeConverters(OwnerConverter::class, ReposConverter::class)
abstract class GitHubDatabase : RoomDatabase() {
    abstract fun getGitHubDao(): GitHubDAO
}