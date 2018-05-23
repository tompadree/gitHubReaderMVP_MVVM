package com.asanatest.di.module

import android.arch.persistence.room.Room
import android.content.Context
import com.asanatest.data.db.GitHubCache
import com.asanatest.data.db.GitHubDatabase
import com.asanatest.data.db.RoomGitHubCache
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Tom on 22.5.2018..
 */
@Module
@Singleton
class DataModule {

    @Singleton
    @Provides
    fun provideRoomDatabase(context: Context): GitHubDatabase {
        return Room.databaseBuilder(
                context,
                GitHubDatabase::class.java,
                "isge_db").build()
    }


    @Singleton
    @Provides
    fun providesRoomISGECache(gitHubDatabase: GitHubDatabase) : GitHubCache = RoomGitHubCache(gitHubDatabase)

}