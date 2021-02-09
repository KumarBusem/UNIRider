package com.uni.data.dataSources.definitions

import com.uni.data.models.MonthYear
import com.uni.data.models.User

abstract class DataSourceSharedPreferences {

    abstract fun saveLoggedInUser(user: User)
    abstract fun getLoggedInUser(): User?
    abstract fun clearLoggedInUser()

    abstract fun saveMonthYear(monthYear: MonthYear)
    abstract fun getMonthYear(): MonthYear?
    abstract fun clearMonthYear()

    abstract fun saveSelectedRunsheetId(runsheetId: String)
    abstract fun clearSelectedRunsheetId()
    abstract fun getSelectedRunsheetId(): String?

    abstract fun saveSelectedPickupId(pickupId: String)
    abstract fun clearSelectedPickupId()
    abstract fun getSelectedPickupId(): String?

    abstract fun isProfilePicUpdated(updated: Boolean)
    abstract fun isProfilePicUpdated(): Boolean

    abstract fun deleteAllPrefs()
}