package com.akiraito.tabrss

import okhttp3.OkHttpClient
import okhttp3.Request

class RssParserTask{
    fun httpGET(url : String): String? {
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        val responce = client.newCall(request).execute()
        return responce.body()?.string()
    }
}