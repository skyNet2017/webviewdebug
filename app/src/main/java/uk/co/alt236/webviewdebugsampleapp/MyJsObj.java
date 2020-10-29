package uk.co.alt236.webviewdebugsampleapp;


import android.util.Log;
import android.webkit.JavascriptInterface;

import com.hss01248.webviewlib.BaseAgentJsObj;


public class MyJsObj extends BaseAgentJsObj {

    public static final String TAG = "MyJsObj";


    @JavascriptInterface
    public String statusBarStyle(String params){
        Log.d("dd","statusBarStyle-"+params);
        //agentWeb.getJsAccessEntrace().callJs("");
        //int i = 6/0;//测试崩溃
        return params+"";
    }

    //jsGo2Address('82446','getNewAddressId')
    @JavascriptInterface
    public void address(String params,String callback){
        agentWeb.getJsAccessEntrace().quickCallJs(callback,"9999");
    }
}
