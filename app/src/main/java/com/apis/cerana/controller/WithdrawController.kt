package com.apis.cerana.controller

import android.os.AsyncTask
import com.apis.cerana.R
import com.apis.cerana.config.Converter
import com.apis.cerana.model.Url
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader

class WithdrawController {
  class Validate(private val token: String) : AsyncTask<Void, Void, JSONObject>() {
    override fun doInBackground(vararg params: Void?): JSONObject {
      try {
        val client = OkHttpClient()
        val request: Request = Request.Builder()
          .url("${Url.get()}/withdraw/show")
          .addHeader("X-Requested-With", "XMLHttpRequest")
          .addHeader(
            "Authorization",
            "Bearer $token"
          ).build()
        val response: Response = client.newCall(request).execute()
        val input = BufferedReader(InputStreamReader(response.body?.byteStream()))

        val inputData: String = input.readLine()
        val convertJSON = JSONObject(inputData)
        input.close()
        return if (response.isSuccessful) {
          JSONObject().put("code", response.code).put("response", convertJSON["response"])
        } else {
          JSONObject().put("code", response.code).put("response", convertJSON["message"])
        }
      } catch (e: Exception) {
        e.printStackTrace()
        return JSONObject().put("code", 500).put("response", R.string.code_500)
      }
    }
  }

  class Post(private val token: String, private var body: HashMap<String, String>) :
    AsyncTask<Void, Void, JSONObject>() {
    override fun doInBackground(vararg params: Void?): JSONObject {
      return try {
        val client = OkHttpClient()
        val mediaType: MediaType = "application/x-www-form-urlencoded".toMediaType()
        val sendBody = Converter().map(body).toRequestBody(mediaType)
        val request: Request = Request.Builder()
          .url("${Url.get()}/withdraw/store")
          .post(sendBody)
          .addHeader("X-Requested-With", "XMLHttpRequest")
          .addHeader("Authorization", "Bearer $token")
          .build()
        val response: Response = client.newCall(request).execute()
        val input = BufferedReader(InputStreamReader(response.body?.byteStream()))

        val inputData: String = input.readLine()
        val convertJSON = JSONObject(inputData)
        input.close()
        return if (response.isSuccessful) {
          JSONObject().put("code", response.code).put("response", convertJSON["response"])
        } else {
          JSONObject().put("code", response.code).put(
            "response", convertJSON
              .getJSONObject("errors")
              .getJSONArray(
                convertJSON
                  .getJSONObject("errors")
                  .names()[0]
                  .toString()
              )[0]
          )
        }
      } catch (e: Exception) {
        JSONObject().put("code", 500).put("response", R.string.code_500)
      }
    }
  }
}