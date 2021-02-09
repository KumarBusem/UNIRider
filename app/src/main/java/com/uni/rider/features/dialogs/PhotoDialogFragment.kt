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
import com.uni.rider.databinding.DialogPhotoBinding

class PhotoDialogFragment private constructor(private val dialogueData: DialogueData) : DialogFragment() {

    private lateinit var mBinding: DialogPhotoBinding

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
        mBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_photo, container, false)
        mBinding.lifecycleOwner = viewLifecycleOwner
        mBinding.data = dialogueData
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {

        mBinding.apply {

            btnPrimary.setOnClickListener {
                dialogueData.onPrimaryAction.invoke()
                if (dialogueData.dismissOnClick) dismiss()
            }

        }
    }

    class Builder {

        private var title: String = "Take a selfie"
        private var message: String = "Clarity, decent lighting and focus are important\nEyes Open and looking directly at camera"
        private var primaryButtonText: String = "Camera"
        private var secondaryButtonText: String = "Cancel"
        private var onPrimaryAction: () -> Unit = {}
        private var onSecondaryAction: () -> Unit = {}
        private var dismissOnClick: Boolean = false

        fun setTitle(title: String): Builder {
            this.title = title
            return this
        }

        fun setMessage(message: String): Builder {
            this.message = message
            return this
        }

        fun setPrimaryButtonText(primaryButtonText: String): Builder {
            this.primaryButtonText = primaryButtonText
            return this
        }

        fun setSecondaryButtonText(secondaryButtonText: String): Builder {
            this.secondaryButtonText = secondaryButtonText
            return this
        }

        fun onPrimaryAction(onClick: () -> Unit): Builder {
            this.onPrimaryAction = onClick
            return this
        }

        fun dismissOnClick(): Builder {
            dismissOnClick = true
            return this
        }

        fun onSecondaryAction(onClick: () -> Unit): Builder {
            this.onSecondaryAction = onClick
            return this
        }

        fun build(): PhotoDialogFragment {

            val dialogueData = DialogueData(title = title,
                    message = message,
                    primaryButtonText = primaryButtonText,
                    secondaryButtonText = secondaryButtonText,
                    onPrimaryAction = onPrimaryAction,
                    onSecondaryAction = onSecondaryAction,
                    dismissOnClick = dismissOnClick)

            return PhotoDialogFragment(dialogueData)
        }
    }

    data class DialogueData(
            val title: String,
            val message: String,
            val primaryButtonText: String,
            val secondaryButtonText: String,
            val onPrimaryAction: () -> Unit,
            val onSecondaryAction: () -> Unit,
            val dismissOnClick: Boolean)
}