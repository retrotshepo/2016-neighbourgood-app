package com.baqspace.proto;

import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Tshepo Lebusa on 12/09/2016.
 */
public class CustomClient extends WebViewClient {

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }
}
