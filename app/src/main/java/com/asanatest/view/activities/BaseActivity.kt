package com.asanatest.view.activities

import android.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.asanatest.App
import com.asanatest.R
import com.asanatest.di.component.AppComponent

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        this.getApplicationComponent()?.inject(this)
    }

//    protected fun addFragment(containerViewId: Int, fragment: Fragment) {
//
//        val fragmentTransaction = this.fragmentManager.beginTransaction()
//        fragmentTransaction.add(containerViewId, fragment)
//        fragmentTransaction.commit()
//    }

    protected fun getApplicationComponent(): AppComponent? {
        return (application as App).appComponent
    }
}
