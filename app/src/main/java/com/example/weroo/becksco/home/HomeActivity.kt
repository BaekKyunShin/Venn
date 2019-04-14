package com.example.weroo.becksco.home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import com.example.weroo.becksco.R
import com.example.weroo.becksco.api.DefaultPrefHelper
import com.example.weroo.becksco.api.postApi
import com.example.weroo.becksco.model.Page
import com.example.weroo.becksco.model.PostSummaryDTO
import com.example.weroo.becksco.post.PostCreateActivity
import com.example.weroo.becksco.sign.SIGN_PREFERENCE
import com.example.weroo.becksco.sign.SignActivity
import kotlinx.android.synthetic.main.activity_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeActivity : AppCompatActivity() {

    private val postSummaryAdapter = PostSummaryAdapter()
    private var currentItems: Int = 0
    private var scrollOutItems: Int = 0
    private var pageNum: Int = 0

    private val SCROLL_DIRECTION_BOTTOM: Int = 1


    // AppCompatActivity()의 onCreate를 overrding, Super은 상위 class를 말함, 즉 AppCompatActivity()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Data Binding
        bindData()
    }

    private fun bindData() {
        floatingActionButton.setOnClickListener {
            startActivity(Intent(this, PostCreateActivity::class.java))
        }
        // activity_home의 postSummaryList id 값
        // recyclerview이기 때문에 layoutManager 등 함수가 있음
        // 설정하는 layoutManager에 따라 보여지는 view 배치 방법이 결정됨
        postSummaryList.layoutManager = LinearLayoutManager(this)

        // adapter로는 postSummaryAdapter 설정
        postSummaryList.adapter = postSummaryAdapter
        requestHome()

        postSummaryList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (!postSummaryList.canScrollVertically(SCROLL_DIRECTION_BOTTOM)) {
                    requestHome()
                }
            }
        })
    }

    private fun requestHome() {

        postSummaryAdapter.setLoading(true)
        postSummaryAdapter.notifyDataSetChanged()
        Handler().postDelayed({
            postApi.getPosts(pageNum).enqueue(object : Callback<Page<PostSummaryDTO>> {
                override fun onResponse(call: Call<Page<PostSummaryDTO>>, response: Response<Page<PostSummaryDTO>>) {
                    if (response.code() != 200) {
                        Toast.makeText(this@HomeActivity, "What the .. 201", Toast.LENGTH_SHORT).show()
                        return
                    }
                    val content = response.body()?.content
                    if (content == null) {
                        signOut()
                        return
                    }

                    ////////////
                    pageNum++;

                    postSummaryAdapter.appendPosts(response.body()?.content ?: listOf())
                    postSummaryAdapter.setLoading(false)

                    // data가 바뀌었다고 view에 알려주는 역할
                    postSummaryAdapter.notifyDataSetChanged()
                }

                override fun onFailure(call: Call<Page<PostSummaryDTO>>, t: Throwable) {
                    postSummaryAdapter.setLoading(false)
                    Toast.makeText(this@HomeActivity, "What the ..", Toast.LENGTH_SHORT).show()
                }

            })
        }, 3000)
    }

    private fun signOut() {
        DefaultPrefHelper.instance().setBoolean(SIGN_PREFERENCE, false)
        startActivity(Intent(this, SignActivity::class.java))
        finish()
    }
}
