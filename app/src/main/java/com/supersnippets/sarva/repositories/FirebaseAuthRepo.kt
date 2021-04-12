package com.supersnippets.sarva.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class FirebaseAuthRepo private constructor(){
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    var userMutableLiveData = MutableLiveData<FirebaseUser>()
    private val TAG = "FirebaseAuthRepo"

    companion object {
        @Volatile
        private var instance: FirebaseAuthRepo? = null

        fun getInstance(): FirebaseAuthRepo =
            instance ?: synchronized(this) {
                instance ?: FirebaseAuthRepo().also { instance = it }
            }
    }

    fun register(email: String, password: String): MutableLiveData<FirebaseUser> {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                Log.d(TAG, "New user registration: " + task.isSuccessful)
                if (task.isSuccessful) {
                    Log.d(TAG, "registration success")
                    userMutableLiveData.value = firebaseAuth.currentUser
                } else {
                    Log.w(TAG, "registration failure", task.exception)
                }
            }
        return userMutableLiveData
    }

    fun login(email: String, password: String): MutableLiveData<FirebaseUser> {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    userMutableLiveData.value = firebaseAuth.currentUser
                } else {
                    Log.w(TAG, "login failure", task.exception)
                }
            }

        return userMutableLiveData
    }

    fun logOut() {
        firebaseAuth.signOut()
        userMutableLiveData.value = null
    }

    fun getCurrentUser(): MutableLiveData<FirebaseUser> {
        return userMutableLiveData
    }
}