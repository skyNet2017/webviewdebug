package uk.co.alt236.webviewdebug;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class AgentWebCallBackLog {

    static final String client= "execution(void callJs(java.lang.String,android.webkit.ValueCallback)) && within(com.just.agentweb.BaseJsAccessEntrace)";

    @Before(client)
    public void addLog(JoinPoint joinPoint) throws Throwable{
         LogMethodAspect.logBefore(true, "AgentWebCallBack", joinPoint, new LogMethodAspect.IBefore() {
            @Override
            public String descExtraForLog() {
                return "";
            }
        });
    }
}
