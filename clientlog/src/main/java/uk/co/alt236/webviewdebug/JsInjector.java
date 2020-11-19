package uk.co.alt236.webviewdebug;

import android.os.Build;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebView;

public class JsInjector {

    public static void logInJsConsole(WebView webView, String str){
        webView.postDelayed(new Runnable() {
            @Override
            public void run() {
                String js = "javascript:console.log('"+str+"')";
                loadJs(webView,js);
            }
        },0);
    }

    static void loadJs(WebView webView,String jscode){
        Log.w("js",jscode);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.evaluateJavascript(jscode, new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    Log.w("onReceiveValue",value);
                }
            });//4.4以上,可以设置webview调用js方法后的回调.
        } else {
            webView.loadUrl(jscode);
        }
    }


    static String fuc3 =
            "(function () { " +
                    "var txt =  document.getElementsByTagName('html')[0].innerHTML;" +
                    "console.log('idx:javascr '+txt.indexOf('npm/eruda'));"+
                    "if (txt.indexOf('npm/eruda') < 0){" +
                    "var script = document.createElement('script'); " +
                    "script.src='https://cdn.jsdelivr.net/npm/eruda';" +
                    "script.onload = function () { setTimeout(function () { console.log('idx:setTimeout');eruda.init();},500)};" +
                    "document.body.appendChild(script); " +
                    " }"+
                    "})();";
    static String fuc4 = "var txt =  document.getElementsByTagName('html')[0].innerHTML;" +
            "console.log('idx:javascript:'+txt.indexOf('eruda.init()'))"+
            "    if(txt.indexOf('eruda') < 0){" +
            "        var script = document.createElement('script');" +
            "        script.src='https://cdn.jsdelivr.net/npm/eruda';" +
            "        script.onload = function () { eruda.init() }" +
            "        document.body.appendChild(script);" +
            "    }" ;
    // "script.onload = function () { eruda.init() } ";

    /*<script src="//cdn.jsdelivr.net/npm/eruda"></script>
        <script>eruda.init();</script>*/

    public static void invokeJsDebugPan(WebView webView){
        webView.postDelayed(new Runnable() {
            @Override
            public void run() {
                String js = "javascript:"+fuc3;
                loadJs(webView,js);
            }
        },0);
    }

}
