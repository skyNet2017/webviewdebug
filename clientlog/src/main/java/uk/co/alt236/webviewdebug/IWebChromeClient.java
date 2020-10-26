package uk.co.alt236.webviewdebug;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Message;
import android.support.annotation.Nullable;
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

public interface IWebChromeClient {

    
    default void onProgressChanged(WebView view, int newProgress) {}

    
    default void onReceivedTitle(WebView view, String title) {}

    
    default void onReceivedIcon(WebView view, Bitmap icon) {}

    
    default void onReceivedTouchIconUrl(WebView view, String url,
                                       boolean precomposed) {}

    
    default void onShowCustomView(View view, WebChromeClient.CustomViewCallback callback) {};

    
    @Deprecated
    default void onShowCustomView(View view, int requestedOrientation,
                                 WebChromeClient.CustomViewCallback callback) {};

    
    default void onHideCustomView() {}

    
    default boolean onCreateWindow(WebView view, boolean isDialog,
                                  boolean isUserGesture, Message resultMsg) {
        return false;
    }

    
    default void onRequestFocus(WebView view) {}

    
    default void onCloseWindow(WebView window) {}

    
    default boolean onJsAlert(WebView view, String url, String message,
                             JsResult result) {
        return false;
    }

    
    default boolean onJsConfirm(WebView view, String url, String message,
                               JsResult result) {
        return false;
    }

    
    default boolean onJsPrompt(WebView view, String url, String message,
                              String defaultValue, JsPromptResult result) {
        return false;
    }

    
    default boolean onJsBeforeUnload(WebView view, String url, String message,
                                    JsResult result) {
        return false;
    }

    
    @Deprecated
    default void onExceededDatabaseQuota(String url, String databaseIdentifier,
                                        long quota, long estimatedDatabaseSize, long totalQuota,
                                        WebStorage.QuotaUpdater quotaUpdater) {
        // This default implementation passes the current quota back to WebCore.
        // WebCore will interpret this that new quota was declined.

    }

    
    @Deprecated
    default void onReachedMaxAppCacheSize(long requiredStorage, long quota,
                                         WebStorage.QuotaUpdater quotaUpdater) {

    }

    
    default void onGeolocationPermissionsShowPrompt(String origin,
                                                   GeolocationPermissions.Callback callback) {}

    
    default void onGeolocationPermissionsHidePrompt() {}

    
    default void onPermissionRequest(PermissionRequest request) {

    }

    
    default void onPermissionRequestCanceled(PermissionRequest request) {}

    
    @Deprecated
    default boolean onJsTimeout() {
        return true;
    }

    
    @Deprecated
    default void onConsoleMessage(String message, int lineNumber, String sourceID) { }

    
    default boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        // Call the old version of this function for backwards compatability.

        return false;
    }

    
    @Nullable
    default Bitmap getDefaultVideoPoster() {
        return null;
    }

    
    @Nullable
    default View getVideoLoadingProgressView() {
        return null;
    }

    
    default void getVisitedHistory(ValueCallback<String[]> callback) {
    }

    
    default boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback,
                                     WebChromeClient.FileChooserParams fileChooserParams) {
        return false;
    }



    //
    @Deprecated
    default void openFileChooser(ValueCallback<Uri> uploadFile, String acceptType, String capture) {

    }

}
