package com.baqspace.proto;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.baqspace.model.User;
import com.squareup.picasso.Picasso;

public class FeedView extends ActivityTools {

    Toolbar toolbar;

    ImageView disImg;
    TextView disName;


    ImageView feedImg;
    TextView feedDesc;
    TextView likeHolder;

    TextView time;

    User session ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_view);

        toolbar = (Toolbar) findViewById(R.id.toolbar_layout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Feed View");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupComponents();
    }

    private void setupComponents()
    {
        session = (User) getIntent().getSerializableExtra("currentSession");
        System.out.println("MAIN: " + session.Email);

        disImg = (ImageView) findViewById(R.id.detail_displaypicture);
        disName  = (TextView) findViewById(R.id.detail_name);

        disName.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);


        feedImg= (ImageView) findViewById(R.id.detail_image);
         feedDesc = (TextView) findViewById(R.id.detail_feed);;
         likeHolder = (TextView) findViewById(R.id.detail_like);

        time= (TextView) findViewById(R.id.detail_time);



        Picasso.with(FeedView.this).load(session.post.getDisplayImage()).into(disImg);
        disName.setText(session.post.getName());

        Picasso.with(FeedView.this).load(session.post.getFeedImage()).into(feedImg);
        feedDesc.setText(Html.fromHtml(session.post.getFeedDescription()));

        likeHolder.setText(session.post.getLikes() + "");
        time.setText(session.post.getTime());






    }

}
