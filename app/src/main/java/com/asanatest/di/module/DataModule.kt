package com.asanatest.di.module

import android.arch.persistence.room.Room
import android.content.Context
import com.asanatest.data.db.GitHubDatabase
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
                "test_db").build()
    }

}
