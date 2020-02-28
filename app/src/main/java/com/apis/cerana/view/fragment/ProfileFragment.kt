package com.apis.cerana.view.fragment


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.apis.cerana.MainActivity
import com.apis.cerana.R
import com.apis.cerana.config.FragmentLoading
import com.apis.cerana.controller.ImageGeneratorController
import com.apis.cerana.controller.LogoutController
import com.apis.cerana.controller.UserController
import com.apis.cerana.model.User
import de.hdodenhof.circleimageview.CircleImageView
import org.json.JSONObject
import java.util.*
import kotlin.concurrent.schedule

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {

  private lateinit var user: User
  private lateinit var loadingFragment: FragmentLoading
  private lateinit var imageGeneratorController: ImageGeneratorController
  private lateinit var profileImage: CircleImageView
  private lateinit var username: TextView
  private lateinit var ktp: TextView
  private lateinit var name: TextView
  private lateinit var email: TextView
  private lateinit var address: TextView
  private lateinit var phone: TextView
  private lateinit var bank: TextView
  private lateinit var pinBank: TextView

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.fragment_profile, container, false)
    user = User(context)
    loadingFragment = FragmentLoading(this.requireActivity())
    loadingFragment.openDialog()
    username = view.findViewById(R.id.usernameTextView)
    profileImage = view.findViewById(R.id.profileImageView)
    ktp = view.findViewById(R.id.ktpTextView)
    name = view.findViewById(R.id.nameTextView)
    email = view.findViewById(R.id.emailTextView)
    address = view.findViewById(R.id.addressTextView)
    phone = view.findViewById(R.id.phoneTextView)
    bank = view.findViewById(R.id.bankTextView)
    pinBank = view.findViewById(R.id.pinBankTextView)
    getUser(user.token)
    return view
  }

  private fun changeProfileImage(urlImage: String) {
    if (urlImage.isNotEmpty()) {
      imageGeneratorController = ImageGeneratorController(urlImage)
      val gitBitmap = imageGeneratorController.execute().get()
      profileImage.setImageBitmap(gitBitmap)
    }
  }

  private fun getUser(token: String) {
    Timer().schedule(100) {
      val response = UserController.Get(token).execute().get()
      if (response["code"] == 200) {
        if (response.getJSONObject("response")["status"] == 0) {
          LogoutController(token).execute().get()
          activity?.runOnUiThread {
            val goTo = Intent(context, MainActivity::class.java)
            User(context).clear()
            loadingFragment.closeDialog()
            startActivity(goTo)
          }
        }
        val province = response.getJSONObject("response")["province"].toString()
        val district = response.getJSONObject("response")["district"].toString()
        val subDistrict = response.getJSONObject("response")["sub_district"].toString()
        val village = response.getJSONObject("response")["village"].toString()
        val descriptionAddress = response.getJSONObject("response")["description_address"].toString()
        val numberAddress = response.getJSONObject("response")["number_address"].toString()
        val formatAddress = "$descriptionAddress, $numberAddress $village $subDistrict $district $province"
        activity?.runOnUiThread {
          changeProfileImage(response.getJSONObject("response")["image"].toString())
          username.text = response.getJSONObject("response")["username"].toString()
          if (response.getJSONObject("response")["username"].toString().isNotEmpty()) {
            ktp.text = response.getJSONObject("response")["id_identity_card"].toString()
          } else {
            ktp.text = response.getJSONObject("response")["id_identity_card"].toString().plus("Anda belum upload KTP")
          }
          name.text = response.getJSONObject("response")["name"].toString()
          email.text = response.getJSONObject("response")["email"].toString()
          address.text = formatAddress
          phone.text = response.getJSONObject("response")["phone"].toString()
          bank.text = response.getJSONObject("response")["bank"].toString()
          pinBank.text = response.getJSONObject("response")["pin_bank"].toString()
          val json = JSONObject()
          json.put("token", token)
          json.put("username", response.getJSONObject("response")["username"].toString())
          json.put("image", response.getJSONObject("response")["image"].toString())
          json.put("status", response.getJSONObject("response")["status"].toString().toInt())
          user.set(json.toString())
          loadingFragment.closeDialog()
        }
      } else {
        activity?.runOnUiThread {
          val goTo = Intent(activity?.applicationContext, MainActivity::class.java)
          user.clear()
          activity?.finish()
          loadingFragment.closeDialog()
          startActivity(goTo)
        }
      }
    }
  }
}
