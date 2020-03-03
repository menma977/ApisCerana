package com.apis.cerana.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.apis.cerana.R
import com.apis.cerana.config.Loading
import com.apis.cerana.controller.UserController
import com.apis.cerana.model.User
import org.json.JSONObject
import java.util.*
import kotlin.concurrent.schedule

class RegisterActivity : AppCompatActivity() {

  private lateinit var user: User
  private lateinit var loading: Loading
  private lateinit var name: EditText
  private lateinit var username: EditText
  private lateinit var email: EditText
  private lateinit var password: EditText
  private lateinit var validationPassword: EditText
  private lateinit var bank: EditText
  private lateinit var pinBank: EditText
  private lateinit var ktpNumber: EditText
  private lateinit var phone: EditText
  private lateinit var province: EditText
  private lateinit var district: EditText
  private lateinit var subDistrict: EditText
  private lateinit var village: EditText
  private lateinit var numberAddress: EditText
  private lateinit var descriptionAddress: EditText
  private lateinit var register: Button
  private lateinit var response: JSONObject
  private lateinit var goTo: Intent

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_register)

    name = findViewById(R.id.nameEditText)
    username = findViewById(R.id.usernameEditText)
    email = findViewById(R.id.emailEditText)
    password = findViewById(R.id.passwordEditText)
    validationPassword = findViewById(R.id.validationPasswordEditText)
    bank = findViewById(R.id.bankEditText)
    pinBank = findViewById(R.id.pinBankEditText)
    ktpNumber = findViewById(R.id.accountIdEditText)
    phone = findViewById(R.id.phoneEditText)
    province = findViewById(R.id.provinceEditText)
    district = findViewById(R.id.districtEditText)
    subDistrict = findViewById(R.id.subDistrictEditText)
    village = findViewById(R.id.villageEditText)
    numberAddress = findViewById(R.id.numberAddressEditText)
    descriptionAddress = findViewById(R.id.descriptionAddressEditText)
    register = findViewById(R.id.registerButton)

    user = User(this)
    loading = Loading(this)
    loading.openDialog()

    register.setOnClickListener {
      loading.openDialog()
      Timer().schedule(100) {
        val massage: String
        val body = HashMap<String, String>()
        body["name"] = name.text.toString()
        body["username"] = username.text.toString()
        body["email"] = email.text.toString()
        body["password"] = password.text.toString()
        body["c_password"] = validationPassword.text.toString()
        body["bank"] = bank.text.toString()
        body["pin_bank"] = pinBank.text.toString()
        body["ktp_number"] = ktpNumber.text.toString()
        body["phone"] = phone.text.toString()
        body["province"] = province.text.toString()
        body["district"] = district.text.toString()
        body["sub_district"] = subDistrict.text.toString()
        body["village"] = village.text.toString()
        body["number_address"] = numberAddress.text.toString()
        body["description_address"] = descriptionAddress.text.toString()
        response = UserController.Register(user.token, body).execute().get()
        if (response["code"] == 200) {
          runOnUiThread {
            loading.closeDialog()
            goTo = Intent(applicationContext, HomeActivity::class.java)
            startActivity(goTo)
          }
        } else {
          when {
            response["response"].toString() in "sponsor" -> {
              massage = response["response"].toString().replace(response["response"].toString(), "Afiliasi")
            }
            response["response"].toString() in "name" -> {
              massage = response["response"].toString().replace(response["response"].toString(), "Nama")
            }
            response["response"].toString() in "username" -> {
              massage = response["response"].toString().replace(response["response"].toString(), "Username")
            }
            response["response"].toString() in "email" -> {
              massage = response["response"].toString().replace(response["response"].toString(), "Email")
            }
            response["response"].toString() in "password" -> {
              massage = response["response"].toString().replace(response["response"].toString(), "Password")
            }
            response["response"].toString() in "c_password" -> {
              massage = response["response"].toString().replace(response["response"].toString(), "Konfrimasi Password")
            }
            response["response"].toString() in "pin_bank" -> {
              massage = response["response"].toString().replace(response["response"].toString(), "Pin Bank")
            }
            response["response"].toString() in "ktp_number" -> {
              massage = response["response"].toString().replace(response["response"].toString(), "Nomor KTP")
            }
            response["response"].toString() in "phone" -> {
              massage = response["response"].toString().replace(response["response"].toString(), "Nomor Telfon")
            }
            response["response"].toString() in "province" -> {
              massage = response["response"].toString().replace(response["response"].toString(), "Profinsi")
            }
            response["response"].toString() in "district" -> {
              massage = response["response"].toString().replace(response["response"].toString(), "Kota/Kabupaten")
            }
            response["response"].toString() in "sub_district" -> {
              massage = response["response"].toString().replace(response["response"].toString(), "Keluarahan")
            }
            response["response"].toString() in "village" -> {
              massage = response["response"].toString().replace(response["response"].toString(), "Kel/Desa")
            }
            response["response"].toString() in "number_address" -> {
              massage = response["response"].toString().replace(response["response"].toString(), "Nomor Rumah")
            }
            response["response"].toString() in "description_address" -> {
              massage =
                response["response"].toString().replace(response["response"].toString(), "Keterangan Alamat Lengkap")
            }
            else -> {
              massage = response["response"].toString()
            }
          }
          runOnUiThread {
            loading.closeDialog()
            Toast.makeText(
              applicationContext,
              massage,
              Toast.LENGTH_SHORT
            ).show()
          }
        }
      }
    }
    loading.closeDialog()
  }
}
