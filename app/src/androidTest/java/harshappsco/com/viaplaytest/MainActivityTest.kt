package harshappsco.com.viaplaytest


import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.IdlingRegistry
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.intent.Intents
import android.support.test.espresso.intent.matcher.ComponentNameMatchers
import android.support.test.espresso.intent.matcher.IntentMatchers
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.espresso.matcher.BoundedMatcher
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import android.view.View
import harshappsco.com.viaplaytest.CustomViewHolder.Companion.SECTIONS_TITLE_KEY
import harshappsco.com.viaplaytest.CustomViewHolder.Companion.SECTION_HREF_KEY
import harshappsco.com.viaplaytest.CustomViewHolder.Companion.SECTION_ID_KEY
import harshappsco.com.viaplaytest.idlingResources.EspressoTestIdlingResource
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val intentsTestRule = IntentsTestRule(MainActivity::class.java)


    @Before
    fun registerIdlingResource() {
        // let espresso know to synchronize with background tasks
        IdlingRegistry.getInstance().register(EspressoTestIdlingResource.getIdlingResource())
    }

    @After
    fun unregisterIdlingResource() {
        //unregister the resource after the test
        IdlingRegistry.getInstance().unregister(EspressoTestIdlingResource.getIdlingResource())
    }

    @Test // Tests if all the sections are displayed
    fun testAllSections_existsTrue() {
        for (i in sectionsExpectedArray.indices) {
            onView(withId(R.id.rv_sections))
                .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(i))
                .check(
                    matches(
                        atPosition(
                            i, hasDescendant(
                                hasDescendant(
                                    allOf(
                                        withId(R.id.tv_section_row_title),
                                        withText(sectionsExpectedArray[i])
                                    )
                                )
                            )
                        )
                    )
                )
        }

        //Test Title Bar on the Main Screen
        onView(
            allOf(withText("Viaplay"), withParent(withId(R.id.action_bar)))
        )
            .check(matches(isDisplayed()))
    }

    @Test //Tests the navigation from RecyclerView Sections to the corresponding sections checks assert if all the data is present
    fun testAllSections_navigateToAndBack() {
        for (i in sectionsExpectedArray.indices) {

            //Click on the RecyclcerView
            onView(withId(R.id.rv_sections))
                .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(i, click()))

            //Check the app bar title of the Sections Screen
            onView(withId(R.id.action_bar))
                .check(matches(hasDescendant(withText(sectionsExpectedArray[i]))))

            //Check the title of the Section
            onView(withId(R.id.tv_sectionTitle))
                .check(matches(withText(sectionsTitleArray[i])))

            //Check the title of the Section
            onView(withId(R.id.tv_sectionDescription))
                .check(matches(withText(sectionsDescriptionArray[i])))

            //click on the back button to go back to the main screen
            onView(
                allOf(withContentDescription("Navigate up"), withParent(withId(R.id.action_bar)))
            )
                .perform(click())
        }
    }

    @Test // Test outgoing intents
    fun sectionIntended(){
        val context = InstrumentationRegistry.getTargetContext()

        onView(withId(R.id.rv_sections))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(6, click()))

        Intents.intended(IntentMatchers.hasExtra(SECTIONS_TITLE_KEY,sectionsExpectedArray[6]))
        Intents.intended(IntentMatchers.hasExtra(SECTION_HREF_KEY,testHref))
        Intents.intended(IntentMatchers.hasExtra(SECTION_ID_KEY, testSectionId))
        Intents.intended(IntentMatchers.hasComponent(ComponentNameMatchers.hasClassName("harshappsco.com.viaplaytest.SectionActivity")))
    }
    private fun atPosition(position: Int, itemMatcher: Matcher<View>): Matcher<View> {
        checkNotNull(itemMatcher)
        return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("has item at position $position: ")
                itemMatcher.describeTo(description)
            }

            override fun matchesSafely(view: RecyclerView): Boolean {
                val viewHolder = view.findViewHolderForAdapterPosition(position)
                    ?: // has no item on such position
                    return false
                return itemMatcher.matches(viewHolder.itemView)
            }
        }
    }

}
