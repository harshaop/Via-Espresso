package harshappsco.com.viaplaytest

import android.os.Bundle

import android.support.v7.app.AppCompatActivity
import com.google.gson.GsonBuilder
import harshappsco.com.viaplaytest.database.SectionEntry
import harshappsco.com.viaplaytest.database.SectionsDbTable
import harshappsco.com.viaplaytest.idlingResources.EspressoTestIdlingResource
import harshappsco.com.viaplaytest.idlingResources.EspressoOkhttpIdlingResources
import kotlinx.android.synthetic.main.sections_activity.*
import okhttp3.*
import java.io.IOException

class SectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sections_activity)

        val href = intent.getStringExtra(CustomViewHolder.SECTION_HREF_KEY)
        val title = intent.getStringExtra(CustomViewHolder.SECTIONS_TITLE_KEY)
        val hrefSplit = splitHref(href)

        supportActionBar?.title = title
        fetchTitleDescription(hrefSplit)
    }

    private fun fetchTitleDescription(href: String) {
        EspressoTestIdlingResource.increment()
        val request = Request.Builder().url(href).build()
        val sectionIdCol = SectionEntry.SECTION_ID + " = ? "
        val id = intent.getStringExtra(CustomViewHolder.SECTION_ID_KEY)
        val whereSel = arrayOf(id)
        val client = OkHttpClient()

        EspressoOkhttpIdlingResources().registerOkHttp(client) // Espresso Idling for Okhttp

        client.newCall(request).enqueue(object : Callback{
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()
                val gson = GsonBuilder().create()

                val titleDescription  = gson.fromJson(body, TitleDescription::class.java)

                SectionsDbTable(applicationContext).deleteTableSectionEntry(sectionIdCol,whereSel)
                SectionsDbTable(applicationContext).storeSectionEntry(titleDescription)

                runOnUiThread{
                    //supportActionBar?.title = sStored.title // used for title fromt the sectionView
                    tv_sectionTitle.text = titleDescription.title
                    tv_sectionDescription.text = titleDescription.description
                    EspressoTestIdlingResource.decrement() // indicates that Espresso can run, now that app is idle
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread{
                    val sStored = SectionsDbTable(applicationContext).readSectionEntry(sectionIdCol,whereSel)
                    tv_sectionTitle.text = sStored.title
                    tv_sectionDescription.text = sStored.description
                    EspressoTestIdlingResource.decrement() // indicates that Espresso can run, now that app is idle
                }
            }
        })
    }

    private fun splitHref(href: String): String {
        val hrefSplit = href.split("{")
        return hrefSplit[0]
    }


}