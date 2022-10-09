package com.assessment.quizebuilder.core

import androidx.lifecycle.ViewModel

open class BaseViewModel: ViewModel() {

    val success = SingleLiveEvent<Boolean>()
    val failure = SingleLiveEvent<String>()
    var progress = SingleLiveEvent<Boolean>()

    fun showLoading() {
        progress.postValue(true)
    }

    fun hideLoading() {
        progress.postValue(false)
    }
}