package uk.co.alt236.webviewdebug;


import android.util.Log;
import android.webkit.WebView;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@Aspect
public class AgentWebCallBackLog {

    static final String client = "execution(void callJs(java.lang.String,android.webkit.ValueCallback)) && within(com.just.agentweb.BaseJsAccessEntrace)";

    @Before(client)
    public void addLog(JoinPoint joinPoint) throws Throwable {
        LogMethodAspect.logBefore(true, "AgentWebCallBack", joinPoint, new LogMethodAspect.IBefore() {
            @Override
            public void before(JoinPoint joinPoin, String desc) {

                try {
                    Object entrace = joinPoint.getThis();
                    Log.d("dd",entrace+"");
                    Field field = entrace.getClass().getDeclaredField("mWebView");
                    field.setAccessible(true);
                    WebView webView = (WebView) field.get(entrace);
                    DebugWebViewClient.logInJsConsole(webView, desc);

                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }

            @Override
            public String descExtraForLog() {
                return "";
            }
        });
    }
}
