package com.baqspace.swipeadapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.baqspace.proto.Tab5;
import com.baqspace.proto.Tab6;
import com.baqspace.proto.Tab7;

/**
 * Created by Tshepo Lebusa on 23/06/2016.
 */
public class FilterPager extends FragmentStatePagerAdapter {

    //counts number of tabs.
    private int tabCount;

    //constructor
    public FilterPager(FragmentManager fm, int tabCount)
    {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position)
        {
            case 0:
                return new Tab5();

            case 1:
                return new Tab6();
            //break;
            case 2:
                return new Tab7();
            //case 3:
            //    return new Tab4();
            //break;
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
