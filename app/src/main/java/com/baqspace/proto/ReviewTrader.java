package com.baqspace.proto;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baqspace.model.User;
import com.threepin.fireexit_wcf.Configurator;
import com.threepin.fireexit_wcf.FireExitClient;

public class ReviewTrader extends ActivityTools {

    Toolbar toolbar;

    DrawerLayout review_page;

    private Handler handler;
    private Input i = new Input();

    String message = "";

    double starCount = 0;
    String review = "";

    RatingBar ratingBar;

    RadioGroup group;

    RadioButton quality;
    RadioButton service;
    RadioButton clean;
    EditText body;
    Button submit;

    User session = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_trader);
        review_page = (DrawerLayout) findViewById(R.id.review_layout);

        session = (User) getIntent().getSerializableExtra("currentSession");
        System.out.println("TRADER_REVIEW: " + session.Email);

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        submit = (Button) findViewById(R.id.btnReview);

        toolbar = (Toolbar) findViewById(R.id.toolbar_layout);
        body = (EditText) findViewById(R.id.review_body);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Trader: " + session.profileName);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        setOnClickListener();
    }

    private void setOnClickListener() {
        group = (RadioGroup) findViewById(R.id.radio_group);

        quality = (RadioButton) findViewById(R.id.radio_quality);
        service = (RadioButton) findViewById(R.id.radio_service);
        clean = (RadioButton) findViewById(R.id.radio_clean);

        //quality.setChecked(true);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                starCount = rating;
                //Toast.makeText(ReviewTrader.this, String.valueOf(starCount), Toast.LENGTH_SHORT).show();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler = new Handler();





                if (clean.isChecked()) {
                    review = "CLEANLINESS\n" + body.getText().toString().trim();
                } else if (quality.isChecked()) {
                    review = "PRODUCT QUALITY\n" + body.getText().toString().trim();
                } else if (service.isChecked()) {
                    review = "TRADER SERVICE\n" + body.getText().toString().trim();
                }
                else
                {
                    //review without title
                    review = body.getText().toString().trim();

                }


                if(!body.isShown())
                {
                    review = "";
                }

                //instantiating review object
                i.review = new ReviewFromMobile();

                i.review.review = review;
                System.out.println("SENDING: " + i.review.review);
                i.review.profileID = session.profileToRate;
                i.review.userID = session.getID();

                if (session.getSession().getType().equals("MANAGER"))
                {
                    i.review.userType = "Manager";
                }
                else if (session.getSession().getType().equals("CUSTOMER"))
                {
                    i.review.userType = "Customer";
                }
                else if (session.getSession().getType().equals("TRADER"))
                {
                    i.review.userType = "Vendor";
                }
                i.review.stars = (int) Math.round(starCount);


                Toast.makeText(ReviewTrader.this, (int) Math.round(starCount) + "\n" + review, Toast.LENGTH_SHORT).show();
                new Async().execute();



            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_review_trader, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_review) {
            body.setVisibility(View.VISIBLE);
            group.setVisibility(View.VISIBLE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    class Async extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {

            FireExitClient client = new FireExitClient(i.WCF_ADDRESS);
            client.configure(new Configurator("http://tempuri.org/", "IService1", "sendReview"));

            client.addParameter("input", i);
            Output o = new Output();

            try {
                o = client.call(o);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return o;
        }

        @Override
        protected void onPostExecute(final Object o) {
            super.onPostExecute(o);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Output out = (Output) o;

                    if (out.Comfirmation.contains("true")) {
                        Toast.makeText(ReviewTrader.this, "Review Posted!", Toast.LENGTH_LONG).show();
                        navigateToActivityWithExtra(new Filter(), session);
                    }
                    else if (out.Comfirmation.contains("false"))
                    {
                        Toast.makeText(ReviewTrader.this, "Could Not Post Review!", Toast.LENGTH_LONG).show();
                    }

                }
            });
        }
    }
}
