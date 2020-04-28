package com.githubreader

import android.app.Application
import com.githubreader.di.component.AppComponent
import com.githubreader.di.component.DaggerAppComponent
import com.githubreader.di.module.AppModule

/**
 * Created by Tom on 24.5.2018..
 */

class CustomApplication : Application() {

    lateinit var component: AppComponent

    override fun onCreate() {
        super.onCreate()

        initAppComponent()

        //component.inject(this)
    }

    private fun initAppComponent() {
        component = DaggerAppComponent
                .builder()
                .appModule(AppModule(this))
                .build()

    }
}