package com.asanatest

import android.app.Fragment
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.asanatest.view.activities.GitResults.GitResultsActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.closeSoftKeyboard
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.isEnabled
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.v7.widget.RecyclerView
import android.view.View
import com.asanatest.di.component.DaggerTestAppComponent
import com.asanatest.di.module.AppModule
import com.asanatest.di.module.DataModule
import com.asanatest.di.module.NetModule
import com.asanatest.di.module.ThreadModule
import kotlinx.android.synthetic.main.activity_git_result.*
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.not
import org.mockito.MockitoAnnotations

/**
 * Created by Tom on 24.5.2018..
 */
@RunWith(AndroidJUnit4::class)
class GitResultsActivityUnitTest {

    private var gitResultsActivity: GitResultsActivity? = null
    private val DUMMY_SEARCH = "a"

    @Rule
    var activityTestRule = ActivityTestRule<GitResultsActivity>(GitResultsActivity::class.java)

    @Before
    @Throws(Exception::class)
    fun setUp() {
        this.gitResultsActivity = activityTestRule.getActivity()
    }

//    @Before
//    fun setup() {
//        MockitoAnnotations.initMocks(this)
//        val app = InstrumentationRegistry.getTargetContext().applicationContext as App
//        val testAppComponent = DaggerTestAppComponent.builder()
//                .appModule(AppModule(app))
//                .netModule(NetModule())
//                .dataModule(DataModule())
//                .threadModule(ThreadModule())
//                .build()
//        app.appComponent = testAppComponent
//        testAppComponent.inject(this)
//    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
    }


    @Test
    fun testLoadIng() {

        onView(withId(R.id.gitResultActivitySwipeLayout)).check(matches(not<View>(isDisplayed())))
        onView(withId(R.id.gitResultActivityProgressBar)).check(matches(isDisplayed()))

    }

    @Test
    fun tesRv() {

        onView(withId(R.id.gitResultActivityRv)).check(matches(isDisplayed()))
        onView(withId(R.id.gitResultActivityRv)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

    }

    @Test
    fun testSearch() {

        onView(withId(R.id.action_search)).perform(ViewActions.clearText()).perform(ViewActions.typeText(DUMMY_SEARCH), closeSoftKeyboard())
        onView(withId(R.id.gitResultActivityProgressBar)).check(matches(isDisplayed()))
        onView(withId(R.id.gitResultActivityRv)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

    }
}