package com.embraser01.android.velibtracking;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.embraser01.android.recyclerview.OnListFragmentInteractionListener;
import com.embraser01.android.velibtracking.models.Station;

import java.util.ArrayList;
import java.util.List;

public class StationListViewAdapter extends RecyclerView.Adapter<StationListViewAdapter.ViewHolder> {

    private List<Station> mValues;

    private final OnListFragmentInteractionListener mListener;

    private Context context;
    private int lastPosition = -1;

    public StationListViewAdapter(Context context, List<Station> items, OnListFragmentInteractionListener listener) {
        this.context = context;
        mValues = new ArrayList<>(items);
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.station_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.mItem = mValues.get(position);

        String tmp = holder.mItem.getName().replaceAll("[^A-Za-z]", "").substring(0, 1);

        TextDrawable textDrawable = TextDrawable.builder()
                .buildRound(tmp,
                        ColorGenerator.MATERIAL.getColor(tmp));
        holder.mImage.setImageDrawable(textDrawable);

        holder.mNameView.setText(holder.mItem.getName());
        holder.mStatusView.setText(holder.mItem.getStatus());
        holder.mBikesAvailable.setText(holder.mItem.getAvailable_bikes() + " / " + holder.mItem.getBike_stands());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onClickItemListener(holder);
                }
            }
        });

        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onLongClickItemListener(holder);
                }
                return true;
            }
        });




        holder.mFav.setImageResource((holder.mItem.isFav()) ? R.drawable.ic_fav_enable_24dp : R.drawable.ic_fav_disable_24dp);
        holder.mFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.mItem.switchFav();
                holder.mFav.setImageResource((holder.mItem.isFav()) ? R.drawable.ic_fav_enable_24dp : R.drawable.ic_fav_disable_24dp);

                ((MainActivity) view.getContext()).saveFavList(); // FIXME Optimisation ??
            }
        });



        setAnimation(holder.mView, position);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    /**
     * Here is the key method to apply the animation
     */
    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    public void updateModels(List<Station> models){
        mValues = new ArrayList<>(models);
    }

    public Station removeItem(int position){
        final Station model = mValues.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, Station model) {
        mValues.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition){
        final Station model = mValues.remove(fromPosition);
        mValues.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void animateTo(List<Station> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<Station> newModels) {
        for (int i = mValues.size() - 1; i >= 0; i--) {
            final Station model = mValues.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<Station> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final Station  model = newModels.get(i);
            if (!mValues.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<Station> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final Station model = newModels.get(toPosition);
            final int fromPosition = mValues.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }


    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        holder.clearAnimation();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public TextView mNameView;
        public TextView mStatusView;
        public TextView mBikesAvailable;
        public Station mItem;
        public ImageView mImage;
        public ImageButton mFav;

        public ViewHolder(View view) {
            super(view);

            mImage = (ImageView) view.findViewById(R.id.station_picture);
            mView = view;
            mNameView = (TextView) view.findViewById(R.id.station_name);
            mStatusView = (TextView) view.findViewById(R.id.station_status);
            mBikesAvailable = (TextView) view.findViewById(R.id.station_available_bikes);
            mFav = (ImageButton) view.findViewById(R.id.station_fav);
        }

        public void clearAnimation() {
            this.mView.clearAnimation();
        }


    }
}
