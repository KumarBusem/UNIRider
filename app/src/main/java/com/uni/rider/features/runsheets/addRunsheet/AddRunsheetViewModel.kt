package com.uni.rider.features.runsheets.addRunsheet

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import com.uni.data.internal.common.ApiException
import com.uni.data.internal.common.RiderLoginException
import com.uni.rider.common.BaseViewModel
import com.uni.rider.common.isStatusSuccess
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.net.SocketTimeoutException

class AddRunsheetViewModel(context: Application) : BaseViewModel(context) {

    val obsIsRunsheetSaved: MutableLiveData<Boolean> = MutableLiveData()

    val obsRunsheetId: MutableLiveData<String> = MutableLiveData()
    val obsOFD: MutableLiveData<String> = MutableLiveData()
    val obsDelivered: MutableLiveData<String> = MutableLiveData()
    val obsRemarks: MutableLiveData<String> = MutableLiveData()
    val obsRunsheetDate: MutableLiveData<String> = MutableLiveData()

    val obsRunsheetPic: MutableLiveData<Bitmap> = MutableLiveData()

    fun saveRunsheet(res: (String) -> Unit) {

        obsIsDataLoading.postValue(true)

        val runsheetId = obsRunsheetId.value
        val ofd = obsOFD.value
        val delivered = obsDelivered.value
        val remarks = obsRemarks.value
        val runsheetDate = obsRunsheetDate.value
        val runsheetImage = obsRunsheetPic.value

        if (runsheetId.isNullOrBlank() || runsheetId.length < 4) {
            res("Please enter valid runsheet id")
            obsIsDataLoading.postValue(false)
            return
        } else if (ofd.isNullOrBlank() || delivered.isNullOrBlank() || runsheetDate.isNullOrBlank() || runsheetImage == null) {
            res("Please enter valid information")
            obsIsDataLoading.postValue(false)
            return
        }

        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        builder.addFormDataPart("runsheet_id", runsheetId)
        builder.addFormDataPart("runsheet_date", runsheetDate)
        builder.addFormDataPart("ofd", ofd)
        builder.addFormDataPart("delivered", delivered)
        builder.addFormDataPart("remarks", remarks.toString())
        val bos = ByteArrayOutputStream()
        runsheetImage.compress(Bitmap.CompressFormat.JPEG, 100, bos)
        builder.addFormDataPart("img", "img", RequestBody.create(MultipartBody.FORM, bos.toByteArray()))

        ioScope.launch {
            try {
                repoImage.addRunsheet(builder.build()) {
                    if (it?.status?.isStatusSuccess()!!) {
                        res("Successfully saved runsheet")
                        obsIsRunsheetSaved.postValue(true)
                    } else {
                        res(it.message.toString())
                        obsIsRunsheetSaved.postValue(false)
                    }
                    obsIsDataLoading.postValue(false)
                }
            } catch (e: ApiException) {
                obsMessage.postValue(e.message!!)
                obsIsDataLoading.postValue(false)
            } catch (e: SocketTimeoutException) {
                obsMessage.postValue("Slow Network!\nPlease ty again")
                obsIsDataLoading.postValue(false)
            } catch (e: RiderLoginException) {
                repoPrefs.clearLoggedInUser()
                isUserLogout.postValue(true)
                obsIsDataLoading.postValue(false)
            } catch (e: Exception) {
                obsMessage.postValue(e.message + "")
                obsIsDataLoading.postValue(false)
            }
        }
    }
}