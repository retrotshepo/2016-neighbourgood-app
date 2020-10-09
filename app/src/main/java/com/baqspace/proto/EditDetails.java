package com.baqspace.proto;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.baqspace.model.User;
import com.squareup.picasso.Picasso;
import com.threepin.fireexit_wcf.Configurator;
import com.threepin.fireexit_wcf.FireExitClient;

public class EditDetails extends ActivityTools {

    private Toolbar toolbar;

    User session = null;

     ImageButton imageButton;
    private static final int IMAGE = 200;
     Uri imageUri;
    EditText txtUsername;
    EditText txtEmail;

     RadioButton subscribe;
     RadioButton unsubscribe;

     Handler handler;
     Input i = new Input();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_details);

        toolbar = (Toolbar) findViewById(R.id.toolbar_layout);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Edit Details");
        session = (User) getIntent().getSerializableExtra("currentSession");
        System.out.println("EDIT: " + session.Email);


        setupControls();

    }

    private void setupControls()
    {
        handler = new Handler();
        txtUsername = (EditText) findViewById(R.id.edit_username);
        txtEmail = (EditText) findViewById(R.id.edit_email);
        imageButton = (ImageButton) findViewById(R.id.edit_image);

        subscribe = (RadioButton) findViewById(R.id.radio_subscribe);
        unsubscribe = (RadioButton) findViewById(R.id.radio_unsubscribe);

        if(session.Subscription.contains("Y"))
        {
            subscribe.setChecked(true);
        }
        else
        {
            unsubscribe.setChecked(true);
        }

        Picasso.with(EditDetails.this).load(session.displayPictureURL).into(imageButton);

        txtEmail.setText(session.Email);
        txtUsername.setText(session.getUsername());


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_commit) {

            //async task
            Toast.makeText(EditDetails.this, "changes will reflect on next login", Toast.LENGTH_LONG).show();
            navigateToActivityWithExtra(new Profile(), session);
            return true;
        }
        else if (id == R.id.action_cancel) {
            Toast.makeText(EditDetails.this, "no change", Toast.LENGTH_LONG).show();
            navigateToActivityWithExtra(new Profile(), session);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == IMAGE)
        {
            imageUri = data.getData();
            imageButton.setImageURI(imageUri);
        }
    }

    private void openGallery()
    {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, IMAGE);
    }



    class Async extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {

            FireExitClient client = new FireExitClient(i.WCF_ADDRESS);
            client.configure(new Configurator("http://tempuri.org/","IService1","setFeeds"));

            client.addParameter("input", i);
            Output o = new Output();

            try {
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

                    if(out.Comfirmation.contains("true"))
                    {
                        //success.setText("Succesful");
                        Toast.makeText(EditDetails.this, "successful", Toast.LENGTH_LONG).show();

                    }
                    else
                    {
                        //success.setText("Unsuccesful");
                        Toast.makeText(EditDetails.this, "unsuccessful", Toast.LENGTH_LONG).show();

                    }

                }
            });
        }
    }

}
