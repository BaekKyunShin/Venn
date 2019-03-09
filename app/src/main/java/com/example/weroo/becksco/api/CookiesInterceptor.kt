package com.example.weroo.becksco.api

import okhttp3.Interceptor
import okhttp3.Response

const val API_PREFERENCE = "api_preference"

// SharedPreference에 이미 cookies가 저장된 상태에서 User가 requset를 보낼 때
// SharedPreference에서 cookies를 가져오는 Interceptor
class AddCookiesInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()

        // Get cookie from shared preference
        val cookies = DefaultPrefHelper.instance().getStringSet(API_PREFERENCE)
        for (cookie in cookies) {
            builder.addHeader("Cookie", cookie.toString())
        }

        // Set user-agent
        builder.removeHeader("User-Agent").addHeader("User-Agent", "Android")
        return chain.proceed(builder.build())
    }
}

// 서버에 Session이 생성된 뒤에 SharedPreference에 cookies를 저장하는 Interceptor
class ReceivedCookiesInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        val cookies = response.headers("Set-Cookie")
        if (cookies.isEmpty()) {
            return response
        }

        // Set cookie to shared preferences
        DefaultPrefHelper.instance().setStringSet(API_PREFERENCE, cookies.toSet())
        return response
    }
}