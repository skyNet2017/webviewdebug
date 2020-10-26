package uk.co.alt236.webviewdebugsampleapp;

import android.webkit.JavascriptInterface;

public class MyJsObj {

    public static final String TAG = "MyJsObj";



    @JavascriptInterface
    public String add(String a,String b){
        return a+b;
    }
}
