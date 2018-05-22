package com.asanatest

import android.app.Application
import com.asanatest.di.component.AppComponent
import com.asanatest.di.component.DaggerAppComponent
import com.asanatest.di.module.AppModule

/**
 * Created by Tom on 22.5.2018..
 */
class App : Application() {

    var appComponent: AppComponent? = null
        private set

    override fun onCreate() {
        super.onCreate()

        instance = this

        this.appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()


    }

    companion object {
        var instance: App? = null
    }
}