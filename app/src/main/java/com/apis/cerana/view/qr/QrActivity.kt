package com.apis.cerana.view.qr

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.apis.cerana.R
import com.apis.cerana.config.Loading
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView

class QrActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {

  private lateinit var loading: Loading
  private lateinit var scannerView: ZXingScannerView
  private lateinit var goTo: Intent

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_qr)
    loading = Loading(this)
    scannerView = ZXingScannerView(this)
    setContentView(scannerView)
  }

  override fun onResume() {
    super.onResume()
    scannerView.setResultHandler(this)
    scannerView.startCamera()
  }

  override fun onPause() {
    super.onPause()
    scannerView.stopCamera()
  }

  override fun handleResult(rawResult: Result?) {
    if (rawResult != null) {
      loading.openDialog()
      goTo = Intent(applicationContext, ResultQrActivity::class.java)
      goTo.putExtra("qr", rawResult.text)
      finishAndRemoveTask()
      loading.closeDialog()
      startActivity(goTo)
    }
    scannerView.resumeCameraPreview(this)
  }

  override fun onBackPressed() {
    super.onBackPressed()
    finishAndRemoveTask()
  }
}
