package com.baqspace.proto;

import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.baqspace.model.User;
import com.baqspace.swipeadapter.SelectedPager;

public class Misc extends ActivityTools implements TabLayout.OnTabSelectedListener {

    private Toolbar toolbar;

    private NavigationView miscView;
    private DrawerLayout misc_page;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private int[] tabIcon = {R.drawable.ic_card_giftcard_black_24dp,
            R.drawable.ic_info_black_24dp,
            R.drawable.ic_star_black_24dp,
            R.drawable.ic_phone_black_24dp};


    User session = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_misc);

        misc_page = (DrawerLayout) findViewById(R.id.misc_layout);

        toolbar = (Toolbar) findViewById(R.id.toolbar_layout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Miscellaneous");

        miscView = (NavigationView) findViewById(R.id.misc_view);
        signedState();
        toggleSetup(misc_page, toolbar);
        MEnavigationActionListeners(misc_page, miscView);


    }


    @Override
    public void onBackPressed() {
        if (misc_page.isDrawerOpen(GravityCompat.START))
        {
            misc_page.closeDrawer(GravityCompat.START);
            return;
        }
        finish();
        navigateToActivityWithExtra(new SplashFilter(), session);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(session.getSession().getType().contains("TRADER")) {
            getMenuInflater().inflate(R.menu.menu_add_menu_item, menu);
        }
        return true;
    }

    private void signedState() {
        //for testing purposes, set visible for every user.

        session = (User) getIntent().getSerializableExtra("currentSession");
        System.out.println("MISC: " + session.Email);

        miscView.getMenu().findItem(R.id.action_checkin).setEnabled(true);


        tabLayoutOnCreation();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_item) {
            navigateToActivityWithExtra(new AddItem(), session);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            return true;
        }

        return super.onOptionsItemSelected(item);
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
        tabLayout.addTab(tabLayout.newTab().setText("").setIcon(tabIcon[2]));
        tabLayout.addTab(tabLayout.newTab().setText("").setIcon(tabIcon[3]));


        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        viewPager = (ViewPager) findViewById(R.id.pager);
        SelectedPager adapter = new SelectedPager(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        //this line right here, \/ beastly..
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(this);
    }


    public void MEnavigationActionListeners(final DrawerLayout layout, NavigationView view) {

        view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            public boolean onNavigationItemSelected(MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.action_checkin:
                        finish();
                        navigateToActivityWithExtra(new CheckIn(), session);
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