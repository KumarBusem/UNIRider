package com.uni.data.dataSources.repos

import com.uni.data.dataSources.definitions.DataSourceSharedPreferences
import com.uni.data.internal.dataSourceImpls.DataSourceImplSharedPreferences
import com.uni.data.models.MonthYear
import com.uni.data.models.User

class RepoSharedPreferences : DataSourceSharedPreferences() {

    private val mSpDS: DataSourceSharedPreferences by lazy { DataSourceImplSharedPreferences() }

    override fun saveLoggedInUser(user: User) = mSpDS.saveLoggedInUser(user)
    override fun getLoggedInUser(): User? = mSpDS.getLoggedInUser()
    override fun clearLoggedInUser() = mSpDS.clearLoggedInUser()

    override fun saveMonthYear(user: MonthYear) = mSpDS.saveMonthYear(user)
    override fun getMonthYear(): MonthYear? = mSpDS.getMonthYear()
    override fun clearMonthYear() = mSpDS.clearMonthYear()

    override fun saveSelectedRunsheetId(runsheetId: String) = mSpDS.saveSelectedRunsheetId(runsheetId)
    override fun clearSelectedRunsheetId() = mSpDS.clearSelectedRunsheetId()
    override fun getSelectedRunsheetId(): String? = mSpDS.getSelectedRunsheetId()

    override fun saveSelectedPickupId(pickupId: String) = mSpDS.saveSelectedPickupId(pickupId)
    override fun clearSelectedPickupId() = mSpDS.clearSelectedPickupId()
    override fun getSelectedPickupId(): String? = mSpDS.getSelectedPickupId()


    override fun isProfilePicUpdated(updated: Boolean) = mSpDS.isProfilePicUpdated(updated)
    override fun isProfilePicUpdated(): Boolean = mSpDS.isProfilePicUpdated()

    override fun deleteAllPrefs() = mSpDS.deleteAllPrefs()
}