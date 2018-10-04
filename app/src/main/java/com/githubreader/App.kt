package com.githubreader

import android.app.Application
import com.githubreader.di.component.AppComponent
import com.githubreader.di.component.DaggerAppComponent
import com.githubreader.di.module.AppModule

/**
 * Created by Tom on 22.5.2018..
 */
class App : Application() {

    var appComponent: AppComponent? = null
        set

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