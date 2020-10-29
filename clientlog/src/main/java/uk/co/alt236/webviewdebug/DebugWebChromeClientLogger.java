package uk.co.alt236.webviewdebug;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
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

import java.text.DecimalFormat;
import java.util.Locale;

@SuppressWarnings("WeakerAccess")
public class DebugWebChromeClientLogger implements LogControl {
    private static final Locale LOCALE = Locale.US;
    private static final String IN = "--->";
    private static final String OUT = "<---";
    private static final String SPACE = "    ";
    private static final String DEFAULT_TAG = "DebugWVClient-chrome";

    private final LogEngine logger;
    private boolean loggingEnabled;
    private boolean logKeyEventsEnabled;

    public DebugWebChromeClientLogger() {
        this(DEFAULT_TAG);
    }

    public DebugWebChromeClientLogger(@NonNull final String tag) {
        this(new LogEngine(tag));
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    protected DebugWebChromeClientLogger(@NonNull final LogEngine logEngine) {
        this.logger = logEngine;
    }

    
    @Override
    public boolean isLoggingEnabled() {
        return loggingEnabled;
    }

    
    @Override
    public void setLoggingEnabled(final boolean enabled) {
        this.loggingEnabled = enabled;
    }

    
    @Override
    public boolean isLogKeyEventsEnabled() {
        return logKeyEventsEnabled;
    }

    
    @Override
    public void setLogKeyEventsEnabled(final boolean enabled) {
        this.logKeyEventsEnabled = enabled;
    }

    
    public void onProgressChanged(WebView view, int newProgress) {
       if(!loggingEnabled){
           return;
       }
        logger.log(String.format(LOCALE, "%s onProgressChanged()        : %s  -->from url: %s", SPACE, newProgress+"", view.getUrl()));
    }


    
    public void onReceivedTitle(WebView view, String title) {
        if(!loggingEnabled){
            return;
        }
        logger.log(String.format(LOCALE, "%s onReceivedTitle()        : %s  -->from url: %s", SPACE, title, view.getUrl()));
    }


    
    public void onReceivedIcon(WebView view, Bitmap icon) {
        if(!loggingEnabled){
            return;
        }
        int size = 0;
        if(icon != null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                size = icon.getAllocationByteCount();
            }else {
                size = icon.getRowBytes() * icon.getHeight();
            }
        }
        if(size > 50*1024){
            logger.logError(String.format(LOCALE, "%s onReceivedIcon()        : %s  -->by from: %s", SPACE, formatFileSize(size), view.getUrl()));
        }else {
            logger.log(String.format(LOCALE, "%s onReceivedIcon()        : %s  -->from url: %s", SPACE, formatFileSize(size), view.getUrl()));
        }

    }

