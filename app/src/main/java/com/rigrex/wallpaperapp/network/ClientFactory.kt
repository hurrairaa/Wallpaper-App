package com.rigrex.wallpaperapp.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.ConnectionSpec
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

class ClientFactory {
    companion object {

        /**
         * Provide the retrofit object using which you can call the methods to produce network api hits
         *
         *@param[gson] Gson object basically thi is the object that converts your
         * json response into the class so you dont have to create objects yourself
         * @param[baseUrl] basically this is the url tat never changed for an api
         * and api has one endpoint basically the server addressso you can add your api afterwards
         * @sample{
         *  //For example Api Urls are
         *  https://rigrex.com/api/function
         *  https://rigrex.com/api/function2
         *
         *  so basically in the upper two addresses we have a common base url 'https://rigrex.com/api/'!!!!!!!!!1
         *  @param[timeOut] this the time after which the network request fail if there is no response from the endpoint server
         *  @param[tag] tag to show the incoming data in the Logcat the full data will be shown
         *  @param[header] this is the headers to send to with the request
         *  REMEMBER the keys of the header is used to recognize it in request
         *  @sample{
         *      if you want to send type:"Json/app"
         *      mapOf("type" to "Json/App")
         * }
         *
         * please refer to [ApiClient] for its uses
         *
         * */
        private fun provideClient(
            gson: Gson,
            baseUrl: String,
            timeOut: Long,
            tag: String,
            header: Map<String, String>
        ): Retrofit {
            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(getHttpClient(timeOut, tag, header))
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava3CallAdapterFactory.createAsync())
                .build()
        }

        /**
         * same as above method with default Gson Parameter so you dont have to initialzie the Gson Factory
         * please refer to the [provideClient] for ore info
         *
         *
         * */

        fun provideClient(
            baseUrl: String,
            timeOut: Long,
            tag: String,
            header: Map<String, String>
        ): Retrofit {
            return provideClient(getGson(), baseUrl, timeOut, tag, header)
        }

        /**Return the default Gson factory that converts your
         * response into objects
         *
         * */

        private fun getGson(): Gson {
            return GsonBuilder().setLenient().create()
        }

        /**Type of connection you want with the server Modern TLS is used we
         * can also use CLEAR_TEXT_TRAFFIC bet let it default change it in case server
         * does not support this format
         *
         * */
        private fun getConnectionSpec(): List<ConnectionSpec> {
            return listOf(ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS).build())
        }

        /**This is provides the OkHttp CLient means the compelete info
         * about the client can be presented in it it is used by retrofit to request
         *
         * it params details are provide in the [provideClient] so please refeer to it
         *
         * */
        private fun getHttpClient(
            timeOut: Long,
            tag: String,
            header: Map<String, String>
        ): OkHttpClient {
            return OkHttpClient().newBuilder()
//                .proxy(Proxy.NO_PROXY)
                .connectTimeout(timeOut, TimeUnit.SECONDS)
                .connectionSpecs(getConnectionSpec())
                .readTimeout(timeOut, TimeUnit.SECONDS)
                .writeTimeout(timeOut, TimeUnit.SECONDS)
                .addInterceptor(getResponseInterceptor(tag))
                .addInterceptor { chain: Interceptor.Chain ->
                    val original = chain.request()
                    val requestBuilder = original.newBuilder().apply {
                        header.forEach {
                            addHeader(it.key, it.value)
                        }
                    }
                    val request = requestBuilder.build()
                    chain.proceed(request)
                }.build()
        }

        /**provide the interceptor that will intercept the request to log its data
         * @param[tag] this is is the tag with which it will print the response from network to the LogCat
         *
         * @return[Interceptor] this is used to print the stream to the log with give [tag]
         *
         * */
        private fun getResponseInterceptor(tag: String): Interceptor {
            return HttpLoggingInterceptor { message -> Logg.ii(tag, message) }.apply { level = HttpLoggingInterceptor.Level.BODY }
        }
    }
}