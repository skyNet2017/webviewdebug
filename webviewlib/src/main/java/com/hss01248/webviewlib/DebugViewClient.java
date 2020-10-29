package com.hss01248.webviewlib;

import android.graphics.Bitmap;
import android.os.Build;
import android.webkit.WebView;

public class DebugViewClient extends WrappedWebviewClient{
    public DebugViewClient(DebugViewHolder debugViewHolder) {
        this.debugViewHolder = debugViewHolder;
    }

    DebugViewHolder debugViewHolder;
    long startMills;
    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        if (debugViewHolder.debugView != null) {
            debugViewHolder.debugView.setText(url + "(长按可消失，点击可显示cookie)");
        }
        startMills = System.currentTimeMillis();
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        if (debugViewHolder.debugView != null && startMills != 0) {
            String cookie = debugViewHolder.getCookieStr();
            debugViewHolder.debugView.setText(
                    url + "\ncost:" + (System.currentTimeMillis() - startMills) + "ms"
                            + cookie);
            startMills = 0;
        }
    }

    @Override
    public void onLoadResource(WebView view, String url) {
        super.onLoadResource(view, url);
    }

    @Override
    public boolean shouldOverrideUrlLoadingAll(WebView view, String newurl) {
        debugViewHolder.debugView.setText(newurl + "(长按可消失，点击可显示cookie)");
        return super.shouldOverrideUrlLoadingAll(view, newurl);
    }

    @Override
    public void onReceivedErrorAll(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedErrorAll(view, errorCode, description, failingUrl);
        String cookie = debugViewHolder.getCookieStr();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            debugViewHolder.debugView.setText(
                    "errorcode:" + errorCode + "\ndesc:" + description + "\ncost:" + (
                            System.currentTimeMillis() - startMills)
                            + "ms\nfailingurl:" + failingUrl + cookie);
        }
        startMills = 0;
    }
}
