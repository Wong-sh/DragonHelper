package com.example.dragonhelpertext1

import android.annotation.SuppressLint
import android.content.*
import android.content.ContentValues.TAG
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.dragonhelpertext1.Adapter.MainTabLayoutAdapter
import com.example.dragonhelpertext1.Fragment.ConsultFragment
import com.example.dragonhelpertext1.Fragment.NewsFragment
import com.example.dragonhelpertext1.Fragment.SettingFragment
import com.example.dragonhelpertext1.WebSocket.DragonHelperWebSocketClient
import com.example.dragonhelpertext1.WebSocket.DragonHelperWebSocketClientService
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {

    private var client: DragonHelperWebSocketClient? = null
    private var errorMsgList = ArrayList<String>()
    private var binder: DragonHelperWebSocketClientService.MyWebSocketClientBinder? = null
    private var  dragonHelperWebSocketClientService: DragonHelperWebSocketClientService? = null
    private var messageReceiver: MessageReceiver? = null
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var view: View
    private lateinit var txt_title: TextView
    private var titleList = ArrayList<String>()
    private var fragmentList = arrayListOf<Fragment>()

    //Service与Activity绑定
    private val serviceConnection: ServiceConnection = object : ServiceConnection{

        override fun onServiceConnected(p0: ComponentName?, iBinder: IBinder) {

            Log.e("MainActivity", "Service成功与Activity绑定")
            binder = iBinder as DragonHelperWebSocketClientService.MyWebSocketClientBinder
            binder!!.getService()
            client = dragonHelperWebSocketClientService?.client
        }

        override fun onServiceDisconnected(p0: ComponentName?) {

            Log.e("MainActivity", "Service成功与Activity断开")
        }
    }

    //内部类广播接收者，获取从Service发来的message
    internal inner class MessageReceiver: BroadcastReceiver(){

        @SuppressLint("LongLogTag")
        override fun onReceive(context: Context?, intent: Intent?) {

            if (intent != null) {
                if ( intent.action == "ALL_DATA" ){
                    errorMsgList = intent.getStringArrayListExtra("errorMsgList")!!
                    Log.e("MainActivity从Service获取到的信息", "$errorMsgList")
                }
            }

            //向NewsFragment发送获得的信息
            val bundle = Bundle()
            bundle.putStringArrayList("news", errorMsgList)
            Log.e("从MainActivity发送给NewsFragment的信息", "$errorMsgList")
            val fragment = NewsFragment()
            fragment.arguments = bundle
            val manager = supportFragmentManager
            val transaction = manager.beginTransaction()
            transaction.add(R.id.main_viewpager, fragment)
            transaction.commit()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //获取从LoginActivity传进来的数值
        getIntentValue()
        //绑定服务,让程序在后台运行
        bindService()
        //注册广播，通过广播接收Service传来的内容
        registerReceiver()
        //定义控件
        findView()
        //把fragment放进列表
        putFragment()
        //为TabLayout添加内容
        putValue()
        //为TabLayout添加事件监听
        putEvent()
        //定义viewPager,并与tabLayout关联
        resetViewPager()
    }

    //获取从LoginActivity传进来的数值
    private fun getIntentValue(){

        val phoneNumber = intent.getStringExtra("phoneNumber")
        val password = intent.getStringExtra("password")
        Log.e("MainActivity", "phoneNumber is$phoneNumber password is$password")
    }

    //绑定服务
    private fun bindService(){

        val bindIntent = Intent( this, DragonHelperWebSocketClientService :: class.java )
        bindService( bindIntent, serviceConnection, BIND_AUTO_CREATE )
    }

    //注册广播
    private fun registerReceiver(){

        messageReceiver = MessageReceiver()
        val filter = IntentFilter()
        filter.addAction("ALL_DATA")
        registerReceiver(messageReceiver, filter)
    }

    //定义控件
    @SuppressLint("InflateParams")
    private fun findView(){

        view = LayoutInflater.from(this).inflate(R.layout.tablayout_main, null)
        txt_title = view.findViewById(R.id.txt_title)
        viewPager = findViewById(R.id.main_viewpager)
        tabLayout = findViewById(R.id.main_tabLayout)
    }

    //把fragment放进列表
    private val newsFragment = NewsFragment()
    private val consultFragment = ConsultFragment()
    private val settingFragment = SettingFragment()

    private fun putFragment(){

        fragmentList.add( newsFragment)
        fragmentList.add( consultFragment)
        fragmentList.add( settingFragment)
    }

    //定义viewPager,并与tabLayout关联
    private fun resetViewPager(){

        viewPager.adapter = MainTabLayoutAdapter(supportFragmentManager, fragmentList, titleList)
        tabLayout.setupWithViewPager(viewPager)
    }

    //TabLayout背景、字体颜色、文字
    private fun putValue(){

        tabLayout.setTabTextColors(Color.BLACK, Color.BLACK)
        titleList.add("新消息")
        titleList.add("信息查询")
        titleList.add("设置")
        setupTabIcon()
    }

    //TabLayout的点击事件监听
    private fun putEvent(){

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{

            override fun onTabSelected(tab: TabLayout.Tab?) {

                //当tab被选中时改变tab和viewPager
                if (tab != null) {
                    if (tab.customView != null) {
                        txt_title = tab.customView!!.findViewById(R.id.txt_title)
                        txt_title.setTextColor(Color.YELLOW)
                        if (txt_title.text.toString() == "新消息"){
                            viewPager.currentItem = 0
                        }else if (txt_title.text.toString() == "信息查询"){
                            viewPager.currentItem = 1
                        }else if (txt_title.text.toString() == "设置") {
                            viewPager.currentItem = 2
                        }else{
                            Log.e(TAG, "tab.customView == null")
                        }
                    }
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {}

            override fun onTabUnselected(tab: TabLayout.Tab?) {

                if (tab != null) {
                    if (tab.customView != null) {
                        txt_title = tab.customView!!.findViewById(R.id.txt_title)
                        txt_title.setTextColor(Color.BLACK)
                    }else{
                        Log.e(TAG, "tab.customView == null")
                    }
                }
            }
        })
    }

    private fun setupTabIcon(){

        tabLayout.getTabAt(0)?.customView = getTabView(0)
        tabLayout.getTabAt(1)?.customView = getTabView(1)
        tabLayout.getTabAt(2)?.customView = getTabView(2)
    }

    private fun getTabView(position: Int) : View{

        txt_title.text = titleList[position]
        if ( position == 0 ){
            txt_title.setTextColor(Color.YELLOW)
        } else {
            txt_title.setTextColor(Color.BLACK)
        }
        return view
    }
}

