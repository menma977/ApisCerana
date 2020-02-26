package com.apis.cerana.view.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.apis.cerana.R
import com.apis.cerana.config.FragmentLoading
import com.apis.cerana.controller.ImageGeneratorController
import de.hdodenhof.circleimageview.CircleImageView

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {

  private lateinit var loadingFragment: FragmentLoading
  private lateinit var imageGeneratorController: ImageGeneratorController
  private lateinit var profileImage: CircleImageView

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.fragment_profile, container, false)
    profileImage = view.findViewById(R.id.profileImageView)
    loadingFragment = FragmentLoading(this.requireActivity())
    loadingFragment.openDialog()
    changeProfileImage("https://images-na.ssl-images-amazon.com/images/I/41HXUK8edZL.png")
    return view
  }

  private fun changeProfileImage(urlImage: String) {
    if (urlImage.isNotEmpty()) {
      imageGeneratorController = ImageGeneratorController(urlImage)
      val gitBitmap = imageGeneratorController.execute().get()
      profileImage.setImageBitmap(gitBitmap)
    }
    loadingFragment.closeDialog()
  }


}
