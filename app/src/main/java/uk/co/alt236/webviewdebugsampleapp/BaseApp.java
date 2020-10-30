package uk.co.alt236.webviewdebugsampleapp;

import android.app.Application;
import android.support.multidex.MultiDexApplication;

import com.hss01248.webviewlib.WebViewLib;

public class BaseApp extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        WebViewLib.init(this,true);
    }
}
