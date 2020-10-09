package com.baqspace.proto;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.baqspace.model.User;
import com.threepin.fireexit_wcf.Configurator;
import com.threepin.fireexit_wcf.FireExitClient;

public class Compose extends ActivityTools {

    private Toolbar toolbar;
    private DrawerLayout compose_page;
    private CoordinatorLayout coordinatorLayout;

    private ImageView imageView;
    private static final int IMAGE = 100;
    private Uri imageUri;

    private EditText message;
    private String eventType = "";
    //private TextView success;
    private String news = "";


    private Handler handler;
    private Input i = new Input();

    User session = null;

    private RadioButton radioDay;
    private RadioButton radioNight;
    private RadioButton radioMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        toolbar = (Toolbar) findViewById(R.id.toolbar_layout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Post Feed");

        setLayoutItems();

    }

    private void setLayoutItems() {
        session = (User) getIntent().getSerializableExtra("currentSession");
        System.out.println("COMPOSE: " + session.Email);

        handler = new Handler();
        compose_page = (DrawerLayout) findViewById(R.id.compose_layout);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.compose_coordinatorlayout);

        imageView = (ImageView) findViewById(R.id.compose_image);
        message = (EditText) findViewById(R.id.compose_feed);
        //success = (TextView) findViewById(R.id.compose_success);

        radioDay = (RadioButton) findViewById(R.id.radio_day);
        radioNight = (RadioButton) findViewById(R.id.radio_night);
        radioMusic = (RadioButton) findViewById(R.id.radio_music);

        radioDay.setChecked(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_compose, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_post) {
            news = message.getText().toString().trim();

            if (news.length() > 0) {
                if (radioDay.isChecked()) {
                    eventType = "Market";
                } else if (radioNight.isChecked()) {
                    eventType = "Night Market";
                } else if (radioMusic.isChecked()) {
                    eventType = "Music";
                }


                //instantiating feed object
                i.feeds = new NewsFeeeds();

                i.feeds.User_ID = session.getID();
                i.feeds.User_Type = "Manager";
                i.feeds.NewsFeed1 = news;
                i.feeds.Event = eventType;
                Toast.makeText(Compose.this, "checking validity of feed", Toast.LENGTH_SHORT).show();
                new Async().execute();
                Toast.makeText(Compose.this, "checking validity of feed", Toast.LENGTH_SHORT).show();

            }
            else
            {
                Toast.makeText(Compose.this, "Can't post an empty feed", Toast.LENGTH_LONG).show();
            }

            return true;
        } else if (id == R.id.action_attach) {
            openGallery();
            return true;
        }
        if (id == R.id.action_cancel) {
            finish();
            navigateToActivityWithExtra(new MainActivity(), session);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        System.out.println("back pressed");
        navigateToActivityWithExtra(new MainActivity(), session);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == IMAGE) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }

    class Async extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] params) {

            FireExitClient client = new FireExitClient(i.WCF_ADDRESS);
            client.configure(new Configurator("http://tempuri.org/", "IService1", "addFeed"));

            client.addParameter("input", i);
            Output o = new Output();

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

                    if (out.Comfirmation.contains("true")) {
                        Toast.makeText(Compose.this, "Feed posted", Toast.LENGTH_LONG).show();
                        finish();
                        navigateToActivityWithExtra(new MainActivity(), session);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                    }
                    else if(out.Comfirmation.contains("false"))
                    {
                        Toast.makeText(Compose.this, "Could not post feed", Toast.LENGTH_LONG).show();
                        finish();
                        navigateToActivityWithExtra(new MainActivity(), session);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                    }
                }
            });
        }
    }

}
