package com.example.weroo.becksco.api

import com.example.weroo.becksco.model.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

val postApi by lazy { PostBoardService.create() }

//DTO: Data Transfer object의 약자 : 직접 서버에 접근해서 객체에 접근하면 객체를 변경할 수도 있으나
// 한번 매핑시켜놓고 transfer만을 위한 객체를 만든 것임

interface PostBoardService {

    @POST("/users/sign")
    fun sign(@Body user: User): Call<User>

    @POST("/posts")
    fun postOnBoard(@Body post: PostCreateDTO): Call<PostCreateDTO>

    @GET("/posts") // "/posts?page=1"
    fun getPosts(@Query("page") page: Int): Call<Page<PostSummaryDTO>>
    // 게시글을 가져오는데 페이지에 따라 내용물이 바뀌는 파라미터가 담기는 곳

    companion object {
        fun create(): PostBoardService {
            // OkHttpClient interceptor is immutable list
            // So, must add interceptor at build step
            val client = OkHttpClient.Builder()
                .addInterceptor(AddCookiesInterceptor())
                .addInterceptor(ReceivedCookiesInterceptor())
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://android-tutorial-server.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            return retrofit.create(PostBoardService::class.java)
        }
    }
}