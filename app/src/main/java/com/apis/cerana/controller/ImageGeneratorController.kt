package com.apis.cerana.controller

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import java.lang.Exception
import java.net.URL

class ImageGeneratorController(private var url: String) : AsyncTask<Void, Void, Bitmap>() {
  override fun doInBackground(vararg params: Void?): Bitmap {
    return try {
      val urlImage: URL = if (url.isEmpty()) {
        URL("https://malahabel.com/dist/img/ApisMelli.png")
      } else {
        URL(url)
      }
      BitmapFactory.decodeStream(urlImage.openConnection().getInputStream())
    } catch (e: Exception) {
      e.printStackTrace()
      val url =
        URL("https://malahabel.com/dist/img/ApisMelli.png")
      BitmapFactory.decodeStream(url.openConnection().getInputStream())
    }
  }
}