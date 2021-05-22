package com.example.neugelb.apis

import retrofit2.Response

fun <T> Response<T>.checkSuccessful(): Response<T> {
    if (!isSuccessful) {
        throw Exception("Exception: ${message()} (code: ${code()})")
    }
    return this
}