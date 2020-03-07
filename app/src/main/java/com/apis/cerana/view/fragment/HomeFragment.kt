package com.apis.cerana.view.fragment

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.apis.cerana.MainActivity
import com.apis.cerana.R
import com.apis.cerana.config.FragmentLoading
import com.apis.cerana.controller.UserController
import com.apis.cerana.model.User
import com.apis.cerana.view.*
import com.apis.cerana.view.qr.QrActivity
import java.util.*
import kotlin.concurrent.schedule

class HomeFragment : Fragment() {

  private lateinit var user: User
  private lateinit var loadingFragment: FragmentLoading
  private lateinit var goTo: Intent
  private lateinit var username: TextView
  private lateinit var balance: TextView
  private lateinit var qrButton: ImageButton
  private lateinit var requestStup: ImageButton
  private lateinit var withdraw: ImageButton
  private lateinit var addUser: ImageButton
  private lateinit var editProfile: ImageButton
  private lateinit var uploadKTP: ImageButton
  private lateinit var share: ImageButton

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view: View = inflater.inflate(R.layout.fragment_home, container, false)
    user = User(context)
    loadingFragment = FragmentLoading(this.requireActivity())
    loadingFragment.openDialog()
    username = view.findViewById(R.id.usernameTextView)
    balance = view.findViewById(R.id.balanceTextView)
    qrButton = view.findViewById(R.id.qrButton)
    requestStup = view.findViewById(R.id.reqeustStupButton)
    withdraw = view.findViewById(R.id.withdrawButton)
    addUser = view.findViewById(R.id.addUserButton)
    editProfile = view.findViewById(R.id.editProfileButton)
    uploadKTP = view.findViewById(R.id.uploadKTPButton)
    share = view.findViewById(R.id.shareButton)

    getBalance()

    qrButton.setOnClickListener {
      goTo = Intent(context, QrActivity::class.java)
      startActivity(goTo)
    }

    requestStup.setOnClickListener {
      goTo = Intent(context, RequestStupActivity::class.java)
      startActivity(goTo)
    }

    withdraw.setOnClickListener {
      goTo = Intent(context, WithdrawActivity::class.java)
      startActivity(goTo)
    }

    addUser.setOnClickListener {
      goTo = Intent(context, RegisterActivity::class.java)
      startActivity(goTo)
    }

    editProfile.setOnClickListener {
      goTo = Intent(context, EditProfileActivity::class.java)
      startActivity(goTo)
    }

    uploadKTP.setOnClickListener {
      if (user.status == 2) {
        Toast.makeText(this.context, "Upload KTP anda sudah di konfirmasi oleh admin", Toast.LENGTH_SHORT).show()
      } else {
        goTo = Intent(context, ImageActivity::class.java)
        startActivity(goTo)
      }
    }

    share.setOnClickListener {
      shareOnWhatApp()
    }

    return view
  }

  private fun getBalance() {
    Timer().schedule(100) {
      val response = UserController.Balance(user.token).execute().get()
      if (response["code"] == 200) {
        activity?.runOnUiThread {
          username.text = user.username
          balance.text = response["response"].toString()
          loadingFragment.closeDialog()
        }
      } else {
        try {
          activity?.runOnUiThread {
            Toast.makeText(context, activity!!.getString(R.string.code_425), Toast.LENGTH_LONG).show()
            loadingFragment.closeDialog()
          }
        } catch (e: Exception) {
          activity?.runOnUiThread {
            val goTo = Intent(activity?.applicationContext, MainActivity::class.java)
            activity?.finish()
            loadingFragment.closeDialog()
            startActivity(goTo)
          }
        }
      }
    }
  }

  private fun shareOnWhatApp() {
    val whatsAppIntent = Intent(Intent.ACTION_SEND)
    whatsAppIntent.type = "text/plain"
    whatsAppIntent.setPackage("com.whatsapp")
    whatsAppIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.sher_on_what_app))
    try {
      activity!!.startActivity(whatsAppIntent)
    } catch (ex: ActivityNotFoundException) {
      Toast.makeText(this.context, "Whatsapp have not been installed.", Toast.LENGTH_LONG).show()
    }
  }
}
