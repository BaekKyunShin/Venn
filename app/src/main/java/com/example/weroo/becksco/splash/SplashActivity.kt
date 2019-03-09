package com.example.weroo.becksco.splash

import android.content.Intent
import android.os.Bundle
import com.example.weroo.becksco.base.BaseActivity
import com.example.weroo.becksco.sign.SignActivity

class SplashActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this, SignActivity::class.java))
    }
}

// startActivity는 Appcompatactivity의 function / intent는 context간 상호통신을 해주는 기능
// intent는 Context가 필요한데 AppCompatacitivity가 Context임 -> 첫번째 파라미터로 시작할 현재 context를 받아주고,
// 그 다음 뭐를 실행할지 두번째 파라미터에 넣어줌 (SignActivity = signactivity.kt의 signactivity class)
