package com.baqspace.proto;


import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.baqspace.model.User;
import com.threepin.fireexit_wcf.Configurator;
import com.threepin.fireexit_wcf.FireExitClient;

import java.util.ArrayList;

/**
 * Created by Tshepo Lebusa on 22/06/2016.
 * handles trader menu.
 */
public class Tab1 extends Fragment{

    Handler handler;
    Input input = new Input();
    Output output = new Output();

    String ID;

    User session = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab1, container, false);

        //Receiving data from Tab 5
        Bundle extras = getActivity().getIntent().getExtras();
        session = (User) getActivity().getIntent().getSerializableExtra("currentSession");

        if (session != null)
        {
            ID = session.extraData;
            input.userID = ID;
            Toast.makeText(getActivity(), "Loading menu...", Toast.LENGTH_LONG).show();

        }

        handler = new Handler();
        new Async().execute();

        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    class Async extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {

            //Establishing connection to the WCF
            FireExitClient client = new FireExitClient(input.WCF_ADDRESS);

            //Calling a method within the WCF
            client.configure(new Configurator("http://tempuri.org/","IService1","GetMenu"));

            //Passing the user ID
            client.addParameter("input", input);


            try {
                //Receive back the information

                output.menu = new ArrayList<>();  //instantiate here
                output =  client.call(output);
            } catch (Exception e)
            {
                e.printStackTrace();
            }

            return output;
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
        CustomMenuAdaptor adapter = new CustomMenuAdaptor(getActivity(),output.menu);

        ListView list = (ListView) getView().findViewById(R.id.traders_menu_ListView);
        list.setAdapter(adapter);

        //clickCallBack();
    }

    private void clickCallBack() {
        ListView list = (ListView) getView().findViewById(R.id.traders_menu_ListView);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {

            }
        });

    }
}
