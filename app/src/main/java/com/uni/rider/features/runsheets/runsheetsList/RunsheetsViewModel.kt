package com.uni.rider.features.runsheets.runsheetsList

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.uni.data.internal.common.ApiException
import com.uni.data.internal.common.RiderLoginException
import com.uni.data.models.MonthYear
import com.uni.data.models.Runsheet
import com.uni.rider.common.BaseViewModel
import com.uni.rider.common.isStatusSuccess
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import java.util.*

class RunsheetsViewModel(context: Application) : BaseViewModel(context) {

    val obsRunsheetssList: MutableLiveData<List<Runsheet>> = MutableLiveData()
    val obsMonthYear: MutableLiveData<MonthYear> = MutableLiveData()

    fun getRunsheetsList() {
        var monthYear = repoPrefs.getMonthYear()
        if (monthYear == null) {
            monthYear = MonthYear(Calendar.getInstance().get(Calendar.MONTH) + 1, Calendar.getInstance().get(Calendar.YEAR))
            repoPrefs.saveMonthYear(monthYear)
        }

        obsMonthYear.postValue(monthYear)

        obsIsDataLoading.postValue(true)
        ioScope.launch {
            try {
                Log.e("MONTH YEAR:::", monthYear.toString())
                repoTextData.getRunsheets(monthYear) { runsheetsResponce ->
                    obsRunsheetssList.postValue(runsheetsResponce)
                    obsIsDataLoading.postValue(false)
                }
            } catch (e: ApiException) {
                obsMessage.postValue(e.message!!)
                obsRunsheetssList.postValue(null)
                obsIsDataLoading.postValue(false)
            } catch (e: SocketTimeoutException) {
                obsMessage.postValue("Slow Network!\nPlease ty again")
                obsRunsheetssList.postValue(null)
                obsIsDataLoading.postValue(false)
            } catch (e: RiderLoginException) {
                repoPrefs.clearLoggedInUser()
                obsRunsheetssList.postValue(null)
                isUserLogout.postValue(true)
                obsIsDataLoading.postValue(false)
            } catch (e: Exception) {
                obsMessage.postValue(e.message + "")
                obsRunsheetssList.postValue(null)
                obsIsDataLoading.postValue(false)
            }
        }
    }

    fun closeRunsheet(runsheetId: String) {
        obsIsDataLoading.postValue(true)
        ioScope.launch {
            try {
                repoTextData.closeRunsheet(runsheetId) { closeResponse ->
                    obsMessage.postValue(closeResponse?.message!!)
                    if (closeResponse.status == "200" || closeResponse.status!!.isStatusSuccess()) {
                        getRunsheetsList()
                    }
                    obsIsDataLoading.postValue(false)
                }
            } catch (e: ApiException) {
                obsMessage.postValue(e.message!!)
                obsIsDataLoading.postValue(false)
            } catch (e: RiderLoginException) {
                repoPrefs.clearLoggedInUser()
                isUserLogout.postValue(true)
                obsIsDataLoading.postValue(false)
            } catch (e: SocketTimeoutException) {
                obsMessage.postValue("Slow Network\nPlease ty again")
                obsIsDataLoading.postValue(false)
            } catch (e: Exception) {
                obsMessage.postValue(e.message.toString())
                obsIsDataLoading.postValue(false)
            }
        }
    }


}