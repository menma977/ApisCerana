package com.apis.cerana.view.fragment

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
import com.apis.cerana.view.qr.QrActivity
import java.util.*
import kotlin.concurrent.schedule

class HomeFragment : Fragment() {

  private lateinit var user: User
  private lateinit var loadingFragment: FragmentLoading
  private lateinit var goTo: Intent
  private lateinit var qrButton: ImageButton
  private lateinit var username: TextView
  private lateinit var balance: TextView

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

    getBalance()

    qrButton.setOnClickListener {
      goTo = Intent(context, QrActivity::class.java)
      startActivity(goTo)
    }
    return view
  }

  private fun getBalance() {
    Timer().schedule(1000) {
      val getBalance = UserController.Balance(user.token).execute().get()
      if (getBalance["code"] == 200) {
        activity?.runOnUiThread {
          username.text = user.username
          balance.text = getBalance["response"].toString()
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
}
