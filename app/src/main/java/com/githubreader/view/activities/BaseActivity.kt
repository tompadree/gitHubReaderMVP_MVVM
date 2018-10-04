package com.githubreader.view.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.githubreader.App
import com.githubreader.R
import com.githubreader.di.component.AppComponent

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        this.getApplicationComponent()?.inject(this)
    }

    protected fun getApplicationComponent(): AppComponent? {
        return (application as App).appComponent
    }
}
