package com.uni.rider.features.help

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.uni.rider.common.BaseViewModel

class HelpViewModel(context: Application) : BaseViewModel(context) {


    val obsIsUserAuthenticated = MutableLiveData<Boolean>()


}