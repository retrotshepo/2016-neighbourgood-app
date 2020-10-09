package com.baqspace.swipeadapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.baqspace.proto.R;

/**
 * Created by Tshepo Lebusa on 17/05/2016.
 */
public class CustomSwipeAdapter extends PagerAdapter {

    private Context ctx;

    /*private int[] images = {R.drawable.slide_1, R.drawable.bg, R.drawable.umbrella,
                            R.drawable.slide_2, R.drawable.slide_3, R.drawable.slide_4,
                            R.drawable.slide_5, R.drawable.slide_6, R.drawable.manager, R.drawable.bgp1,
                            R.drawable.tumblr01, R.drawable.tumblr02};
    */

    private int[] images = {R.drawable.slide_1, R.drawable.bg, R.drawable.umbrella,
            R.drawable.slide_2, R.drawable.slide_3, R.drawable.slide_4,
            R.drawable.slide_5, R.drawable.slide_6, R.drawable.manager, R.drawable.bgp1,
            R.drawable.tumblr01, R.drawable.tumblr02};
    private LayoutInflater inflater;


    public CustomSwipeAdapter(Context c)
    {
        ctx = c;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {

        return view == (LinearLayout)object;
        //return false;
    }

    public Object instantiateItem(ViewGroup container, int position)
    {
        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item_view = inflater.inflate(R.layout.swipe_layout, container, false);

        ImageView imageView = (ImageView) item_view.findViewById(R.id.img_view);

        imageView.setImageResource(images[position]);

        container.addView(item_view);

        return item_view;

        //return super.instantiateItem(container, position);
    }

    public void destroyItem(ViewGroup container, int position, Object object)
    {
        container.removeView((LinearLayout)object);
        //super.destroyItem(container, position, object);
    }



}
