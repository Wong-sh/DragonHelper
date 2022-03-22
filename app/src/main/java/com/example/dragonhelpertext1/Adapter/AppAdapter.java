package com.example.dragonhelpertext1.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class AppAdapter<T> extends RecyclerView.Adapter<AppHolder> {

    public  Context mContext;
    public  int mLayoutRes;
    public  List<T> mItems;
    public  OnItemClickListener mOnItemClickListener;

    public AppAdapter(Context context, @LayoutRes int layoutResId, @Nullable List<T> data) {
        this.mContext = context;
        this.mItems = data == null ? new ArrayList<T>() : data;
        if (layoutResId != 0) {
            this.mLayoutRes = layoutResId;
        }
    }

    @NonNull
    public AppHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AppHolder(LayoutInflater.from(mContext).inflate(mLayoutRes, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AppHolder holder, int position) {
        convert(holder, position, mItems.get(position));
    }

    public int getItemCount() {
        return mItems.size();
    }


    /**
     * 需要重写的方法
     * @param holder
     * @param position
     */
    protected abstract void convert(AppHolder holder, int position, T item);


    public class OnItemClickHelper implements View.OnClickListener {

        private int position;

        public OnItemClickHelper(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            mOnItemClickListener.onItemClick(v, position);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
}

