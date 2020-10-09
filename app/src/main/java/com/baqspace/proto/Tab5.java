package com.baqspace.proto;


//import android.app.Fragment;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

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
 * Created by Tshepo Lebusa on 23/06/2016.
 * handles cuisine
 */
public class Tab5 extends Fragment {

    Handler handler;
    Input i = new Input();
    Output o = new Output();

    SwipeRefreshLayout swipeRefreshLayout;
    User session = null;
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.tab5, container, false);

        handler = new Handler();
        new Async().execute();
        Toast.makeText(getActivity(), "Loading Gourmet...", Toast.LENGTH_LONG).show();


        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.tab5SwipeDownLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshFeeds();
            }
        });

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    class Async extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {

            FireExitClient client = new FireExitClient(i.WCF_ADDRESS);
            client.configure(new Configurator("http://tempuri.org/", "IService1", "GetGourmetList"));

            client.addParameter("input", i);

            try {

                o.user = new ArrayList<>(); //instantiation here
                o = client.call(o);
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
                    populateListView();

                }
            });
        }
    }

    private void populateListView() {
        CustomTraderAdapter adapter = new CustomTraderAdapter(getActivity(), o.user);

        ListView list = (ListView) getView().findViewById(R.id.cuisine_listview);
        list.setAdapter(adapter);

        swipeRefreshLayout.setRefreshing(false);
        clickCallBack();
    }

    private void clickCallBack() {
        ListView list = (ListView) getView().findViewById(R.id.cuisine_listview);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                Users user = (Users) parent.getItemAtPosition(position);

                String mess = "" + o.user.get(position).User_ID;
                session = (User) getActivity().getIntent().getSerializableExtra("currentSession");
                System.out.println("TAB 5: " + session.getSession());

                session.extraData = mess;
                session.profileToRate = o.user.get(position).Profile_ID;
                session.profileName = o.user.get(position).UserName;

                System.out.println("Profile to rate: " + session.profileToRate);

                Intent intent = new Intent(getActivity(), SplashGourmet.class);
                intent.putExtra("currentSession", session);
                startActivity(intent);

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