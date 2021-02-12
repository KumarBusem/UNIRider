package com.uni.rider.features.profile.editProfile

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.uni.data.internal.common.ApiException
import com.uni.data.internal.common.RiderLoginException
import com.uni.data.models.User
import com.uni.rider.common.BaseViewModel
import com.uni.rider.common.isStatusSuccess
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.net.SocketTimeoutException

class ProfileEditViewModel(context: Application) : BaseViewModel(context) {


    val obsIsProfileUpdated = MutableLiveData<Boolean>()
    val obsIsOtpSent = MutableLiveData<Boolean>()
    val obsIsOtpVerified = MutableLiveData<Boolean>()

    val obsAadhar: MutableLiveData<String> = MutableLiveData()
    val obsPan: MutableLiveData<String> = MutableLiveData()
    val obsEmail: MutableLiveData<String> = MutableLiveData()
    val obsAccount: MutableLiveData<String> = MutableLiveData()
    val obsIFSC: MutableLiveData<String> = MutableLiveData()
    val obsAlternate: MutableLiveData<String> = MutableLiveData()
    val obsUser: MutableLiveData<User> = MutableLiveData()

    init {
        var user = repoPrefs.getLoggedInUser()
        if(user == null){
            repoPrefs.clearLoggedInUser()
            isUserLogout.postValue(true)
        }else{
            obsUser.postValue(user!!)
            obsAadhar.postValue(user.aadhar_number!!)
            obsPan.postValue(user.pan_card!!)
            obsEmail.postValue(user.email!!)
            obsAccount.postValue(user.bank_account_no!!)
            obsIFSC.postValue(user.ifsc_no!!)
            obsAlternate.postValue(user.alternate_no!!)
        }
    }

    fun saveProfile(res: (String) -> Unit) {

        obsIsDataLoading.postValue(true)

        val aadhar = obsAadhar.value
        val pan = obsPan.value
        val email = obsEmail.value
        val account = obsAccount.value
        val ifsc = obsIFSC.value
        val alternate = obsAlternate.value

        ioScope.launch {
            try {
                repoUser.editProfile(aadhar!!, pan!!, account!!, ifsc!!, email!!, alternate!!) {
                    if (it?.status?.isStatusSuccess()!!) {
                        res("Saved successfully")
                        obsIsProfileUpdated.postValue(true)
                        saveDetailsToSharedPreferences()
                    } else {
                        res("Something went wrong, Please try again")
                    }
                    obsIsDataLoading.postValue(false)
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

    fun sendOTP() {
        obsIsDataLoading.postValue(true)
        ioScope.launch {
            if (repoPrefs.getLoggedInUser()?.phone_number.isNullOrEmpty()) {
                obsMessage.postValue("Phone number not available")
                obsIsDataLoading.postValue(false)
                return@launch
            }
            try {
                repoUser.sendOTP(repoPrefs.getLoggedInUser()?.phone_number) { otpResponse ->
                    Log.e("SEND OTP RES::::", otpResponse.toString())
                    if (otpResponse?.status.equals("success")) {
                        obsMessage.postValue("OTP sent ")
                        obsIsOtpSent.postValue(true)
                    } else {
                        obsMessage.postValue("Sending OTP Failed")
                    }
                    obsIsDataLoading.postValue(false)
                }

            } catch (e: ApiException) {
                obsMessage.postValue(e.message!!)
                obsIsDataLoading.postValue(false)
            } catch (e: SocketTimeoutException) {
                obsMessage.postValue("Slow Network!\nPlease go back and come again")
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

    fun submitOtp(otp: String) {
        obsIsDataLoading.postValue(true)
        ioScope.launch {
            try {
                repoUser.verifyOTP(getVerifyOTPbody(otp)) { otpResponse ->

                    Log.e("VERIFY OTP RES::::", "${otpResponse.toString()}, ${getVerifyOTPbody(otp)}")
                    if (otpResponse?.status.equals("success")) {
                        obsMessage.postValue("OTP Verified ")
                        obsIsOtpVerified.postValue(true)
                    } else {
                        obsMessage.postValue("Verify OTP Failed")
                    }
                }
                obsIsDataLoading.postValue(false)
            } catch (e: ApiException) {
                obsMessage.postValue(e.message!!)
                obsIsDataLoading.postValue(false)
            } catch (e: RiderLoginException) {
                repoPrefs.clearLoggedInUser()
                isUserLogout.postValue(true)
                obsIsDataLoading.postValue(false)
            } catch (e: SocketTimeoutException) {
                obsMessage.postValue("Slow Network!\nPlease go back and come again")
                obsIsDataLoading.postValue(false)
            } catch (e: Exception) {
                obsMessage.postValue(e.message + "")
                obsIsDataLoading.postValue(false)
            }
        }

    }

    private fun getVerifyOTPbody(otp: String): RequestBody {
        val json = JSONObject()
        json.put("ph_num", repoPrefs.getLoggedInUser()?.phone_number.toString())
        json.put("otp", otp)
        return json.toString().toRequestBody("application/json".toMediaTypeOrNull())
    }

    private fun saveDetailsToSharedPreferences() {
        var user = repoPrefs.getLoggedInUser()
        user?.aadhar_number = obsAadhar.value
        user?.pan_card = obsPan.value
        user?.bank_account_no = obsAccount.value
        user?.ifsc_no = obsIFSC.value
        user?.email = obsEmail.value
        user?.alternate_no = obsAlternate.value
        repoPrefs.saveLoggedInUser(user!!)
    }

}