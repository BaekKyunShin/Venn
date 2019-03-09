package com.example.weroo.becksco.home

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity;
import com.example.weroo.becksco.R
import com.example.weroo.becksco.post.PostCreateActivity
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    // AppCompatActivity()의 onCreate를 overrding, Super은 상위 class를 말함, 즉 AppCompatActivity()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        floatingActionButton.setOnClickListener {
            onClickCreatePostButton()}
    }

    private fun onClickCreatePostButton() {
        startActivity(Intent(this, PostCreateActivity::class.java))
    }


    }

