package uk.co.alt236.webviewdebug;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.GeolocationPermissions;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebStorage;
import android.webkit.WebView;

public class DebugWebChromeClient extends WebChromeClient implements LogControl {
    private final WebChromeClient client;
    private final DebugWebChromeClientLogger logger;


    public DebugWebChromeClient() {
        this(new WebChromeClient());
    }

    public DebugWebChromeClient(@NonNull final WebChromeClient client) {
        this(client, new DebugWebChromeClientLogger());
    }

    @SuppressWarnings("WeakerAccess")
    public DebugWebChromeClient(
            @NonNull final WebChromeClient client,
            @NonNull final DebugWebChromeClientLogger logger) {
        this.logger = logger;
        this.client = client;
        validate();
    }

    private void validate() {
        try{
            if (!new Validation().validateChrome(client.getClass(), this.getClass())) {
            Log.e(DebugWebChromeClient.class.getSimpleName(),
                    "invalid: the DebugClient does not override all methods overridden in the wrapped client");
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
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



    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        logger.onProgressChanged(view, newProgress );
        client.onProgressChanged(view, newProgress);
    }


    @Override
    public void onReceivedTitle(WebView view, String title) {
        logger.onReceivedTitle(view, title );
        client.onReceivedTitle(view, title);
    }


    @Override
    public void onReceivedIcon(WebView view, Bitmap icon) {
        logger.onReceivedIcon(view, icon );
        client.onReceivedIcon(view, icon);
    }


    @Override
    public void onReceivedTouchIconUrl(WebView view, String url,
                                       boolean precomposed) {
        logger.onReceivedTouchIconUrl(view, url,precomposed);
        client.onReceivedTouchIconUrl(view, url,precomposed);
    }




    @Override
    public void onShowCustomView(View view, WebChromeClient.CustomViewCallback callback) {
        logger.onShowCustomView(view, callback);
        client.onShowCustomView(view, callback);
    }


    @Override
    @Deprecated
    public void onShowCustomView(View view, int requestedOrientation,
                                 WebChromeClient.CustomViewCallback callback) {
        logger.onShowCustomView(view,requestedOrientation, callback);
        client.onShowCustomView(view,requestedOrientation, callback);
    }


    @Override
    public void onHideCustomView() {
        logger.onHideCustomView();
        client.onHideCustomView();
    }


    @Override
    public boolean onCreateWindow(WebView view, boolean isDialog,
                                  boolean isUserGesture, Message resultMsg) {
        boolean retVal = client.onCreateWindow(view,isDialog,isUserGesture,resultMsg);
        logger.onCreateWindow(view,isDialog,isUserGesture,resultMsg,retVal);
        return retVal;
    }


    @Override
    public void onRequestFocus(WebView view) {
        logger.onRequestFocus(view);
        client.onRequestFocus(view);
    }


    @Override
    public void onCloseWindow(WebView window) {
        logger.onCloseWindow(window);
        client.onCloseWindow(window);
    }


    @Override
    public boolean onJsAlert(WebView view, String url, String message,
                             JsResult result) {
        boolean retVal = client.onJsAlert(view,url,message,result);
        logger.onJsAlert(view,url,message,result,retVal);
        return retVal;
    }


    @Override
    public boolean onJsConfirm(WebView view, String url, String message,
                               JsResult result) {
        boolean retVal = client.onJsConfirm(view,url,message,result);
        logger.onJsConfirm(view,url,message,result,retVal);
        return retVal;

    }


    @Override
    public boolean onJsPrompt(WebView view, String url, String message,
                              String defaultValue, JsPromptResult result) {
        boolean retVal = client.onJsPrompt(view,url,message,defaultValue,result);
        logger.onJsPrompt(view,url,message,defaultValue,result,retVal);
        return retVal;
    }


    @Override
    public boolean onJsBeforeUnload(WebView view, String url, String message,
                                    JsResult result) {
        boolean retVal = client.onJsBeforeUnload(view,url,message,result);
        logger.onJsBeforeUnload(view,url,message,result,retVal);
        return retVal;
    }


    @Override
    @Deprecated
    public void onExceededDatabaseQuota(String url, String databaseIdentifier,
                                        long quota, long estimatedDatabaseSize, long totalQuota,
                                        WebStorage.QuotaUpdater quotaUpdater) {
        logger.onExceededDatabaseQuota(url,databaseIdentifier,quota,estimatedDatabaseSize,totalQuota,quotaUpdater);
        client.onExceededDatabaseQuota(url,databaseIdentifier,quota,estimatedDatabaseSize,totalQuota,quotaUpdater);
    }


    @Override
    @Deprecated
    public void onReachedMaxAppCacheSize(long requiredStorage, long quota,
                                         WebStorage.QuotaUpdater quotaUpdater) {
        logger.onReachedMaxAppCacheSize(requiredStorage,quota,quotaUpdater);
        client.onReachedMaxAppCacheSize(requiredStorage,quota,quotaUpdater);
    }


    @Override
    public void onGeolocationPermissionsShowPrompt(String origin,
                                                   GeolocationPermissions.Callback callback) {
        logger.onGeolocationPermissionsShowPrompt(origin,callback);
        client.onGeolocationPermissionsShowPrompt(origin,callback);
    }


    @Override
    public void onGeolocationPermissionsHidePrompt() {
        logger.onGeolocationPermissionsHidePrompt();
        client.onGeolocationPermissionsHidePrompt();
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onPermissionRequest(PermissionRequest request) {
        logger.onPermissionRequest(request);
        client.onPermissionRequest(request);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onPermissionRequestCanceled(PermissionRequest request) {
        logger.onPermissionRequestCanceled(request);
        client.onPermissionRequestCanceled(request);
    }


    @Override
    @Deprecated
    public boolean onJsTimeout() {
        boolean retVal = client.onJsTimeout();
        logger.onJsTimeout(retVal);
        return retVal;
    }


    @Deprecated
    public void onConsoleMessage(String message, int lineNumber, String sourceID) {
        logger.onConsoleMessage(message,lineNumber,sourceID);
        client.onConsoleMessage(message,lineNumber,sourceID);
    }


    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        boolean retVal = client.onConsoleMessage(consoleMessage);
        logger.onConsoleMessage(consoleMessage,retVal);
        return retVal;
    }


    @Override
    @Nullable
    public Bitmap getDefaultVideoPoster() {
        Bitmap retVal = client.getDefaultVideoPoster();
        logger.getDefaultVideoPoster(retVal);
        return retVal;
    }

    /**
     * Obtains a View to be displayed while buffering of full screen video is taking
     * place. The host application can override this method to provide a View
     * containing a spinner or similar.
     *
     * @return View The View to be displayed whilst the video is loading.
     */
    @Override
    @Nullable
    public View getVideoLoadingProgressView() {
        View retVal = client.getVideoLoadingProgressView();
        logger.getVideoLoadingProgressView(retVal);
        return retVal;
    }

    /** Obtains a list of all visited history items, used for link coloring
     */
    @Override
    public void getVisitedHistory(ValueCallback<String[]> callback) {
        logger.getVisitedHistory(callback);
        client.getVisitedHistory(callback);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback,
                                     FileChooserParams fileChooserParams) {
        boolean retVal = client.onShowFileChooser(webView,filePathCallback,fileChooserParams);
        logger.onShowFileChooser(webView,filePathCallback,fileChooserParams,retVal);
        return retVal;

    }




}
