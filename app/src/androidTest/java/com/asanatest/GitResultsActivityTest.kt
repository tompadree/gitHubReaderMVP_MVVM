package com.githubreader

import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.githubreader.view.activities.GitResults.GitResultsActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.matcher.ViewMatchers
import com.githubreader.di.DaggerAppComponentTest
import com.githubreader.di.module.AppModule
import com.githubreader.di.module.DataModule
import com.githubreader.di.module.NetModule
import com.githubreader.di.module.ThreadModule
import org.hamcrest.Matchers
import org.mockito.Mock
import com.githubreader.App


/**
 * Created by Tom on 24.5.2018..
 */
@RunWith(AndroidJUnit4::class)
class GitResultsActivityTest {

    var gitResultsActivity: GitResultsActivity? = null
    private val DUMMY_SEARCH = "a"

    @Mock
    val netModule = NetModule()

    @Mock
    val dataModule = DataModule()

    @Mock
    val threadModule = ThreadModule()

    @Rule @JvmField
    val activityTestRule = ActivityTestRule<GitResultsActivity>(GitResultsActivity::class.java)

//    @get:Rule
//    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Before
    @Throws(Exception::class)
    fun setUp() {
        this.gitResultsActivity = activityTestRule.activity
    }

    @Before
    fun setup() {
//        MockitoAnnotations.initMocks(this)

        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val app = InstrumentationRegistry.getTargetContext().applicationContext as App

        val testAppComponent = DaggerAppComponentTest.builder()
                .appModule(AppModule(app))
                .netModule(netModule)
                .dataModule(dataModule)
                .threadModule(threadModule)
                .build()
        app.appComponent = testAppComponent
        testAppComponent.inject(this)

        activityTestRule.launchActivity(null)

    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
    }

    @Test
    fun isDisplayed() {

//        onView(withId(R.id.action_search)).check(matches(isDisplayed()))
//        onView(withId(R.id.gitResultActivityRv)).check(matches(isDisplayed()))
//
//        onView(withId(R.id.gitResultActivitySwipeLayout)).check(matches(isDisplayed()))
//        onView(withId(R.id.gitResultActivityProgressBar)).check(matches(isDisplayed()))

        onView(Matchers.allOf(ViewMatchers.withId(R.id.gitResultActivityRv)))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        onView(Matchers.allOf(ViewMatchers.withId(R.id.action_search)))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

    }
//
//    @Test
//    fun tesRv() {
//
////        onView(withId(R.id.gitResultActivityRv)).check(matches(isDisplayed()))
////        onView(withId(R.id.gitResultActivityRv)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
//
//        onView(RecyclerViewMatcher(R.id.gitResultActivityRv).atPositionOnView(0, R.id.repo_item_name_text_view))
//                .check(ViewAssertions.matches(ViewMatchers.withText("angular/a"))) // fmtn/a
//    }

//    @Test
//    fun testSearch() {
//
//        onView(withId(R.id.action_search)).perform(ViewActions.clearText()).perform(ViewActions.typeText(DUMMY_SEARCH), closeSoftKeyboard())
//        onView(withId(R.id.gitResultActivityProgressBar)).check(matches(isDisplayed()))
//        onView(withId(R.id.gitResultActivityRv)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
//
//    }
//
//
//  @Test
//    fun rvClick() {
//
//        onView(withId(R.id.gitResultActivityRv)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
//
//    }
}