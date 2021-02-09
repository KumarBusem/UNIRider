package com.uni.rider.common

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ExifInterface
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.databinding.ViewDataBinding
import com.bumptech.glide.load.resource.bitmap.TransformationUtils.rotateImage
import java.io.File
import java.io.IOException


abstract class ImageCaptureFragment<T : BaseViewModel, U : ViewDataBinding>(fragmentRes: Int) : BaseAbstractFragment<T, U>(fragmentRes) {

    private val mPermissionManager: PermissionManager by lazy { PermissionManager(this@ImageCaptureFragment) }

    protected abstract fun onImageCaptured(bitmap: Bitmap)
    protected abstract fun onFileSelected(file: Uri?)
    protected abstract fun onImageCaptureFailure(message: String, exception: Exception? = null)

    protected fun launchCameraForImage(requestCode: Int = IMAGE_CAPTURE_REQUEST_CODE) {
        if (mPermissionManager.areAllPermissionsGranted()) {

            Log.e("Launch Camera::", "START")
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(requireActivity().packageManager) != null) {
                var photoFile: File? = null
                try {
                    photoFile = createImageFile()
                } catch (e: IOException) {
                    onImageCaptureFailure("Image creating failed", e)
                }
                if (photoFile != null) {
                    val photoURI: Uri = FileProvider.getUriForFile(requireContext(), "com.dcartlogistics.rider.fileprovider",
                            photoFile)
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, requestCode)
                }
            }
        } else mPermissionManager.requestAllPermissions()
    }

    protected fun launchGalleyForImage(requestCode: Int = IMAGE_GALLERY_REQUEST_CODE) {

        if (mPermissionManager.areAllPermissionsGranted()) {

            val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(pickPhoto, requestCode)

        } else mPermissionManager.requestAllPermissions()
    }

    protected fun launchFolderForPDF(requestCode: Int = FILE_PDF_REQUEST_CODE) {

        if (mPermissionManager.areAllPermissionsGranted()) {

            val fileIntent = Intent(Intent.ACTION_GET_CONTENT)
            fileIntent.type = "application/pdf"
            startActivityForResult(fileIntent, requestCode)

        } else mPermissionManager.requestAllPermissions()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IMAGE_CAPTURE_REQUEST_CODE) {

            if (resultCode == AppCompatActivity.RESULT_OK) {

                val file = File(mCameraPhotoPath)
                val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, Uri.fromFile(file))
                if (bitmap != null) {
                    onImageCaptured(bitmap.rotateImageIfRequired(mCameraPhotoPath))
                } else {
                    onImageCaptureFailure("Image capture failed, Try again")
                }
            } else {
                onImageCaptureFailure("Didn't take picture, Try again")
            }
        } else if (requestCode == IMAGE_GALLERY_REQUEST_CODE) {

            if (resultCode == AppCompatActivity.RESULT_OK && data != null) {

                try {
                    val selectedImage = data.data
                    val imageStream = requireActivity().contentResolver.openInputStream(selectedImage!!)
                    val bitmap = BitmapFactory.decodeStream(imageStream)
                    onImageCaptured(bitmap)
                } catch (e: Exception) {
                    onImageCaptureFailure("Didn't take picture $e")
                }

            } else {
                onImageCaptureFailure("Didn't take picture")
            }
        } else if (requestCode == FILE_PDF_REQUEST_CODE) {

            if (resultCode == AppCompatActivity.RESULT_OK && data != null) {

                val file = data.data
                onFileSelected(file)
                Log.e("FILEPATH1::", file.toString())

            } else {
                onImageCaptureFailure("Didn't take picture")
            }
        }
    }

    companion object {

        protected val IMAGE_CAPTURE_REQUEST_CODE: Int = 963
        protected val IMAGE_GALLERY_REQUEST_CODE: Int = 953
        protected val FILE_PDF_REQUEST_CODE: Int = 943
        var mCameraPhotoPath: String? = null
    }

    private fun createImageFile(): File? {
        val imageFileName = "sendfast_img"
        val storageDir: File? = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image: File = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",  /* suffix */
                storageDir /* directory */
        )
        mCameraPhotoPath = image.absolutePath
        return image
    }


    fun Bitmap.getResizedByteArrayImage(maxSize: Int): Bitmap {

        var width: Int = this.width
        var height: Int = this.height

        Log.e("IMAGE SIZE BEFORE:", "$width * $height")

        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 1) {
            width = maxSize
            height = (width / bitmapRatio).toInt()
        } else {
            height = maxSize
            width = (height * bitmapRatio).toInt()
        }
        val newBitmap = Bitmap.createScaledBitmap(this, width, height, true)

        Log.e("IMAGE SIZE AFTER:", "${newBitmap.width} * ${newBitmap.height}")
        return newBitmap


    }


    fun Bitmap.rotateImageIfRequired(selectedImage: String?): Bitmap {
        val ei = ExifInterface(selectedImage!!)
        val orientation =
                ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(this, 90)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(this, 180)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(this, 270)
            else -> this
        }
    }


}