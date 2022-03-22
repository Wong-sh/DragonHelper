package com.example.dragonhelpertext1.Adapter;

import android.content.Context;

import androidx.annotation.Nullable;

import com.example.dragonhelpertext1.Bean.MessageBean.CustomBindingBean;
import com.example.dragonhelpertext1.Bean.MessageBean.CustomNewsBean;
import com.example.dragonhelpertext1.R;


import java.util.List;

public class SettingAdapter extends AppAdapter<CustomBindingBean>{

    public SettingAdapter(Context context, int layoutResId, @Nullable List<CustomBindingBean> data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(AppHolder holder, int position, CustomBindingBean bean){
        holder.setText(R.id.custom_phone, bean.getPhone())
                .setOnClickListener(R.id.custom_unbind, new OnItemClickHelper(position))
                .setText(R.id.custom_rent, bean.getRent())
                .setText(R.id.custom_name, bean.getNickName())
                .setText(R.id.custom_id, bean.getId())
                .setText(R.id.custom_online, bean.getOnline());
    }
}
