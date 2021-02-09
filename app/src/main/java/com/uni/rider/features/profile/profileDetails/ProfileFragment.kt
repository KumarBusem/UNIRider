package com.uni.rider.features.profile.profileDetails

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.uni.rider.BuildConfig
import com.uni.rider.R
import com.uni.rider.common.BUILD_TYPE_DEBUG
import com.uni.rider.common.ImageCaptureFragment
import com.uni.rider.common.ViewModelFactory
import com.uni.rider.databinding.FragmentProfileBinding
import com.uni.rider.features.dialogs.ChangePasswordDialog
import com.uni.rider.features.dialogs.FilePickerDialogFragment
import com.uni.rider.features.dialogs.TwoButtonAlertDialogFragment

class ProfileFragment : ImageCaptureFragment<ProfileViewModel, FragmentProfileBinding>(R.layout.fragment_profile) {
    private val changePasswordDialog: ChangePasswordDialog by lazy {
        ChangePasswordDialog(
                onChangePasswordCLicked = { oldPassword: String, newPassword: String ->
                    mViewModel.changePassword(oldPassword, newPassword)
                })
    }

    override fun setViewModel(): ProfileViewModel =
            ViewModelProvider(this@ProfileFragment, ViewModelFactory {
                ProfileViewModel(requireActivity().application)
            }).get(ProfileViewModel::class.java)

    override fun setupViews(): FragmentProfileBinding.() -> Unit = {
        toggleBottomBarVisibility(true)
        setAppVersion()

        btnHelp.setOnClickListener {
            navigateById(R.id.action_profileFragment_to_helpFragment)
        }

        btnTerms.setOnClickListener {
            navigateById(R.id.action_profileFragment_to_termsFragment)
        }
        btnEditProfile.setOnClickListener {
            navigateById(R.id.action_profileFragment_to_profileEditFragment)
        }

        btnLogout.setOnClickListener {
            showConfirmationDialogueFor("Are you sure you want Logout") {
                mViewModel.onLogoutButtonClick()
            }
        }
        mBinding.btnPlaystore.setOnClickListener {
            val browserIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=com.dcartlogistics.rider")
            )
            startActivity(browserIntent)
        }
        mBinding.btnPrivacy.setOnClickListener {
            val browserIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://sendfast.in/privacy-policy/")
            )
            startActivity(browserIntent)
        }

        mBinding.btnPasswordChange.setOnClickListener {
            changePasswordDialog.show(childFragmentManager, ProfileFragment::class.java.simpleName)
        }

        mBinding.mcvProfilePic.setOnClickListener { showFileTypeDialogue() }

    }

    override fun setupObservers(): ProfileViewModel.() -> Unit = {

        obsUser.observe(viewLifecycleOwner, Observer {
            Log.e("VIEW SETUP:::", obsUser.value?.profile_pic_url.toString())
            obsProgressBar.postValue(false)
            if (obsUser.value?.profile_pic_url.isNullOrBlank()) {
                mBinding.ivProfilePic.setImageResource(R.drawable.default_profile)
            } else {
                Glide.with(requireContext()).load(obsUser.value?.profile_pic_url)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(mBinding.ivProfilePic)
            }
        })
    }

    private fun setAppVersion() {
        val manager = requireActivity().packageManager
        val info = manager.getPackageInfo(requireActivity().packageName, 0)
        val version = info.versionName
        if (BuildConfig.BUILD_TYPE == BUILD_TYPE_DEBUG) mBinding.tvAppVersion.text = "Test Version $version"
        else mBinding.tvAppVersion.text = "Version $version"
    }

    private fun showFileTypeDialogue() {
        FilePickerDialogFragment(
                applicableTypes = listOf(FilePickerDialogFragment.Companion.FILE_TYPES.CAMERA, FilePickerDialogFragment.Companion.FILE_TYPES.GALLERY),
                onItemPicked = { type ->
                    when (type) {
                        FilePickerDialogFragment.Companion.FILE_TYPES.CAMERA -> launchCameraForImage()
                        FilePickerDialogFragment.Companion.FILE_TYPES.GALLERY -> launchGalleyForImage()
                    }
                }
        ).show(childFragmentManager, FilePickerDialogFragment::class.java.simpleName)
    }

    fun showConfirmationDialogueFor(message: String, onConfirmation: () -> Unit) {
        TwoButtonAlertDialogFragment.Builder()
                .setMessage(message)
                .onPrimaryAction(onConfirmation)
                .dismissOnClick()
                .build()
                .show(this@ProfileFragment.childFragmentManager, TwoButtonAlertDialogFragment::class.java.simpleName)
    }

    override fun onImageCaptured(bitmap: Bitmap) {
        mViewModel.uploadProfilePicture(bitmap.getResizedByteArrayImage(300))
    }

    override fun onFileSelected(file: Uri?) {

    }

    override fun onResume() {
        mViewModel.obsUser.postValue(repoPrefs.getLoggedInUser())
        super.onResume()
    }

    override fun onImageCaptureFailure(message: String, exception: Exception?) = showToast(message)

}
