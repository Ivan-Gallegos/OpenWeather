package com.example.network

import android.util.Log
import retrofit2.Response

fun <T> T.logSuccessResponse(tag: String?) {
    Log.d(tag, "$this")
}

fun <T> Response<T>.logErrorResponse(tag: String?) {
    Log.e(
        tag, "${code()}\n" +
                "${errorBody()?.string()}"+
                "${message()}\n" +
                "${raw()}\n"
    )
}
