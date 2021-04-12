package com.supersnippets.sarva

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseUser
import com.supersnippets.sarva.helpers.hideKeyboard
import com.supersnippets.sarva.viewModels.AuthViewModel
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {
    private lateinit var content: View
    private val callbackManager = CallbackManager.Factory.create()
    lateinit var viewModel: AuthViewModel

    override fun onStart() {
        super.onStart()

        val sharedPref = getSharedPreferences(AUTH_PREF, Context.MODE_PRIVATE)
        val isFbAuth = sharedPref.getBoolean("fbAuth", false)

        if (isFbAuth) {
            val intent = Intent(this@LoginActivity, Dashboard::class.java)
            startActivity(intent)
            this@LoginActivity.finish()
        } else {
            viewModel.getCurrentUser()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        content = findViewById(android.R.id.content)

        configViewModel()
        setBtnClickListeners()

        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult?> {
                override fun onSuccess(loginResult: LoginResult?) {
                    val accessToken = AccessToken.getCurrentAccessToken()
                    val isLoggedIn = accessToken != null && !accessToken.isExpired

                    println("fb login success $isLoggedIn")

                    if (isLoggedIn) {
                        val sharedPref = getSharedPreferences(AUTH_PREF, Context.MODE_PRIVATE)
                        with(sharedPref.edit()) {
                            putBoolean("fbAuth", true)
                            apply()
                        }
                        val intent = Intent(this@LoginActivity, Dashboard::class.java)
                        startActivity(intent)
                        this@LoginActivity.finish()
                    }

                }

                override fun onCancel() {
                    println("fb login cancelled")
                }

                override fun onError(exception: FacebookException) {
                    println("fb login error")
                }
            })
    }

    private fun setBtnClickListeners() {
        btnLogin.setOnClickListener {
            hideKeyboard(this)
            if (validateFields()) {
                viewModel.login(txtUsername.text.toString(), txtPassword.text.toString())
            } else {
                Snackbar.make(
                    content,
                    "Please enter valid details",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

        btnFb.setOnClickListener {
            btnFbLogin.performClick()
        }

        btnRegister.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegistrationActivity::class.java)
            startActivity(intent)
        }
    }

    private fun configViewModel() {
        viewModel = ViewModelProvider(this).get(AuthViewModel::class.java)

        viewModel.getCurrentUser().observe(this, Observer {
            navigateToDashboard(it)
        })
    }

    private fun validateFields(): Boolean {
        return txtUsername.text.isNotEmpty()
                && txtPassword.text.isNotEmpty()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun navigateToDashboard(user: FirebaseUser?) {
        if (user != null) {
            val intent = Intent(this@LoginActivity, Dashboard::class.java)
            startActivity(intent)
            this@LoginActivity.finish()
        }
    }
}

