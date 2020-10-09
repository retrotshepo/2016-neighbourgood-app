package com.baqspace.proto;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Tshepo Lebusa on 30/10/2016.
 */
public class Tab9 extends Fragment {

    SwipeRefreshLayout swipeRefreshLayout;
    View view;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.tab9, container, false);


        return view;
    }
}
