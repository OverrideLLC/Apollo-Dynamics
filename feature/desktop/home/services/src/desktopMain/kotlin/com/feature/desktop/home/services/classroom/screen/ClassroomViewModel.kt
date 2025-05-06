package com.feature.desktop.home.services.classroom.screen

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ClassroomViewModel : ViewModel() {
    private val _state = MutableStateFlow(ClassroomData())
    val state = _state.asStateFlow()

    fun update(update: ClassroomData.() -> ClassroomData) {
        _state.value = update(_state.value)
    }
}