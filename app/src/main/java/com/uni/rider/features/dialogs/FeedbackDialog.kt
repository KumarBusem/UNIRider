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
import com.uni.rider.databinding.DialogFeedbackBinding


class FeedbackDialog(val onSubmitFeedbackCLicked: (String, String, String) -> Unit) : DialogFragment() {

    private lateinit var mBinding: DialogFeedbackBinding

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
        mBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_feedback, container, false)
        mBinding.lifecycleOwner = viewLifecycleOwner
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.etOrderID.setText("")
        mBinding.etName.setText("")
        mBinding.etPhone.setText("")
        setupViews()
    }

    override fun onDestroy() {
        mBinding.etOrderID.setText("")
        mBinding.etName.setText("")
        mBinding.etPhone.setText("")
        super.onDestroy()
    }

    private fun setupViews() {

        mBinding.apply {

            etOrderID.setText("")
            etName.setText("")
            etPhone.setText("")

            btnSubmit.setOnClickListener {
                tvORDERError.hide()
                tvNAMEError.hide()
                tvPHONEError.hide()
                when {
                    etOrderID.text.toString().trim().isBlank() || etOrderID.text.toString().length != 6 -> {
                        tvORDERError.show()
                    }
                    etName.text.toString().trim().isBlank() -> {
                        tvNAMEError.show()
                    }
                    etPhone.text.toString().trim().isBlank() || etPhone.text.toString().length != 10 -> {
                        tvPHONEError.show()
                    }
                    else -> {
                        onSubmitFeedbackCLicked(etOrderID.text.toString(), etName.text.toString(), etPhone.text.toString())
                    }
                }

            }
        }
    }
}