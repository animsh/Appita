package com.animsh.appita.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Created by animsh on 3/15/2021.
 */
class SharedViewModel : ViewModel() {
    private val mutableSelectedItem = MutableLiveData<Boolean>()
    val backFrom: LiveData<Boolean> get() = mutableSelectedItem

    fun setBackFrom(item: Boolean) {
        mutableSelectedItem.value = item
    }
}