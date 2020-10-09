package com.baqspace.proto;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class ContactUs extends AppCompatActivity {

    private Toolbar toolbar;
    private DrawerLayout contact_page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        contact_page = (DrawerLayout)findViewById(R.id.contact_layout);

        toolbar = (Toolbar) findViewById(R.id.toolbar_layout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Contact us");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        contact_page = (DrawerLayout) findViewById(R.id.contact_layout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact_us, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_home) {
            //sometingToBeDone(new MainActivity());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    private void sometingToBeDone(AppCompatActivity object)
    {
        AppCompatActivity temp = object;
        Intent intent = new Intent(this, temp.getClass());
        startActivity(intent);
    }

}
