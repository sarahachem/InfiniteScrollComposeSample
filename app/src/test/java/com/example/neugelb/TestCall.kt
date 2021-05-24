package com.example.neugelb

import okhttp3.MediaType
import okhttp3.Request
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TestCall<T>(
    private val response: Response<T>
)
: Call<T> {

companion object {
    inline fun <reified T> buildSuccess(body: T): TestCall<T> {
        return TestCall(Response.success(body))
    }

    inline fun <reified T> buildHttpError(errorCode: Int, contentType: String, content: String): TestCall<T> {
        return TestCall(Response.error(errorCode, ResponseBody.create(MediaType.parse(contentType), content)))
    }
}

override fun execute(): Response<T> = response

override fun enqueue(callback: Callback<T>?) {}

override fun isExecuted(): Boolean = false

override fun clone(): Call<T> = this

override fun isCanceled(): Boolean = false

override fun cancel() {}

override fun request(): Request? = null
}