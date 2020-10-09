package com.baqspace.model;

import android.app.IntentService;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Tshepo Lebusa on 17/10/2016.
 */
public class FeedService extends IntentService {

    public FeedService()
    {
        super("FeedService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        System.out.println("Service running");
    }
}
