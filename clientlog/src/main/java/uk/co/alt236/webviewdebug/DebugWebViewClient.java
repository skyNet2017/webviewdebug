package uk.co.alt236.webviewdebug;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
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


import java.util.Arrays;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DebugWebViewClient extends WebViewClient implements LogControl {
    private final WebViewClient client;
    private final DebugWebViewClientLogger logger;
    private final OnUnhandledInputEventMethodProxy onUnhandledInputEventMethodProxy;


    /*public static Object wrapAClassByMethodProxy(Context context,Class clazz){

        Enhancer enhancer = new Enhancer(context.getApplicationContext());
        enhancer.setSuperclass(clazz);
        //目标对象拦截器，实现MethodInterceptor
        //Object object为目标对象
        //Method method为目标方法
        //Object[] args 为参数，
        //MethodProxy proxy CGlib方法代理对象
        enhancer.setCallback(new MethodInterceptor() {
            @Override
            public Object intercept(Object object, Object[] args, MethodProxy proxy) throws Exception {
                Object obj = null;
                Log.d("MethodProxy", String.format("method name: %s, args: %s",proxy.getMethodName(),Arrays.toString(args)));
                long startTime = System.nanoTime();
                obj = proxy.invokeSuper(object, args);
                long cost = System.nanoTime() - startTime;
                if(cost > 1000000){
                    Log.d("MethodProxy", String.format("method name: %s, time cost %sms, return value: %s",
                            proxy.getMethodName(),cost/1000000,obj== null ? "null" : obj.toString()));
                }else {
                    Log.d("MethodProxy", String.format("method name: %s, time cost %sus, return value: %s",
                            proxy.getMethodName(),cost/1000,obj== null ? "null" : obj.toString()));
                }

                return obj;
            }
        });
        return enhancer.create();

    }*/

    public boolean isJsDebugPannelEnable() {
        return jsDebugPannelEnable;
    }

    public void setJsDebugPannelEnable(boolean jsDebugPannelEnable) {
        this.jsDebugPannelEnable = jsDebugPannelEnable;
    }

    private boolean jsDebugPannelEnable;
    private String userAgent;

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
        if (!new Validation().validate(client.getClass(), this.getClass())) {
            Log.e(DebugWebViewClient.class.getSimpleName(),
                    "invalid: the DebugClient does not override all methods overridden in the wrapped client");
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
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        //noinspection deprecation
         WebResourceResponse retVal = client.shouldInterceptRequest(view, url);
         if(jsDebugPannelEnable){
             if(view.getUrl().equals(url) && url.startsWith("http")){
                 if(retVal != null){
                     if(retVal.getData() != null && retVal.getMimeType().contains("text/html")){
                         Log.d(BuildConfig.DEFAULT_LOG_TAG,"encoding:"+retVal.getEncoding());
                         String html = StreamUtil.getStreamToStr(retVal.getData());
                         if(html.contains("<head>") && !html.contains("eruda.init(")){
                             String str = "<head>"+"<script src=\"https://cdnjs.cloudflare.com/ajax/libs/eruda/1.5.8/eruda.min.js\"></script>\n" +
                                     "<script>eruda.init()</script>";
                             html = html.replace("<head>",str);
                             String charset = getCharset(html);
                             retVal = new WebResourceResponse("text/html",charset,StreamUtil.getStrToStream(html,charset));
                             logger.shouldInterceptRequest(view, url, retVal);
                             return retVal;
                         }
                     }
                 }else {
                     if (okHttpClient == null) {
                         okHttpClient = buildOkClient();
                     }

                     Request.Builder okRequest = new Request.Builder()
                             .addHeader("User-Agent",userAgent)
                             .url(url);
                     okRequest.get();
                     try {
                         Response response = okHttpClient.newCall(okRequest.build()).execute();
                         String html = response.body().string();
                         if (html.contains("<head>") && !html.contains("eruda.init(")) {
                             String str = "<head>" + "<script src=\"https://cdnjs.cloudflare.com/ajax/libs/eruda/1.5.8/eruda.min.js\"></script>\n" +
                                     "<script>eruda.init()</script>";
                             html = html.replace("<head>", str);
                             String charset = getCharset(html);
                             retVal = new WebResourceResponse("text/html", charset, StreamUtil.getStrToStream(html,charset));
                             logger.shouldInterceptRequest(view, url, retVal);
                             Log.d(BuildConfig.DEFAULT_LOG_TAG, "buildOkClient: html" + html);
                             return retVal;
                         }
                     } catch (Throwable e) {
                         e.printStackTrace();
                     }
                 }
             }
         }


        logger.shouldInterceptRequest(view, url, retVal);
        return retVal;
    }

    private String getCharset(String html) {
        if(TextUtils.isEmpty(html)){
            return "UTF-8";
        }
        if(html.contains("charset=gbk")){
            return "gbk";
        }
        return "UTF-8";
    }

    OkHttpClient okHttpClient;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        WebResourceResponse retVal = client.shouldInterceptRequest(view, request);
        if(jsDebugPannelEnable){
            if(request.isForMainFrame() && request.getUrl().toString().startsWith("http")){
                if(retVal != null){
                    if(retVal.getStatusCode() == 200 && retVal.getMimeType().contains("text/html")){
                        Log.d(BuildConfig.DEFAULT_LOG_TAG,"encoding:"+retVal.getEncoding());
                        String html = StreamUtil.getStreamToStr(retVal.getData());
                        if(html.contains("<head>") && !html.contains("eruda.init(")){
                            String str = "<head>"+"<script src=\"https://cdnjs.cloudflare.com/ajax/libs/eruda/1.5.8/eruda.min.js\"></script>\n" +
                                    "<script>eruda.init()</script>";
                            html = html.replace("<head>",str);
                            String charset = getCharset(html);
                            retVal = new WebResourceResponse("text/html",charset,StreamUtil.getStrToStream(html,charset));
                            logger.shouldInterceptRequest(view, request, retVal);
                            return retVal;
                        }
                    }
                }else {
                    if(okHttpClient == null){
                        okHttpClient = buildOkClient();
                    }
                    Map<String,String> headers = request.getRequestHeaders();
                    Request.Builder okRequest = new Request.Builder()
                            .url(request.getUrl().toString());
                    if(!headers.isEmpty()){
                        for (Map.Entry<String, String> entry :
                                headers.entrySet()) {
                            okRequest.addHeader(entry.getKey(), entry.getValue());
                        }
                    }
                    if("GET".equals(request.getMethod())){
                        okRequest.get();
                        try {
                            Response response =okHttpClient.newCall(okRequest.build()).execute();
                            String html = response.body().string();
                            if(html.contains("<head>")&& !html.contains("eruda.init(")){
                                String str = "<head>"+"<script src=\"https://cdnjs.cloudflare.com/ajax/libs/eruda/1.5.8/eruda.min.js\"></script>\n" +
                                        "<script>eruda.init()</script>";
                                html = html.replace("<head>",str);
                                String charset = getCharset(html);
                                retVal = new WebResourceResponse("text/html",charset,StreamUtil.getStrToStream(html,charset));
                                logger.shouldInterceptRequest(view, request, retVal);
                                Log.d(BuildConfig.DEFAULT_LOG_TAG,"buildOkClient: html"+html);
                                return retVal;
                            }
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        logger.shouldInterceptRequest(view, request, retVal);
        return retVal;
    }

    private OkHttpClient buildOkClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        userAgent = System.getProperty("http.agent");
        return builder.build();
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
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        logger.onPageFinished(view, url);
        client.onPageFinished(view, url);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onReceivedClientCertRequest(WebView view, ClientCertRequest request) {
        logger.onReceivedClientCertRequest(view, request);
        client.onReceivedClientCertRequest(view, request);
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
