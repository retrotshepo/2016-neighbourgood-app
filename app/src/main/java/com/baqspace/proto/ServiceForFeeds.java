package com.baqspace.proto;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.threepin.fireexit_wcf.Configurator;
import com.threepin.fireexit_wcf.FireExitClient;

import java.util.ArrayList;
import java.util.Locale;

public class ServiceForFeeds extends Service {

    public ServiceForFeeds() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        System.out.println(" feed service running");

        Runnable r = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 5; i++) {
                    long futureTime = System.currentTimeMillis() + 10000;
                    long currentTime = System.currentTimeMillis();

                    while (currentTime < futureTime) {
                        synchronized (this) {
                            try {

                                wait(futureTime - currentTime);
                                //System.out.println(" notification service running");
                                checkForNewFeed();


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

        Toast.makeText(ServiceForFeeds.this, "feed service no longer running", Toast.LENGTH_SHORT).show();
    }


    private void checkForNewFeed() {

        new Async().execute();

        for (int x = 0; x < o.feeds.size(); x++)
        {
            if(o.feeds.get(x).timeStemp.toUpperCase(Locale.ENGLISH).contains("JUST NOW"))
            {
                feedNotification(o.feeds.get(x).NewsFeed1);
            }
            else{
                //System.out.println("no update");
            }
        }

    }



    NotificationCompat.Builder notification;
    private static final int notificationID = 200;
    NotificationManager manager;
    String msg = "\n";

    public void feedNotification(String feed) {
        //String msgText = "Hi "  + ", JUICED Co. is in the same area as you are, we thought you might wanna check their store out.";

        //NotificationManager notificationManager = getNotificationManager();
        PendingIntent pi = getPendingIntent();

        Notification.Builder builder = new Notification.Builder(this);

        long [] array = {1 , 2, 3};
        builder.setContentTitle("Neighbourgoods Feed Update")
                .setVibrate(array)
                .setTicker("Neighbourgoods Market Update")
                //.setContentText("Neighbourgoods Market Johannesburg")
                .setSmallIcon(R.drawable.ic_local_mall_black_24dp)
                .setPriority(Notification.PRIORITY_HIGH)
                .setWhen(System.currentTimeMillis());

        builder.setContentIntent(pi);
        builder.setAutoCancel(true);
        Notification notification = new Notification.BigTextStyle(builder)
                .bigText(msg + feed).build();

        //sending out notification to device
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        manager.notify(notificationID, notification);

    }

    public PendingIntent getPendingIntent() {
        return PendingIntent.getActivity(this, 0, new Intent(this,
                Login.class), 0);
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
            client.configure(new Configurator("http://tempuri.org/", "IService1", "GetNewsFeed"));


            //Passing parameters to the given method
            client.addParameter("input", i);
            try {
                //instantiating feeds list to store data from WCF
                o.feeds = new ArrayList<>();    //text
                o.images = new ArrayList<>();   //image

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
