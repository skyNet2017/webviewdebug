package uk.co.alt236.webviewdebug;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Message;
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

    default boolean shouldOverrideUrlLoadingAll(WebView view, String newurl) {
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    default void onReceivedError(final WebView view, final WebResourceRequest request, final WebResourceError error) {

    }

    default void onReceivedErrorAll(WebView view, int errorCode, String description,
                            String failingUrl){}

    default void onReceivedError(final WebView view, final int errorCode, final String description, final String failingUrl) {

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    default void onReceivedHttpError(final WebView view, final WebResourceRequest request, final WebResourceResponse errorResponse) {

    }

    
    default void onReceivedSslError(final WebView view, final SslErrorHandler handler, final SslError error) {

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    default boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

        return false;
    }

    
    default boolean shouldOverrideUrlLoading(WebView view, String url) {
        return false;
    }

    
    default void onLoadResource(WebView view, String url) {

    }

    @TargetApi(Build.VERSION_CODES.M)
    default void onPageCommitVisible(WebView view, String url) {

    }


    
    @Deprecated
    default WebResourceResponse shouldInterceptRequest(final WebView view, String url) {
        return null;
    }





    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    default WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {

        return null;
    }



    
    @Deprecated
    @SuppressWarnings("deprecation") //for use with older versions
    default void onTooManyRedirects(WebView view, Message cancelMsg, Message continueMsg) {

    }

    
    default void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {

    }

    
    default void onPageStarted(WebView view, String url, Bitmap facIcon) {

    }

    
    default void onPageFinished(WebView view, String url) {

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    
    default void onReceivedClientCertRequest(WebView view, ClientCertRequest request) {

    }

    
    default void onFormResubmission(final WebView view, final Message dontResend, final Message resend) {

    }

    
    default void doUpdateVisitedHistory(final WebView view, final String url, final boolean isReload) {

    }

    
    default boolean shouldOverrideKeyEvent(final WebView view, final KeyEvent event) {

        return false;
    }

    //this is a valid, method but it was fully removed from the SDK in API24
    // it was added in API21.
    //
    @SuppressWarnings("unused")
    default void onUnhandledInputEvent(final WebView view, final InputEvent event) {

    }

    
    default void onUnhandledKeyEvent(final WebView view, final KeyEvent event) {

    }

    
    default void onScaleChanged(final WebView view, final float oldScale, final float newScale) {

    }

    
    default void onReceivedLoginRequest(final WebView view, final String realm, final String account, final String args) {

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    
    default boolean onRenderProcessGone(final WebView view, final RenderProcessGoneDetail detail) {

        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.O_MR1)
    
    default void onSafeBrowsingHit(final WebView view, final WebResourceRequest request, final int threatType, final SafeBrowsingResponse callback) {

    }

    


    
    default boolean isLogKeyEventsEnabled() {
        return false;
    }

    
    default void setLogKeyEventsEnabled(final boolean enabled) {

    }
}
