package com.supersnippets.sarva.repositories

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class BaseRepository {
    fun <T> Call<T>.makeCall(callback: CallBackKt<T>.() -> Unit) {
        val callBackKt = CallBackKt<T>()
        callback.invoke(callBackKt)
        this.enqueue(callBackKt)
    }

    class CallBackKt<T> : Callback<T> {
        var onResponseSuccess: ((Response<T>) -> Unit)? = null
        var onResponseFailure: ((error: Throwable/*APIErrors*/?) -> Unit)? = null

        override fun onFailure(call: Call<T>, t: Throwable) {
            println("repo on failure")
            print(t.stackTrace)
            onResponseFailure?.invoke(t/*APIErrors()*/)  //error handling
        }

        override fun onResponse(call: Call<T>, response: Response<T>) {
            if (response.isSuccessful) {
                println("repo success")
                onResponseSuccess?.invoke(response)
            } else {
                println("repo failure")
                //onResponseFailure?.invoke(ErrorUtils.parseError(response)) //error handling
            }
        }
    }
}