package com.baqspace.proto;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * Created by Tshepo Lebusa on 08/10/2016.
 */
public class CustomHistoryAdaptor extends ArrayAdapter<cReviews> {

    TextView reviewed;
    TextView review;

    public CustomHistoryAdaptor(Context context, Output out) {
        super(context, R.layout.custom_layout_reviews, out.reviews);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater tempInflater = LayoutInflater.from(getContext());

        View customView = tempInflater.inflate(R.layout.custom_layout_history, parent, false);

        String username = getItem(position).trader;
        String review1 = getItem(position).Review1;

        int starNum = Integer.parseInt(getItem(position).Stars);

        review = (TextView) customView.findViewById(R.id.review_description_history);
        reviewed = (TextView) customView.findViewById(R.id.reviewed_trader);

        TextView txtDate = (TextView) customView.findViewById(R.id.review_date_history);
        RatingBar stars = (RatingBar) customView.findViewById(R.id.review_stars_history);

        stars.setRating(starNum);

        reviewed.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);

        review.setTypeface(Typeface.SANS_SERIF, Typeface.NORMAL);
        reviewed.setText("To: " + username);
        review.setText(review1);

        String dateString = getItem(position).Date;

        String subdate = dateString.substring(0, 10);
        txtDate.setText("On: " + subdate);

        return customView;
    }
}

