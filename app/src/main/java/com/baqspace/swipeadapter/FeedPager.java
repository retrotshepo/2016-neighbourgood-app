package com.baqspace.swipeadapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.baqspace.proto.Tab10;
import com.baqspace.proto.Tab11;

/**
 * Created by Tshepo Lebusa on 30/10/2016.
 */
public class FeedPager extends FragmentStatePagerAdapter {

    //counts number of tabs.
    private int tabCount;

    //constructor
    public FeedPager(FragmentManager fm, int tabCount)
    {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position)
        {
            case 0:
                return new Tab10();

            case 1:
                return new Tab11();

            default:
                return null;
        }

        //return null;
    }


    @Override
    public int getCount() {
        return tabCount;
    }
}
