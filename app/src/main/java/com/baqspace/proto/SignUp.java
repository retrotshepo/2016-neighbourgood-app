package com.baqspace.proto;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.threepin.fireexit_wcf.Configurator;
import com.threepin.fireexit_wcf.FireExitClient;

import java.util.Date;

public class SignUp extends ActivityTools {

    private Toolbar toolbar;

    Handler handler;
    //InputLoginRegistration inputRegLogin = new InputLoginRegistration();

    Input i = new Input();

    String username;
    String email;
    String password1;
    String password2;


    Date date = new Date();
    Button register;

    EditText Email, Password1, Password2, UserName;
    TextView lblRegistrationStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        toolbar = (Toolbar) findViewById(R.id.toolbar_layout);


        handler = new Handler();
        sutupActionListener();
    }

    private void sutupActionListener() {
        register = (Button) findViewById(R.id.btnRegister);

        Email = (EditText) findViewById(R.id.email_id);
        Password1 = (EditText) findViewById(R.id.pass1);
        Password2 = (EditText) findViewById(R.id.pass2);
        UserName = (EditText) findViewById(R.id.UserName);

        lblRegistrationStatus = (TextView) findViewById(R.id.lblRegistrationStatus);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validateInputs()) {
                    System.out.println("YES");

                    //instantiating register object to send to WCF
                    i.reg = new Register();
                    i.reg.Username = username;
                    i.reg.Email = email;
                    i.reg.Password = password1;

                    //book keeping code
                    i.reg.User_Type = "Customer";
                    i.reg.Subscription = "Y";

                    new Async().execute();
                    register.setEnabled(false);
                } else {
                    System.out.println("NOPE");

                    lblRegistrationStatus.setText("Failed to register your account");
                    register.setEnabled(true);
                }


            }
        });

    }

    private boolean validateInputs() {
        boolean valid;

        username = UserName.getText().toString().trim();
        email = Email.getText().toString().trim();
        password1 = Password1.getText().toString().trim();
        password2 = Password2.getText().toString().trim();

        System.out.println(username.length());
        System.out.println(email.length());
        System.out.println(password1.length());
        System.out.println(password2.length());

        if ((username.length() > 0) && (email.length() > 0) && (email.contains("@"))) {
            System.out.println("username & email are correct");
            valid = true;
        } else {
            Toast.makeText(SignUp.this, "please re-enter valid details", Toast.LENGTH_LONG).show();
            valid = false;
            return valid;
        }

        if ((password1.length() > 1) && (password1.equals(password2))) {
            System.out.println("passwords correct");
            valid = true;
        } else {
            valid = false;
            Toast.makeText(SignUp.this, "please re-enter matching passwords", Toast.LENGTH_LONG).show();
        }

        System.out.println(valid);

        return valid;
    }


    class Async extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {

            FireExitClient client = new FireExitClient(i.WCF_ADDRESS);
            client.configure(new Configurator("http://tempuri.org/", "IService1", "Register"));

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
                        Toast.makeText(SignUp.this, "new user registered", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), Login.class);
                        startActivity(intent);
                    } else {
                        register.setEnabled(true);
                        Toast.makeText(SignUp.this, "could not be registered", Toast.LENGTH_LONG).show();
                        lblRegistrationStatus.setText("Invalid Information");
                    }

                }
            });
        }
    }


}
