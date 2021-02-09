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
import com.uni.rider.common.show
import com.uni.rider.databinding.FragmentFilePickerDialogBinding
import com.uni.rider.features.dialogs.FilePickerDialogFragment.Companion.FILE_TYPES.CAMERA
import com.uni.rider.features.dialogs.FilePickerDialogFragment.Companion.FILE_TYPES.GALLERY

class FilePickerDialogFragment(val applicableTypes: List<FILE_TYPES> = listOf(CAMERA, GALLERY),
                               val onItemPicked: (FILE_TYPES) -> Unit) : DialogFragment() {

    private lateinit var mBinding: FragmentFilePickerDialogBinding

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_file_picker_dialog, container, false)
        mBinding.lifecycleOwner = viewLifecycleOwner
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {

        mBinding.apply {

            if (applicableTypes.find { it == CAMERA } != null) {
                mBinding.clCamera.show()
                clCamera.setOnClickListener {
                    dismiss()
                    onItemPicked(CAMERA)
                }
            }

            if (applicableTypes.find { it == GALLERY } != null) {
                clGallery.show()
                clGallery.setOnClickListener {
                    dismiss()
                    onItemPicked(GALLERY)
                }
            }

        }
    }

    companion object {
        enum class FILE_TYPES { CAMERA, GALLERY }
    }
}