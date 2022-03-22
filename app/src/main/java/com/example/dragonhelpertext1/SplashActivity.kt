package com.example.dragonhelpertext1

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import java.lang.Exception

class SplashActivity : AppCompatActivity() {

    //splashActivity样式：满屏
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN)
        setContentView(R.layout.activity_splash)

        //创建线程，跳转到MainActivity
        Thread {
            try{
                Thread.sleep(3000)
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } catch (e: Exception){
                println("Exception")
                println(e.message)
            }
        }.start()
    }

}