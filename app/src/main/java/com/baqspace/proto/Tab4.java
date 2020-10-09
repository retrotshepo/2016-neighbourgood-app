package com.baqspace.proto;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baqspace.model.User;
import com.threepin.fireexit_wcf.Configurator;
import com.threepin.fireexit_wcf.FireExitClient;

/**
 * Created by Tshepo Lebusa on 22/06/2016.
 * handles trader contact.
 */
public class Tab4 extends Fragment {

    Handler handler;
    Input i = new Input();
    Output o = new Output();

    User session = null;

    View view;

    RelativeLayout instagramLayout;
    RelativeLayout facebookLayout;
    RelativeLayout twitterLayout;
    RelativeLayout websiteLayout;



    String ID;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.tab4, container, false);

        //Receiving data from Tab 5
        Bundle extras = getActivity().getIntent().getExtras();
        session = (User) getActivity().getIntent().getSerializableExtra("currentSession");

        if (session != null)
        {
            ID = session.extraData;
            i.userID = ID;
            Toast.makeText(getActivity(), "Loading contact details...", Toast.LENGTH_LONG).show();

        }

        setupLayouts();

        handler = new Handler();
        new Async().execute();

        return view;
    }

    class Async extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {

            //Establishing connection to the WCF
            FireExitClient client = new FireExitClient(i.WCF_ADDRESS);

            //Calling a method within the WCF
            client.configure(new Configurator("http://tempuri.org/","IService1","GetContact"));


            //Passing the user ID
            client.addParameter("input", i);


            try {
                //Receive back the information
                o.Cdetails = new Contacts();    //instantiating contact details holder
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
        TextView facebook = (TextView) getView().findViewById(R.id.facebook);
        TextView Instagram = (TextView) getView().findViewById(R.id.Instgram);
        TextView Website = (TextView) getView().findViewById(R.id.Website);
        TextView Twiter = (TextView) getView().findViewById(R.id.twitter);

        facebook.setText(o.Cdetails.Facebook);
        Instagram.setText(o.Cdetails.Instagram);
        Website.setText(o.Cdetails.Website);
        Twiter.setText(o.Cdetails.Twitter);


        //To do action Events

    }

    private void setupLayouts()
    {
        instagramLayout = (RelativeLayout) view.findViewById(R.id.instagram_portion);
        facebookLayout = (RelativeLayout) view.findViewById(R.id.facebook_portion);
        twitterLayout= (RelativeLayout) view.findViewById(R.id.twitter_portion);
        websiteLayout = (RelativeLayout) view.findViewById(R.id.website_portion);


        instagramLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(o.Cdetails.Instagram != null)
                {
                    String weburl = o.Cdetails.Instagram.trim();
                    goToURL(weburl);
                }
                else
                {
                    Toast.makeText(getContext(), "Not Found", Toast.LENGTH_LONG).show();
                }

            }
        });

        facebookLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(o.Cdetails.Facebook != null)
                {
                    String weburl = o.Cdetails.Facebook.trim();
                    goToURL(weburl);
                }
                else
                {
                    Toast.makeText(getContext(), "Not Found", Toast.LENGTH_LONG).show();
                }
            }
        });


        twitterLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //System.out.println("twitter clicked");

                if(o.Cdetails.Twitter != null)
                {
                    String weburl = o.Cdetails.Twitter.trim();
                    goToURL(weburl);
                }
                else
                {
                    Toast.makeText(getContext(), "Not Found", Toast.LENGTH_LONG).show();
                }
            }
        });

        websiteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //System.out.println("web clicked");

                if(o.Cdetails.Website != null)
                {
                    String weburl = o.Cdetails.Website.trim();
                    goToURL(weburl);
                }
                else
                {
                    Toast.makeText(getContext(), "Not Found", Toast.LENGTH_LONG).show();
                }

            }
        });


        Toast.makeText(getActivity(), "Tap images to open", Toast.LENGTH_LONG ).show();
    }


    private void goToURL(String url)
    {
        Uri uriURL = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriURL);
        startActivity(launchBrowser);
    }





}