package uk.co.alt236.webviewdebugsampleapp;


import android.util.Log;
import android.webkit.JavascriptInterface;

import com.hss01248.webviewlib.BaseAgentJsObj;


/**
 * The type My js obj.
 */
public class MyJsObj extends BaseAgentJsObj {

    /**
     * The constant TAG.
     */
    public static final String TAG = "MyJsObj";


    /**
     * Status bar style string.
     *
     * @param params the params
     * @return the string
     */
    @JavascriptInterface
    public String statusBarStyle(String params){
        Log.d("dd","statusBarStyle-"+params);
        //agentWeb.getJsAccessEntrace().callJs("");
        //int i = 6/0;//测试崩溃
        return params+"";
    }

    /**
     * Address.
     *
     * @param params   the params
     * @param callback the callback
     */
//jsGo2Address('82446','getNewAddressId')
    @JavascriptInterface
    public void address(String params,String callback){
        agentWeb.getJsAccessEntrace().quickCallJs(callback,"9999");
    }
}
