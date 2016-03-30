package com.embraser01.android.recyclerview;

import com.embraser01.android.velibtracking.models.Station;

/**
 * Created by Marc-Antoine on 15/03/2016.
 */
public interface OnListFragmentInteractionListener {

    void onClickItemListener(Station mItem);

    void onLongClickItemListener(Station mItem);

}
