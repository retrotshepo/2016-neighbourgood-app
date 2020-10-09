package com.baqspace.proto;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baqspace.model.User;
import com.threepin.fireexit_wcf.Configurator;
import com.threepin.fireexit_wcf.FireExitClient;

/**
 * Created by Tshepo Lebusa on 22/06/2016.
 * handles trader bio.
 */
public class Tab2 extends Fragment {

    String ID;

    User session = null;

    Handler handler;
    Input i = new Input();
    Output o = new Output();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab2, container, false);

        //Receiving data from Tab 5
        Bundle extras = getActivity().getIntent().getExtras();
        session = (User) getActivity().getIntent().getSerializableExtra("currentSession");

        if (session != null)
        {
            ID = session.extraData;
            i.userID = ID;
            Toast.makeText(getActivity(), "Loading bio...", Toast.LENGTH_LONG).show();

        }

        //Starting the Asynchronous
        handler = new Handler();
        new Async().execute();

        return  view;
        //return super.onCreateView(inflater, container, savedInstanceState);
    }

    class Async extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {

            //Establishing connection to the WCF
            FireExitClient client = new FireExitClient(i.WCF_ADDRESS);

            //Calling a method within the WCF
            client.configure(new Configurator("http://tempuri.org/","IService1","GetBio"));


            //Passing the user ID
            client.addParameter("input", i);


            try {
                //Receive back the information
                o.VendorProfile = new UserProfile(); //instantiated user profile.
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
                    //Displaying the information of the user
                    populateListView();

                }
            });
        }
    }


    private void populateListView()
    {
        RatingBar rating = (RatingBar) getView().findViewById(R.id.ratingBarStarsBio);
        rating.setRating(Float.parseFloat(o.VendorProfile.Stars));

        TextView lblBio = (TextView) getView().findViewById(R.id.cuisine_bio_text);

        lblBio.setText(o.VendorProfile.Profile_Bio);




    }

}