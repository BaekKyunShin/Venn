package com.example.weroo.becksco.sign

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.example.weroo.becksco.R
import com.example.weroo.becksco.api.DefaultPrefHelper
import com.example.weroo.becksco.api.postApi
import com.example.weroo.becksco.base.BaseActivity
import com.example.weroo.becksco.home.HomeActivity
import com.example.weroo.becksco.model.User
import kotlinx.android.synthetic.main.activity_sign.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val SIGN_PREFERENCE = "sign_preference"

class SignActivity: BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // R은 Resource, 즉 res 디렉토리를 의미
        // activity_sign을 그려줌
        setContentView(R.layout.activity_sign)

        // signButton은 activity_sign의 id
        // setOnClickListener는 click event가 실행될 때 어떤 행동을 해주라는 뜻
        // 즉 signButton을 id로 가지고 있는 button이 클릭될 때 onClickSignButton() 실행해주라는 의미
        signButton.setOnClickListener {
            onClickSignButton()
        }
    }

    private fun onClickSignButton() {
        //postApi 객체를 queue에 담고 callback을 실행
         postApi.sign(User(signEditText.text.toString()))
            .enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    handleSign(response.code(), response.body())
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    // 모든 상수 값은 R.string에서 관리해주는 것이 좋음
                    Toast.makeText(this@SignActivity, R.string.failure_message, Toast.LENGTH_LONG).show()
                }
            })
    }

private fun handleSign(code: Int, user: User?) {
    if (code != 201) {
        Toast.makeText(this, R.string.failure_sign_message, Toast.LENGTH_SHORT).show()
        return
    }

    DefaultPrefHelper.instance().setBoolean(SIGN_PREFERENCE, true)
    // this: signActivity / HomeActivity 실행
    startActivity(Intent(this, HomeActivity::class.java))
}
}