package com.baqspace.proto;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import com.baqspace.model.GPSTracker;
import com.baqspace.model.User;
import com.threepin.fireexit_wcf.Configurator;
import com.threepin.fireexit_wcf.FireExitClient;

import java.util.ArrayList;
import java.util.Locale;

public class LocationService extends Service {
    public LocationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
    }

    User session = null;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //session = (User) getIntent().getSerializableExtra("currentSession");
        //System.out.println("MAIN: " + session.Email);

        session = (User) intent.getSerializableExtra("currentSession");
        System.out.println("SERVICE: " + session.Email);

        System.out.println("SERVICE: " + session.latitude);
        System.out.println("SERVICE: " + session.longitude);

        //System.out.println(" location service running");

        //checkLocation();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 5; i++) {
                    long futureTime = System.currentTimeMillis() + 5000;
                    long currentTime = System.currentTimeMillis();

                    while (currentTime < futureTime) {
                        synchronized (this) {
                            try {
                               // System.out.println("SERVICE: WAITING");

                                wait(futureTime - currentTime);
//                                checkLocation();

                            } catch (Exception e) {

                            }
                        }
                    }
                }
            }
        };

        Thread notificationThread = new Thread(r);
        notificationThread.start();

        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private static final int notificationID = 100;
    NotificationManager manager;


    public void locationNotification() {
        String msgText = "Hi " + session.getUsername() + ", " + session.traderName + " is in the same area as you are, we thought you might wanna check their store out.";

        PendingIntent pi = getPendingIntent();
        Notification.Builder builder = new Notification.Builder(this);

        long[] array = {1, 2, 3};
        builder
                //.setTicker("Neighbourgoods Market Johannesburg")
                .setContentTitle("Update from " + session.traderName)
                .setVibrate(array)
                .setContentText("Update from " + session.traderName)
                .setSmallIcon(R.drawable.ic_local_mall_black_24dp)
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setWhen(System.currentTimeMillis());
        //.addAction(R.drawable.ic_shopping_cart_black_36dp, "open app", pi);
        builder.setContentIntent(pi);
        builder.setAutoCancel(true);
        Notification notification = new Notification.BigTextStyle(builder)
                .bigText(msgText).build();

        //sending out notification to device
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        manager.notify(notificationID, notification);

        stopNotification = true;
    }


    //User session = null;
    public PendingIntent getPendingIntent() {

        Intent intent = new Intent(getApplicationContext(), StoreLocation.class);
        intent.putExtra("session", session);

        return PendingIntent.getActivity(this, 0, intent, 0);
    }


    boolean stopNotification = false;

    private void checkLocation() {


        new Async().execute();

        for (int x = 0; x < o.locations.size(); x++) {

            //System.out.println(o.locations.get(x).ProfileID +" :: " + o.locations.get(x).TraderName);
            //System.out.println("Lat: " + o.locations.get(x).Lat + "\nLong: "+ o.locations.get(x).Long + "\n\n");

            double tempLat = session.latitude - o.locations.get(x).Lat; //current = 2
            double tempLong = session.longitude - o.locations.get(x).Long;

            double a = (Math.pow(Math.sin(tempLat / 2), 2) + Math.cos(o.locations.get(x).Lat) * Math.cos(session.latitude) * Math.pow(Math.sin(tempLong / 2), 2));
            double c = (2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)));
            double r = 6373 * c;

            //System.out.print(session.latitude + " : " + session.longitude);
            //System.out.println("DISTANCE " + r);


            if (r <= 15) {


                System.out.print("LOOP: " + o.locations.get(x).TraderName + ": " + r);
                System.out.println();
                session.traderName = o.locations.get(x).TraderName;
                session.trLatitude = o.locations.get(x).Lat;
                session.trLongitude = o.locations.get(x).Long;



                if (!stopNotification) {
                    locationNotification();
                }
                return;
            }
        }

    }




    Handler handler = new Handler();
    Input i = new Input();
    Output o = new Output();

    class Async extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {

            //Establishing connection between mobile and wcf
            FireExitClient client = new FireExitClient(i.WCF_ADDRESS);

            //Calling the appropriate method
            client.configure(new Configurator("http://tempuri.org/", "IService1", "getStoreLocations"));


            //Passing parameters to the given method
            client.addParameter("input", i);
            try {

                //instantiating feeds list to store data from WCF
                //o.feeds = new ArrayList<>();    //text
                // o.images = new ArrayList<>();   //image

                o.locations = new ArrayList<>();

                o = client.call(o); //getting data back

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

                    //populate the list view with the retrieved information
                    //populateListView();

                }
            });
        }
    }
}