     static String formatFileSize(long size) {
        try {
            DecimalFormat dff = new DecimalFormat(".00");
            if (size >= 1024 * 1024) {
                double doubleValue = ((double) size) / (1024 * 1024);
                String value = dff.format(doubleValue);
                return value + "MB";
            } else if (size > 1024) {
                double doubleValue = ((double) size) / 1024;
                String value = dff.format(doubleValue);
                return value + "KB";
            } else {
                return size + "B";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.valueOf(size);
    }

    static String toStr(Object object){
        if(object == null){
            return "null";
        }
        return object.toString();
    }

    
    public void onReceivedTouchIconUrl(WebView view, String url,
                                       boolean precomposed) {
        if(!loggingEnabled){
            return;
        }
        logger.log(String.format(LOCALE, "%s onReceivedTouchIconUrl()        : %s, %s  -->from url: %s", SPACE, precomposed+"",url, view.getUrl()));
    }




    
    public void onShowCustomView(View view, WebChromeClient.CustomViewCallback callback) {
        if(!loggingEnabled){
            return;
        }
        logger.log(String.format(LOCALE, "%s onShowCustomView()        : view: %s  -->callback %s", SPACE, toStr(view), toStr(callback)));
    }


    
    @Deprecated
    public void onShowCustomView(View view, int requestedOrientation,
                                 WebChromeClient.CustomViewCallback callback) {
        if(!loggingEnabled){
            return;
        }
        logger.log(String.format(LOCALE, "%s onShowCustomView()        : view: %s , requestedOrientation:%s, callback %s",
                SPACE, toStr(view),requestedOrientation+"", toStr(callback)));
    }


    
    public void onHideCustomView() {
        if(!loggingEnabled){
            return;
        }
        logger.log(String.format(LOCALE, "%s onHideCustomView()        : ",
                SPACE));
    }


    
    public boolean onCreateWindow(WebView view, boolean isDialog,
                                  boolean isUserGesture, Message resultMsg, boolean retVal) {
        if(!loggingEnabled){
            return retVal;
        }
        logger.log(String.format(LOCALE, "%s onCreateWindow()        : handled: %s, isDialog: %s , isUserGesture:%s, resultMsg: %s, -->from url: %s",
                SPACE,retVal+"", isDialog+"",isUserGesture+"",resultMsg,view.getUrl()));
        return retVal;
    }


    
    public void onRequestFocus(WebView view) {
        if(!loggingEnabled){
            return;
        }
        logger.log(String.format(LOCALE, "%s onRequestFocus()        : from url: %s", SPACE,view.getUrl()));
    }


    
    public void onCloseWindow(WebView window) {
        if(!loggingEnabled){
            return;
        }
        logger.log(String.format(LOCALE, "%s onCloseWindow()        : from url: %s", SPACE,window.getUrl()));
    }


    
    public boolean onJsAlert(WebView view, String url, String message,
                             JsResult result, boolean retVal) {
        if(!loggingEnabled){
            return retVal;
        }
        logger.logWarn(String.format(LOCALE, "%s onJsAlert()        : handled: %s, url: %s , message:%s, result: %s, -->from url: %s",
                SPACE,retVal+"", url+"",message+"",toStr(result),view.getUrl()));
        return retVal;
    }


    
    public boolean onJsConfirm(WebView view, String url, String message,
                               JsResult result, boolean retVal) {
        if(!loggingEnabled){
            return retVal;
        }
        logger.log(String.format(LOCALE, "%s onJsConfirm()        : handled: %s, url: %s , message:%s, result: %s, -->from url: %s",
                SPACE,retVal+"", url+"",message+"",toStr(result),view.getUrl()));
        return retVal;
    }


    
    public boolean onJsPrompt(WebView view, String url, String message,
                              String defaultValue, JsPromptResult result, boolean retVal) {
        if(!loggingEnabled){
            return retVal;
        }
        logger.log(String.format(LOCALE, "%s onJsPrompt()        : handled: %s, url: %s , message:%s, defaultValue: %s, result: %s, -->from url: %s",
                SPACE,retVal+"", url+"",message+"",defaultValue,toStr(result),view.getUrl()));
        return retVal;
    }


    
    public boolean onJsBeforeUnload(WebView view, String url, String message,
                                    JsResult result, boolean retVal) {
        if(!loggingEnabled){
            return retVal;
        }
        logger.log(String.format(LOCALE, "%s onJsBeforeUnload()        : handled: %s, url: %s , message:%s, result: %s, -->from url: %s",
                SPACE,retVal+"", url+"",message+"",toStr(result),view.getUrl()));
        return retVal;
    }


    /**
     * 超出数据库配额
     * @param url
     * @param databaseIdentifier
     * @param quota
     * @param estimatedDatabaseSize
     * @param totalQuota
     * @param quotaUpdater
     */
    @Deprecated
    public void onExceededDatabaseQuota(String url, String databaseIdentifier,
                                        long quota, long estimatedDatabaseSize, long totalQuota,
                                        WebStorage.QuotaUpdater quotaUpdater) {
        if(!loggingEnabled){
            return;
        }
        logger.logError(String.format(LOCALE, "%s onExceededDatabaseQuota()        : databaseIdentifier: %s , quota:%s, estimatedDatabaseSize %s," +
                        "  totalQuota:%s,  quotaUpdater: %s" +
                        " -->from url: %s",
                SPACE,databaseIdentifier+"",quota+"",estimatedDatabaseSize+"" ,totalQuota+"",toStr(quotaUpdater),url));
    }


    
    @Deprecated
    public void onReachedMaxAppCacheSize(long requiredStorage, long quota,
                                         WebStorage.QuotaUpdater quotaUpdater) {
        if(!loggingEnabled){
            return;
        }
        logger.logError(String.format(LOCALE, "%s onReachedMaxAppCacheSize()        : requiredStorage: %s , quota:%s, quotaUpdater %s",
                SPACE,requiredStorage+"",quota+"", toStr(quotaUpdater)));
    }


    
    public void onGeolocationPermissionsShowPrompt(String origin,
                                                   GeolocationPermissions.Callback callback) {
        if(!loggingEnabled){
            return;
        }
        logger.log(String.format(LOCALE, "%s onGeolocationPermissionsShowPrompt()        : origin: %s , callback:%s",
                SPACE,origin, toStr(callback)));
    }


    
    public void onGeolocationPermissionsHidePrompt() {
        if(!loggingEnabled){
            return;
        }
        logger.log(String.format(LOCALE, "%s onGeolocationPermissionsHidePrompt()        : ",
                SPACE));
    }


    
    public void onPermissionRequest(PermissionRequest request) {
        if(!loggingEnabled){
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            logger.log(String.format(LOCALE, "%s onGeolocationPermissionsHidePrompt()        : getResources: %s, getOrigin : %s",
                    SPACE,request.getResources(),request.getOrigin()));
        }else {
            logger.log(String.format(LOCALE, "%s onGeolocationPermissionsHidePrompt()        : request: %s",
                    SPACE,toStr(request)));
        }
    }


    
    public void onPermissionRequestCanceled(PermissionRequest request) {
        if(!loggingEnabled){
            return;
        }
        logger.logWarn(String.format(LOCALE, "%s onPermissionRequestCanceled()        : request: %s",
                SPACE,toStr(request)));
    }


    
    @Deprecated
    public boolean onJsTimeout(boolean retVal) {
        if(!loggingEnabled){
            return retVal;
        }
        logger.logError(String.format(LOCALE, "%s onJsTimeout()        : handled: %s",
                SPACE,retVal+""));
        return retVal;
    }


    @Deprecated
    public void onConsoleMessage(String message, int lineNumber, String sourceID) {
        if(!loggingEnabled){
            return;
        }
        logger.log(String.format(LOCALE, "%s onConsoleMessage()        : message: %s, lineNumber: %s , sourceID:%s",
                SPACE,message, lineNumber+"",sourceID));
    }


    
    public boolean onConsoleMessage(ConsoleMessage consoleMessage, boolean retVal) {

        if(!loggingEnabled){
            return retVal;
        }
        if(ConsoleMessage.MessageLevel.WARNING.equals(consoleMessage.messageLevel())){
            logger.logWarn(String.format(LOCALE, "%s onConsoleMessage()        : handled: %s, message: %s, lineNumber: %s , sourceID:%s ",
                    SPACE,retVal+"", consoleMessage.message(),consoleMessage.lineNumber()+"",consoleMessage.sourceId()+""));
        }else if(ConsoleMessage.MessageLevel.ERROR.equals(consoleMessage.messageLevel())){
            logger.logError(String.format(LOCALE, "%s onConsoleMessage()        : handled: %s, message: %s, lineNumber: %s , sourceID:%s ",
                    SPACE,retVal+"", consoleMessage.message(),consoleMessage.lineNumber()+"",consoleMessage.sourceId()+""));
        }else if(ConsoleMessage.MessageLevel.DEBUG.equals(consoleMessage.messageLevel())){
            logger.logDebug(String.format(LOCALE, "%s onConsoleMessage()        : handled: %s, message: %s, lineNumber: %s , sourceID:%s ",
                    SPACE,retVal+"", consoleMessage.message(),consoleMessage.lineNumber()+"",consoleMessage.sourceId()+""));
        }else {
            logger.log(String.format(LOCALE, "%s onConsoleMessage()        : handled: %s, message: %s, lineNumber: %s , sourceID:%s ",
                    SPACE,retVal+"", consoleMessage.message(),consoleMessage.lineNumber()+"",consoleMessage.sourceId()+""));
        }

        return retVal;
    }


    
    @Nullable
    public Bitmap getDefaultVideoPoster(Bitmap icon) {
        if(!loggingEnabled){
            return icon;
        }
        int size = 0;
        if(icon != null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                size = icon.getAllocationByteCount();
            }else {
                size = icon.getRowBytes() * icon.getHeight();
            }
        }
        logger.log(String.format(LOCALE, "%s getDefaultVideoPoster()        : %s ", SPACE, formatFileSize(size)));
        return icon;
    }


    @Nullable
    public View getVideoLoadingProgressView(View retVal) {
        if(!loggingEnabled){
            return retVal;
        }
        return retVal;
    }

    /** Obtains a list of all visited history items, used for link coloring
     */
    
    public void getVisitedHistory(ValueCallback<String[]> callback) {
        if(!loggingEnabled){
            return;
        }
        logger.log(String.format(LOCALE, "%s getVisitedHistory()        : %s ", SPACE, toStr(callback)));
    }


    
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback,
                                     WebChromeClient.FileChooserParams fileChooserParams, boolean retVal) {
        if(!loggingEnabled){
            return retVal;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            logger.log(String.format(LOCALE, "%s onShowFileChooser()        : handled: %s, filePathCallback: %s , " +
                            "getMode:%s" +
                            "getAcceptTypes:%s" +
                            "isCaptureEnabled:%s" +
                            "getTitle:%s" +
                            "getFilenameHint:%s" +
                            " -->from url: %s",
                    SPACE,retVal+"", toStr(filePathCallback),
                    fileChooserParams.getMode()+"",
                    fileChooserParams.getAcceptTypes()+"",
                    fileChooserParams.isCaptureEnabled()+"",
                    fileChooserParams.getTitle()+"",
                    fileChooserParams.getFilenameHint()+"",
                    webView.getUrl()));
        }else {
            logger.log(String.format(LOCALE, "%s onShowFileChooser()        : handled: %s, filePathCallback: %s , " +
                            "fileChooserParams:%s" +
                            " -->from url: %s",
                    SPACE,retVal+"", toStr(filePathCallback),
                    toStr(fileChooserParams),
                    webView.getUrl()));
        }
        return retVal;
    }

}
