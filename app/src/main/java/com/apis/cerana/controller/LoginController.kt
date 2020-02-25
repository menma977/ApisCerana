package com.apis.cerana.controller

import android.os.AsyncTask
import com.apis.cerana.R
import com.apis.cerana.config.Converter
import com.apis.cerana.model.Url
import com.squareup.okhttp.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader

class LoginController(private var body: HashMap<String, String>) : AsyncTask<Void, Void, JSONObject>() {
  override fun doInBackground(vararg params: Void?): JSONObject {
    return try {
      val client = OkHttpClient()
      val mediaType: MediaType = MediaType.parse("application/x-www-form-urlencoded")
      val sendBody = RequestBody.create(mediaType, Converter().map(body))
      val request: Request = Request.Builder()
        .url("${Url.get()}/login")
        .method("POST", sendBody)
        .addHeader("X-Requested-With", "XMLHttpRequest")
        .build()
      val response: Response = client.newCall(request).execute()
      val input = BufferedReader(InputStreamReader(response.body().byteStream()))

      val inputData: String = input.readLine()
      val convertJSON = JSONObject(inputData)
      input.close()
      return if (response.isSuccessful) {
        JSONObject().put("code", response.code()).put("response", convertJSON["response"])
      } else {
        JSONObject().put("code", response.code()).put(
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