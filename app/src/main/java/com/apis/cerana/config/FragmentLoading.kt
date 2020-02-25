package com.apis.cerana.config

import android.app.Dialog
import androidx.fragment.app.FragmentActivity
import android.R.style.Theme_Translucent_NoTitleBar
import com.apis.cerana.R

class FragmentLoading(fragmentActivity: FragmentActivity) {
  private val dialog = Dialog(fragmentActivity, Theme_Translucent_NoTitleBar)

  init {
    val view = fragmentActivity.layoutInflater.inflate(R.layout.loading_model, null)
    dialog.setContentView(view)
    dialog.setCancelable(false)
  }

  fun openDialog() {
    dialog.show()
  }

  fun closeDialog() {
    dialog.dismiss()
  }
}