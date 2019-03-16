package com.example.weroo.becksco.home

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.example.weroo.becksco.R
import com.example.weroo.becksco.api.DefaultPrefHelper
import com.example.weroo.becksco.api.postApi
import com.example.weroo.becksco.model.HomeResponse
import com.example.weroo.becksco.model.Page
import com.example.weroo.becksco.model.PostSummaryDTO
import com.example.weroo.becksco.post.PostCreateActivity
import kotlinx.android.synthetic.main.activity_home.*
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class HomeActivity : AppCompatActivity() {

    private val postSummaryAdapter = PostSummaryAdapter()

    // AppCompatActivity()의 onCreate를 overrding, Super은 상위 class를 말함, 즉 AppCompatActivity()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initHome()
    }

    private fun initHome() {
        floatingActionButton.setOnClickListener{
            startActivity(Intent(this, PostCreateActivity::class.java))
        }
        // activity_home의 postSummaryList id 값
        // recyclerview이기 때문에 layoutManager 등 함수가 있음
        // 설정하는 layoutManager에 따라 보여지는 view 배치 방법이 결정됨
        postSummaryList.layoutManager = LinearLayoutManager(this)

        // adapter로는 postSummaryAdapter 설정
        postSummaryList.adapter = postSummaryAdapter
    }
    }
