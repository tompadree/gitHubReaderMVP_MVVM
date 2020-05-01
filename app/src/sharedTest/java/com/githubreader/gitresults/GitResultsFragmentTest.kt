package com.githubreader.gitresults

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import com.githubreader.R
import com.githubreader.TestApp
import com.githubreader.data.models.RepoObject
import com.githubreader.data.source.FakeRepository
import com.githubreader.data.source.GitHubResultsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import org.mockito.Mockito
import org.mockito.Mockito.verify

/**
 * @author Tomislav Curis
 */

@RunWith(AndroidJUnit4::class)
@MediumTest
@ExperimentalCoroutinesApi
class GitResultsFragmentTest : KoinTest {

    // Use a fake repository to be injected
    private lateinit var repository: GitHubResultsRepository

    private var repos = listOf(RepoObject(1,"Repo1"), RepoObject(2,"Repo2"))

    private val viewModel : GitResultsViewModel by inject()
    @Before
    fun initRepo() {

        repository = FakeRepository()

        val application = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as TestApp
        application.injectModule(module {
            single(override = true) { repository }
        })

        // Fill the db
        runBlocking {  repository.saveGitHubResultsDB("RepoTest", repos) }
    }

    @Test
    fun displayRepos() {
        // GIVEN - On the home screen
        launchFragment()

        // THEN - Verify repos are displayed on screen
        onView(withText("Repo1")).check(matches(isDisplayed()))
        onView(withText("Repo2")).check(matches(isDisplayed()))
    }

    @Test
    fun displaySavedRepos() {
        // WHEN Add fake repos
        runBlocking {  repository.saveGitHubResultsDB("RepoTest",
            listOf(RepoObject(100,"Repo11"), RepoObject(200,"Repo22"))) }

        // GIVEN - On the home screen
        launchFragment()

        // THEN - Verify repo is displayed on screen
        onView(withText("Repo11")).check(matches(isDisplayed()))
        onView(withText("Repo22")).check(matches(isDisplayed()))
    }

    @Test
    fun onSearchClickAndSearchText() {

        // GIVEN - On the home screen
        launchFragment()

        // WHEN - Enter the search term
        onView(withId(R.id.action_search)).perform(ViewActions.click())
        onView(withId(R.id.search_src_text)).perform(replaceText("Repo"))
        viewModel.onSearchTextChanged("Repo")

        onView(withText("Repo1")).check(matches(isDisplayed()))
        onView(withText("Repo2")).check(matches(isDisplayed()))

        onView(withId(R.id.search_src_text)).perform(replaceText("kfhsafh"))
        viewModel.onSearchTextChanged("kfhsafh")

        onView(withText("Repo1")).check(matches(isDisplayed()))
        onView(withText("Repo2")).check(matches(isDisplayed()))
    }

    @Test
    fun clickRepo_navigateGitResultsDetailsFragment() {
        // GIVEN - On the home screen
        val scenario = launchFragmentInContainer<GitResultsFragment>(Bundle(), R.style.AppTheme)

        val navController = Mockito.mock(NavController::class.java)
        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }

        // WHEN - Click on the list item
        onView(withId(R.id.gitResultFragRv))
            .perform(
                RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(hasDescendant(withText("Repo1")), click()
                ))

        // THEN - Verify that we navigate to the owner screen
        verify(navController).navigate(
            GitResultsFragmentDirections.actionGitResultsFragmentToGitResultsDetailsFragment(repos[0])
        )
    }

    private fun launchFragment() {
        // GIVEN - On the home screen
        val scenario = launchFragmentInContainer<GitResultsFragment>(Bundle(), R.style.AppTheme)

        val navController = Mockito.mock(NavController::class.java)
        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }
    }

}