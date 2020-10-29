package uk.co.alt236.webviewdebug;

import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;



public class BaseJsObj {
    public WebView getWebView() {
        return webView;
    }

    protected WebView webView;
    protected AppCompatActivity activity;


    public void set(WebView webView, AppCompatActivity activity){
        this.activity = activity;
        this.webView = webView;
    }


}
