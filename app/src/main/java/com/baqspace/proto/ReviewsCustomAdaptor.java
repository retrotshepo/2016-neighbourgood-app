package com.baqspace.proto;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by DEE-KAY on 2016/08/09.
 */

class ReviewsCustomAdaptor extends ArrayAdapter<cReviews> {
    int starsCount = 0;

    Context ctx;
    TextView reviewer;
    TextView review;

    public ReviewsCustomAdaptor(Context context, Output out) {
        super(context, R.layout.custom_layout_reviews, out.reviews);
        //starsCount = Integer.parseInt(out.VendorProfile.Stars);


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater tempInflater = LayoutInflater.from(getContext());

        View customView = tempInflater.inflate(R.layout.custom_layout_reviews, parent, false);

        String username = getItem(position).UserName;
        String review1 = getItem(position).Review1;
        int starNum = Integer.parseInt(getItem(position).Stars);

        review = (TextView) customView.findViewById(R.id.review_description);
        reviewer = (TextView) customView.findViewById(R.id.reviewer_name);
        TextView txtDate = (TextView) customView.findViewById(R.id.review_date);
        RatingBar stars = (RatingBar) customView.findViewById(R.id.review_stars);

        stars.setRating(starNum);

        reviewer.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);

        review.setTypeface(Typeface.SANS_SERIF, Typeface.NORMAL);
        reviewer.setText("By: " + username);

        review.setText(review1);

        String dateString = getItem(position).Date;

        //Date date = new Date();
        //SimpleDateFormat format = new SimpleDateFormat("dd/mm/yyyy");

        String subdate = dateString.substring(0, 9);
        //date = format.parse(subdate);
        txtDate.setText("On: " + subdate);

        return customView;
    }
}