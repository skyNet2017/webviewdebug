package com.hss01248.webviewlib;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hss01248.activityresult.StartActivityUtil;
import com.hss01248.activityresult.TheActivityListener;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.MiddlewareWebChromeBase;

import uk.co.alt236.webviewdebug.DebugWebViewClientLogger;


public class JsWindowOpenImpl2 extends MiddlewareWebChromeBase {

    volatile WebView newWebView;
    IWebViewInit webViewInit;
   volatile DefaultWebActivity fullScreenActivity;

    public JsWindowOpenImpl2(IWebViewInit webViewInit) {
        this.webViewInit = webViewInit;
    }

    @Override
    public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
        try {


            newWebView = new WebView(view.getContext());
            Log.w("webdebug", "onCreateWindow:isDialog:" + isDialog + ",isUserGesture:" + isUserGesture + ",msg:" + resultMsg + "\n chromeclient:" + this+","+newWebView);
            WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
            transport.setWebView(newWebView);
            resultMsg.sendToTarget();

            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    try {
                        View view1 = initView(view.getContext(), newWebView);
                        Activity activity = getActivityFromContext(view.getContext());
                        if(activity instanceof  AppCompatActivity){
                            AppCompatActivity compatActivity = (AppCompatActivity) activity;
                            StartActivityUtil.startActivity(compatActivity,
                                    DefaultWebActivity.class,new Intent(activity,DefaultWebActivity.class),
                                    false, new TheActivityListener<DefaultWebActivity>(){
                                        @Override
                                        protected void onActivityCreated(@NonNull DefaultWebActivity activity, @Nullable Bundle savedInstanceState) {
                                            super.onActivityCreated(activity, savedInstanceState);
                                            //fullScreenActivity = activity;
                                            activity.setContent(view1);
                                            webViewInit.getImpl().fullScreenActivity = activity;


                                        }
                                    });
                        }else {
                            Log.w("jsoprnwindow","not compactactivity:"+activity);
                        }


                    }catch (Throwable throwable){
                        throwable.printStackTrace();
                    }
                }
            });

            return true;
        } catch (Throwable e) {
            e.printStackTrace();
            try {
                WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
                transport.setWebView(view);
                resultMsg.sendToTarget();
            }catch (Throwable throwable){
                throwable.printStackTrace();
            }

            return true;
        }
    }

    public static Activity getActivityFromContext(Context context) {
        if (context == null) {
            return null;
        }
        if (context instanceof Activity) {
            return (Activity) context;
        }
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

    private View initView(Context context, WebView webView) {
        Activity activity = getActivityFromContext(context);
        WebviewHolder holder = new WebviewHolder(activity);
        //配置
        holder.configLog(WebViewLib.enableLog,WebViewLib.enableLog);//配置logcat日志和view上日志
        DebugWebViewClientLogger.logRequestOfNotMainFrame = false;
        holder.setWebConfig(new IWebConfig() {
            @Override
            public void config(AgentWeb.CommonBuilder builder) {
                builder.setWebView(webView);
                builder.useMiddlewareWebChrome(new MiddlewareWebChromeBase(){

                });
            }
        });
        holder.loadUrl("");
        return holder.root;
    }




    /**
     * https://blog.csdn.net/asdfghgw/article/details/76066769
     * 此方法在新打开的页面里响应回调.
     * @param window
     */
    @Override
    public void onCloseWindow(WebView window) {
        Log.w("webdebug", "onCloseWindow:window:" +window+  this+","+fullScreenActivity+","+newWebView+",");
        if(fullScreenActivity != null){
            fullScreenActivity.finish();
            fullScreenActivity = null;
        }else {
            Activity activity = getActivityFromContext(window.getContext());
            if(activity != null){
                activity.finish();
            }
        }
        try {
            if (newWebView != null) {
                //window.removeView(newWebView);
                //newWebView.destroy();
                newWebView = null;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


}
