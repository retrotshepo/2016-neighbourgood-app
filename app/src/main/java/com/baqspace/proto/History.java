package com.baqspace.proto;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.baqspace.model.User;
import com.threepin.fireexit_wcf.Configurator;
import com.threepin.fireexit_wcf.FireExitClient;

import java.util.ArrayList;

public class History extends AppCompatActivity {

    private Toolbar toolbar;
    User session = null;

    Handler handler;
    Input i = new Input();
    Output o = new Output();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        toolbar = (Toolbar) findViewById(R.id.toolbar_layout);
handler = new Handler();

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Review Log");

        session = (User) getIntent().getSerializableExtra("currentSession");

        i.userReview = new UserReview();
        i.userReview.userID = session.getID();
        i.userID = String.valueOf(session.getID());
        i.userReview.userName = session.getUsername();

        if(session.getSession().getType().equals("MANAGER"))
        {
            i.userReview.userType = "Manager";
        }
        else if(session.getSession().getType().equals("CUSTOMER"))
        {
            i.userReview.userType = "Customer";
        }
        else if(session.getSession().getType().equals("TRADER"))
        {
            i.userReview.userType = "Vendor";
        }

        new Async().execute();
    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_history, menu);
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














    class Async extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {

            //Establishing connection to the WCF
            FireExitClient client = new FireExitClient(i.WCF_ADDRESS);

            //Calling a method within the WCF
            client.configure(new Configurator("http://tempuri.org/","IService1","ReviewHistory"));

            //Passing the user ID
            client.addParameter("input", i);

            try {
                //Receive back the information
                o.reviews = new ArrayList<>();
                o =  client.call(o);
            } catch (Exception e)
            {
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
                    Output out = (Output)o;
                    //Displaying the information of the user
                    populateListView();

                }
            });
        }
    }

    private void populateListView()
    {
        CustomHistoryAdaptor adapter = new CustomHistoryAdaptor(History.this,o);
        ListView list = (ListView) findViewById(R.id.history_listview);
        list.setAdapter(adapter);
    }


}
