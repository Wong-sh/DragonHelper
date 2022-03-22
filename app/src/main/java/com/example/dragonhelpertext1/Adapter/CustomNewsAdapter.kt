package com.example.dragonhelpertext1.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dragonhelpertext1.Bean.MessageBean.CustomNewsBean
import com.example.dragonhelpertext1.R
import java.lang.Exception

class CustomNewsAdapter (private val customNewsBeanList: List<CustomNewsBean> ) : RecyclerView.Adapter<CustomNewsAdapter.ViewHolder>() {

    //内部类，定义Adapter中的控件
    inner class ViewHolder( view: View ) : RecyclerView.ViewHolder(view){

        var newsNo: TextView = view.findViewById(R.id.news_No)
        var newsText: TextView = view.findViewById(R.id.news_Text)
    }

    //onCreateViewHolder(),用于创建ViewHolder，加载RecyclerView布局
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomNewsAdapter.ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_customnews, parent, false)
        return ViewHolder(view)
    }

    //onBindViewHolder(),用于对RecyclerView子项赋值
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val customNews = customNewsBeanList[position]
        try{
            holder.newsNo.text = customNews.no.toString()
            holder.newsText.text = customNews.text
        } catch ( e: Exception ){
            e.message
        }
    }

    // getItemCount(),用于给RecyclerView有多少个子项赋值
    override fun getItemCount(): Int {
        return customNewsBeanList.size
    }
}