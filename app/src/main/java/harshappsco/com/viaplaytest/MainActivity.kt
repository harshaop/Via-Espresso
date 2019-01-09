package harshappsco.com.viaplaytest

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.google.gson.GsonBuilder
import harshappsco.com.viaplaytest.database.SectionsDbTable
import harshappsco.com.viaplaytest.idlingResources.EspressoOkhttpIdlingResources
import harshappsco.com.viaplaytest.idlingResources.EspressoTestIdlingResource
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rv_sections.layoutManager = LinearLayoutManager(this)
        // rv_sections.adapter = SectionsAdapter() //commenting the adapter and moving to runOn UIThread
        fetchJson()
    }

    private fun fetchJson() {
        EspressoTestIdlingResource.increment()
        val url = "https://content.viaplay.se/androiddash-se"
        val request = Request.Builder().url(url).build()

        val client = OkHttpClient()
        EspressoOkhttpIdlingResources().registerOkHttp(client)

        client.newCall(request).enqueue(object : Callback {

            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()
                val jsonArray = extractFeatureFromJson(body.toString()).toString()
                val gson = GsonBuilder().create()
                val sections = gson.fromJson(jsonArray, Array<Sections>::class.java)

                SectionsDbTable(applicationContext).deleteTableSections()
                sections.forEach { item ->
                    SectionsDbTable(applicationContext).storeSections(item)
                }

                runOnUiThread {
                    rv_sections.adapter = SectionsAdapter(sections)
                    EspressoTestIdlingResource.decrement()
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                Log.d("OnFailure", "Connection Failed: Failed to execute request for JSON")
                val sectionsStored = SectionsDbTable(applicationContext).readSections()
                val sectionsArray = sectionsStored.toTypedArray()
                runOnUiThread {
                    rv_sections.adapter = SectionsAdapter(sectionsArray)
                    EspressoTestIdlingResource.decrement()
                }

            }
        })
    }

    private fun extractFeatureFromJson(ViaplayResponse: String): JSONArray {
        val baseJsonResponse = JSONObject(ViaplayResponse)
        val linksArray = baseJsonResponse.getJSONObject("_links")
        val sectionsList = linksArray.getJSONArray("viaplay:sections")
        return sectionsList

    }

}

