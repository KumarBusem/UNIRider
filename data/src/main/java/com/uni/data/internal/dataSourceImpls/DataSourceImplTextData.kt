package com.uni.data.internal.dataSourceImpls

import com.uni.data.dataSources.definitions.DataSourceTextData
import com.uni.data.models.*
import okhttp3.RequestBody

internal class DataSourceImplTextData : DataSourceTextData() {


    override suspend fun getRunsheets(monthYear: MonthYear, res: (List<Runsheet>?) -> Unit) {
        res(apiRequest { API.getRunsheets(monthYear.month.toString(), monthYear.year.toString(), repoPrefs.getLoggedInUser()?.token) })
    }

    override suspend fun getFeedbacks(res: (FeedbackResponse?) -> Unit){
        res(apiRequest { API.getFeedbacks(repoPrefs.getLoggedInUser()?.token) })
    }

    override suspend fun getSalary(res: (SalaryResponse?) -> Unit) {
        res(apiRequest { API.getSalary(repoPrefs.getLoggedInUser()?.token) })
    }

    override suspend fun addFeedback(requestBody: RequestBody, res: (SimpleResponse?) -> Unit) {
        res(apiRequest { API.customerDetails(requestBody, repoPrefs.getLoggedInUser()?.token) })
    }

    override suspend fun verifyReference(requestBody: RequestBody, res: (SimpleResponse?) -> Unit) {
        res(apiRequest { API.refVerifiedPost(requestBody, repoPrefs.getLoggedInUser()?.token) })
    }

    override suspend fun changePassword(requestBody: RequestBody, res: (SimpleResponse?) -> Unit){
        res(apiRequest { API.changePassword(requestBody, repoPrefs.getLoggedInUser()?.token) })
    }

    override suspend fun closeRunsheet(runsheetId: String, res: (SimpleResponse?) -> Unit){
        res(apiRequest { API.closeRunsheet(runsheetId, repoPrefs.getLoggedInUser()?.token)})
    }

}