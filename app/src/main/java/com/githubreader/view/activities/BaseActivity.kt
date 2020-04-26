package com.githubreader.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.githubreader.App
import com.githubreader.R

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
    }

}
