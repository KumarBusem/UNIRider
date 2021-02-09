package com.uni.rider.features.services

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.uni.rider.common.BaseViewModel

class ServicesViewModel(context: Application) : BaseViewModel(context) {


    val obsIsUserAuthenticated = MutableLiveData<Boolean>()


}