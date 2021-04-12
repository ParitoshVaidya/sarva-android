package com.supersnippets.sarva.helpers

import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity

fun hideKeyboard(activity: AppCompatActivity) {
    activity.currentFocus?.let { view ->
        val imm =
            activity.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
    }
}