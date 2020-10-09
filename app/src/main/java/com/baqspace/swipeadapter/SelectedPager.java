package com.baqspace.swipeadapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.baqspace.proto.Tab1;
import com.baqspace.proto.Tab2;
import com.baqspace.proto.Tab3;
import com.baqspace.proto.Tab4;

/**
 * Created by Tshepo Lebusa on 22/06/2016.
 */
public class SelectedPager extends FragmentStatePagerAdapter {

    //counts number of tabs.
    private int tabCount;

    //constructor
    public SelectedPager(FragmentManager fm, int tabCount)
    {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position)
        {
            case 0:
                return new Tab1();

            case 1:
                return new Tab2();
               //break;
            case 2:
                return new Tab3();
            case 3:
                return new Tab4();
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
