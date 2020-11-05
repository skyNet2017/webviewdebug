package uk.co.alt236.webviewdebugsampleapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.hss01248.webviewlib.DefaultWebActivity;
import com.hss01248.webviewlib.IWebErrorView;
import com.hss01248.webviewlib.WebviewHolder;
import com.just.agentweb.AgentWeb;

import uk.co.alt236.webviewdebug.DebugWebViewClientLogger;


public class AgentWebActivity extends DefaultWebActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        String url2 = "http://www.jd.com";
        String url3 = "file:///android_asset/test.html";
        getIntent().putExtra(KEY_URL,url3);
        super.onCreate(savedInstanceState);

        MyJsObj jsObj = new MyJsObj();

        agentWeb.getJsInterfaceHolder().addJavaObject(MyJsObj.TAG,jsObj);
        jsObj.set(agentWeb);
        jsObj.set(agentWeb.getWebCreator().getWebView(),this);

        JsCodeGen.test();
    }

    @Override
    protected IWebErrorView configWebErrorView() {
        return new IWebErrorView() {
            TextView textView;
            @Override
            public View getView(Context context) {
              View root =   View.inflate(context,R.layout.web_error,null);
                textView = root.findViewById(R.id.web_tv_error);
                return root;
            }

            @Override
            public void setErrorMsg(String url, int code, String desMsg) {
                textView.setText(desMsg+"\ncode:"+code+"\nurl:"+url);
            }
        };
    }
}
