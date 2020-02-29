package com.apis.cerana

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.apis.cerana.config.Loading
import com.apis.cerana.controller.LoginController
import com.apis.cerana.model.User
import com.apis.cerana.view.HomeActivity
import org.json.JSONObject
import java.util.*
import kotlin.concurrent.schedule

class LoginActivity : AppCompatActivity() {

  private lateinit var login: Button
  private lateinit var username: EditText
  private lateinit var password: EditText
  private lateinit var goTo: Intent
  private lateinit var loading: Loading

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login)

    login = findViewById(R.id.loginButton)
    username = findViewById(R.id.usernameEditText)
    password = findViewById(R.id.passwordEditText)

    doRequestPermission()

    loading = Loading(this)

    login.setOnClickListener {
      loading.openDialog()
      Timer().schedule(100) {
        val body = HashMap<String, String>()
        body["username"] = username.text.toString()
        body["password"] = password.text.toString()
        val response = LoginController(body).execute().get()
        runOnUiThread {
          if (response["code"] == 200) {
            val json = JSONObject()
            json.put("token", response["response"].toString())
            json.put("username", "")
            json.put("image", "")
            json.put("status", 0)
            User(applicationContext).set(json.toString())
            goTo = Intent(applicationContext, HomeActivity::class.java)
            loading.closeDialog()
            finishAndRemoveTask()
            startActivity(goTo)
          } else {
            loading.closeDialog()
            try {
              Toast.makeText(
                applicationContext,
                getString(response["response"].toString().toInt()),
                Toast.LENGTH_SHORT
              ).show()
            } catch (e: Exception) {
              Toast.makeText(applicationContext, response["response"].toString(), Toast.LENGTH_SHORT).show()
            }
          }
        }
      }
    }
  }

  private fun doRequestPermission() {
    if (
      ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.CAMERA
      ) != PackageManager.PERMISSION_GRANTED ||
      ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
      ) != PackageManager.PERMISSION_GRANTED ||
      ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.READ_EXTERNAL_STORAGE
      ) != PackageManager.PERMISSION_GRANTED
    ) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        requestPermissions(
          arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
          ), 100
        )
      }
    }
  }
}
