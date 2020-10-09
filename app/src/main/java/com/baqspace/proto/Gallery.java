package com.baqspace.proto;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.baqspace.model.User;
import com.baqspace.swipeadapter.CustomSwipeAdapter;

public class Gallery extends ActivityTools {

    private ViewPager gallery_pager;
    private CustomSwipeAdapter swipe_adapter;
    private Toolbar toolbar;

    private DrawerLayout gallery_page;
    private NavigationView gallery_view;

    public static FloatingActionButton removeImg;
    public static int imageResource = 0;

    User session = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        gallery_pager = (ViewPager) findViewById(R.id.gal_pager);
        swipe_adapter = new CustomSwipeAdapter(this);
        gallery_pager.setAdapter(swipe_adapter);

        toolbar = (Toolbar) findViewById(R.id.toolbar_layout);
        setSupportActionBar(toolbar);

        gallery_page = (DrawerLayout) findViewById(R.id.gallery_layout);
        gallery_view = (NavigationView) findViewById(R.id.gallery_view);

        signedState();
        MEnavigationActionListeners(gallery_page, gallery_view);
    }

    @Override
    public void onBackPressed() {
        if (gallery_page.isDrawerOpen(GravityCompat.START))
        {
            gallery_page.closeDrawer(GravityCompat.START);

            return;
        }
        finish();
        navigateToActivityWithExtra(new MainActivity(), session);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_gallery, menu);
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
        else if(id == R.id.action_about)
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void signedState()
    {
        session = (User) getIntent().getSerializableExtra("currentSession");
        System.out.println("GALLERY: " + session.Email);

        removeImg = (FloatingActionButton) findViewById(R.id.fab_gallery);
        gallery_view.getMenu().findItem(R.id.action_gallery).setEnabled(false);

        if(session.getSession().getType().equals("MANAGER"))
        {
            removeImg.hide();
        }
        else
        {
            removeImg.hide();
        }


        removeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    public void MEnavigationActionListeners(final DrawerLayout layout, NavigationView view) {

        view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            public boolean onNavigationItemSelected(MenuItem item) {

                switch (item.getItemId()) {

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