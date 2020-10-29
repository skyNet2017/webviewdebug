package uk.co.alt236.webviewdebug;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
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

public interface IWebviewClient {

    
    @Deprecated
    default boolean shouldOverrideUrlLoading(WebView view, String url) {
        return false;
    }

    
    default boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        return false;
    }



    
    default void onPageStarted(WebView view, String url, Bitmap favicon) {
    }

    
    default void onPageFinished(WebView view, String url) {
    }

    
    default void onLoadResource(WebView view, String url) {
    }

    
    default void onPageCommitVisible(WebView view, String url) {
    }

    
    @Deprecated
    @Nullable
    default WebResourceResponse shouldInterceptRequest(WebView view,
                                                      String url) {
        return null;
    }

    
    @Nullable
    default WebResourceResponse shouldInterceptRequest(WebView view,
                                                      WebResourceRequest request) {
        return null;
    }

    
    @Deprecated
    default void onTooManyRedirects(WebView view, Message cancelMsg,
                                   Message continueMsg) {

    }



    
    @Deprecated
    default void onReceivedError(WebView view, int errorCode,
                                String description, String failingUrl) {
    }

    
    default void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {

    }

    
    default void onReceivedHttpError(
            WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
    }

    
    default void onFormResubmission(WebView view, Message dontResend,
                                   Message resend) {

    }

    
    default void doUpdateVisitedHistory(WebView view, String url,
                                       boolean isReload) {
    }

    
    default void onReceivedSslError(WebView view, SslErrorHandler handler,
                                   SslError error) {

    }

    
    default void onReceivedClientCertRequest(WebView view, ClientCertRequest request) {

    }

    
    default void onReceivedHttpAuthRequest(WebView view,
                                          HttpAuthHandler handler, String host, String realm) {

    }

    default void  onProceededAfterSslError(WebView view,android.net.http.SslError sslError){

    }

    default void onReceivedClientCertRequest(android.webkit.WebView webView,Object requestHandler,java.lang.String url){
        //android.webkit.ClientCertRequestHandler

    }

    
    default boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
        return false;
    }

    
    default void onUnhandledKeyEvent(WebView view, KeyEvent event) {

    }

    default void onUnhandledInputEvent(WebView view,InputEvent inputEvent){

    }



    
    default void onScaleChanged(WebView view, float oldScale, float newScale) {
    }

    
    default void onReceivedLoginRequest(WebView view, String realm,
                                       @Nullable String account, String args) {
    }

    
    default boolean onRenderProcessGone(WebView view, RenderProcessGoneDetail detail) {
        return false;
    }

    
    default void onSafeBrowsingHit(WebView view, WebResourceRequest request,
                                  int threatType, SafeBrowsingResponse callback) {

    }
   
}
