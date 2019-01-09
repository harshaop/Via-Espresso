package harshappsco.com.viaplaytest


import android.content.Intent
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso
import android.support.test.espresso.IdlingRegistry
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.hasDescendant
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import harshappsco.com.viaplaytest.idlingResources.EspressoTestIdlingResource
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@LargeTest
@RunWith(AndroidJUnit4::class)
class SectionsActivityTest2 {

    @Rule
    @JvmField
    val activityTestRule = ActivityTestRule(SectionActivity::class.java, true, false)

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

    @Test //Tests the SectionActivity with Intents Passed with Test data
    fun testSection() {


        val targetContext = InstrumentationRegistry.getInstrumentation()
            .targetContext
        val i = Intent(targetContext, SectionActivity::class.java)
        i.putExtra("HREF_KEY", testHref)
        i.putExtra("SECTIONS_TITLE", testTitle)
        i.putExtra("ID_KEY", testSectionId) // to check the DB data when offline
        activityTestRule.launchActivity(i)

        //Check the app bar title of the Sections Screen
        Espresso.onView(ViewMatchers.withId(R.id.action_bar))
            .check(matches(hasDescendant(withText("Section Title"))))

        //Check the title of the Section
        Espresso.onView(ViewMatchers.withId(R.id.tv_sectionTitle))
            .check(matches(withText(sectionsTitleArray[6])))

        //Check the title of the Section
        Espresso.onView(ViewMatchers.withId(R.id.tv_sectionDescription))
            .check(matches(withText(sectionsDescriptionArray[6])))


    }
}

