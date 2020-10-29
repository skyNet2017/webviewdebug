package uk.co.alt236.webviewdebug;

import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class AgentWebClientAspect {

    public static  boolean enableLog = true;

    static final String client= "execution(* setWeb*(..)) && within(com.just.agentweb.AbsAgentWebSettings)";

    @Around(client)
    public Object addDebugClient(ProceedingJoinPoint joinPoin) throws Throwable{
        LogMethodAspect.logBefore(enableLog, "AgentWebClient", joinPoin, new LogMethodAspect.IBefore() {
            @Override
            public String descExtraForLog() {
                return "";
            }
        });
        try {
            String name = joinPoin.getSignature().getName();
            if("setWebViewClient".equals(name)){
                Object[] args = joinPoin.getArgs();
                //WebView webView = (WebView) joinPoin.getArgs()[0];
                WebViewClient webViewClient = (WebViewClient)args[1];
                DebugWebViewClient  debugWebViewClient = new DebugWebViewClient(webViewClient);
                debugWebViewClient.setLoggingEnabled(enableLog);
                debugWebViewClient.setJsDebugPannelEnable(enableLog);
                args[1] = debugWebViewClient;
               return joinPoin.proceed(args);

            }else if("setWebChromeClient".equals(name)){
                Log.d("dd","setWebChromeClient  eq");
                Object[] args = joinPoin.getArgs();
               // WebView webView = (WebView) joinPoin.getArgs()[0];
                WebChromeClient webChromeClient = (WebChromeClient) args[1];
                DebugWebChromeClient chromeClient = new DebugWebChromeClient(webChromeClient);
                chromeClient.setLoggingEnabled(enableLog);
                args[1] = chromeClient;
                return joinPoin.proceed(args);
            }
            return joinPoin.proceed(joinPoin.getArgs());
        }catch (Throwable throwable){
            throwable.printStackTrace();
            return joinPoin.proceed(joinPoin.getArgs());
        }
    }
}
