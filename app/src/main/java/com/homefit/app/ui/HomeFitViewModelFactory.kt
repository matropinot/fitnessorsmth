package com.homefit.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.homefit.app.data.HomeFitRepository

class HomeFitViewModelFactory(
    private val repository: HomeFitRepository,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass.isAssignableFrom(HomeFitViewModel::class.java))
        return HomeFitViewModel(repository) as T
    }
}
