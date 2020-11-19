package uk.co.alt236.webviewdebugsampleapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.widget.TextView;

import com.hss01248.webviewlib.DefaultWebActivity;
import com.hss01248.webviewlib.IWebConfig;
import com.hss01248.webviewlib.IWebErrorView;
import com.hss01248.webviewlib.WebviewHolder;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.MiddlewareWebChromeBase;

import org.devio.takephoto.wrap.TakeOnePhotoListener;
import org.devio.takephoto.wrap.TakePhotoUtil;

import java.io.File;

import uk.co.alt236.webviewdebug.DebugWebChromeClientLogger;
import uk.co.alt236.webviewdebug.DebugWebViewClientLogger;


public class AgentWebActivity extends DefaultWebActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        String url2 = "http://www.jd.com";
        String url3 = "file:///android_asset/test.html";
        String url4 = "https://dwz1.cc/O6GrohYO";
        getIntent().putExtra(KEY_URL,url4);
        super.onCreate(savedInstanceState);

        MyJsObj jsObj = new MyJsObj();

        agentWeb.getJsInterfaceHolder().addJavaObject(MyJsObj.TAG,jsObj);
        jsObj.set(agentWeb);
        jsObj.set(agentWeb.getWebCreator().getWebView(),this);



        JsCodeGen.test();
    }

    @Override
    protected IWebConfig configWebAgent() {
        return new IWebConfig() {
            @Override
            public void config(AgentWeb.CommonBuilder builder) {
                builder.useMiddlewareWebChrome(new MiddlewareWebChromeBase(){
                    @Override
                    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                        TakePhotoUtil.startPickOne(AgentWebActivity.this, true, new TakeOnePhotoListener() {
                            @Override
                            public void onSuccess(String path) {
                                Log.d(DebugWebChromeClientLogger.DEFAULT_TAG,"onShowFileChooser-path:"+path);
                                Uri[] uris = new Uri[1];
                                uris[0] = Uri.fromFile(new File(path));
                                filePathCallback.onReceiveValue(uris);
                            }

                            @Override
                            public void onFail(String path, String msg) {
                                Log.d(DebugWebChromeClientLogger.DEFAULT_TAG,"onShowFileChooser-onFail:"+path);
                                filePathCallback.onReceiveValue(null);
                            }

                            @Override
                            public void onCancel() {
                                Log.d(DebugWebChromeClientLogger.DEFAULT_TAG,"onShowFileChooser-onCancel:");
                                filePathCallback.onReceiveValue(null);
                            }
                        });
                        return true;
                    }
                });
            }
        };
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
