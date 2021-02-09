package com.uni.rider.features.dialogs


import android.app.Dialog
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.uni.data.models.User
import com.uni.rider.R
import com.uni.rider.common.disable
import com.uni.rider.common.enableIf
import com.uni.rider.common.hide
import com.uni.rider.common.show
import com.uni.rider.databinding.DialogOtpBinding
import kotlinx.android.synthetic.main.dialog_otp.*


class OTPDialog(user: User?, val onSendOTPCLicked: () -> Unit, val onSubmitOTPCLicked: (String) -> Unit) : DialogFragment() {

    private lateinit var mBinding: DialogOtpBinding
    var cTimer: CountDownTimer? = null
    var user = user

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
        dialog?.setCancelable(false)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_otp, container, false)
        mBinding.lifecycleOwner = viewLifecycleOwner
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.e("OTP DIALOG CREATED:::", "00000000")
        setupViews()
        setUpTimer()
    }

    private fun setUpTimer() {
        tvResend.hide()
        cTimer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                tvTimer.text = "You can resend OTP in " + millisUntilFinished / 1000 + " sec"

            }

            override fun onFinish() {
                tvTimer.text = "Click here to"
                tvResend.show()
                cTimer?.cancel()
            }
        }.start()
    }

    //cancel timer
    fun cancelTimer() {
        if (cTimer != null) cTimer!!.cancel()
    }

    override fun onDestroy() {
        mBinding.etOTP.setText("")
        cancelTimer()
        super.onDestroy()
    }

    override fun onDetach() {
        mBinding.etOTP.setText("")
        super.onDetach()
    }

    private fun setupViews() {

        mBinding.apply {

            tvPhone.text = "OTP sent to ${user?.phone_number}"

            btnSubmitOTP.disable()

            btnClose.setOnClickListener {
                dialog?.dismiss()
            }

            tvResend.setOnClickListener {
                onSendOTPCLicked()
                setUpTimer()
            }

            btnSubmitOTP.setOnClickListener {

                onSubmitOTPCLicked(mBinding.etOTP.text.toString().trim())
                mBinding.etOTP.setText("")
            }

            etOTP.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) = mBinding.btnSubmitOTP.enableIf(!s?.toString().isNullOrEmpty() && s?.toString()?.length == 4)
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
            })
        }
    }
}