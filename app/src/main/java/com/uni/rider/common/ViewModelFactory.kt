package com.uni.rider.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Common factory class for instantiating viewModels with additional parameters
 */
class ViewModelFactory<VM : BaseViewModel>(val creator: () -> VM) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return creator() as T
    }
}