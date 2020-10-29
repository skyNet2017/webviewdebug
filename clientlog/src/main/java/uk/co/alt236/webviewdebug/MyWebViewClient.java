package uk.co.alt236.webviewdebug;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Message;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
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


/**
 * 用于测试子类可实现的方法和其版本兼容性
 */
 class MyWebViewClient extends WebViewClient {

    @Override
    @Deprecated
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return false;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        return false;
    }



    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
    }

    @Override
    public void onPageFinished(WebView view, String url) {
    }

    @Override
    public void onLoadResource(WebView view, String url) {
    }

    @Override
    public void onPageCommitVisible(WebView view, String url) {
    }

    @Override
    @Deprecated
    @Nullable
    public WebResourceResponse shouldInterceptRequest(WebView view,
                                                      String url) {
        return null;
    }

    @Override
    @Nullable
    public WebResourceResponse shouldInterceptRequest(WebView view,
                                                      WebResourceRequest request) {
        return null;
    }

    @Override
    @Deprecated
    public void onTooManyRedirects(WebView view, Message cancelMsg,
                                   Message continueMsg) {

    }



    @Override
    @Deprecated
    public void onReceivedError(WebView view, int errorCode,
                                String description, String failingUrl) {
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {

    }

    @Override
    public void onReceivedHttpError(
            WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
    }

    @Override
    public void onFormResubmission(WebView view, Message dontResend,
                                   Message resend) {

    }

    @Override
    public void doUpdateVisitedHistory(WebView view, String url,
                                       boolean isReload) {
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler,
                                   SslError error) {

    }

    @Override
    public void onReceivedClientCertRequest(WebView view, ClientCertRequest request) {

    }

    @Override
    public void onReceivedHttpAuthRequest(WebView view,
                                          HttpAuthHandler handler, String host, String realm) {

    }

    public void  onProceededAfterSslError(WebView view,android.net.http.SslError sslError){

    }

    public void onReceivedClientCertRequest(android.webkit.WebView webView,Object requestHandler,java.lang.String url){
        //android.webkit.ClientCertRequestHandler

    }

    @Override
    public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
        return false;
    }

    @Override
    public void onUnhandledKeyEvent(WebView view, KeyEvent event) {

    }

    public void onUnhandledInputEvent(WebView view,InputEvent inputEvent){

    }



    @Override
    public void onScaleChanged(WebView view, float oldScale, float newScale) {
    }

    @Override
    public void onReceivedLoginRequest(WebView view, String realm,
                                       @Nullable String account, String args) {
    }

    @Override
    public boolean onRenderProcessGone(WebView view, RenderProcessGoneDetail detail) {
        return false;
    }

    @Override
    public void onSafeBrowsingHit(WebView view, WebResourceRequest request,
                                   int threatType, SafeBrowsingResponse callback) {

    }

}
