package com.example.dragonhelpertext1.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.WorkerThread;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.dragonhelpertext1.Adapter.AppAdapter;
import com.example.dragonhelpertext1.Adapter.SettingAdapter;
import com.example.dragonhelpertext1.Bean.MessageBean.CustomBindingBean;
import com.example.dragonhelpertext1.R;
import com.example.dragonhelpertext1.WebSocket.DragonHelperWebSocketClient;
import com.example.dragonhelpertext1.WebSocket.WebSocketInitUtils;

import java.util.ArrayList;
import java.util.List;

import android.os.Handler;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


public class SettingFragment extends Fragment implements AppAdapter.OnItemClickListener{

    private SettingAdapter settingAdapter;
    private RecyclerView recyclerView;
    private EditText phoneEditText;
    private EditText passwordEditText;
    private Button bindButton;

    private DragonHelperWebSocketClient client;

    private int event1 = 100;
    private int event2 = 100;
    private int errorCode = 100;
    private String message = null;

    private ArrayList<String> idList;
    private ArrayList<String> nickNameList;
    private ArrayList<String> onlineList;
    private ArrayList<String> phoneList;
    private ArrayList<String> rentList;

    private List<CustomBindingBean> customBindingList;
    private List<CustomBindingBean> customBindingBeans;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg){

            switch (msg.what){
                case 1:
                    settingAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){

        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        //????????????
        findView(view);
        //??????recyclerView
        showBindList();
        //??????????????????
        putData();
        //??????client
        getWebSocketClient();
        //????????????????????????
        phoneListener();
        //??????????????????
        passwordListener();
        //??????????????????
        buttonListener();
        return view;
    }

    //????????????
    private void findView(View view){

        recyclerView = view.findViewById(R.id.binding_recyclerView);
        phoneEditText = view.findViewById(R.id.phone_setting);
        passwordEditText = view.findViewById(R.id.password_setting);
        bindButton = view.findViewById(R.id.button_bind);
    }

    //????????????????????????
    private void showBindList(){

        settingAdapter = new SettingAdapter(getContext(), R.layout.adapter_custombindding, getCustomBindingBean());
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(settingAdapter);
        settingAdapter.setOnItemClickListener(this);
    }

    public List<CustomBindingBean> getCustomBindingBean(){

        if ( customBindingList == null ){
            customBindingList = new ArrayList<>();
        }
        return customBindingList;
    }

    //??????client
    private void getWebSocketClient(){

        client = WebSocketInitUtils.Companion.getClient();
    }

    //????????????
    private void sendMessage(String msg){
        if ( client != null ){
            Log.e("MainWebsocketClient", "???????????????" + msg.getBytes());
            client.send(msg.getBytes());
        }
    }

    //??????????????????
    private void phoneListener(){

        phoneEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    //????????????
    private void passwordListener(){

        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    //???????????????????????????????????????????????????
    private void runTimer(){

        CountDownTimer timer = new CountDownTimer(10 * 1000, 1000) {
            @Override
            public void onTick(long l) {
                if ( bindButton != null ){
                    bindButton.setClickable(false);
                    bindButton.setEnabled(false);
                    bindButton.setText("??????????????????");
                    bindButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20F);
                    bindButton.setTextColor(Color.parseColor("#ffffff"));
                }
            }

            @Override
            public void onFinish() {
                bindButton.setClickable(true);
                bindButton.setEnabled(true);
                bindButton.setText("??????");
                bindButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20F);
                bindButton.setTextColor(Color.parseColor("#ffffff"));
            }
        };
        timer.start();
    }

    //??????????????????
    private void buttonListener(){

        bindButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){

                runTimer();

                String phone = phoneEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                //????????????
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //??????????????????????????????????????????
                        JSONObject json = new JSONObject();
                        try {
                            json.put("event1", 3);
                            json.put("event2", 0);
                            json.put("callCode", 0);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        JSONObject body = new JSONObject();
                        try {
                            body.put("phone", phone);
                            body.put("password", password);
                            json.put("body", body);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        sendMessage(json.toString());

                        int time;
                        for ( time = 1; time <= 10; time++ ){
                            //????????????????????????
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            getValue();
                            if ( event1 == 3 && event2 == 0 ){
                                if ( errorCode == 0 ){
                                    //??????3_1??????
                                    sendSecondMessage();
                                    Looper.prepare();
                                    Toast.makeText(getContext().getApplicationContext(), "????????????????????????????????????????????????", Toast.LENGTH_LONG ).show();
                                    Looper.loop();
                                } else {
                                    Looper.prepare();
                                    Toast.makeText(getContext().getApplicationContext(), message, Toast.LENGTH_LONG ).show();
                                    Looper.loop();
                                }
                                break;
                            }
                            if ( time == 10 ){
                                Looper.prepare();
                                Toast.makeText(getContext().getApplicationContext(), "??????????????????????????????????????????", Toast.LENGTH_LONG ).show();
                                Looper.loop();
                            }
                        }
                    }
                }).start();
            }
        });
    }

    private void getValue(){

        event1 = WebSocketInitUtils.Companion.getEvent1real();
        event2 = WebSocketInitUtils.Companion.getEvent2real();
        errorCode = WebSocketInitUtils.Companion.getErrorCodereal();
        message = WebSocketInitUtils.Companion.getMessagereal();
    }

    //??????3_1??????????????????
    private void sendSecondMessage(){

        //??????????????????????????????????????????
        JSONObject json = new JSONObject();
        try {
            json.put("event1", 3);
            json.put("event2", 1);
            json.put("callCode", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject body = new JSONObject();
        try {
            json.put("body", body);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sendMessage(json.toString());

        int time;
        for ( time = 1; time <= 10; time++ ){
            //????????????????????????
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            getSecondValue();
            if ( event1 == 3 && event2 == 1 ){
                if ( errorCode == 0 ){
                    putData();
                } else {
                    Looper.prepare();
                    Toast.makeText(getContext().getApplicationContext(), message, Toast.LENGTH_LONG ).show();
                    Looper.loop();
                }
                break;
            }
            if ( time == 10 ){
                Looper.prepare();
                Toast.makeText(getContext().getApplicationContext(), "??????????????????????????????????????????", Toast.LENGTH_LONG ).show();
                Looper.loop();
            }
        }
    }

    private void getSecondValue(){

        event1 = WebSocketInitUtils.Companion.getEvent1real();
        event2 = WebSocketInitUtils.Companion.getEvent2real();
        errorCode = WebSocketInitUtils.Companion.getErrorCodereal();
        message = WebSocketInitUtils.Companion.getMessagereal();
        idList = WebSocketInitUtils.Companion.getIDListreal();
        nickNameList = WebSocketInitUtils.Companion.getNickNameListreal();
        onlineList = WebSocketInitUtils.Companion.getOnlineListreal();
        phoneList = WebSocketInitUtils.Companion.getPhoneListreal();
        rentList = WebSocketInitUtils.Companion.getRentListreal();
    }

    private void putData(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<CustomBindingBean> customBindingBean = getCustomBindingList(false);
                    if ( customBindingBean == null || customBindingBean.isEmpty() ){
                        Log.e("??????","?????????????????????");
                        return;
                    }
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                } catch (Exception ignored){

                }
            }
        }).start();
    }

    @WorkerThread
    synchronized public List<CustomBindingBean> getCustomBindingList(boolean refresh){

        if ( !refresh && customBindingList != null && !customBindingList.isEmpty() ){
            return customBindingList;
        }
        try {
            customBindingBeans = new ArrayList<>();
            int customBindingListLenght = idList.size();
            Log.e("customBindingListLenght", String.valueOf(customBindingListLenght));
            int index;
            for ( index = 0; index < customBindingListLenght; index++ ){
                CustomBindingBean customBindingBean = new CustomBindingBean(
                        idList.get(index),
                        nickNameList.get(index),
                        onlineList.get(index),
                        phoneList.get(index),
                        rentList.get(index)
                );
                customBindingBeans.add(customBindingBean);
            }
            customBindingList.clear();
            customBindingList.addAll(customBindingBeans);
            return customBindingBeans;
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public void onItemClick(View view, int position){

    }
}
