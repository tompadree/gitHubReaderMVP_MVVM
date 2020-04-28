package com.githubreader

import android.app.Application
import com.githubreader.di.AppModule
import com.githubreader.di.DataModule
import com.githubreader.di.NetModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.module.Module

/**
 * @author Tomislav Curis
 */
class TestApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@TestApp)
            modules(listOf(AppModule, DataModule, NetModule))
        }
    }

    internal fun injectModule(module: Module) {
        loadKoinModules(module)
    }
}