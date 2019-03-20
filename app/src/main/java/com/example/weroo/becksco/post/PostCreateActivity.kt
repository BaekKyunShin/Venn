package com.example.weroo.becksco.post

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.weroo.becksco.R
import com.example.weroo.becksco.api.postApi
import com.example.weroo.becksco.base.BaseActivity
import com.example.weroo.becksco.model.PostCreateDTO
import kotlinx.android.synthetic.main.activity_post_create.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// PostOnBoard 를 호출 했을 때 Call 이라는 retrofit 객체를 반환
// enqueue를 호출하면 실제 서버에 요청을 보냄
// 이때 넘어가는 데이터는 json 객체의 형식을 띄며,
// 이는 Call 생성 시 넘겨준 DTO 를 retrofit-gson-converter 가 json 형태로 바꿔주어 생성됨
// 이렇게 넘어간 데이터를 서버에서 받아서 가공해서 사용하게 되며,
// postOnBoard로 요청을 보내는 end-point (요청을 보내는 경로)에서는
// 이를 post 라는 객체로 가공해 DB에 저장하는 일을 하고 있기 때문에 해당 API를 통해 데이터를 저장할 수 있음

// postOnBoard의 요청에 대해 서버가 답변을 하는데,
// retrofit 에서는 이러한 답변을 처리하기 위해서 Callback 이라는 인터페이스를 사용
// Callback 인터페이스는 generic을 사용하여 생성함
// Callback<T>에서 서버에서 넘어올 것으로 예상되는 답변을 T 라는 class 에 매핑 시키고 싶을 때
// Callback<PostCreateDTO>와 같이 작성해줌
// 이는 PostOnBoard 요청을 날렸을 때 PostCreateDTO 형태의 답변이 서버에서 날아올 것을 기대한 것

// Callback은 서버에서 돌아온 요청을 처리하기 위한 객체
// 서버에서 날아온 데이터는 gson-converter 을 통해
// Callback 객체 뒤에 generic type 으로 명시해준 객체형태로 변환되어 옴

// CallBack 인터페이스에서 onRespoonse는 서버의 답변이 왔을 때 불리고, onFailure는 반대의 경우에 불림

class PostCreateActivity: BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_create)

        setSupportActionBar(toolbar)

//        postSubmitBtn.setOnClickListener {
//           postOnBoard()
//        }
    }

    private fun postOnBoard() {
        postApi.postOnBoard(PostCreateDTO(
                postTitle.text.toString(),
                postContents.text.toString()
            )).enqueue(object : Callback<PostCreateDTO> {
            override fun onResponse(call: Call<PostCreateDTO>, response: Response<PostCreateDTO>) {
                handlePostOnBoard(response.code())
            }

            override fun onFailure(call: Call<PostCreateDTO>, t: Throwable) {
                Toast.makeText(this@PostCreateActivity, R.string.failure_message, Toast.LENGTH_SHORT).show()

            }
        })
    }

    fun handlePostOnBoard(code: Int) {
        if (code != 201) {
            Toast.makeText(this, R.string.post_create_message, Toast.LENGTH_SHORT).show()
            return
        }
        finish()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.post, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId
        when (id) {
            R.id.post_action -> postOnBoard()
        }

        return super.onOptionsItemSelected(item)
    }
}

