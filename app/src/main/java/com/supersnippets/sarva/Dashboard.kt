package com.supersnippets.sarva

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.facebook.CallbackManager
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseUser
import com.supersnippets.sarva.viewModels.AuthViewModel
import kotlinx.android.synthetic.main.activity_dashboard.*

class Dashboard : AppCompatActivity() {
    lateinit var viewModel: AuthViewModel
    private val callbackManager = CallbackManager.Factory.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        configActionBar()
        configViewModel()
        setBtnClickListeners()
    }

    private fun setBtnClickListeners() {
        btnLogout.setOnClickListener {
            val sharedPref = getSharedPreferences(AUTH_PREF, MODE_PRIVATE)
            val isFbAuth = sharedPref.getBoolean("fbAuth", false)

            if (isFbAuth) {
                LoginManager.getInstance().logOut()

                with(sharedPref.edit()) {
                    putBoolean("fbAuth", false)
                    apply()
                }

                val intent = Intent(this@Dashboard, LoginActivity::class.java)
                startActivity(intent)
                this@Dashboard.finish()
            } else {
                viewModel.logout()
            }
        }
    }

    private fun configViewModel() {
        viewModel = ViewModelProvider(this).get(AuthViewModel::class.java)

        viewModel.getCurrentUser().observe(this, Observer {
            navigateToLogin(it)
        })
    }

    private fun configActionBar() {
        supportActionBar?.setBackgroundDrawable(ColorDrawable(getColor(R.color.screen_background)))
        supportActionBar?.title = ""
        supportActionBar?.elevation = 0F
    }

    private fun navigateToLogin(user: FirebaseUser?) {
        if (user == null) {
            val intent = Intent(this@Dashboard, LoginActivity::class.java)
            startActivity(intent)
            this@Dashboard.finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}