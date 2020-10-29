package uk.co.alt236.webviewdebug;

import android.content.Intent;
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

/**
 * 用于测试子类可实现的方法和其版本兼容性
 */
 class MyChromeClient extends WebChromeClient {
    @Override
    public void onProgressChanged(WebView view, int newProgress) {}

    @Override
    public void onReceivedTitle(WebView view, String title) {}

    @Override
    public void onReceivedIcon(WebView view, Bitmap icon) {}

    @Override
    public void onReceivedTouchIconUrl(WebView view, String url,
                                       boolean precomposed) {}

    @Override
    public void onShowCustomView(View view, WebChromeClient.CustomViewCallback callback) {};

    @Override
    @Deprecated
    public void onShowCustomView(View view, int requestedOrientation,
                                 WebChromeClient.CustomViewCallback callback) {};

    @Override
    public void onHideCustomView() {}

    @Override
    public boolean onCreateWindow(WebView view, boolean isDialog,
                                  boolean isUserGesture, Message resultMsg) {
        return false;
    }

    @Override
    public void onRequestFocus(WebView view) {}

    @Override
    public void onCloseWindow(WebView window) {}

    @Override
    public boolean onJsAlert(WebView view, String url, String message,
                             JsResult result) {
        return false;
    }

    @Override
    public boolean onJsConfirm(WebView view, String url, String message,
                               JsResult result) {
        return false;
    }

    @Override
    public boolean onJsPrompt(WebView view, String url, String message,
                              String defaultValue, JsPromptResult result) {
        return false;
    }

    @Override
    public boolean onJsBeforeUnload(WebView view, String url, String message,
                                    JsResult result) {
        return false;
    }

    @Override
    @Deprecated
    public void onExceededDatabaseQuota(String url, String databaseIdentifier,
                                        long quota, long estimatedDatabaseSize, long totalQuota,
                                        WebStorage.QuotaUpdater quotaUpdater) {
        // This default implementation passes the current quota back to WebCore.
        // WebCore will interpret this that new quota was declined.

    }

    @Override
    @Deprecated
    public void onReachedMaxAppCacheSize(long requiredStorage, long quota,
                                         WebStorage.QuotaUpdater quotaUpdater) {

    }

    @Override
    public void onGeolocationPermissionsShowPrompt(String origin,
                                                   GeolocationPermissions.Callback callback) {}

    @Override
    public void onGeolocationPermissionsHidePrompt() {}

    @Override
    public void onPermissionRequest(PermissionRequest request) {

    }

    @Override
    public void onPermissionRequestCanceled(PermissionRequest request) {}

    @Override
    @Deprecated
    public boolean onJsTimeout() {
        return true;
    }

    @Override
    @Deprecated
    public void onConsoleMessage(String message, int lineNumber, String sourceID) { }

    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        // Call the old version of this function for backwards compatability.

        return false;
    }

    @Override
    @Nullable
    public Bitmap getDefaultVideoPoster() {
        return null;
    }

    @Override
    @Nullable
    public View getVideoLoadingProgressView() {
        return null;
    }

    @Override
    public void getVisitedHistory(ValueCallback<String[]> callback) {
    }

    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback,
                                     WebChromeClient.FileChooserParams fileChooserParams) {
        return false;
    }



    //@Override
    @Deprecated
    public void openFileChooser(ValueCallback<Uri> uploadFile, String acceptType, String capture) {

    }


}
