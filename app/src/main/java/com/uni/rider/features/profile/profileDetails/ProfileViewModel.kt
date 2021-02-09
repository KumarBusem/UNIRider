package com.uni.rider.features.profile.profileDetails

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import com.uni.data.internal.common.ApiException
import com.uni.data.internal.common.RiderLoginException
import com.uni.data.models.User
import com.uni.rider.common.BaseViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.net.SocketTimeoutException

class ProfileViewModel(context: Application) : BaseViewModel(context) {

    val obsUser: MutableLiveData<User> = MutableLiveData()
    val obsProgressBar: MutableLiveData<Boolean> = MutableLiveData()

    init {
        getLoggedInUser()
    }

    private fun getLoggedInUser() {
        obsUser.postValue(repoPrefs.getLoggedInUser())
    }

    fun onLogoutButtonClick() {
        repoPrefs.clearLoggedInUser()
        isUserLogout.postValue(true)
    }

    fun uploadProfilePicture(bitmap: Bitmap) {
        obsProgressBar.postValue(true)
        ioScope.launch {
            try {
                repoImage.uploadProfilePic(getImageUploadBody(bitmap)) { profilePicResponce ->
                    if (profilePicResponce?.status.equals("success")) {
                        val user = obsUser.value
                        user?.is_profile_pic_uploaded = true
                        user?.profile_pic_url = profilePicResponce?.image_url
                        repoPrefs.saveLoggedInUser(user!!)
                        obsUser.postValue(user)
                        repoPrefs.isProfilePicUpdated(true)
                    }
                    obsMessage.postValue(profilePicResponce!!.message.toString())
                }

            } catch (e: ApiException) {
                obsMessage.postValue(e.message!!)
            } catch (e: SocketTimeoutException) {
                obsMessage.postValue("Slow Network!")
            } catch (e: RiderLoginException) {
                repoPrefs.clearLoggedInUser()
                isUserLogout.postValue(true)
            } catch (e: Exception) {
                obsMessage.postValue(e.message + "")
            }
        }

    }

    fun changePassword(oldPassword: String, newPassword: String) {
        obsProgressBar.postValue(true)
        ioScope.launch {
            try {
                repoTextData.changePassword(getChangePasswordBody(oldPassword, newPassword)) { passwordResponce ->
                    if (passwordResponce?.status.equals("success")) {
                        repoPrefs.clearLoggedInUser()
                        isUserLogout.postValue(true)
                    }
                    obsMessage.postValue(passwordResponce!!.message.toString())
                }

            } catch (e: ApiException) {
                obsMessage.postValue(e.message!!)
            } catch (e: SocketTimeoutException) {
                obsMessage.postValue("Slow Network!")
            } catch (e: RiderLoginException) {
                repoPrefs.clearLoggedInUser()
                isUserLogout.postValue(true)
            } catch (e: Exception) {
                obsMessage.postValue(e.message + "")
            }
        }

    }


    private fun getChangePasswordBody(oldPassword: String, newPassword: String): RequestBody {
        val json = JSONObject()
        json.put("current_password", oldPassword)
        json.put("new_password", newPassword)
        return json.toString().toRequestBody("application/json".toMediaTypeOrNull())
    }

    private fun getImageUploadBody(bitmap: Bitmap): RequestBody {
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        builder.addFormDataPart("rider_id", obsUser.value?.sf_id!!)
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
        builder.addFormDataPart("image", "image", RequestBody.create(MultipartBody.FORM, bos.toByteArray()))
        return builder.build()
    }

}