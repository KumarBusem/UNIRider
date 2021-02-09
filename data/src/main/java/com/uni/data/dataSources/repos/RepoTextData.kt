package com.uni.data.dataSources.repos

import com.uni.data.dataSources.definitions.DataSourceTextData
import com.uni.data.internal.dataSourceImpls.DataSourceImplTextData
import com.uni.data.models.*
import okhttp3.RequestBody

class RepoTextData : DataSourceTextData() {

    private val mRunsheetDataSource: DataSourceTextData by lazy { DataSourceImplTextData() }

    override suspend fun getRunsheets(monthYear: MonthYear, res: (List<Runsheet>?) -> Unit)
            = mRunsheetDataSource.getRunsheets(monthYear, res)

    override suspend fun getFeedbacks(res: (FeedbackResponse?) -> Unit)
            = mRunsheetDataSource.getFeedbacks(res)

    override suspend fun getSalary(res: (SalaryResponse?) -> Unit)
            = mRunsheetDataSource.getSalary(res)

    override suspend fun addFeedback(requestBody: RequestBody, res: (SimpleResponse?) -> Unit)
            = mRunsheetDataSource.addFeedback(requestBody, res)

    override suspend fun verifyReference(requestBody: RequestBody, res: (SimpleResponse?) -> Unit)
            = mRunsheetDataSource.verifyReference(requestBody, res)

    override suspend fun changePassword(requestBody: RequestBody, res: (SimpleResponse?) -> Unit)
            = mRunsheetDataSource.changePassword(requestBody, res)

    override suspend fun closeRunsheet(runsheetId: String, res: (SimpleResponse?) -> Unit)
            = mRunsheetDataSource.closeRunsheet(runsheetId, res)
}