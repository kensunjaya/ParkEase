package com.example.parkease.utilities

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit
import java.net.SocketTimeoutException

suspend fun fetchValuesWithOkHttp(url: String): List<ParkingLotData>? = withContext(Dispatchers.IO) {
    try {
        val client = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .build()
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).execute().use { response ->
            if (response.isSuccessful) {
                val json = response.body?.string()
                val type = object : TypeToken<List<ParkingLotData>>() {}.type
                Gson().fromJson<List<ParkingLotData>>(json, type)
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