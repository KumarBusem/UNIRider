package com.uni.rider.features.profile.profileDetails

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


        btnEditProfile.setOnClickListener {
            navigateById(R.id.action_profileFragment_to_profileEditFragment)
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
