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
        URL("https://res.cloudinary.com/teepublic/image/private/s--Q_jIsWsv--/t_Resized%20Artwork/c_fit,g_north_west,h_1054,w_1054/co_ffffff,e_outline:53/co_ffffff,e_outline:inner_fill:53/co_bbbbbb,e_outline:3:1000/c_mpad,g_center,h_1260,w_1260/b_rgb:eeeeee/c_limit,f_jpg,h_630,q_90,w_630/v1550196849/production/designs/4204312_0.jpg")
      } else {
        URL(url)
      }
      BitmapFactory.decodeStream(urlImage.openConnection().getInputStream())
    } catch (e: Exception) {
      val url =
        URL("https://res.cloudinary.com/teepublic/image/private/s--Q_jIsWsv--/t_Resized%20Artwork/c_fit,g_north_west,h_1054,w_1054/co_ffffff,e_outline:53/co_ffffff,e_outline:inner_fill:53/co_bbbbbb,e_outline:3:1000/c_mpad,g_center,h_1260,w_1260/b_rgb:eeeeee/c_limit,f_jpg,h_630,q_90,w_630/v1550196849/production/designs/4204312_0.jpg")
      BitmapFactory.decodeStream(url.openConnection().getInputStream())
    }
  }
}