package com.supersnippets.sarva

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseUser
import com.supersnippets.sarva.helpers.hideKeyboard
import com.supersnippets.sarva.viewModels.AuthViewModel
import kotlinx.android.synthetic.main.activity_registration.*


class RegistrationActivity : AppCompatActivity() {
    lateinit var viewModel: AuthViewModel
    lateinit var content: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        content = findViewById(android.R.id.content)

        configActionBar()
        configViewModel()
        setBtnClickListeners()
    }

    private fun configViewModel() {
        viewModel = ViewModelProvider(this).get(AuthViewModel::class.java)

        viewModel.getCurrentUser().observe(this, Observer {
            updateUI(it)
        })
    }

    private fun setBtnClickListeners() {
        btnLogin.setOnClickListener {
            onBackPressed()
        }

        btnRegister.setOnClickListener {
            hideKeyboard(this)

            if (validateFields()) {
                viewModel.register(txtEmail.text.toString(), txtPassword.text.toString())
            } else {
                Snackbar.make(
                    content,
                    "Please enter valid details",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun validateFields(): Boolean {
        return txtEmail.text.isNotEmpty()
                && txtPassword.text.isNotEmpty()
                && txtConfirmPassword.text.isNotEmpty()
                && txtPassword.text.toString() == txtConfirmPassword.text.toString()
    }

    private fun configActionBar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setBackgroundDrawable(ColorDrawable(getColor(R.color.screen_background)))
        supportActionBar?.title = ""
        supportActionBar?.elevation = 0F
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            val intent = Intent(this@RegistrationActivity, Dashboard::class.java)
            startActivity(intent)
            this@RegistrationActivity.finish()
        }
    }
}