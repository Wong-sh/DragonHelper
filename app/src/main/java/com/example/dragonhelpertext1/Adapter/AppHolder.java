package com.example.dragonhelpertext1.Adapter;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;

import androidx.recyclerview.widget.RecyclerView;

public class AppHolder extends RecyclerView.ViewHolder {

    private final SparseArray<View>mViews;

    public AppHolder(@NonNull View itemView){
        super(itemView);
        mViews = new SparseArray<>();
    }

    public <V extends View> V getView(@IdRes int res) {
        View view = mViews.get(res);
        if (view == null) {
            view = itemView.findViewById(res);
            mViews.put(res, view);
        }
        return (V)view;
    }


    public AppHolder loadImage(@IdRes int id, String url){
        if (TextUtils.isEmpty(url)) {
            return this;
        }
        View view = getView(id);
        if (view instanceof ImageView) {
            Glide.with(view).load(url).into((ImageView) view);
        }
        return this;
    }

    public AppHolder loadFileImage(@IdRes int id, String path){
        if (TextUtils.isEmpty(path)) {
            return this;
        }
        File file = new File(path);
        if (!file.exists()) {
            return this;
        }
        View view = getView(id);
        if (view instanceof ImageView) {
            Glide.with(view).load(file).into((ImageView) view);
        }
        return this;
    }

    public AppHolder loadImage(@IdRes int id, String url, @DrawableRes int holder){
        if (TextUtils.isEmpty(url)) {
            return this;
        }
        View view = getView(id);
        if (view instanceof ImageView) {
            Glide.with(view).load(url).placeholder(holder).into((ImageView) view);
        }
        return this;
    }

    public AppHolder setText(@IdRes int res, CharSequence text){
        View view = getView(res);
        if (view instanceof TextView) {
            ((TextView) view).setText(text);
        }
        return this;
    }

    public AppHolder setTextSize(@IdRes int res, float size){
        View view = getView(res);
        if (view instanceof TextView) {
            ((TextView) view).setTextSize(size);
        }
        return this;
    }

    public AppHolder setTextSize(@IdRes int res, float size, int unit){
        View view = getView(res);
        if (view instanceof TextView) {
            ((TextView) view).setTextSize(unit, size);
        }
        return this;
    }


    public AppHolder setTextBold(@IdRes int res){
        View view = getView(res);
        if (view instanceof TextView) {
            ((TextView) view).getPaint().setFakeBoldText(true);
            view.invalidate();
        }
        return this;
    }

    public AppHolder setTextColor(@IdRes int res, int color){
        View view = getView(res);
        if (view instanceof TextView) {
            ((TextView) view).setTextColor(color);
        }
        return this;
    }


    public AppHolder setBackground(@IdRes int res, @DrawableRes int bg){
        View view = getView(res);
        if (view != null) {
            view.setBackgroundResource(bg);
        }
        return this;
    }

    public AppHolder setText(@IdRes int id, @SuppressLint("SupportAnnotationUsage") @StringRes String text){
        View view = getView(id);
        if (view instanceof TextView) {
            ((TextView) view).setText(text);
        }
        return this;
    }

    public AppHolder setText(@IdRes int id, @StringRes int text){
        View view = getView(id);
        if (view instanceof TextView) {
            ((TextView) view).setText(text);
        }
        return this;
    }

    public AppHolder setImage(@IdRes int id, @DrawableRes int drawable){
        View view = getView(id);
        if (view instanceof ImageView) {
            ((ImageView) view).setImageResource(drawable);
        }
        return this;
    }

    public AppHolder setImageBitmap(@IdRes int id, Bitmap bitmap){
        if (bitmap == null) {
            return this;
        }
        View view = getView(id);
        if (view instanceof ImageView) {
            ((ImageView) view).setImageBitmap(bitmap);
        }
        return this;
    }


    public AppHolder setVisibility(@IdRes int id, boolean visible){
        View view = getView(id);
        if (view != null) {
            if (visible) {
                view.setVisibility(View.VISIBLE);
            } else {
                view.setVisibility(View.GONE);
            }
        }
        return this;
    }

    public AppHolder setProgress(@IdRes int id, int progress){
        View view = getView(id);
        if (view instanceof ProgressBar) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                ((ProgressBar) view).setProgress(progress, true);
            } else {
                ((ProgressBar) view).setProgress(progress);
            }

        }
        return this;
    }

    public AppHolder setRating(@IdRes int id, float rate){
        View view = getView(id);
        if (view instanceof RatingBar) {
            ((RatingBar) view).setRating(rate);

        }
        return this;
    }

    public AppHolder setOnClickListener(@IdRes int id, View.OnClickListener listener){
        View view = getView(id);
        view.setOnClickListener(listener);
        return this;
    }
}
