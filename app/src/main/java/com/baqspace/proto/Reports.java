package com.baqspace.proto;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

public class Reports extends AppCompatActivity {

    WebView webView;
    String url = "https://app.powerbi.com/view?r=eyJrIjoiZGQ5MDNhMzMtODIzMS00MjRlLThkYjUtZjgwNmU4ZTQyNzJjIiwidCI6ImQ4YmY3YzE4LTU3MjUtNGI5ZS1iMTE4LTEzMzg4ZjUyZTQ0ZSIsImMiOjh9";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        webView = (WebView) findViewById(R.id.webReports);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setWebViewClient(new CustomClient());
        try
        {
            webView.loadUrl(url);
            //webView.loadUrl("http://eve.uj.ac.za/rkw/roster.php");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_reports, menu);
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
    */
}
