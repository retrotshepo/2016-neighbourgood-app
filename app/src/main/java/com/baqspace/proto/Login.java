package com.baqspace.proto;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baqspace.model.User;
import com.threepin.fireexit_wcf.Configurator;
import com.threepin.fireexit_wcf.FireExitClient;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Login extends AppCompatActivity {

    Handler handler;

    Input i = new Input();
    Output o = new Output();

    EditText Email, Password;
    TextView lblLoginStatus;

    User session = null;
    TextView register;

    String comfirm = "";
    private Toolbar toolbar;
    Button btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        toolbar = (Toolbar) findViewById(R.id.toolbar_layout);
        btn = (Button) findViewById(R.id.btnLogin);

        handler = new Handler();

        //start news feed service.
        Intent intent = new Intent(this, ServiceForFeeds.class);
        startService(intent);





        Email = (EditText) findViewById(R.id.login_email);
        Password = (EditText) findViewById(R.id.login_password);


        register = (TextView) findViewById(R.id.registration);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToAvtivity(new SignUp());
            }
        });
        lblLoginStatus = (TextView) findViewById(R.id.lblLogInStatus);



        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((Email.getText().toString().trim().length() < 1) || (Password.getText().toString().trim().length() < 1)) {
                    Toast.makeText(Login.this, "please enter valid credentials", Toast.LENGTH_LONG).show();
                } else {
                    //instantiating login object.
                    i.login = new SignIn();

                    i.login.EmailAddress = Email.getText().toString().trim();
                    i.login.Password = Password.getText().toString();
                    //System.out.println("TIME: " + DateFormat.getDateInstance().format(System.currentTimeMillis()));

                    long currentTime = System.currentTimeMillis();
                    //SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm");
                    SimpleDateFormat sdf = new SimpleDateFormat("MMM/dd/yyyy HH:mm");
                    Date date = new Date(currentTime);

                    System.out.println("TESTING FORMAT: " + sdf.format(date));

                    new Async().execute();
                    btn.setEnabled(false);
                }


            }
        });


    }


    @Override
    public void onBackPressed() {
        //this.finish();
        System.exit(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    private void navigateToAvtivity(AppCompatActivity object) {
        AppCompatActivity temp = object;
        Intent intent = new Intent(this, temp.getClass());
        startActivity(intent);
    }

    class Async extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {

            FireExitClient client = new FireExitClient(i.WCF_ADDRESS);
            client.configure(new Configurator("http://tempuri.org/", "IService1", "Login"));

            client.addParameter("input", i);


            try {
                o.single = new Users(); //instantiating new user
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
                    comfirm = out.Comfirmation;
                    if (comfirm.contains("true")) {
                        createSession();
                        //startSplash();
                        btn.setEnabled(true);

                        finish();

                    } else if (comfirm.contains("false")) {
                        Toast.makeText(Login.this, "Email, Password combination do not match our records", Toast.LENGTH_LONG).show();
                        btn.setEnabled(true);

                    } else {
                        Toast.makeText(Login.this, "Failed to connect to database\nPlease check your network connection", Toast.LENGTH_LONG).show();
                        btn.setEnabled(true);

                    }

                }
            });
        }

        private void createSession() {

            session = new User();
            session.setID(o.single.User_ID);
            session.setUsername(o.single.UserName);

            session.setPassword(o.single.Password);
            session.Subscription = o.single.Subscription;
            session.Registration_Date = o.single.Registration_Date;
            session.Email = o.single.Email;

            session.displayPictureURL = o.single.UserImg;

            if (o.single.User_Type.toUpperCase().equals("VENDOR")) {
                session.setSession("TRADER");
                session.Profile_ID = o.single.Profile_ID;

                session.active = o.single.active;


                session.stars = o.single.stars;

            } else {
                session.setSession(o.single.User_Type.toUpperCase());
            }

            Intent intent = new Intent(getApplicationContext(), Splash.class);
            intent.putExtra("currentSession", session);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        }

    }
}