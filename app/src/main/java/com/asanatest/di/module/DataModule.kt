package com.asanatest.di.module

import android.arch.persistence.room.Room
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Tom on 22.5.2018..
 */
@Module
@Singleton
class DataModule {

//    @Singleton
//    @Provides
//    fun provideRoomDatabase(context: Context): ISGEDatabase {
//        return Room.databaseBuilder(
//                context,
//                ISGEDatabase::class.java,
//                "isge_db").build()
//    }
//
//
//    @Singleton
//    @Provides
//    fun providesRoomISGECache(isgeDatabase: ISGEDatabase) : ISGECache = RoomISGECache(isgeDatabase)

}