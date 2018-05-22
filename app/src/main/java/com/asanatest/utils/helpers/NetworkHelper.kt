package com.asanatest.utils.helpers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.NetworkInfo
import android.os.Parcelable
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.View

/**
 * Created by Tom on 22.5.2018..
 */
class NetworkHelper {

    companion object {


        fun isInternetAvailable(mParentLayout: View, message: String): BroadcastReceiver {

            val noIntSnaBar = Snackbar.make(mParentLayout, message, Snackbar.LENGTH_INDEFINITE)

            return object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {

                    val extras = intent.extras
                    val info = extras!!.getParcelable<Parcelable>("networkInfo") as NetworkInfo

                    val state = info.state

                    if (state == NetworkInfo.State.CONNECTED) {
                        noIntSnaBar.dismiss()
                    } else {
                        noIntSnaBar.show()
                    }
                }
            }
        }

    }

}