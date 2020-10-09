package com.baqspace.proto;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import com.baqspace.model.User;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class CheckIn extends ActivityTools implements ZXingScannerView.ResultHandler {

    private Toolbar toolbar;
    private ZXingScannerView mScannerView;

    User session = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toast.makeText(CheckIn.this, "search for an N G M QR code and scan", Toast.LENGTH_LONG).show();
        setContentView(R.layout.activity_check_in);

        toolbar = (Toolbar) findViewById(R.id.toolbar_layout);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("NGM Checkin");

        session = (User) getIntent().getSerializableExtra("currentSession");
        System.out.println("CHK_IN: " + session.Email);

        callScanner();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_check_in, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        navigateToActivityWithExtra(new Filter(), session);

    }

    public void callScanner() {

        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);

        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {
        // Do something with the result here
        Log.e("handler", result.getText()); // Prints scan results
        Log.e("handler", result.getBarcodeFormat().toString()); // Prints the scan format (qrcode)

        // show the scanner result into dialog box.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Scan Result");
        builder.setMessage(result.getText());

        //AlertDialog alert1 = builder.create();

        //alert1.show();
        if(result.getText().contains("http://jozimarket.azurewebsites.net"))
        {
            Intent intent = new Intent(this, ReviewTrader.class);
            intent.putExtra("currentSession", session);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        }
        else
        {
            Toast.makeText(CheckIn.this, "Unknown code", Toast.LENGTH_LONG).show();
        }

        mScannerView.resumeCameraPreview(this);
    }
}
