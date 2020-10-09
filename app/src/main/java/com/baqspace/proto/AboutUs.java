package com.baqspace.proto;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.baqspace.model.User;

public class AboutUs extends ActivityTools {

    private NavigationView aboutUs_view;
    private Toolbar toolbar;
    private DrawerLayout about_page;

    User session = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_about_us);

        toolbar = (Toolbar) findViewById(R.id.toolbar_layout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("About us ");


        about_page = (DrawerLayout) findViewById(R.id.about_layout);
        about_page.setBackgroundColor(Color.WHITE);
        aboutUs_view = (NavigationView) findViewById(R.id.about_view);

        session = (User) getIntent().getSerializableExtra("currentSession");
        System.out.println("ABOUT_US: " + session.Email);

        signedState();
        toggleSetup(about_page, toolbar);
        MEnavigationActionListeners(about_page, aboutUs_view);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        toggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (about_page.isDrawerOpen(GravityCompat.START))
        {
            about_page.closeDrawer(GravityCompat.START);
            return;
        }

        finish();
        navigateToActivityWithExtra(new MainActivity(), session);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_browse) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void signedState()
    {
        aboutUs_view.getMenu().findItem(R.id.action_about).setEnabled(false);
    }

    public void MEnavigationActionListeners(final DrawerLayout layout, NavigationView view) {

        view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            public boolean onNavigationItemSelected(MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.action_contact:
                        navigateToActivity(new ContactUs());
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

                    case R.id.action_trader:
                        finish();
                        navigateToActivityWithExtra(new SplashFilter(), session);
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


                }
                return false;
            }
        });
    }


}
