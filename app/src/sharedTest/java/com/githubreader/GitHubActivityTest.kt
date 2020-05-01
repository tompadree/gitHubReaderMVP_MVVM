package com.githubreader

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.currencytrackingapp.util.DataBindingIdlingResource
import com.currencytrackingapp.util.monitorActivity
import com.currencytrackingapp.utils.EspressoIdlingResource
import com.githubreader.base.GitHubActivity
import com.githubreader.data.models.OwnerObject
import com.githubreader.data.models.RepoObject
import com.githubreader.data.source.FakeRepository
import com.githubreader.data.source.GitHubResultsRepository
import com.githubreader.gitresults.GitResultsViewModel
import com.githubreader.gitresultsdetails.GitResultsDetailsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject

/**
 * @author Tomislav Curis
 *
 * Large End-to-End test.
 *
 * UI tests usually use [ActivityTestRule] but there's no API to perform an action before
 * each test. The workaround is to use `ActivityScenario.launch()` and `ActivityScenario.close()`.
 */

@RunWith(AndroidJUnit4::class)
@LargeTest
@ExperimentalCoroutinesApi
class GitHubActivityTest : KoinTest {

    // Use a fake repository to be injected
    private lateinit var repository: GitHubResultsRepository

    // An Idling Resource that waits for Data Binding to have no pending bindings
    private val  dataBindingIdlingResource = DataBindingIdlingResource()

    private var owners = listOf(OwnerObject("User1","Repo1"), OwnerObject("User2","Repo2"))
    private var repos = listOf(RepoObject(1,"Repo1"), RepoObject(2,"Repo2"))

    private val gitResultsViewModel : GitResultsViewModel by inject()
    private val gitResultsDetailsViewModel : GitResultsDetailsViewModel by inject()

    @Before
    fun initRepo() {

        repository = FakeRepository()

        val application = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as TestApp
        application.injectModule(module {
            single(override = true) { repository }
        })

        // Fill the db
        runBlocking {  repository.saveGitHubResultsDB("Repo1", repos) }
        runBlocking {  repository.saveGitHubResultSubscribersDB("Repo1", owners) }
    }

    // Register IdlingResource
    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().register(dataBindingIdlingResource)
    }


    //Unregister Idling Resource so it can be garbage collected and does not leak any memory.
    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().unregister(dataBindingIdlingResource)
    }

    @Test
    fun displayRepos() {
        // Start up Activity screen and start monitor
        val activityScenario = ActivityScenario.launch(GitHubActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // THEN - Verify repos are displayed on screen
        onView(withText("Repo1")).check(matches(isDisplayed()))
        onView(withText("Repo2")).check(matches(isDisplayed()))

        activityScenario.close()
    }

    @Test
    fun displaySavedRepos() {
        // WHEN Add fake repos
        runBlocking {  repository.saveGitHubResultsDB("RepoTest",
            listOf(RepoObject(100,"Repo11"), RepoObject(200,"Repo22"))) }

        // Start up Activity screen and start monitor
        val activityScenario = ActivityScenario.launch(GitHubActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // THEN - Verify repo is displayed on screen
        onView(withText("Repo11")).check(matches(isDisplayed()))
        onView(withText("Repo22")).check(matches(isDisplayed()))

        activityScenario.close()
    }

    @Test
    fun onSearchClickAndSearchText() {

        // Start up Activity screen and start monitor
        val activityScenario = ActivityScenario.launch(GitHubActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // WHEN - Enter the search term
        onView(withId(R.id.action_search)).perform(ViewActions.click())
        onView(withId(R.id.search_src_text)).perform(replaceText("Repo"))
        gitResultsViewModel.onSearchTextChanged("Repo")

        onView(withText("Repo1")).check(matches(isDisplayed()))
        onView(withText("Repo2")).check(matches(isDisplayed()))

        onView(withId(R.id.search_src_text)).perform(replaceText("kfhsafh"))
        gitResultsViewModel.onSearchTextChanged("kfhsafh")

        onView(withText("Repo1")).check(matches(isDisplayed()))
//        onView(withText("Repo2")).check(doesNotExist())

        activityScenario.close()
    }

    @Test
    fun clickRepo_navigateGitResultsDetailsFragment() {
        // Start up Activity screen and start monitor
        val activityScenario = ActivityScenario.launch(GitHubActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // WHEN - Click on the list item
        onView(withId(R.id.gitResultFragRv))
            .perform(
                RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(hasDescendant(withText("Repo1")), click()
                ))

        // THEN - Verify that we navigate to the owner screen
        // THEN - Verify repos are displayed on screen
        onView(withText("User1")).check(matches(isDisplayed()))
        onView(withText("User2")).check(matches(isDisplayed()))

        activityScenario.close()
    }

    @Test
    fun displaySavedSubscribers() {
        // WHEN Add fake repos
        runBlocking {  repository.saveGitHubResultSubscribersDB("RepoTest",
            listOf(OwnerObject("User11","Repo1"), OwnerObject("User22","Repo1"))) }

        // Start up Activity screen and start monitor
        val activityScenario = ActivityScenario.launch(GitHubActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // WHEN - Click on the list item
        onView(withId(R.id.gitResultFragRv))
            .perform(
                RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(hasDescendant(withText("Repo1")), click()
                ))

        // THEN - Verify subscriber is displayed on screen
        onView(withText("User11"))
            .check(matches(isDisplayed()))
        onView(withText("User22"))
            .check(matches(isDisplayed()))
    }

    @Test
    fun clickBack_navigateBackToGitResults() {
        // Start up Activity screen and start monitor
        val activityScenario = ActivityScenario.launch(GitHubActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // WHEN - Click on the list item
        onView(withId(R.id.gitResultFragRv))
            .perform(
                RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(hasDescendant(withText("Repo1")), click()
                ))

        // WHEN - Click on back
        pressBack()

        // THEN - Verify repos are displayed on screen
        onView(withText("Repo1")).check(matches(isDisplayed()))
        onView(withText("Repo2")).check(matches(isDisplayed()))

        activityScenario.close()
    }

}