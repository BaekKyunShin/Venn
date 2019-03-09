package com.example.weroo.becksco.post

import android.os.Bundle
import android.widget.Toast
import com.example.weroo.becksco.R
import com.example.weroo.becksco.api.postApi
import com.example.weroo.becksco.base.BaseActivity
import com.example.weroo.becksco.model.PostCreateDTO
import kotlinx.android.synthetic.main.activity_post_create.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostCreateActivity: BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_create)

        postSubmitBtn.setOnClickListener {
            onClickPostSubmit()
        }
    }

    private fun onClickPostSubmit() {
        postApi.postOnBoard(
            PostCreateDTO(
                titleText.text.toString(),
                bodyText.text.toString()
            )
        ).enqueue(object : Callback<PostCreateDTO> {
            override fun onResponse(call: Call<PostCreateDTO>, response: Response<PostCreateDTO>) {
                handlePostOnBoard(response.code())
            }

            override fun onFailure(call: Call<PostCreateDTO>, t: Throwable) {

            }
        })
        Toast.makeText(this, "Post on board", Toast.LENGTH_SHORT).show()
    }
    fun handlePostOnBoard(code: Int) {
        if (code != 201) {
            return
        }
        finish()
    }
}