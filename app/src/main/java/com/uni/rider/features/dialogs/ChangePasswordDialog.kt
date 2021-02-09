package com.uni.rider.features.dialogs


import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.uni.rider.R
import com.uni.rider.common.hide
import com.uni.rider.common.show
import com.uni.rider.databinding.DialogChangePasswordBinding


class ChangePasswordDialog(val onChangePasswordCLicked: (String, String) -> Unit) : DialogFragment() {

    private lateinit var mBinding: DialogChangePasswordBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            setCanceledOnTouchOutside(true)
            window?.apply {
                attributes?.windowAnimations = R.style.DialogSideInOutAnimation
                setBackgroundDrawableResource(android.R.color.transparent)
                setGravity(Gravity.CENTER_VERTICAL)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_change_password, container, false)
        mBinding.lifecycleOwner = viewLifecycleOwner
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.etOldPassword.setText("")
        mBinding.etNewPassword.setText("")
        mBinding.etConfirmPassword.setText("")
        setupViews()
    }

    override fun onDestroy() {
        mBinding.etOldPassword.setText("")
        mBinding.etNewPassword.setText("")
        mBinding.etConfirmPassword.setText("")
        super.onDestroy()
    }

    private fun setupViews() {

        mBinding.apply {

            etOldPassword.setText("")
            etNewPassword.setText("")
            etConfirmPassword.setText("")

            btnSubmit.setOnClickListener {
                tvPHONEError.hide()
                tvNAMEError.hide()
                when {
                    etOldPassword.text.toString().trim().isBlank() || etNewPassword.text.toString().trim().isBlank() || etConfirmPassword.text.toString().trim().isBlank() -> {
                        tvNAMEError.show()
                    }
                    etNewPassword.text.toString().trim() != etConfirmPassword.text.toString().trim() -> {
                        tvPHONEError.show()
                    }
                    else -> {
                        onChangePasswordCLicked(etOldPassword.text.toString(), etNewPassword.text.toString().trim())
                        dialog?.dismiss()
                    }
                }


            }
        }
    }
}