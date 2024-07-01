package com.example.milkymate.Store.presentation.UI.Viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.milkymate.Store.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeScreenViewModel: ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> get() = _user

    init {
        Log.e("MilkyMateHome", "HomeScreenViewModel initialized")
    }

    fun setUser(user: User) {
        viewModelScope.launch {
            _user.emit(user)
        }
    }
}
