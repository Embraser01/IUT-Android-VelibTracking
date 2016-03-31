package com.embraser01.android.recyclerview;

import android.graphics.drawable.Drawable;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class ItemListViewBuilder {

    private View view = null;
    private ImageView imageView = null;
    private ArrayList<TextView> textViews = null;
    private LayoutInflater inflater = null;
    private
    @LayoutRes
    int resource = 0;

    public ItemListViewBuilder(LayoutInflater inflater, @LayoutRes int resource) {
        init(inflater, resource);
    }

    public ItemListViewBuilder addContent(@IdRes int textViewId, String text) {
        TextView tmp = (TextView) view.findViewById(textViewId);
        tmp.setText(text);
        return this;
    }

    public ItemListViewBuilder setImage(@IdRes int imageViewId, Drawable image) {
        imageView = (ImageView) view.findViewById(imageViewId);
        imageView.setImageDrawable(image);
        return this;
    }

    public View make() {
        return view;
    }

    public ItemListViewBuilder clear() {
        if (textViews != null) textViews.clear();
        imageView = null;
        if (inflater != null) view = inflater.inflate(resource, null);
        return this;
    }

    public ItemListViewBuilder init(LayoutInflater inflater, @LayoutRes int resource) {
        clear();
        this.inflater = inflater;
        this.resource = resource;

        view = inflater.inflate(resource, null);
        textViews = new ArrayList<>();
        return this;
    }

}
