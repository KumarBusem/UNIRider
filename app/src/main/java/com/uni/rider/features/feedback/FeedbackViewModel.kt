package com.uni.rider.features.feedback

import android.app.Application
import android.location.Location
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.uni.data.internal.common.ApiException
import com.uni.data.internal.common.RiderLoginException
import com.uni.data.models.Feedback
import com.uni.rider.common.BaseViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.net.SocketTimeoutException

class FeedbackViewModel(context: Application) : BaseViewModel(context) {

    val obsFeedbackList: MutableLiveData<List<Feedback>> = MutableLiveData()
    val obsIsDetailsSubmitted = MutableLiveData<Boolean>()

    fun getFeedbackList() {

        obsIsDataLoading.postValue(true)
        ioScope.launch {
            try {
                repoTextData.getFeedbacks { responce ->
                    Log.e("FEEDBACKS::", responce.toString())
                    obsFeedbackList.postValue(responce?.data)
                    obsIsDataLoading.postValue(false)
                }
            } catch (e: ApiException) {
                obsMessage.postValue(e.message!!)
                obsFeedbackList.postValue(emptyList())
                obsIsDataLoading.postValue(false)
            } catch (e: SocketTimeoutException) {
                obsMessage.postValue("Slow Network!\nPlease ty again")
                obsFeedbackList.postValue(emptyList())
                obsIsDataLoading.postValue(false)
            } catch (e: RiderLoginException) {
                repoPrefs.clearLoggedInUser()
                obsFeedbackList.postValue(emptyList())
                isUserLogout.postValue(true)
                obsIsDataLoading.postValue(false)
            } catch (e: Exception) {
                obsMessage.postValue(e.message + "")
                obsFeedbackList.postValue(emptyList())
                obsIsDataLoading.postValue(false)
            }
        }
    }

    fun saveFeedback(orderId: String, customerName: String, phoneNumber: String, location: Location?) {

        obsIsDataLoading.postValue(true)
        val json = JSONObject()
        json.put("order_id", orderId)
        json.put("customer_name", customerName)
        json.put("ph_no", phoneNumber)
        json.put("lat", "${location?.latitude}")
        json.put("long", "${location?.longitude}")

        val requestBody = json.toString().toRequestBody("application/json".toMediaTypeOrNull())

        ioScope.launch {
            try {
                repoTextData.addFeedback(requestBody) { responce ->
                    if (responce?.status.equals("success"))
                        obsIsDetailsSubmitted.postValue(true)
                    obsMessage.postValue(responce?.message!!)
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