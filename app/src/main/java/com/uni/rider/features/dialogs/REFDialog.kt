package com.uni.rider.features.dialogs


import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.uni.rider.R
import com.uni.rider.common.disable
import com.uni.rider.common.enableIf
import com.uni.rider.common.hide
import com.uni.rider.common.show
import com.uni.rider.databinding.DialogRefBinding


class REFDialog(val onConfirmREFCLicked: () -> Unit) : DialogFragment() {

    private lateinit var mBinding: DialogRefBinding

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
        mBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_ref, container, false)
        mBinding.lifecycleOwner = viewLifecycleOwner
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }


    private fun setupViews() {

        mBinding.apply {

            btnConfirm.disable()

            btnConfirm.setOnClickListener {
                tvAMOUNTError.hide()
                tvREFError.hide()
                when {
                    etAmount.text.toString().trim().toFloat().toInt() != 0 -> {
                        tvAMOUNTError.show()
                    }
                    etREF.text.toString().trim() != "pickup.reference_no" -> {
                        tvREFError.show()
                    }
                    else -> {
                        onConfirmREFCLicked()
                    }
                }

            }

            etREF.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) =
                        mBinding.btnConfirm.enableIf(!s?.toString().isNullOrEmpty() && s?.toString()?.length == 10)

                override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                ) = Unit

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) =
                        Unit
            })
        }
    }
}