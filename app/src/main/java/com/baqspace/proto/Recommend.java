package com.baqspace.proto;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.baqspace.model.User;
import com.threepin.fireexit_wcf.Configurator;
import com.threepin.fireexit_wcf.FireExitClient;

import java.util.ArrayList;

public class Recommend extends ActivityTools {

    Toolbar toolbar;

    Handler handler;
    Input i = new Input();
    Output o = new Output();

    User session = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);

        toolbar = (Toolbar) findViewById(R.id.toolbar_layout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Recommended");

        session = (User) getIntent().getSerializableExtra("currentSession");
        System.out.println("RECO: " + session.Email);

        i.userID = session.getID() + "";
        handler = new Handler();
        new Async().execute();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_recommend, menu);
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

            FireExitClient client = new FireExitClient(i.WCF_ADDRESS);
            client.configure(new Configurator("http://tempuri.org/","IService1","GetFood"));

            client.addParameter("input", i);


            try {
                o.user = new ArrayList<>(); //instantiation here

                o = client.call(o);
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
                    populateListView();

                }
            });
        }
    }

    private void populateListView()
    {
        CustomTraderAdapter adapter = new CustomTraderAdapter(this, o.user);

        ListView list = (ListView) findViewById(R.id.recommend_listview);
        list.setAdapter(adapter);

        //swipeRefreshLayout.setRefreshing(false);
        clickCallBack();
    }

    private void clickCallBack()
    {
        ListView list = (ListView) findViewById(R.id.recommend_listview);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {

                Users user = (Users) parent.getItemAtPosition(position);

                Toast.makeText(Recommend.this, String.valueOf(user.User_ID), Toast.LENGTH_LONG).show();
                String mess = "" + o.user.get(position).User_ID;
                Intent intent = new Intent(Recommend.this, Cuisine.class);
                session.extraData = mess;
                intent.putExtra("currentSession", session);
                //Toast.makeText(Recommend.this, String.valueOf(user.User_Type), Toast.LENGTH_LONG).show();

                intent.putExtra("message", mess);
                startActivity(intent);
            }
        });

    }
}
