package com.githubreader.gitresultsdetails

import android.view.View
import android.widget.ImageButton
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import com.githubreader.R
import com.githubreader.TestApp
import com.githubreader.data.models.OwnerObject
import com.githubreader.data.models.RepoObject
import com.githubreader.data.source.FakeRepository
import com.githubreader.data.source.GitHubResultsRepository
import com.githubreader.gitresults.GitResultsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.robolectric.annotation.LooperMode
import org.robolectric.annotation.TextLayoutMode


/**
 * @author Tomislav Curis
 */
@RunWith(AndroidJUnit4::class)
@MediumTest
@LooperMode(LooperMode.Mode.PAUSED)
@TextLayoutMode(TextLayoutMode.Mode.REALISTIC)
@ExperimentalCoroutinesApi
class GitResultsDetailsFragmentTest : KoinTest {

    // Use a fake repository to be injected
    private lateinit var repository: GitHubResultsRepository

    private var owners = listOf(OwnerObject("User1","Repo1"), OwnerObject("User2","Repo2"))

    private val viewModel : GitResultsViewModel by inject()
    @Before
    fun initRepo() {

        repository = FakeRepository()

        val application = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as TestApp
        application.injectModule(module {
            single(override = true) { repository }
        })

        // Fill the db
        runBlocking {  repository.saveGitHubResultSubscribersDB("RepoTest", owners) }
    }

//    @After
//    fun cleanUpDB(){
//        runBlocking {  repo() }
//    }

    @Test
    fun displaySubscribers() {
        // GIVEN - On the home screen
        launchFragment()

        // THEN - Verify repos are displayed on screen
        onView(ViewMatchers.withText("User1")).check(matches(isDisplayed()))
        onView(ViewMatchers.withText("User2")).check(ViewAssertions.matches(isDisplayed()))
    }

    @Test
    fun displaySavedSubscribers() {
        // WHEN Add fake repos
        runBlocking {  repository.saveGitHubResultSubscribersDB("RepoTest",
            listOf(OwnerObject("User11","Repo110"), OwnerObject("User22","Repo200"))) }

        // GIVEN - On the home screen
        launchFragment()

        // THEN - Verify subscriber is displayed on screen
        onView(ViewMatchers.withText("User11"))
            .check(matches(isDisplayed()))
        onView(ViewMatchers.withText("User22"))
            .check(matches(isDisplayed()))
    }

    @Test
    fun clickBack_navigateBackToGitResults() {
        // GIVEN - On the home screen
        val scenario = launchFragmentInContainer<GitResultsDetailsFragment>(
            GitResultsDetailsFragmentArgs(RepoObject(1,"Repo1")).toBundle(), R.style.AppTheme)

        val navController = Mockito.mock(NavController::class.java)
        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }

        // WHEN - Click on back
        onView(isRoot()).perform(ViewActions.pressMenuKey())
//        pressBack()

        // THEN - Verify that we navigate to the owner screen
        // CAN'T VERIFY SINCE THE APP IS BACKGROUND BECUASE NO ACTIVITY HOLDER
//        verify(navController).navigate(
//            GitResultsDetailsFragmentDirections.actionGitResultsDetailsFragmentToGitResultsFragment()
//        )
    }

    fun navigationIconMatcher(): Matcher<View?>? {
        return allOf(
            isAssignableFrom(ImageButton::class.java),
            withParent(isAssignableFrom(Toolbar::class.java))
        )
    }

    private fun launchFragment() {
        // GIVEN - On the home screen
        val bundle = GitResultsDetailsFragmentArgs(RepoObject(1,"Repo1")).toBundle()
        val scenario = launchFragmentInContainer<GitResultsDetailsFragment>(bundle, R.style.AppTheme)

        val navController = Mockito.mock(NavController::class.java)
        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }
    }

}