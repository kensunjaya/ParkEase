package com.example.parkease

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit
import java.net.SocketTimeoutException
import com.google.firebase.Timestamp

suspend fun fetchValuesWithOkHttp(url: String): List<Item>? = withContext(Dispatchers.IO) {
    try {
        val client = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build()
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).execute().use { response ->
            if (response.isSuccessful) {
                val json = response.body?.string()
                val type = object : TypeToken<List<Item>>() {}.type
                Gson().fromJson<List<Item>>(json, type)
            } else {
                null
            }
        }
    } catch (e: SocketTimeoutException) {
        println(e)
        emptyList()
    }

    catch (e: Exception) {
        println(e)
        null
    }
}

data class Item(
    val id: String,
    val status: Int
)

data class ActiveParking(
    val id: String = "",
    val start: Timestamp? = null
)

data class User(
    val activeParking: ActiveParking? = null,
    val name: String = ""
)