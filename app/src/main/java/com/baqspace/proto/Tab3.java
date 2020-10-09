package com.baqspace.proto;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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
 * Created by Tshepo Lebusa on 22/06/2016.
 * handles trader reviews.
 */
public class Tab3 extends Fragment {

    Handler handler;
    Input i = new Input();
    Output o = new Output();

    User session = null;

    String ID;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab3, container, false);

        //Receiving data from Tab 5
        Bundle extras = getActivity().getIntent().getExtras();
        session = (User) getActivity().getIntent().getSerializableExtra("currentSession");

        if (session != null)
        {
            ID = session.extraData;
            i.userID = ID;
            Toast.makeText(getActivity(), "Loading reviews...", Toast.LENGTH_LONG).show();
        }

        handler = new Handler();
        new Async().execute();

        return  view;
    }

    class Async extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {

            //Establishing connection to the WCF
            FireExitClient client = new FireExitClient(i.WCF_ADDRESS);

            //Calling a method within the WCF
            client.configure(new Configurator("http://tempuri.org/","IService1","GetReview"));

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

        ReviewsCustomAdaptor adapter = new ReviewsCustomAdaptor(getActivity(),o);
        ListView list = (ListView) getView().findViewById(R.id.reviews_listView);
        list.setAdapter(adapter);

    }
}