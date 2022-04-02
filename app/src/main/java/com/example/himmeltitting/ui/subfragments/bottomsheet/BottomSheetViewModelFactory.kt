package com.example.himmeltitting.ui.subfragments.bottomsheet

import com.example.himmeltitting.ui.SharedViewModel

interface Factory<T> {
    fun create(): BottomSheetViewModel
}

class BottomSheetViewModelFactory(private val sharedViewModel: SharedViewModel) : Factory<BottomSheetViewModel> {
    override fun create(): BottomSheetViewModel {
        return BottomSheetViewModel(sharedViewModel)
    }
}