package com.uni.data.internal.dataSourceImpls

import com.uni.data.dataSources.definitions.DataSourceSharedPreferences
import com.uni.data.internal.common.SharedPreferenceHelper
import com.uni.data.models.MonthYear
import com.uni.data.models.User

class DataSourceImplSharedPreferences : DataSourceSharedPreferences() {

    private val mSpHelper: SharedPreferenceHelper by lazy { SharedPreferenceHelper.getInstance() }

    override fun saveLoggedInUser(user: User) = mSpHelper.putObject(SP_LOGGED_IN_USER, user)
    override fun getLoggedInUser(): User? = mSpHelper.getObject(SP_LOGGED_IN_USER)
    override fun clearLoggedInUser() = mSpHelper.remove(SP_LOGGED_IN_USER)

    override fun saveMonthYear(user: MonthYear) = mSpHelper.putObject(SP_RUNSHEETS_MONTH_YEAR, user)
    override fun getMonthYear(): MonthYear? = mSpHelper.getObject(SP_RUNSHEETS_MONTH_YEAR)
    override fun clearMonthYear() = mSpHelper.remove(SP_LOGGED_IN_USER)

    override fun saveSelectedRunsheetId(runsheetId: String) = mSpHelper.putString(SP_SELECTED_RUNSHEET_ID, runsheetId)
    override fun clearSelectedRunsheetId() = mSpHelper.remove(SP_SELECTED_RUNSHEET_ID)
    override fun getSelectedRunsheetId(): String? = mSpHelper.getString(SP_SELECTED_RUNSHEET_ID)

    override fun saveSelectedPickupId(pickupId: String) = mSpHelper.putString(SP_SELECTED_PICKUP_ID, pickupId)
    override fun clearSelectedPickupId() = mSpHelper.remove(SP_SELECTED_PICKUP_ID)
    override fun getSelectedPickupId(): String? = mSpHelper.getString(SP_SELECTED_PICKUP_ID)

    override fun isProfilePicUpdated(updated: Boolean) = mSpHelper.putBoolean(SP_IS_PROFILE_PIC_UPDATED, updated)
    override fun isProfilePicUpdated(): Boolean = mSpHelper.getBoolean(SP_IS_PROFILE_PIC_UPDATED)


    override fun deleteAllPrefs() = mSpHelper.clear()

    companion object {

        private const val SP_LOGGED_IN_USER: String = "SP_LOGGED_IN_USER"
        private const val SP_RUNSHEETS_MONTH_YEAR: String = "SP_RUNSHEETS_MONTH_YEAR"
        private const val SP_SELECTED_RUNSHEET_ID: String = "SP_SELECTED_RUNSHEET_ID"
        private const val SP_SELECTED_PICKUP_ID: String = "SP_SELECTED_PICKUP_ID"
        private const val SP_SELECTED_PICKUP: String = "SP_SELECTED_PICKUP"
        private const val SP_IS_PROFILE_PIC_UPDATED: String = "SP_IS_PROFILE_PIC_UPDATED"
    }
}