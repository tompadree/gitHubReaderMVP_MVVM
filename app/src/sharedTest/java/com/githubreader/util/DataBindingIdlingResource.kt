package com.currencytrackingapp.util

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentActivity
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.IdlingResource
import com.githubreader.splash.SplashFragment
import java.util.UUID

/**
 * @author Tomislav Curis
 */
class DataBindingIdlingResource : IdlingResource {
    // List of registered callbacks
    private val idlingCallbacks = mutableListOf<IdlingResource.ResourceCallback>()
    // Give it a unique id to work around an Espresso bug where you cannot register/unregister
    // an idling resource with the same name.
    private val id = UUID.randomUUID().toString()
    // Holds whether isIdle was called and the result was false. We track this to avoid calling
    // onTransitionToIdle callbacks if Espresso never thought we were idle in the first place.
    private var wasNotIdle = false

    lateinit var activity: FragmentActivity

    override fun getName() = "DataBinding $id"

    override fun isIdleNow(): Boolean {
        val idle = (!getBindings().any { it.hasPendingBindings() }) && (getSplashFrag() == null)
        @Suppress("LiftReturnOrAssignment")
        if (idle) {
            if (wasNotIdle) {
                // Notify observers to avoid Espresso race detector.
                idlingCallbacks.forEach { it.onTransitionToIdle() }
            }
            wasNotIdle = false
        } else {
            wasNotIdle = true
            // Check next frame.
            activity.findViewById<View>(android.R.id.content).postDelayed({
                isIdleNow
            }, 16)
        }
        return idle
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback) {
        idlingCallbacks.add(callback)
    }

    /**
     * Find all binding classes in all currently available fragments.
     */
    private fun getBindings(): List<ViewDataBinding> {
        val fragments = (activity as? FragmentActivity)
            ?.supportFragmentManager
            ?.fragments

        val bindings =
            fragments?.mapNotNull {
                it.view?.getBinding()
            } ?: emptyList()
        val childrenBindings = fragments?.flatMap { it.childFragmentManager.fragments }
            ?.mapNotNull { it.view?.getBinding() } ?: emptyList()

        return bindings + childrenBindings
    }

    /**
     *  Detect splash fragment.
     */
    private fun getSplashFrag(): SplashFragment? {
        val fragments = (activity as? FragmentActivity)
            ?.supportFragmentManager
            ?.fragments
        val bindings =
            fragments?.mapNotNull {
                it.childFragmentManager.primaryNavigationFragment
            } ?: emptyList()
        val childrenFrags = fragments?.flatMap { it.childFragmentManager.fragments }
            ?.mapNotNull { it } ?: emptyList()


        for(frag in childrenFrags)
            if(frag is SplashFragment)
                return frag

        return null
    }
}

private fun View.getBinding(): ViewDataBinding? = DataBindingUtil.getBinding(this)

/**
 * Sets the activity from an [ActivityScenario] to be used from [DataBindingIdlingResource].
 */
fun DataBindingIdlingResource.monitorActivity(
    activityScenario: ActivityScenario<out FragmentActivity>
) {
    activityScenario.onActivity {
        this.activity = it
    }
}

/**
 * Sets the fragment from a [FragmentScenario] to be used from [DataBindingIdlingResource].
 */
//fun DataBindingIdlingResource.monitorFragment(fragmentScenario: FragmentScenario<out Fragment>) {
//    fragmentScenario.onFragment {
//        this.activity = it.requireActivity()
//    }
//}