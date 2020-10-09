package com.baqspace.proto;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.baqspace.model.GPSTracker;
import com.baqspace.model.User;
import com.baqspace.swipeadapter.FeedPager;


public class MainActivity extends ActivityTools implements TabLayout.OnTabSelectedListener {

    Handler handler;
    Input i = new Input();
    Output o = new Output();

    private TabLayout tabLayout;
    private ViewPager viewPager;

    SwipeRefreshLayout swipeRefreshLayout;
    Toolbar toolbar;
    DrawerLayout home_page;
    NavigationView homeView;
    FloatingActionButton compose;

    User session = null;

    GPSTracker mGPS;
    Intent intentService;


    private int[] tabIcon = {R.drawable.ic_import_contacts_black_24dp,
            R.drawable.ic_notifications_black_24dp};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /*
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeDownLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshFeeds();
            }
        });
        */

        Toast.makeText(MainActivity.this, "Loading News Feed", Toast.LENGTH_LONG).show();
        home_page = (DrawerLayout) findViewById(R.id.home_layout);

        toolbar = (Toolbar) findViewById(R.id.toolbar_layout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.action_home);
        homeView = (NavigationView) findViewById(R.id.home_view);


        toggleSetup(home_page, toolbar);
        MEnavigationActionListeners(home_page, homeView);

        session = (User) getIntent().getSerializableExtra("currentSession");
        System.out.println("MAIN: " + session.Email);

        getLocationTry();

        intentService = new Intent(this, LocationService.class);
        intentService.putExtra("currentSession", session);
        startService(intentService);

        signedState();

    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {

        if (home_page.isDrawerOpen(GravityCompat.START)) {
            home_page.closeDrawer(GravityCompat.START);
            return;
        }


        if (doubleBackToExitPressedOnce) {
            finish();
            stopService(intentService);
            Toast.makeText(this, "Logged out", Toast.LENGTH_LONG).show();
            navigateToActivity(new Login());
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Tap again to sign out", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 1500);
        return;


    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        toggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_profile) {
            finish();
            stopService(intentService);
            navigateToActivityWithExtra(new Profile(), session);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void signedState() {

        tabLayoutOnCreation();

        /*
        compose = (FloatingActionButton) findViewById(R.id.fab);

        compose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //stopService(intentService);
                navigateToActivityWithExtra(new Compose(), session);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        */


        /*
        if (session.getSession().getType().equals("MANAGER")) {
            //compose.show();
        } else {
            //compose.hide();
        }
        */
        homeView.getMenu().findItem(R.id.action_home).setEnabled(false);

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }



    private void tabLayoutOnCreation() {
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);


        tabLayout.addTab(tabLayout.newTab().setText("").setIcon(tabIcon[0]));
        tabLayout.addTab(tabLayout.newTab().setText("").setIcon(tabIcon[1]));


        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) findViewById(R.id.pager);

        FeedPager adapter = new FeedPager(getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(adapter);
        //this line right here, \/ beastly..
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(this);
    }





    public void MEnavigationActionListeners(final DrawerLayout layout, NavigationView view) {

        view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            public boolean onNavigationItemSelected(MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.action_trader:
                        finish();
                        stopService(intentService);
                        navigateToActivityWithExtra(new SplashFilter(), session);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        item.setChecked(true);
                        layout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.action_gallery:
                        finish();
                        stopService(intentService);
                        navigateToActivityWithExtra(new Gallery(), session);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        item.setChecked(true);
                        layout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.action_about:
                        finish();
                        stopService(intentService);
                        navigateToActivityWithExtra(new AboutUs(), session);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        item.setChecked(true);
                        layout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.action_locate:
                        navigateToActivity(new Locate());
                        stopService(intentService);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        item.setChecked(true);
                        layout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.action_logout:
                        //manager.setPreferences(MainActivity.this, "status", "0");
                        finish();
                        session = null;
                        navigateToActivity(new Login());
                        item.setChecked(true);
                        layout.closeDrawer(GravityCompat.START);
                        break;
                }
                return false;
            }
        });
    }

    /*
    private void refreshFeeds() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //new Async().execute();
            }
        }, 1000);


    }*/



    private void getLocationTry() {

        mGPS = new GPSTracker(this);

        if (mGPS.canGetLocation) {
            mGPS.getLocation();

            session.latitude = mGPS.getLatitude();
            session.longitude = mGPS.getLongitude();
        } else {
            System.out.println("Unable");
        }

    }


}

