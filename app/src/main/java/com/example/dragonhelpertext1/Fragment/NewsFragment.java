package com.example.dragonhelpertext1.Fragment;

import androidx.annotation.UiThread;
import androidx.annotation.WorkerThread;
import androidx.fragment.app.Fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.List;
import com.example.dragonhelpertext1.Adapter.AppAdapter;
import com.example.dragonhelpertext1.Adapter.NewsAdapter;
import com.example.dragonhelpertext1.Bean.MessageBean.CustomNewsBean;
import com.example.dragonhelpertext1.R;

public class NewsFragment extends Fragment implements AppAdapter.OnItemClickListener {

    private NewsAdapter newsAdapter;
    private TextView tips;
    private RecyclerView recyclerView;

    private List<CustomNewsBean> customNewsList;
    private List<CustomNewsBean> customNewsBeans;

    private ArrayList<String>errorMsg;
    private ArrayList<String> errorMsgList;

    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg){

            switch (msg.what){
                case 1:
                    Log.e("1","getList");
                    tips.setVisibility(View.GONE);
//                    newsAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){

        View view = inflater.inflate(R.layout.fragment_news, container, false);
        //定义控件
        findView(view);
        //定义recyclerView
        showNews();
        //监听是否有信息
        getValueSignal();
        //显示用户信息
        putData();
        return view;

    }

    @Override
    public void onResume(){

        putData();
        super.onResume();
    }

    //定义控件
    private void findView(View view){
        recyclerView = view.findViewById(R.id.customNews_recyclerView);
        tips = view.findViewById(R.id.customNews_tips);
    }

    //定义recyclerView
    private void showNews(){

        newsAdapter = new NewsAdapter(getContext(), R.layout.adapter_customnews, getCustomNewsBean());
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(newsAdapter);
        newsAdapter.setOnItemClickListener(this);

    }

    public List<CustomNewsBean> getCustomNewsBean() {

        if (customNewsList == null) {
            customNewsList = new ArrayList<>();
        }
        return customNewsList;
    }

    //监听是否有信息
    private void getValueSignal(){

        Log.e("getValue","--------------------");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bundle bundle = getArguments();
                if ( bundle != null ){
                    errorMsg = bundle.getStringArrayList("news");
                    Log.e("从MainActivity获得的", String.valueOf(errorMsg));
//                    //把获取的errorMsg放入sharePreferences
//                    int errorMsgLength = errorMsg.size();
//                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("errorMsg", Context.MODE_PRIVATE);
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.clear();
//                    editor.putInt("errorMsgSize", errorMsgLength);
//                    for ( int index = 0; index <errorMsgLength; index++ ){
//                        editor.putString(String.valueOf(index), errorMsg.get(index));
//                    }
//                    editor.commit();
                    errorMsgList = errorMsg;
                    putData();
                }
            }
        }).start();
    }


    //显示用户信息
    private void putData(){

        tips.setVisibility(View.GONE);
        //创建线程
        new Thread(new Runnable() {
            @Override
            public void run() {

//                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("errorMsg", Context.MODE_PRIVATE);
//                int errorMsgLenght = sharedPreferences.getInt("errorMsgSize",0);
//                for ( int index = 0; index < errorMsgLenght; index++ ){
//                    errorMsgList.add(sharedPreferences.getString(String.valueOf(index),null));
//                }
                try {
                    List<CustomNewsBean> customNewsBean = getNewsList(false);
                    if (customNewsBean == null || customNewsBean.isEmpty()) {
                        Log.e("数据", "暂时还没有数据");
                        tips.setVisibility(View.VISIBLE);
                        return;
                    }
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    //发送请求
    @WorkerThread
    synchronized public List<CustomNewsBean> getNewsList(boolean refresh){

        if (!refresh && customNewsList != null && !customNewsList.isEmpty()) {
            return customNewsList;
        }
        try {
            customNewsBeans = new ArrayList<>();
            int errorMsgListLenght = errorMsgList.size();
            Log.e("errorMsgListLenght", String.valueOf(errorMsgListLenght));
            int index;
            for (index = 0; index < errorMsgListLenght; index++ ){
                CustomNewsBean customNewsBean = new CustomNewsBean(
                        String.valueOf(index + 1),
                        errorMsgList.get(index)
                );
                customNewsBeans.add(customNewsBean);
            }
            customNewsList.clear();
            customNewsList.addAll(customNewsBeans);
            return customNewsBeans;
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public void onItemClick(View view, int position){

    }

}
