package uk.co.alt236.webviewdebug;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.view.InputEvent;
import android.view.KeyEvent;
import android.webkit.ClientCertRequest;
import android.webkit.HttpAuthHandler;
import android.webkit.RenderProcessGoneDetail;
import android.webkit.SafeBrowsingResponse;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.TlsVersion;

public class DebugWebViewClient extends WebViewClient implements LogControl {
    //https://github.com/liriliri/eruda
    private static final String CDN2 =  "<head>"+"<script src=\"https://cdn.jsdelivr.net/npm/eruda\"></script>" +
            "<script>eruda.init();</script>";
    private final WebViewClient client;
    private final DebugWebViewClientLogger logger;
    private final OnUnhandledInputEventMethodProxy onUnhandledInputEventMethodProxy;


    public static boolean isJsDebugPannelEnable() {
        return jsDebugPannelEnable;
    }


    public static void setJsDebugPannelEnable(boolean jsDebugPannelEnable) {
        DebugWebViewClient.jsDebugPannelEnable = jsDebugPannelEnable;
    }

    static boolean jsDebugPannelEnable;


    public DebugWebViewClient() {
        this(new WebViewClient());
    }

    public DebugWebViewClient(@NonNull final WebViewClient client) {
        this(client, new DebugWebViewClientLogger());
    }

    @SuppressWarnings("WeakerAccess")
    public DebugWebViewClient(
            @NonNull final WebViewClient client,
            @NonNull final DebugWebViewClientLogger logger) {
        this.logger = logger;
        this.client = client;
        this.onUnhandledInputEventMethodProxy = new OnUnhandledInputEventMethodProxy(client);
        validate();
    }

