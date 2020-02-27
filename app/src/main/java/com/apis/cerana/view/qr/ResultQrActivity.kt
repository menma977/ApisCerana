package com.apis.cerana.view.qr

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.apis.cerana.R
import com.apis.cerana.config.Loading
import com.apis.cerana.controller.BeeController
import com.apis.cerana.model.User
import java.util.*
import kotlin.concurrent.schedule

class ResultQrActivity : AppCompatActivity() {

  private lateinit var loading: Loading
  private lateinit var user: User
  private lateinit var username: TextView
  private lateinit var qrCode: TextView
  private lateinit var getDate: TextView
  private lateinit var sendDate: TextView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_result_qr)
    username = findViewById(R.id.usernameTextView)
    qrCode = findViewById(R.id.qrCodeTextView)
    getDate = findViewById(R.id.getDateTextView)
    sendDate = findViewById(R.id.sendDateTextView)
    loading = Loading(this)
    loading.openDialog()
    user = User(this)
    val qrResponse = intent.getSerializableExtra("qr").toString()
    Timer().schedule(1000) {
      val body = HashMap<String, String>()
      body["qr"] = qrResponse
      val responseBee = BeeController.Get(user.token, body).execute().get()
      if (responseBee["code"] == 200) {
        runOnUiThread {
          username.text = responseBee.getJSONObject("response")["user"].toString()
          qrCode.text = responseBee.getJSONObject("response")["code"].toString()
          getDate.text = responseBee.getJSONObject("response")["start"].toString()
          sendDate.text = responseBee.getJSONObject("response")["end"].toString()
          loading.closeDialog()
        }
      } else {
        runOnUiThread {
          loading.closeDialog()
          Toast.makeText(applicationContext, responseBee["response"].toString(), Toast.LENGTH_SHORT).show()
        }
      }
    }
  }
}
