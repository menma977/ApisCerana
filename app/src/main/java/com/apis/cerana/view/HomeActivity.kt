package com.apis.cerana.view

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.apis.cerana.MainActivity
import com.apis.cerana.R
import com.apis.cerana.config.Loading
import com.apis.cerana.controller.LogoutController
import com.apis.cerana.model.User
import com.apis.cerana.view.fragment.HomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*
import kotlin.concurrent.schedule

class HomeActivity : AppCompatActivity() {

  private lateinit var loading: Loading
  private lateinit var logout: ImageButton
  private lateinit var navView: BottomNavigationView
  private lateinit var goTo: Intent

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_home)

    loading = Loading(this)
    logout = findViewById(R.id.logoutButton)
    navView = findViewById(R.id.nav_view)
    navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
    loading.openDialog()

    val fragment = HomeFragment()
    addFragment(fragment)

    logout.setOnClickListener {
      loading.openDialog()
      Timer().schedule(1000) {
        val logoutResponse = LogoutController(User(applicationContext).token).execute().get()
        if (logoutResponse["code"] == 200) {
          runOnUiThread {
            goTo = Intent(applicationContext, MainActivity::class.java)
            User(applicationContext).clear()
            loading.closeDialog()
            startActivity(goTo)
          }
        } else {
          runOnUiThread {
            loading.closeDialog()
            Toast.makeText(
              applicationContext,
              applicationContext.getString(
                logoutResponse["response"]
                  .toString()
                  .toInt()
              ),
              Toast.LENGTH_SHORT
            ).show()
          }
        }
      }
    }

    loading.closeDialog()
  }

  private val onNavigationItemSelectedListener =
    BottomNavigationView.OnNavigationItemSelectedListener { item ->
      when (item.itemId) {
        R.id.navigation_home -> {
          val fragment = HomeFragment()
          addFragment(fragment)
          return@OnNavigationItemSelectedListener true
        }
      }
      false
    }

  @SuppressLint("PrivateResource")
  private fun addFragment(fragment: Fragment) {
    supportFragmentManager.beginTransaction()
      .setCustomAnimations(
        R.anim.design_bottom_sheet_slide_in,
        R.anim.design_bottom_sheet_slide_out
      ).replace(R.id.contentFragment, fragment, fragment.javaClass.simpleName).commit()
  }
}
