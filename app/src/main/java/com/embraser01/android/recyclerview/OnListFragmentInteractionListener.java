package com.embraser01.android.recyclerview;

import com.embraser01.android.velibtracking.StationListViewAdapter;

/**
 * Created by Marc-Antoine on 15/03/2016.
 */
public interface OnListFragmentInteractionListener {

    void onClickItemListener(StationListViewAdapter.ViewHolder viewHolder);

    void onLongClickItemListener(StationListViewAdapter.ViewHolder viewHolder);

}
