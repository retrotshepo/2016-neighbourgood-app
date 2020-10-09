package com.baqspace.proto;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

/**
 * Created by Tshepo Lebusa on 19/08/2016.
 */
public class CustomTraderAdapter extends ArrayAdapter<Users> {

    public CustomTraderAdapter(Context context, List<Users> menu) {
        super(context, R.layout.custom_layout_traders,menu);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater tempInflater = LayoutInflater.from(getContext());
        View traderView = tempInflater.inflate(R.layout.custom_layout_traders, parent, false);

        //int image = R.drawable.fresh;
        String name = getItem(position).UserName;

        //ImageView imageView = (ImageView) traderView.findViewById(R.id.trader_image);
        TextView textView = (TextView) traderView.findViewById(R.id.trader_name);

        textView.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);

        textView.setText(name);

        return traderView;
    }
}
