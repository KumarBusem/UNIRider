package com.uni.data.dataSources.definitions

import com.uni.data.models.*
import okhttp3.RequestBody

abstract class DataSourceTextData : BaseDataSource() {

    abstract suspend fun getRunsheets(monthYear: MonthYear, res: (List<Runsheet>?) -> Unit)

    abstract suspend fun getFeedbacks(res: (FeedbackResponse?) -> Unit)

    abstract suspend fun getSalary(res: (SalaryResponse?) -> Unit)

    abstract suspend fun addFeedback(requestBody: RequestBody, res: (SimpleResponse?) -> Unit)

    abstract suspend fun verifyReference(requestBody: RequestBody, res: (SimpleResponse?) -> Unit)

    abstract suspend fun changePassword(requestBody: RequestBody, res: (SimpleResponse?) -> Unit)

    abstract suspend fun closeRunsheet(runsheetId: String, res: (SimpleResponse?) -> Unit)
}