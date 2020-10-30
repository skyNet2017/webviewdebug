package com.hss01248.webviewlib;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.just.agentweb.AgentWeb;
import com.just.agentweb.MiddlewareWebChromeBase;
import com.just.agentweb.MiddlewareWebClientBase;
import com.just.agentweb.WebChromeClient;

import uk.co.alt236.webviewdebug.DebugWebChromeClientLogger;
import uk.co.alt236.webviewdebug.DebugWebViewClient;
import uk.co.alt236.webviewdebug.JsObjAspect;

public class WebviewHolder {
    public LinearLayout root;

    public AgentWeb getAgentWeb() {
        return agentWeb;
    }

   protected AgentWeb agentWeb;
    protected Activity activity;
    protected ITitleBar titleBar;
    protected DebugViewHolder debugViewHolder;
    protected boolean debugViewEnable;
    protected String begainUrl;
    public String currentUrl;

    public void setWebErrorView(IWebErrorView webErrorView) {
        this.webErrorView = webErrorView;
    }

    protected IWebErrorView webErrorView;

    public void setWebConfig(IWebConfig webConfig) {
        this.webConfig = webConfig;
    }

    IWebConfig webConfig;



    public void configLog(boolean enableLog, boolean debugViewEnable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(enableLog);
        }
        JsObjAspect.enableLog = enableLog;
        this.debugViewEnable = debugViewEnable;
        DebugWebViewClient.setJsDebugPannelEnable(enableLog);
        //DebugWebViewClientLogger.logRequestOfNotMainFrame = false;
    }

    public WebviewHolder(Activity context) {
        activity = context;
        this.root = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.lib_viewholder_web, null);
        titleBar = root.findViewById(R.id.ll_web_titlebar);
        if(titleBar.getLeftImageButton() != null){
            titleBar.getLeftImageButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.onBackPressed();
                }
            });
        }else if( titleBar.getLeftTextView() != null){
            titleBar.getLeftTextView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.onBackPressed();
                }
            });
        }
    }


    public void loadUrl(String url) {
        begainUrl  = url;
        if (debugViewEnable) {
            debugViewHolder = new DebugViewHolder(activity, titleBar);
        }
        AgentWeb.CommonBuilder builder = AgentWeb.with(activity)
                .setAgentWebParent(root, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator();
                /*.setWebChromeClient(new WebChromeClient() {

                    @Override
                    public void onReceivedTitle(WebView view, String title) {
                        super.onReceivedTitle(view, title);
                        if(title.startsWith("http://") || title.startsWith("https://")){
                            Log.w(DebugWebChromeClientLogger.DEFAULT_TAG,"不显示纯网址的title");
                            return;
                        }
                        if(titleBar.getCenterTextView() != null){
                            titleBar.getCenterTextView().setText(title);
                        }else if(titleBar.getLeftTextView() != null){
                            titleBar.getLeftTextView().setText(title);
                        }
                    }
                });*/

        if(webErrorView != null){
            builder.setMainFrameErrorView(webErrorView.getView());
        }
        /*if (debugViewEnable) {
            wrappedWebviewClient = new DebugViewClient(debugViewHolder);
        }else {
            wrappedWebviewClient = new WrappedWebviewClient();
        }
        wrappedWebviewClient.setWebErrorView(webErrorView);*/

        builder.useMiddlewareWebChrome(new MiddlewareWebChromeBase(){
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if(title.startsWith("http://") || title.startsWith("https://")){
                    Log.w(DebugWebChromeClientLogger.DEFAULT_TAG,"不显示纯网址的title");
                    return;
                }
                if(titleBar.getCenterTextView() != null){
                    titleBar.getCenterTextView().setText(title);
                }else if(titleBar.getLeftTextView() != null){
                    titleBar.getLeftTextView().setText(title);
                }
            }
        });
        builder.useMiddlewareWebClient(new WrappedWebviewClient());
        if (debugViewEnable) {
            builder.useMiddlewareWebClient( new DebugViewClient(debugViewHolder));
        }
        builder.useMiddlewareWebClient(new MiddlewareWebClientBase(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                currentUrl = url;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        if(request.isForMainFrame() && request.isRedirect()){
                            //currentUrl = url;
                        }
                    }
                }
                return super.shouldOverrideUrlLoading(view, request);
            }
        });
        if(webConfig != null){
            webConfig.config(builder);
        }

        agentWeb = builder.createAgentWeb()
                .ready().go(url);

        //debug辅助功能
        if (debugViewEnable) {
            debugViewHolder.agentWeb = agentWeb;
        }

    }

}
