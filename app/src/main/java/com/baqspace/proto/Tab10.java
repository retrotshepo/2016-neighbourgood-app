package com.baqspace.proto;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.baqspace.model.User;
import com.threepin.fireexit_wcf.Configurator;
import com.threepin.fireexit_wcf.FireExitClient;

import java.util.ArrayList;

/**
 * Created by Tshepo Lebusa on 30/10/2016.
 */
public class Tab10 extends Fragment {

    Handler handler;
    Input i = new Input();
    Output o = new Output();

    SwipeRefreshLayout swipeRefreshLayout;
    View view;
    User session = null;

    FloatingActionButton compose;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab10, container, false);

        session = (User) getActivity().getIntent().getSerializableExtra("currentSession");

        System.out.println("NEW FEED: " + session.Email);

        handler = new Handler();
        new Async().execute();
        Toast.makeText(getActivity(), "Loading News Feed", Toast.LENGTH_LONG).show();
        signedState();


        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.tab10SwipeDownLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshFeeds();
            }
        });

        handler = new Handler();
        new Async().execute();

        return view;
    }


    private void signedState() {



        compose = (FloatingActionButton) view.findViewById(R.id.fab);

        compose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish();
                //stopService(intentService);
                Intent i = new Intent(getActivity(), Compose.class);
                i.putExtra("currentSession", session);

                startActivity(i);
                //navigateToActivityWithExtra(new Compose(), session);
                //overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });



        if (session.getSession().getType().equals("MANAGER")) {
            compose.show();
        } else {
            compose.hide();
        }
        //homeView.getMenu().findItem(R.id.action_home).setEnabled(false);

    }


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
                    populateListView();

                }
            });
        }
    }


    private void populateListView() {

        CustomNewsFeedAdapter adapter = new CustomNewsFeedAdapter(getActivity(), o.feeds, o.images, session);
        ListView list = (ListView) view.findViewById(R.id.feed_listview);
        list.setAdapter(adapter);

        swipeRefreshLayout.setRefreshing(false);
        //clickCallBack();
    }


    private void clickCallBack() {
        ListView list = (ListView) view.findViewById(R.id.feed_listview);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                Feeds feed = (Feeds) (parent.getItemAtPosition(position));

                String feed_id = "" + o.feeds.get(position).feedID;

                Toast.makeText(getActivity(), feed_id, Toast.LENGTH_LONG).show();

            }
        });


        list.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getActivity(), "feed deleted", Toast.LENGTH_LONG).show();
                return false;
            }
        });

    }



    private void refreshFeeds()
    {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new Async().execute();
            }
        }, 1000);

        //Toast.makeText(getContext(), "Refreshed", Toast.LENGTH_SHORT).show();

    }
}
