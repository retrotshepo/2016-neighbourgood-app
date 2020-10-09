package com.baqspace.proto;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.baqspace.model.User;

public class SplashGourmet extends AppCompatActivity {


    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

    User session = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_gourmet);

        session = (User) getIntent().getSerializableExtra("currentSession");
        System.out.println("SPLASH_FILTER: " + session.Email);

        startSplash();
    }


    private void startSplash()
    {
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity

                Intent intent = new Intent(getApplicationContext(), Cuisine.class);
                intent.putExtra("currentSession", session);

                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                // close this activity
                finish();

                System.out.println("Splashed Gou: " + session.getSession().getType());

            }
        }, SPLASH_TIME_OUT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_splash_gourmet, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
