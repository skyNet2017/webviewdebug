package com.hss01248.webviewlib;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.just.agentweb.AgentWeb;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.logging.Handler;

import uk.co.alt236.webviewdebug.LogMethodAspect;

public class DebugViewHolder {

    TextView debugView;

    boolean showCookie;

    boolean enableUrlLine = true;
    CommonTitleBar mCommonTitleBar;
    Activity context;
    AgentWeb agentWeb;

    public DebugViewHolder(Activity context, CommonTitleBar titleBar) {

        mCommonTitleBar = titleBar;
        this.context = context;
        setDebugLine();

    }



     void setDebugLine() {

        if ( !enableUrlLine) {
            if (mCommonTitleBar != null) {
                mCommonTitleBar.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        enableUrlLine = true;
                        setDebugLine();
                        return true;
                    }
                });
            }
            return;
        }

        FrameLayout layout2 = context.findViewById(android.R.id.content);
        if (layout2 == null) {
            return;
        }
        if (debugView == null) {
            debugView = new TextView(context);
            debugView.setTextColor(Color.BLUE);
            debugView.setId(R.id.tv_actual_payment_discount);
            debugView.setPadding(20, (int) (20*context.getResources().getDisplayMetrics().density), 15, 15);
            debugView.setTextSize(11);
            layout2.postDelayed(new Runnable() {
                @Override
                public void run() {
                    layout2.addView(debugView,
                            new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT));
                }
            },2000);

            debugView.setText("init debugview");
            debugView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (showCookie) {
                        showCookie = false;
                        String text = debugView.getText().toString().trim();
                        if (text.contains("\ncookie:")) {
                            text = text.substring(0, text.indexOf("\ncookie:"));
                            debugView.setText(text);
                        }
                    } else {
                        showCookie = true;
                        String cookie = getCookieStr();
                        String text = debugView.getText().toString().trim();
                        if (!text.contains("\ncookie:")) {
                            text = text + cookie;
                            debugView.setText(text);
                        }
                    }
                }
            });

            debugView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    debugView.setVisibility(View.GONE);
                    return true;
                }
            });
        } else {
            debugView.setVisibility(View.VISIBLE);
            if (TextUtils.isEmpty(debugView.getText().toString())) {
                if(agentWeb!=null){
                    debugView.setText(agentWeb.getWebCreator().getWebView().getUrl());
                }

            }
        }
    }

     String getCookieStr() {
        if(agentWeb!=null){
            String cookie = showCookie ? "\ncookie: " + CookieManager.getInstance()
                    .getCookie(agentWeb.getWebCreator().getWebView().getUrl()) : "";
            try {
                cookie = URLDecoder.decode(cookie, "utf-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
            cookie = cookie.replaceAll(";", ";\n");
            return cookie;
        }
        return "";
    }


}
