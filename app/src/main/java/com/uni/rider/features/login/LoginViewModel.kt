package com.uni.rider.features.login

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.uni.data.internal.common.ApiException
import com.uni.data.internal.common.RiderLoginException
import com.uni.data.models.User
import com.uni.rider.common.BaseViewModel
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class LoginViewModel(context: Application) : BaseViewModel(context) {

    val obsIsUserAuthenticated = MutableLiveData<User>()

    fun loginUser(username: String, password: String) {

        obsIsDataLoading.postValue(true)
        ioScope.launch {
            try {
                repoUser.loginUser(username, password) { user ->
                    when {
                        user == null -> obsMessage.postValue("Please Try Again")
                        user.rider_type == User.RIDER_TYPE_LIQUOR -> obsMessage.postValue("You are not LMA")
                        else -> {
                            user.self_declaration = false
                            repoPrefs.saveLoggedInUser(user)
                            obsIsUserAuthenticated.postValue(user)
                        }
                    }
                    obsIsDataLoading.postValue(false)
                }
            } catch (e: SocketTimeoutException) {
                obsMessage.postValue("Slow Network")
                obsIsDataLoading.postValue(false)
            } catch (e: RiderLoginException) {
                obsMessage.postValue("Login Failed")
                obsIsDataLoading.postValue(false)
            } catch (e: ApiException) {
                obsMessage.postValue("APIException ${e.message.toString()}")
                obsIsDataLoading.postValue(false)
            } catch (e: Exception) {
                obsMessage.postValue(e.message + "")
                obsIsDataLoading.postValue(false)
            }
        }
    }
}