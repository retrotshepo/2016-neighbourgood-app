package com.baqspace.proto;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baqspace.model.GPSTracker;
import com.baqspace.model.User;
import com.squareup.picasso.Picasso;
import com.threepin.fireexit_wcf.Configurator;
import com.threepin.fireexit_wcf.FireExitClient;

public class Profile extends ActivityTools {

    private Toolbar toolbar;
    private DrawerLayout profile_page;
    private NavigationView profile_view;


    TextView txtUsername;
    TextView txtEmail;
    TextView txtSession;
    TextView txtSubcribe;
    TextView txtDate;
    TextView txtActive;

    ImageView profilePicture;

    RatingBar ratProfile;

    User session = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profile_page = (DrawerLayout) findViewById(R.id.profile_layout);

        toolbar = (Toolbar) findViewById(R.id.toolbar_layout);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");
        profile_view = (NavigationView) findViewById(R.id.profile_view);
        toggleSetup(profile_page, toolbar);
        MEnavigationActionListeners(profile_page, profile_view);

        setupAndDisplay();

    }


    private void setupAndDisplay() {
        session = (User) getIntent().getSerializableExtra("currentSession");
        System.out.println("PROFILE: " + session.Email);

        System.out.println("PROFILE: " + session.getSession());

        //Picasso.with(getContext()).load(images.get(position).Img_Name).into(imageView);

        profilePicture = (ImageView) findViewById(R.id.profile_picture);


        txtUsername = (TextView) findViewById(R.id.pro_username);
        txtEmail = (TextView) findViewById(R.id.pro_email);
        txtSession = (TextView) findViewById(R.id.pro_session);
        txtSubcribe = (TextView) findViewById(R.id.pro_subscription);
        txtDate = (TextView) findViewById(R.id.pro_date);
        txtActive = (TextView) findViewById(R.id.pro_active);

        ratProfile = (RatingBar) findViewById(R.id.pro_stars);

        Picasso.with(Profile.this).load(session.displayPictureURL).into(profilePicture);
        txtUsername.setTypeface(Typeface.SANS_SERIF, Typeface.NORMAL);
        txtEmail.setTypeface(Typeface.SANS_SERIF, Typeface.NORMAL);
        txtSession.setTypeface(Typeface.SANS_SERIF, Typeface.NORMAL);
        txtSubcribe.setTypeface(Typeface.SANS_SERIF, Typeface.NORMAL);
        txtDate.setTypeface(Typeface.SANS_SERIF, Typeface.NORMAL);

        txtUsername.setText(session.getUsername());
        txtEmail.setText(session.Email);
        txtSession.setText(session.getSession().getType());

        if (session.Subscription.equals("Y")) {
            txtSubcribe.setText("You are subscribed (:");
        } else {
            txtSubcribe.setText("You are not subscribed :(");
        }

        txtDate.setText(session.Registration_Date.substring(0, 9));

        if (session.getSession().getType().equals("TRADER")) {

            txtActive.setVisibility(View.VISIBLE);
            ratProfile.setVisibility(View.VISIBLE);

            System.out.println("PROFILE_ACTive: " + session.active);

            if (session.active == 1) {
                txtActive.setText("ACTIVE");
            } else {
                txtActive.setText("INACTIVE");
            }

            ratProfile.setRating(session.stars);

        } else {
            txtActive.setVisibility(View.GONE);
            ratProfile.setVisibility(View.GONE);

        }
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (profile_page.isDrawerOpen(GravityCompat.START)) {
            profile_page.closeDrawer(GravityCompat.START);

            return;
        }

        finish();
        navigateToActivityWithExtra(new MainActivity(), session);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_profile, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem reports = menu.findItem(R.id.action_reports);

        MenuItem location = menu.findItem(R.id.action_set_location);

        MenuItem reportsTrader = menu.findItem(R.id.action_reports_trader);

        if (!session.getSession().getType().contains("MANAGER")) {
            reports.setVisible(false);
        } else if (session.getSession().getType().contains("MANAGER")) {
            reports.setVisible(true);
        }



        if(!session.getSession().getType().contains("TRADER"))
        {
            location.setVisible(false);
            reportsTrader.setVisible(false);

        }
        else if(session.getSession().getType().contains("TRADER"))
        {
            location.setVisible(true);
            reportsTrader.setVisible(true);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.action_reports) {
            navigateToActivity(new Reports());
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            return true;
        } else if (id == R.id.action_location) {

            getLocationTry();
            return true;
        }
        else if (id == R.id.action_set_location) {
            //System.out.println("setting location");
            setLocation();
            return true;
        }
        else if (id == R.id.action_reports_trader) {
            navigateToActivity(new ReportsTrader());
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    GPSTracker mGPS;
    private void getLocationTry() {

        mGPS = new GPSTracker(this);

        if (mGPS.canGetLocation) {
            mGPS.getLocation();
            //System.out.println("Lat: " + mGPS.getLatitude() + "\nLong: " + mGPS.getLongitude());
            //Toast.makeText(Profile.this, "Lat: " + mGPS.getLatitude() + "\nLong: " + mGPS.getLongitude() , Toast.LENGTH_SHORT).show();
            //text1.setText("Lat" + mGPS.getLatitude() + "Lon" + mGPS.getLongitude());
        } else {
            Toast.makeText(Profile.this, "Unable" , Toast.LENGTH_SHORT).show();
            //text1.setText("Unabletofind");
            System.out.println("Unable");
        }

    }


    Input i = new Input();
    Handler handler = new Handler();

    private void setLocation()
    {
        mGPS = new GPSTracker(this);

        //instantiate location class
        i.setLocation = new SetLocation();

        if (mGPS.canGetLocation)
        {
            mGPS.getLocation();
            //System.out.println("Lat: " + mGPS.getLatitude() + "\nLong: " + mGPS.getLongitude());

            //Toast.makeText(Profile.this, "Lat: " + mGPS.getLatitude() + "\nLong: " + mGPS.getLongitude() , Toast.LENGTH_SHORT).show();

            i.setLocation.profileID = session.Profile_ID;
            i.setLocation.traderName = session.getUsername();
            i.setLocation.latitude = mGPS.getLatitude();
            i.setLocation.longitude = mGPS.getLongitude();


            /*
            System.out.println(i.setLocation.profileID);
            System.out.println(i.setLocation.traderName);
            System.out.println(i.setLocation.latitude);
            System.out.println(i.setLocation.longitude);

*/
            new Async().execute();


        }
        else
        {
            Toast.makeText(Profile.this, "Unable" , Toast.LENGTH_SHORT).show();
            System.out.println("Unable");
        }
    }



    class Async extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] params) {

            FireExitClient client = new FireExitClient(i.WCF_ADDRESS);
            client.configure(new Configurator("http://tempuri.org/", "IService1", "setStoreLocation"));

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
                        Toast.makeText(Profile.this, "New store location successfully set:\nLat: " + mGPS.getLatitude() + "\nLong: " + mGPS.getLongitude() , Toast.LENGTH_LONG).show();

                    }
                    else if(out.Comfirmation.contains("false"))
                    {
                        Toast.makeText(Profile.this, "unsuccessful", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    public void MEnavigationActionListeners(final DrawerLayout layout, NavigationView view) {

        view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            public boolean onNavigationItemSelected(MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.action_history:
                        navigateToActivityWithExtra(new History(), session);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                        item.setChecked(true);
                        layout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.action_home:
                        finish();
                        navigateToActivityWithExtra(new MainActivity(), session);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                        item.setChecked(true);
                        layout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.action_gallery:
                        finish();
                        navigateToActivityWithExtra(new Gallery(), session);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                        item.setChecked(true);
                        layout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.action_trader:
                        finish();
                        navigateToActivityWithExtra(new SplashFilter(), session);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                        item.setChecked(true);
                        layout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.action_about:
                        finish();
                        navigateToActivityWithExtra(new AboutUs(), session);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                        item.setChecked(true);
                        layout.closeDrawer(GravityCompat.START);
                        break;

                }
                return false;
            }
        });
    }


}
