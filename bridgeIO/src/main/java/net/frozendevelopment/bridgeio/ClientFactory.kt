package net.frozendevelopment.bridgeio

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import net.frozendevelopment.bridgeio.extensions.applyBridgeHeaders
import net.frozendevelopment.bridgeio.extensions.applyBridgeHost
import net.frozendevelopment.bridgeio.extensions.applyBridgePath
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ClientFactory {

    private val bridgeInterceptor = Interceptor { chain ->
        val url = chain.request().url.newBuilder()
            .applyBridgeHost(BridgeContext.host)
            .applyBridgePath(BridgeContext.token, chain.request().url.pathSegments)
            .build()

        val request = chain.request().newBuilder()
            .applyBridgeHeaders()
            .url(url)
            .build()

        chain.proceed(request)
    }


    fun <T> buildService(clazz: Class<T>) : T {

        val client: OkHttpClient = OkHttpClient
            .Builder()
            .callTimeout(1, TimeUnit.MINUTES)
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .addInterceptor(bridgeInterceptor)
            .build()

        // this will convert the apis snake_case names to our expected camelCase names!
        val gson = GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            .create()

        val retrofit = Retrofit.Builder()
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl("http://${BridgeContext.host}")
            .build()

        return retrofit.create(clazz)
    }

}
