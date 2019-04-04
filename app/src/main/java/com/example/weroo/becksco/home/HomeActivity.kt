package com.example.weroo.becksco.home

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.AbsListView
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
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import com.github.ybq.android.spinkit.style.DoubleBounce
import com.github.ybq.android.spinkit.sprite.Sprite





class HomeActivity : AppCompatActivity() {

    private val postSummaryAdapter = PostSummaryAdapter()
    private var scrollState: Int = 0 // 0 = idle state, 1 = End of List, -1 = Top of List
    private var currentItems: Int = 0
    private var totalItems: Int = 0
    private var scrollOutItems: Int = 0
    private var pageNum: Int = 0


    // AppCompatActivity()의 onCreate를 overrding, Super은 상위 class를 말함, 즉 AppCompatActivity()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // apply a spin kit
        val doubleBounce = DoubleBounce()
        spin_kit.setIndeterminateDrawable(doubleBounce)

        // Data Binding
        bindData()
    }

    // 화면이 가려졌다가 다시 불리면 onResume이 불려 서버에 저장된 데이터를 다시 가져옴
    override fun onResume() {
        super.onResume()
        bindData()
    }

    private fun bindData() {
        floatingActionButton.setOnClickListener{
            startActivity(Intent(this, PostCreateActivity::class.java))
        }
        // activity_home의 postSummaryList id 값
        // recyclerview이기 때문에 layoutManager 등 함수가 있음
        // 설정하는 layoutManager에 따라 보여지는 view 배치 방법이 결정됨
        postSummaryList.layoutManager = LinearLayoutManager(this)

        // adapter로는 postSummaryAdapter 설정
        postSummaryList.adapter = postSummaryAdapter
        requestHome(0)

        postSummaryList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
           override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
               super.onScrollStateChanged(recyclerView, newState)
               if (!postSummaryList.canScrollVertically(-1)) {
                   Log.i("TopOfList", "Top of list")
                   spin_kit.visibility = View.VISIBLE
                   goToPage(-1)
               } else if (!postSummaryList.canScrollVertically(1)) {
                   Log.i("EndOfList", "End of list")
                   spin_kit.visibility = View.VISIBLE
                   goToPage(1)
               } else {
                   Log.i("idleState", "idle")
               }

               //if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
               //    isScrolling = true
               //}
           }
            /**
           override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
               super.onScrolled(recyclerView, dx, dy)
               // currentItems = LinearLayoutManager(this@HomeActivity)!!.childCount

               if (scrollState == -1) {
                   scrollState = 0
                   goToPage(-1)
               } else if (scrollState == 1) {
                   scrollState = 0
                   goToPage(1)
               }

                totalItems = LinearLayoutManager(this@HomeActivity)!!.itemCount
                scrollOutItems = LinearLayoutManager(this@HomeActivity)!!.findFirstVisibleItemPosition()

                if (isScrolling && (currentItems + scrollOutItems == totalItems)) {
                    isScrolling = false

                }

           }
             */
       })
    }

    private fun goToPage(scrollState: Int) {
        if (pageNum < 0) {
            requestHome(0)
            pageNum = 0
        } else if (pageNum > 3) {
            requestHome(2)
            pageNum = 2
        } else {
            pageNum += scrollState
            requestHome(pageNum)
        }
    }


    private fun requestHome(page: Int) {
        postApi.getPosts(page).enqueue(object : Callback<Page<PostSummaryDTO>> {
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

                postSummaryAdapter.resetPosts(response.body()?.content ?: listOf())

                // data가 바뀌었다고 view에 알려주는 역할
                postSummaryAdapter.notifyDataSetChanged()

                //totalItems = postSummaryList.layoutManager!!.itemCount
                currentItems = LinearLayoutManager(this@HomeActivity).childCount
                scrollOutItems = LinearLayoutManager(this@HomeActivity).findFirstVisibleItemPosition()
                //Log.d("total", totalItems.toString())
                Log.d("scrolloutItems", scrollOutItems.toString())

                Toast.makeText(this@HomeActivity, "currentItems", Toast.LENGTH_SHORT).show()
                spin_kit.visibility = View.GONE
            }

            override fun onFailure(call: Call<Page<PostSummaryDTO>>, t: Throwable) {
                Toast.makeText(this@HomeActivity, "What the ..", Toast.LENGTH_SHORT).show()
            }

        })
    }
    private fun signOut() {
        DefaultPrefHelper.instance().setBoolean(SIGN_PREFERENCE, false)
        startActivity(Intent(this, SignActivity::class.java))
        finish()
    }
    }
