package com.hss01248.webviewlib;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.just.agentweb.AgentWeb;
import com.just.agentweb.WebChromeClient;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;
import uk.co.alt236.webviewdebug.JsObjAspect;

public class WebviewHolder {
    public LinearLayout root;

    public AgentWeb getAgentWeb() {
        return agentWeb;
    }

    AgentWeb agentWeb;
    Activity activity;
    CommonTitleBar titleBar;
    DebugViewHolder debugViewHolder;
    boolean debugViewEnable;

    public void configLog(boolean enableLog, boolean debugViewEnable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(enableLog);
        }
        JsObjAspect.enableLog = enableLog;
        this.debugViewEnable = debugViewEnable;
        //DebugWebViewClientLogger.logRequestOfNotMainFrame = false;
    }

    public WebviewHolder(Activity context) {
        activity = context;
        this.root = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.lib_viewholder_web, null);
        titleBar = root.findViewById(R.id.ll_web_titlebar);
        titleBar.getLeftTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        });


    }
    boolean appendJsDebugPan = false;
    public void loadUrl(String url) {
        if (debugViewEnable) {
            debugViewHolder = new DebugViewHolder(activity, titleBar);
        }
        AgentWeb.CommonBuilder builder = AgentWeb.with(activity)
                .setAgentWebParent(root, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .setWebChromeClient(new WebChromeClient() {

                    @Override
                    public void onReceivedTitle(WebView view, String title) {
                        super.onReceivedTitle(view, title);
                        titleBar.getLeftTextView().setText(title);
                    }

                    @Override
                    public void onProgressChanged(WebView view, int newProgress) {
                        super.onProgressChanged(view, newProgress);
                        if(JsObjAspect.enableLog){
                            if(newProgress > 10 && !appendJsDebugPan){
                                appendJsDebugPan = true;
                                ChuckJsObj.invokeJsDebugPan(view);
                            }
                        }
                    }
                });
        if (debugViewEnable) {
            builder.setWebViewClient(new DebugViewClient(debugViewHolder){
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    appendJsDebugPan = false;

                }
            });
        }
        agentWeb = builder.createAgentWeb()
                .ready().go(url);

        //debug辅助功能
        if (debugViewEnable) {
            debugViewHolder.agentWeb = agentWeb;
            debugViewHolder.setDebugLine();
        }
        if (JsObjAspect.enableLog) {
            ChuckJsObj.init(agentWeb.getWebCreator().getWebView());
        }

    }


}
