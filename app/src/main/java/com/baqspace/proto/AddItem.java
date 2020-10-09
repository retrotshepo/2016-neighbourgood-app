package com.baqspace.proto;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.baqspace.model.User;
import com.threepin.fireexit_wcf.Configurator;
import com.threepin.fireexit_wcf.FireExitClient;

public class AddItem extends ActivityTools {

    Handler handler;
    Input i = new Input();
    Output o = new Output();

    private Toolbar toolbar;

    EditText txtTitle;
    EditText txtDescription;
    EditText txtPrice;

    User session = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        toolbar = (Toolbar) findViewById(R.id.toolbar_layout);

        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add New Item");

        session = (User) getIntent().getSerializableExtra("currentSession");
        System.out.println("ADD_ITEM: " + session.Email);

        txtTitle = (EditText) findViewById(R.id.item_title);
        txtDescription = (EditText) findViewById(R.id.item_description);
        txtPrice = (EditText) findViewById(R.id.item_price);
        handler = new Handler();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add)
        {
            String title = txtTitle.getText().toString().trim();
            String description = txtDescription.getText().toString().trim();
            Double price = Double.parseDouble(txtPrice.getText().toString().trim());

            if (title.length() > 0)
            {
                if (description.length() > 0)
                {
                    if (price > 0)
                    {
                        //instantiating new menu item to send to WCF
                        i.newItem = new MenuItemNew();

                        i.newItem.title = title;
                        i.newItem.description = description;
                        i.newItem.price = price;
                        i.newItem.profileID = session.Profile_ID;

                        new Async().execute();

                        Toast.makeText(AddItem.this, "Adding New Item", Toast.LENGTH_LONG).show();
                    }
                }
            }
            return true;
        }
        else if (id == R.id.action_cancel)
        {
            finish();
            Toast.makeText(AddItem.this, "Nothing Added\n Redirecting...", Toast.LENGTH_LONG).show();
            navigateToActivityWithExtra(new Filter(), session);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        navigateToActivityWithExtra(new SplashFilter(), session);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);


    }

    class Async extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {

            FireExitClient client = new FireExitClient(i.WCF_ADDRESS);
            client.configure(new Configurator("http://tempuri.org/", "IService1", "addMenuItem"));

            client.addParameter("input", i);


            try {
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
                    if(out.Comfirmation.contains("true"))
                    {
                        Toast.makeText(AddItem.this, "Item added successfully", Toast.LENGTH_LONG).show();
                        finish();
                        navigateToActivityWithExtra(new SplashFilter(), session);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                    }
                    else
                    {
                        Toast.makeText(AddItem.this, "Failed to add item", Toast.LENGTH_LONG).show();
                        //navigateToActivityWithExtra(new Filter(), session);
                    }

                }
            });
        }
    }
}
