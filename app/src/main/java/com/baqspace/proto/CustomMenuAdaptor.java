package com.baqspace.proto;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by DEE-KAY on 2016/08/08.
 */

class CustomMenuAdaptor extends ArrayAdapter<Items> {

    TextView itemLikes;

    public CustomMenuAdaptor(Context context, List<Items> menu) {
        super(context, R.layout.custom_layout_menu_list, menu);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater tempInflater = LayoutInflater.from(getContext());
        View customView = tempInflater.inflate(R.layout.custom_layout_menu_list, parent, false);


        double actualPrice = Double.parseDouble(getItem(position).Item_Price);

        String price = String.format("%.2f", actualPrice);
        price.replace(",", ".");

        String itemName = getItem(position).Item_Name;
        String itemDescription = getItem(position).Item_Description;

        int likeNum = getItem(position).likes;


        TextView name = (TextView) customView.findViewById(R.id.itemName);
        TextView description = (TextView) customView.findViewById(R.id.itemDescription);

        itemLikes = (TextView) customView.findViewById(R.id.menuItem_likes);
        itemLikes.setText(likeNum + "");

        name.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);

        name.setText(itemName.toUpperCase() + ": R" + price);
        description.setTypeface(Typeface.SERIF, Typeface.NORMAL);

        description.setText(itemDescription);

        return customView;
    }
}

