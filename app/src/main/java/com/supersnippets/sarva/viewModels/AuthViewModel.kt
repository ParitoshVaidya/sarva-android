package com.supersnippets.sarva.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.supersnippets.sarva.repositories.FirebaseAuthRepo

class AuthViewModel : ViewModel() {
    private val firebaseAuthRepo = FirebaseAuthRepo.getInstance()
    var userMutableLiveData = MutableLiveData<FirebaseUser>()

    fun register(email: String, password: String): MutableLiveData<FirebaseUser> {
        return firebaseAuthRepo.register(email, password)
    }

    fun login(email: String, password: String): MutableLiveData<FirebaseUser> {
        return firebaseAuthRepo.login(email, password)
    }

    fun getCurrentUser(): MutableLiveData<FirebaseUser> {
        userMutableLiveData = firebaseAuthRepo.getCurrentUser()
        return userMutableLiveData
    }

    fun logout() {
        firebaseAuthRepo.logOut()
    }
}