package uk.co.alt236.webviewdebug;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;


/**
 * 缺陷:
 * 1.只会切子类已经重写的方法,未重写的不打印
 * 2. 打印格式不够美观,不能自由地自定义
 */
@Deprecated
//@Aspect
public class WebviewClientCallbackAspect {

    public static final String TAG = "WebClientLog";
   public static boolean enableLog = true;
   static final String client= "execution(* android.webkit.WebViewClient+.*(..))";
    static final String chromeClient= "execution(* android.webkit.WebChromeClient+.*(..))";

    //@Around(client +" || "+chromeClient)
    public Object addLog(ProceedingJoinPoint joinPoint) throws Throwable{
        return LogMethodAspect.logAround(enableLog, TAG,true, joinPoint, new LogMethodAspect.IAround() {

            @Override
            public String descExtraForLog() {
                return "";
            }
        });
    }


}
