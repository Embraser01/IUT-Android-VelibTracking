package com.embraser01.android.recyclerview;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class ItemListViewBuilder {

    public final static double PERCENTAGE_PRIMARY = 0.87;
    public final static double PERCENTAGE_SECONDARY = 54;

    public final static int PRIMARY_TEXT = Color.argb((int) (255 * PERCENTAGE_PRIMARY), 0, 0, 0);
    public final static int SECONDARY_TEXT = Color.argb((int) (255 * PERCENTAGE_SECONDARY), 0, 0, 0);

    private View view = null;
    private ImageView imageView = null;
    private LayoutInflater inflater = null;
    private int resource = 0;
    private int imageColor = 0;
    private int nb_content = 0;


    public ItemListViewBuilder(LayoutInflater inflater, @LayoutRes int resource) {
        init(inflater, resource);
    }

    public ItemListViewBuilder init(LayoutInflater inflater, @LayoutRes int resource) {
        this.inflater = inflater;
        this.resource = resource;
        return clear();
    }


    public ItemListViewBuilder addContent(@IdRes int textViewId, String text) {
        TextView tmp = (TextView) view.findViewById(textViewId);
        tmp.setTextColor(nb_content == 0 ? PRIMARY_TEXT : SECONDARY_TEXT);
        tmp.setText(text);
        return this;
    }

    public ItemListViewBuilder addContent(@IdRes int textViewId, String text, int color) {
        TextView tmp = (TextView) view.findViewById(textViewId);
        tmp.setTextColor(color);
        tmp.setText(text);
        return this;
    }


    public ItemListViewBuilder setImage(@IdRes int imageViewId, Drawable image) {
        imageView = (ImageView) view.findViewById(imageViewId);
        imageView.setImageDrawable(image);
        return this;
    }

    public ItemListViewBuilder setImageColor(int color) {
        imageColor = color;
        return this;
    }

    public View make() {
        if (imageView != null) imageView.setColorFilter(imageColor, PorterDuff.Mode.MULTIPLY);
        return view;
    }


    public ItemListViewBuilder clear() {
        nb_content = 0;
        imageView = null;
        if (inflater != null) view = inflater.inflate(resource, null);
        return this;
    }
}