    private void validate() {
        try{
            if (!new Validation().validate(WebViewClient.class, this.getClass())) {
            Log.e(DebugWebViewClient.class.getSimpleName(),
                    "invalid: the DebugClient does not override all methods overridden in the wrapped client");
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        
    }




    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceivedError(final WebView view, final WebResourceRequest request, final WebResourceError error) {
        logger.onReceivedError(view, request, error);
        client.onReceivedError(view, request, error);
    }

    @Override
    @Deprecated
    public void onReceivedError(final WebView view, final int errorCode, final String description, final String failingUrl) {
        logger.onReceivedError(view, errorCode, description, failingUrl);
        //noinspection deprecation
        client.onReceivedError(view, errorCode, description, failingUrl);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceivedHttpError(final WebView view, final WebResourceRequest request, final WebResourceResponse errorResponse) {
        logger.onReceivedHttpError(view, request, errorResponse);
        client.onReceivedHttpError(view, request, errorResponse);
    }

    @Override
    public void onReceivedSslError(final WebView view, final SslErrorHandler handler, final SslError error) {
        logger.onReceivedSslError(view, handler, error);
        client.onReceivedSslError(view, handler, error);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        final boolean retVal = client.shouldOverrideUrlLoading(view, request);
        logger.shouldOverrideUrlLoading(view, request, retVal);
        return retVal;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        //noinspection deprecation
        final boolean retVal = client.shouldOverrideUrlLoading(view, url);
        logger.shouldOverrideUrlLoading(view, url, retVal);
        return retVal;
    }

    @Override
    public void onLoadResource(WebView view, String url) {
        logger.onLoadResource(view, url);
        client.onLoadResource(view, url);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onPageCommitVisible(WebView view, String url) {
        logger.onPageCommitVisible(view, url);
        client.onPageCommitVisible(view, url);
    }


    @Override
    @Deprecated
    public WebResourceResponse shouldInterceptRequest(final WebView view, String url) {
        //noinspection deprecation
         WebResourceResponse retVal = client.shouldInterceptRequest(view, url);
        logger.shouldInterceptRequest(view, url, retVal);
        if(isJsDebugPannelEnable()){
            if(url.contains("cdn.jsdelivr.net/npm/eruda")){
                return loadErudaFromAssert(view.getResources());
            }
        }
        return retVal;
    }

    private WebResourceResponse loadErudaFromAssert(Resources resources) {
        /*try {
            AssetManager am = resources.getAssets();
            InputStream inputStream = am.open("eruda.js");
            if (inputStream != null) {
                WebResourceResponse  response = new WebResourceResponse("application/octet-stream", "UTF-8", inputStream);
                Log.d("DebugWVClient","load eruda from assets");
                return response;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        return null;
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        WebResourceResponse retVal = client.shouldInterceptRequest(view, request);
        logger.shouldInterceptRequest(view, request, retVal);
        if(isJsDebugPannelEnable()){
            if(request.getUrl().toString().contains("cdn.jsdelivr.net/npm/eruda")){
                return loadErudaFromAssert(view.getResources());
            }
        }
        return retVal;
    }

    @Override
    @Deprecated
    @SuppressWarnings("deprecation") //for use with older versions
    public void onTooManyRedirects(WebView view, Message cancelMsg, Message continueMsg) {
        logger.onTooManyRedirects(view, cancelMsg, continueMsg);
        //noinspection deprecation
        client.onTooManyRedirects(view, cancelMsg, continueMsg);
    }

    @Override
    public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
        logger.onReceivedHttpAuthRequest(view, handler, host, realm);
        client.onReceivedHttpAuthRequest(view, handler, host, realm);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap facIcon) {
        logger.onPageStarted(view, url, facIcon);
        client.onPageStarted(view, url, facIcon);
        hasAppendJsDebugPan = false;
    }
    volatile boolean hasAppendJsDebugPan = false;
    @Override
    public void onPageFinished(WebView view, String url) {
        logger.onPageFinished(view, url);
        client.onPageFinished(view, url);
        if(jsDebugPannelEnable){
            if(!hasAppendJsDebugPan){
                hasAppendJsDebugPan = true;
                invokeJsDebugPan(view);
            }
        }


    }
    static String fuc3 = "(function () { var script = document.createElement('script'); " +
            "script.src=\"https://cdn.jsdelivr.net/npm/eruda\"; " +
            "document.body.appendChild(script); " +
            "script.onload = function () { eruda.init() } })();";
    static String fuc4 = "var script = document.createElement('script'); " +
            "script.src=\"https://cdn.jsdelivr.net/npm/eruda\"; " +
            "script.onload = function () { eruda.init() } "+
            "document.body.appendChild(script); " ;
    // "script.onload = function () { eruda.init() } ";

    /*<script src="//cdn.jsdelivr.net/npm/eruda"></script>
        <script>eruda.init();</script>*/

    public static void invokeJsDebugPan(WebView webView){
        webView.postDelayed(new Runnable() {
            @Override
            public void run() {
                String js = "javascript:"+fuc3;
                Log.w("js",js);
                webView.loadUrl(js);
            }
        },0);
    }

    public static void logInJsConsole(WebView webView,String str){
        webView.postDelayed(new Runnable() {
            @Override
            public void run() {
                String js = "javascript:console.log('"+str+"')";
                Log.w("js",js);
                webView.loadUrl(js);
            }
        },0);
    }

    //@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onReceivedClientCertRequest(WebView view, ClientCertRequest request) {
        logger.onReceivedClientCertRequest(view, request);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            client.onReceivedClientCertRequest(view, request);
        }
    }

    @Override
    public void onFormResubmission(final WebView view, final Message dontResend, final Message resend) {
        logger.onFormResubmission(view, dontResend, resend);
        client.onFormResubmission(view, dontResend, resend);
    }

    @Override
    public void doUpdateVisitedHistory(final WebView view, final String url, final boolean isReload) {
        logger.doUpdateVisitedHistory(view, url, isReload);
        client.doUpdateVisitedHistory(view, url, isReload);
    }

    @Override
    public boolean shouldOverrideKeyEvent(final WebView view, final KeyEvent event) {
        final boolean retVal = client.shouldOverrideKeyEvent(view, event);
        logger.shouldOverrideKeyEvent(view, event, retVal);
        return retVal;
    }

    //this is a valid, method but it was fully removed from the SDK in API24
    // it was added in API21.
    //@Override
    @SuppressWarnings("unused")
    public void onUnhandledInputEvent(final WebView view, final InputEvent event) {
        logger.onUnhandledInputEvent(view, event);
        onUnhandledInputEventMethodProxy.onUnhandledInputEvent(view, event);
    }

    @Override
    public void onUnhandledKeyEvent(final WebView view, final KeyEvent event) {
        logger.onUnhandledKeyEvent(view, event);
        client.onUnhandledKeyEvent(view, event);
    }

    @Override
    public void onScaleChanged(final WebView view, final float oldScale, final float newScale) {
        logger.onScaleChanged(view, oldScale, newScale);
        client.onScaleChanged(view, oldScale, newScale);
    }

    @Override
    public void onReceivedLoginRequest(final WebView view, final String realm, final String account, final String args) {
        logger.onReceivedLoginRequest(view, realm, account, args);
        client.onReceivedLoginRequest(view, realm, account, args);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onRenderProcessGone(final WebView view, final RenderProcessGoneDetail detail) {
        final boolean retVal = client.onRenderProcessGone(view, detail);
        logger.onRenderProcessGone(view, detail, retVal);
        return retVal;
    }

    @RequiresApi(api = Build.VERSION_CODES.O_MR1)
    @Override
    public void onSafeBrowsingHit(final WebView view, final WebResourceRequest request, final int threatType, final SafeBrowsingResponse callback) {
        logger.onSafeBrowsingHit(view, request, threatType, callback);
        client.onSafeBrowsingHit(view, request, threatType, callback);
    }

    @Override
    public boolean isLoggingEnabled() {
        return logger.isLoggingEnabled();
    }

    @Override
    public void setLoggingEnabled(final boolean enabled) {
        logger.setLoggingEnabled(enabled);
    }

    @Override
    public boolean isLogKeyEventsEnabled() {
        return logger.isLogKeyEventsEnabled();
    }

    @Override
    public void setLogKeyEventsEnabled(final boolean enabled) {
        logger.setLogKeyEventsEnabled(enabled);
    }
}
