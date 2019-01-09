package harshappsco.com.viaplaytest.idlingResources

import android.support.test.espresso.IdlingRegistry
import com.jakewharton.espresso.OkHttp3IdlingResource
import okhttp3.OkHttpClient

class EspressoOkhttpIdlingResources {
    fun registerOkHttp(client: OkHttpClient) {
        IdlingRegistry.getInstance().register(
            OkHttp3IdlingResource.create(
                "okhttp", client
            )
        )
    }
}



