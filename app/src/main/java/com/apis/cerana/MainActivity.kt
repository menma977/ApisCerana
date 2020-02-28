package com.apis.cerana

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.apis.cerana.controller.AuthController
import com.apis.cerana.model.User
import com.apis.cerana.view.HomeActivity
import java.util.*
import kotlin.concurrent.schedule

class MainActivity : AppCompatActivity() {

  private lateinit var goto: Intent

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    Timer().schedule(100) {
      if (User(applicationContext).token.isEmpty()) {
        goto = Intent(applicationContext, LoginActivity::class.java)
        finishAndRemoveTask()
        startActivity(goto)
      } else {
        val response = AuthController(User(applicationContext).token).execute().get()
        if (response["code"] == 200) {
          goto = Intent(applicationContext, HomeActivity::class.java)
          finishAndRemoveTask()
          startActivity(goto)
        } else {
          goto = Intent(applicationContext, LoginActivity::class.java)
          finishAndRemoveTask()
          startActivity(goto)
        }
      }
    }
  }
}
