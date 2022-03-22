package com.example.dragonhelpertext1.Adapter;

import android.content.Context;
import android.widget.Button;

import androidx.annotation.Nullable;

import java.text.DecimalFormat;
import java.util.List;

import com.example.dragonhelpertext1.Bean.MessageBean.CustomNewsBean;
import com.example.dragonhelpertext1.R;

public class NewsAdapter extends AppAdapter<CustomNewsBean>{

    public NewsAdapter(Context context, int layoutResId, @Nullable List<CustomNewsBean> data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(AppHolder holder, int position, CustomNewsBean bean) {
        holder.setText(R.id.news_No, bean.getNo())
                .setText(R.id.news_Text, bean.getText());
    }
}
