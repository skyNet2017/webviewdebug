package uk.co.alt236.webviewdebugsampleapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.hss01248.webviewlib.WebviewHolder;
import com.just.agentweb.AgentWeb;

import uk.co.alt236.webviewdebug.DebugWebViewClientLogger;


public class AgentWebActivity extends AppCompatActivity {

   protected AgentWeb mAgentWeb;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String url2 = "http://www.jd.com";

        MyJsObj jsObj = new MyJsObj();


        WebviewHolder holder = new WebviewHolder(this);
        holder.configLog(true,true);//配置logcat日志和view上日志
        DebugWebViewClientLogger.logRequestOfNotMainFrame = true;
        holder.loadUrl(url2);

        setContentView(holder.root);

        mAgentWeb = holder.getAgentWeb();
        mAgentWeb.getJsInterfaceHolder().addJavaObject(MyJsObj.TAG,jsObj);
        jsObj.set(mAgentWeb);
        jsObj.set(mAgentWeb.getWebCreator().getWebView(),this);

    }

    @Override
    public void onBackPressed() {
        if(!mAgentWeb.back()){
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAgentWeb.getWebLifeCycle().onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAgentWeb.getWebLifeCycle().onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAgentWeb.getWebLifeCycle().onDestroy();
    }
}
