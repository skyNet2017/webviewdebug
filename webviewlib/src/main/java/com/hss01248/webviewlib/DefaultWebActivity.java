package com.hss01248.webviewlib;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.just.agentweb.AgentWeb;

import uk.co.alt236.webviewdebug.DebugWebViewClientLogger;

public class DefaultWebActivity extends AppCompatActivity {
    protected AgentWeb agentWeb;
   public WebviewHolder holder;
    public static final String KEY_URL = "url";

    public static void launch(Activity activity,String url){
        Intent intent = new Intent(activity,DefaultWebActivity.class);
        intent.putExtra(KEY_URL,url);
        activity.startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         holder = new WebviewHolder(this);

         //配置
        holder.configLog(WebViewLib.enableLog,WebViewLib.enableLog);//配置logcat日志和view上日志
        DebugWebViewClientLogger.logRequestOfNotMainFrame = false;
        //一些自定义功能
        holder.setWebErrorView(configWebErrorView());
        holder.setWebConfig(configWebAgent());

        setContentView(holder.root);

        //加载url
        String url = getIntent().getStringExtra(KEY_URL);
        holder.loadUrl(url);
        agentWeb = holder.agentWeb;
    }

    protected IWebErrorView configWebErrorView() {
        return null;
    }

    protected IWebConfig configWebAgent() {
        return null;
    }

    @Override
    public void onBackPressed() {
        if(!agentWeb.back()){
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        agentWeb.getWebLifeCycle().onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        agentWeb.getWebLifeCycle().onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        agentWeb.getWebLifeCycle().onDestroy();
    }
}
