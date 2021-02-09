package com.uni.rider.features.runsheets.addRunsheet


import android.app.DatePickerDialog
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.uni.rider.R
import com.uni.rider.common.*
import com.uni.rider.databinding.FragmentAddRunsheetBinding
import com.uni.rider.features.dialogs.FilePickerDialogFragment
import java.text.SimpleDateFormat
import java.util.*

class AddRunsheetFragment : ImageCaptureFragment<AddRunsheetViewModel, FragmentAddRunsheetBinding>(R.layout.fragment_add_runsheet) {

    override fun setViewModel(): AddRunsheetViewModel =
            ViewModelProvider(this@AddRunsheetFragment, ViewModelFactory {
                AddRunsheetViewModel(requireActivity().application)
            }).get(AddRunsheetViewModel::class.java)

    override fun setupViews(): FragmentAddRunsheetBinding.() -> Unit = {

        toggleBottomBarVisibility(false)
        ivBack.setOnClickListener { navigateBack() }

        tvCreateProject.setOnClickListener {
            tvCreateProject.disable()
            createProject()
        }

        cvEstEndDate.setOnClickListener {
            datePickerDialog()
        }

        cvTakePicture.setOnClickListener { showFileTypeDialogue() }
    }

    override fun setupObservers(): AddRunsheetViewModel.() -> Unit = {
        obsIsRunsheetSaved.observe(viewLifecycleOwner, Observer {
            if (it) navigateBack()
        })
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

    private fun createProject() {

        mViewModel.saveRunsheet {
            requireActivity().runOnUiThread {
                showToast(it)
                mBinding.tvCreateProject.enable()
            }
        }
    }

    override fun onImageCaptured(bitmap: Bitmap) {
        mBinding.tvDispatchTitle.text = "Image Captured"
        mBinding.ivDispatchImageCaptured.show()
        mViewModel.obsRunsheetPic.postValue(bitmap.getResizedByteArrayImage(2048))
    }

    override fun onFileSelected(file: Uri?) {}

    override fun onImageCaptureFailure(message: String, exception: Exception?) {}

    private fun datePickerDialog() {

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        // Create a new instance of DatePickerDialog and return it
        val picker = DatePickerDialog(
                requireActivity(),
                DatePickerDialog.OnDateSetListener { _, i, i2, i3 ->
                    // if month is May it will give 4 (i2)
                    val strCurrentDate = "$i3-${i2 + 1}-$i"
                    var defaultFormat = SimpleDateFormat("dd-MM-yyyy")
                    val newDate = defaultFormat.parse(strCurrentDate)

                    var finalFormat = SimpleDateFormat("yyyy-MM-dd")
                    val date = finalFormat.format(newDate)
                    mViewModel.obsRunsheetDate.postValue(date)
                },
                year,
                month,
                day
        )
        picker.datePicker.maxDate = c.timeInMillis
        picker.show()
    }
}
