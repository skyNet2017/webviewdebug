package com.hss01248.webviewlib;

import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.just.agentweb.AgentWeb;

public class BaseJsObj {
   protected WebView webView;
    protected AppCompatActivity activity;
    protected AgentWeb agentWeb;

    public void set(WebView webView, AppCompatActivity activity){
        this.activity = activity;
        this.webView = webView;
    }

    public void set(AgentWeb agentWeb){
        this.agentWeb = agentWeb;
    }
}
