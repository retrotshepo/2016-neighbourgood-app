package com.baqspace.proto;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.baqspace.model.User;

/**
 * Created by Tshepo Lebusa on 21/05/2016.
 */
public class ActivityTools extends AppCompatActivity {

    protected ActionBarDrawerToggle toggle;
    public final String TRADER_OBJECT = "traderObj";

    //protected Market market = new Market(2);
    User user = new User();

    public void navigateToActivity(FragmentActivity activity) {
        Intent intent = new Intent(this, activity.getClass());
        startActivity(intent);

    }

    public void navigateToActivityWithExtra(FragmentActivity activity, User extra) {
        Intent intent = new Intent(this, activity.getClass());
        intent.putExtra("currentSession", extra);
        startActivity(intent);
    }

    public void toggleSetup(DrawerLayout layout, Toolbar toolbar) {
        toggle = new ActionBarDrawerToggle(this, layout, toolbar, R.string.drawer_open, R.string.drawer_close);
        layout.setDrawerListener(toggle);
    }

    public void navigationActionListeners(final DrawerLayout layout, NavigationView view) {

        view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            public boolean onNavigationItemSelected(MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.action_gallery:
                        navigateToActivity(new Gallery());
                        item.setChecked(true);
                        layout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.action_trader:
                        navigateToActivity(new Filter());
                        item.setChecked(true);
                        layout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.action_logout:

                        item.setChecked(true);
                        layout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.action_about:
                        navigateToActivity(new AboutUs());
                        item.setChecked(true);
                        layout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.action_locate:
                        navigateToActivity(new Locate());
                        item.setChecked(true);
                        layout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.action_home:
                        navigateToActivity(new MainActivity());
                        item.setChecked(true);
                        layout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.action_contact:
                        navigateToActivity(new ContactUs());
                        item.setChecked(true);
                        layout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.action_recommendations:
                        navigateToActivity(new Recommend());
                        item.setChecked(true);
                        layout.closeDrawer(GravityCompat.START);
                        break;


                    case R.id.action_checkin:
                        navigateToActivity(new CheckIn());
                        item.setChecked(true);
                        layout.closeDrawer(GravityCompat.START);
                        break;
                }
                return false;
            }
        });
    }


    NotificationCompat.Builder notification;

    private static final int notificationID = 100;
    NotificationManager manager;

    public void notificationHandler(User session)
    {
        notification = new NotificationCompat.Builder(this);

        //clears notification clears when clicked
        notification.setAutoCancel(true);

        //building the notification
        notification.setWhen(System.currentTimeMillis());

        notification.setSmallIcon(R.drawable.ic_local_mall_black_24dp);

        //notification.setStyle(Notification.BigTextStyle);
        notification.setTicker(session.getUsername());
        notification.setContentTitle(session.getSession().getType());
        notification.setContentText("Neighbourgoods Market Johannesburg sent you a message\n" + session.Email);

        //opens app from anywhere on the device
        Intent intent = new Intent(this, Login.class);
        //intent.putExtra("session", session);
        PendingIntent pending = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        notification.setContentIntent(pending);

        //sending out notification to device
         manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        manager.notify(notificationID, notification.build());

        //startThread();
    }



    public void locationNotification(User session) {
        String msgText = "Hi " + session.getUsername() + ", JUICED Co. is in the same area as you are, we thought you might wanna check their store out.";

        //NotificationManager notificationManager = getNotificationManager();
        PendingIntent pi = getPendingIntent();

        Notification.Builder builder = new Notification.Builder(this);

        long [] array = {1 , 2, 3};
        builder.setTicker("Neighbourgoods Update")
                .setContentTitle("Neighbourgoods JHB Update")
                .setVibrate(array)
                .setContentText("Neighbourgoods Market Johannesburg")
                .setSmallIcon(R.drawable.ic_local_mall_black_24dp)
                .setPriority(Notification.PRIORITY_MAX)
                .setWhen(System.currentTimeMillis());
                //.addAction(R.drawable.ic_shopping_cart_black_36dp, "open app", pi);

        builder.setContentIntent(pi);
        builder.setAutoCancel(true);
        Notification notification = new Notification.BigTextStyle(builder)
                .bigText(msgText).build();


        //opens app from anywhere on the device
        //Intent intent = new Intent(this, Login.class);
        //intent.putExtra("session", session);
        //PendingIntent pending = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        //sending out notification to device
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        manager.notify(notificationID, notification);

        startThread();
    }

    //User session = null;
    public PendingIntent getPendingIntent() {


        //session.setSession("BROWSER");

        return PendingIntent.getActivity(this, 0, new Intent(this,
                Login.class), 0);
    }



    int count = 0;
    public void startThread()
    {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                System.out.println("Count "+count++);
            }
        }, 1000);

    }
}
