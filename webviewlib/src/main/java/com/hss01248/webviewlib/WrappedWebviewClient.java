package com.hss01248.webviewlib;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;

import com.just.agentweb.WebViewClient;

import java.util.List;

public class WrappedWebviewClient extends WebViewClient {



    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onReceivedHttpError(WebView view, WebResourceRequest request,
                                    WebResourceResponse errorResponse) {
        //super.onReceivedHttpError(view, request, errorResponse);
        if (request.isForMainFrame()) {
            this.onReceivedError(view, errorResponse.getStatusCode(), errorResponse.getReasonPhrase(),
                    request.getUrl().toString());
        } else {
            Log.w("WrappedWebviewClient","onReceivedError iframe:" + errorResponse.getStatusCode() + "," + errorResponse.getReasonPhrase()
                    + "--url：" + request.getUrl().toString());
        }

    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        if (request.isForMainFrame()) {
            this.onReceivedError(view,
                    error.getErrorCode(), error.getDescription().toString(),
                    request.getUrl().toString());
        } else {
            Log.w("WrappedWebviewClient","onReceivedError iframe:" + error.getErrorCode() + "," + error.getDescription().toString()
                    + "--url：" + request.getUrl().toString());
        }
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description,
                                String failingUrl) {
        this.onReceivedErrorAll(view, errorCode, description, failingUrl);
    }

    public void onReceivedErrorAll(WebView view, int errorCode, String description,
                                   String failingUrl) {
        Log.w("WrappedWebviewClient","onReceivedError : errorcode:" + errorCode + "--desc:" + description + "--failingurl:" + failingUrl);
    }










    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        if(request.isForMainFrame()){
            return this.shouldOverrideUrlLoading(view, request.getUrl().toString());
        }else {
            return super.shouldOverrideUrlLoading(view,request);
        }
        //XLogUtil.d("Url:", "shouldOverrideUrlLoading(>=api21):" + request.getUrl().toString());

    }

    /**
     * 多页面在同一个WebView中打开，就是不新建activity或者调用系统浏览器打开
     */
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String newurl) {
        //view.loadUrl(newurl);  加了这行代码会导致JavaScript 的 location.reload() location.replace() API 失效，进而导致堆栈管理异常
        try {

            // 支持路由的跳转
           /* if (RouterUtil.isAKRouter(newurl)) {
                RouterUtil.navigation(ActivityStackManager.getInstance().getTopActivity(), newurl);
                return true;
            }*/
            //处理intent协议
            if (newurl.startsWith("intent://")) {
                Intent intent;
                try {
                    intent = Intent.parseUri(newurl, Intent.URI_INTENT_SCHEME);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setComponent(null);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                        intent.setSelector(null);
                    }
                    List<ResolveInfo> resolves = view.getContext().getPackageManager().queryIntentActivities(intent, 0);
                    if (resolves.size() > 0) {
                        ((Activity) view.getContext()).startActivityIfNeeded(intent, -1);
                        return true;
                    }

                } catch (Exception e) {
                   e.printStackTrace();
                }
            }

            // 处理自定义scheme协议
            if (!newurl.startsWith("http")) {
                try {
                    // 以下固定写法
                    final Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(newurl));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    view.getContext().startActivity(intent);
                } catch (Exception e) {
                    // 防止没有安装的情况
                    e.printStackTrace();
                }
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this.shouldOverrideUrlLoadingAll(view, newurl);
    }

    public static boolean toMarket(Context context, String appPkg, String marketPkg, String googlePlayRefer) {
        Uri uri = Uri.parse("market://details?id=" + appPkg);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if(!TextUtils.isEmpty(googlePlayRefer)){
            intent.putExtra("referrer",googlePlayRefer);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (TextUtils.isEmpty(marketPkg)) {// 如果没给市场的包名，则系统会弹出市场的列表让你进行选择。
            intent.setPackage(marketPkg);
        }
        try {
            context.startActivity(intent);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean shouldOverrideUrlLoadingAll(WebView view, String newurl) {
        return super.shouldOverrideUrlLoading(view, newurl);
    }

}
