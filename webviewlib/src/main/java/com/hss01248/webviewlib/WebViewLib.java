package com.hss01248.webviewlib;

import android.app.Application;

public class WebViewLib {

    public static boolean enableLog;
    public static Application app;
    public static void init(Application context,boolean enableLog){
        app = context;
        WebViewLib.enableLog = enableLog;
    }
}
