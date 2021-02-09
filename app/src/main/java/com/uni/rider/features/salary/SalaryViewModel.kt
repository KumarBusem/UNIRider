package com.uni.rider.features.salary

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.uni.data.internal.common.ApiException
import com.uni.data.internal.common.RiderLoginException
import com.uni.data.models.Salary
import com.uni.rider.common.BaseViewModel
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class SalaryViewModel(context: Application) : BaseViewModel(context) {

    val obsSalaryList: MutableLiveData<List<Salary>> = MutableLiveData()

    fun getSalaryList() {

        obsIsDataLoading.postValue(true)
        ioScope.launch {
            try {
                repoTextData.getSalary { responce ->
                    Log.e("Salary::", responce.toString())
                    obsSalaryList.postValue(responce?.data)
                    obsIsDataLoading.postValue(false)
                }
            } catch (e: ApiException) {
                obsMessage.postValue(e.message!!)
                obsSalaryList.postValue(null)
                obsIsDataLoading.postValue(false)
            } catch (e: SocketTimeoutException) {
                obsMessage.postValue("Slow Network!\nPlease ty again")
                obsSalaryList.postValue(null)
                obsIsDataLoading.postValue(false)
            } catch (e: RiderLoginException) {
                repoPrefs.clearLoggedInUser()
                obsSalaryList.postValue(null)
                isUserLogout.postValue(true)
                obsIsDataLoading.postValue(false)
            } catch (e: Exception) {
                obsMessage.postValue(e.message + "")
                obsSalaryList.postValue(null)
                obsIsDataLoading.postValue(false)
            }
        }
    }

}