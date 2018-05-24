package com.asanatest.view.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.asanatest.R
import com.asanatest.utils.AppConstants.Companion.SPLASH_DISPLAY_LENGTH
import com.asanatest.view.activities.GitResults.GitResultsActivity

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            startActivity(Intent(this@SplashActivity, GitResultsActivity::class.java))
            finish()}, SPLASH_DISPLAY_LENGTH)

    }
}

