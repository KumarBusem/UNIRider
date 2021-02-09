package com.uni.rider.features.profile.editProfile

import android.util.Patterns
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.uni.rider.R
import com.uni.rider.common.*
import com.uni.rider.databinding.FragmentProfileEditBinding
import com.uni.rider.features.dialogs.OTPDialog
import java.util.regex.Pattern

class ProfileEditFragment : BaseAbstractFragment<ProfileEditViewModel, FragmentProfileEditBinding>(R.layout.fragment_profile_edit) {
    private val otpDialog: OTPDialog by lazy {
        OTPDialog(repoPrefs.getLoggedInUser(),
                onSendOTPCLicked = { mViewModel.sendOTP() },
                onSubmitOTPCLicked = { mViewModel.submitOtp(it) })
    }

    override fun setViewModel(): ProfileEditViewModel =
            ViewModelProvider(this@ProfileEditFragment, ViewModelFactory {
                ProfileEditViewModel(requireActivity().application)
            }).get(ProfileEditViewModel::class.java)

    override fun setupViews(): FragmentProfileEditBinding.() -> Unit = {

        toggleBottomBarVisibility(false)
        ivBack.setOnClickListener { navigateBack() }
        btnSave.setOnClickListener {
            saveProfile()
        }
    }

    private fun saveProfile() {

        if (checkProfileParams()) {
            if (checkIsBankChanged()) {
                mViewModel.sendOTP()
            } else {
                mViewModel.saveProfile {
                    requireActivity().runOnUiThread {
                        showToast(it)
                        mBinding.btnSave.enable()
                    }
                }
            }
        }
    }

    override fun setupObservers(): ProfileEditViewModel.() -> Unit = {

        obsIsProfileUpdated.observe(viewLifecycleOwner, Observer {
            if (it == true)
                navigateBack()
        })
        obsMessage.observe(viewLifecycleOwner, Observer {
            if (!(it.isNullOrEmpty())) {
                showToast(it)
            }
        })
        obsIsOtpSent.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it == true) {
                if (!otpDialog.isVisible)
                    otpDialog.show(
                            childFragmentManager,
                            ProfileEditFragment::class.java.simpleName
                    )
            }
        })
        obsIsOtpVerified.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it == true) {
                mViewModel.saveProfile {
                    requireActivity().runOnUiThread {
                        showToast(it)
                        mBinding.btnSave.enable()
                    }
                }
            }
        })
    }

    private fun checkProfileParams(): Boolean {
        val aadhar = mViewModel.obsAadhar.value
        val pan = mViewModel.obsPan.value
        val email = mViewModel.obsEmail.value
        val account = mViewModel.obsAccount.value
        val ifsc = mViewModel.obsIFSC.value
        val alternate = mViewModel.obsAlternate.value

        val panPattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}")
        val aadharPattern = Pattern.compile("[0-9]{12}")
        val ifscPattern = Pattern.compile("[A-Z]{4}[A-Z0-9]{7}")

        if (aadhar.isNullOrBlank() || !(aadharPattern.matcher(aadhar.trim()).matches())) {
            showToast("Invalid Aadhar Number")
            return false
        } else if (pan.isNullOrBlank() || !(panPattern.matcher(pan.trim()).matches())) {
            showToast("Invalid Pan Number")
            return false
        } else if (ifsc.isNullOrBlank() || !(ifscPattern.matcher(ifsc.trim()).matches())) {
            showToast("Invalid IFSC Code")
            return false
        } else if (account.isNullOrBlank()) {
            showToast("Invalid Account Number")
            return false
        } else if (email.isNullOrBlank() || !(Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches())) {
            showToast("Invalid Email")
            return false
        } else if (alternate.isNullOrBlank() || alternate.trim().length != 10 || !(Patterns.PHONE.matcher(alternate.trim()).matches())) {
            showToast("Invalid Phone Number")
            return false
        } else
            return true
    }

    private fun checkIsBankChanged(): Boolean {
        val previous = mViewModel.obsUser.value
        return previous?.bank_account_no != mViewModel.obsAccount.value || previous?.ifsc_no != mViewModel.obsIFSC.value
    }
}
