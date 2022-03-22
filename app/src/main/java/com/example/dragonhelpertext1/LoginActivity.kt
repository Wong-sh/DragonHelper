package com.example.dragonhelpertext1

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.*
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.dragonhelpertext1.Utils.PhoneUtils
import com.example.dragonhelpertext1.WebSocket.DragonHelperWebSocketClient
import com.example.dragonhelpertext1.WebSocket.WebSocketInitUtils
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    private lateinit var phoneEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var phoneWarningText: TextView
    private lateinit var passwordWarningText: TextView
    private lateinit var checkBox: CheckBox
    private lateinit var button: Button

    private var client: DragonHelperWebSocketClient? = null

    private var phoneLenght = 0
    private var passwordLenght = 0
    private var checkBoxWarning = 0

    private  var event1: Int = 100
    private var event2: Int = 100
    private var errorCode: Int = 100
    private var message: String? = null

    //使UI获得更新
    private val handler: Handler = @SuppressLint("HandlerLeak")
    object : Handler(){

        override fun handleMessage(msg: Message) {

            when( msg.what ){
                1 -> {
                    phoneWarningText.visibility = View.VISIBLE
                }
                2 -> {
                    phoneWarningText.visibility = View.GONE
                }
                3 -> {
                    passwordWarningText.visibility = View.VISIBLE
                }
                4 -> {
                    passwordWarningText.visibility = View.GONE
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //定义控件
        findView()
        //初始化webSocket
        initWebSocketClient()
        //设置手机号码监听
        phoneListener()
        //设置密码监听
        passwordListener()
        //设置勾选框监听
        checkBoxListener()
        //设置按钮监听
        buttonListener()

    }

    //定义控件
    private fun findView(){

        phoneEditText = findViewById(R.id.phone)
        phoneWarningText = findViewById(R.id.phone_warning_textview)
        passwordEditText = findViewById(R.id.password)
        passwordWarningText = findViewById(R.id.password_warning_textview)
        button = findViewById(R.id.button)
        checkBox = findViewById(R.id.checkbox)
    }

    //初始化webSocketClient,从WebSocketInitUtils获取
    private fun initWebSocketClient(){

        WebSocketInitUtils.initWebSocket()
        client = WebSocketInitUtils.getClient()
    }

    //发送信息
    private fun sendMessage(msg: String){
        if ( client != null ){
            Log.e("MainWebsocketClient", "发送消息：${msg.toByteArray()}")
            client!!.send(msg.toByteArray())
        }
    }

    //监听手机号码
    private fun phoneListener(){

        phoneEditText.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                //判断是否大于6位
                Thread{
                    try{
                        if ( p0.toString().length < 6 ){
                            phoneLenght = 0
                            val msg = Message()
                            msg.what = 1
                            handler.sendMessage(msg)
                        } else {
                            phoneLenght = 1
                            val msg = Message()
                            msg.what = 2
                            handler.sendMessage(msg)
                        }
                    } catch (e: Exception){
                        println("Exception")
                        println(e.message)
                    }
                }.start()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
    }

    //监听密码
    private fun passwordListener(){

        passwordEditText.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                //判断是否大于6位
                Thread{
                    try{
                        if ( p0.toString().length < 6 ){
                            passwordLenght = 0
                            val msg = Message()
                            msg.what = 3
                            handler.sendMessage(msg)
                        } else {
                            passwordLenght = 1
                            val msg = Message()
                            msg.what = 4
                            handler.sendMessage(msg)
                        }
                    } catch (e: Exception){
                        println("Exception")
                        println(e.message)
                    }
                }.start()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
    }

    //监听勾选框
    private fun checkBoxListener(){

        //设置初始状态
        checkBox.isChecked = false

        checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if ( isChecked ){
                checkBoxWarning = 1
            } else {
                checkBoxWarning = 0
            }
        }
    }

    //为登录按钮增加倒计时，防止频繁点击
    private fun runTimer(){

        val timer = object : CountDownTimer(10 * 1000, 1000) {

            @SuppressLint("SetTextI18n")
            override fun onTick(p0: Long) {
                button.isClickable = false
                button.isEnabled = false
                button.text = ""+ p0 / 1000 +"秒后再试试"
                button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20F)
                button.setTextColor(Color.parseColor("#ffffff"))
            }

            override fun onFinish() {
                button.isClickable = true
                button.isEnabled = true
                button.text = "登录"
                button.setTextColor(Color.parseColor("#ffffff"))
            }

        }
        timer.start()
    }

    //监听登录按钮
    private fun buttonListener(){

        button.setOnClickListener{

            //对手机号码、密码、和勾选框进行监听，如不符合给出对应的Toast
            if ( phoneLenght == 0 ){
                Toast.makeText(applicationContext, "您的手机号码格式并不正确，请输入正确的手机号码", Toast.LENGTH_LONG).show()
            } else if ( passwordLenght == 0 ){
                Toast.makeText(applicationContext, "您的密码格式并不正确，请输入正确的密码", Toast.LENGTH_LONG).show()
            } else if ( checkBoxWarning == 0 ){
                Toast.makeText(applicationContext, "您还没有进行协议确认勾选，请认真阅读后进行勾选", Toast.LENGTH_LONG).show()
            }
            if ( phoneLenght == 1 && passwordLenght == 1 && checkBoxWarning == 1 ){

                runTimer()

                val phone: String = phoneEditText.text.toString()
                val password: String = passwordEditText.text.toString()

                //执行线程
                Thread {
                    //发送用户输入的信息，等待回复
                    val json = JSONObject()
                    json.put("event1",1)
                    json.put("event2",0)
                    json.put("callCode",0)
                    val body = JSONObject()
                    body.put("phone",phone)
                    body.put("password",password)
                    body.put("version",PhoneUtils.getVersionName(this))
                    body.put("ip",PhoneUtils.getIpAddressString())
                    json.put("body",body)

                    sendMessage(json.toString())

                    for ( time in 1..10 ){
                        //预留响应等待时间
                        Thread.sleep(1000)
                        //获得返回的数据
                        getValue()
                        if ( event1 == 1 && event2 == 0 ){
                            if ( errorCode == 0 ) {
                                //跳转到MainActivity
                                try{
                                    val intent = Intent(this, MainActivity :: class.java)
                                    intent.putExtra("phoneNumber", phone)
                                    intent.putExtra("password", password)
                                    Log.e("LoginActivity", "phoneNumber is${phoneEditText.text} password is${passwordEditText.text}")
                                    startActivity(intent)
                                    finish()
                                    Looper.prepare()
                                    Toast.makeText(applicationContext, "登录成功，欢迎使用“一条龙小助手”", Toast.LENGTH_LONG ).show()
                                    Looper.loop()
                                } catch (e: Exception){
                                    println("Exception")
                                    println(e.message)
                                }
                            } else {
                                initWebSocketClient()
                                Looper.prepare()
                                Toast.makeText(applicationContext, message, Toast.LENGTH_LONG ).show()
                                Looper.loop()
                            }
                            break
                        }
                        if ( time == 10 ){
                            Looper.prepare()
                            Toast.makeText(applicationContext, "连接超时，请检查网络是否正常", Toast.LENGTH_LONG ).show()
                            Looper.loop()
                            initWebSocketClient()
                        }
                    }
                }.start()
            }
        }
    }

    private fun getValue(){

        event1 = WebSocketInitUtils.getEvent1()!!
        event2 = WebSocketInitUtils.getEvent2()!!
        errorCode = WebSocketInitUtils.getErrorCode()!!
        message = WebSocketInitUtils.getMessage()
    }

}