package uk.co.alt236.webviewdebugsampleapp;

import android.os.Build;
import android.util.Log;
import android.webkit.JavascriptInterface;

import java.lang.reflect.Method;
import java.util.Arrays;

public class JsCodeGen {

    public static void test(){
        Method[] methods =   MyJsObj.class.getDeclaredMethods();
        for (Method method : methods) {
            Object obj = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                obj = method.getAnnotation(JavascriptInterface.class);
            }
            //Log.d("JsCodeGen",obj+"--->");
            if(obj != null){
                Log.d("JsCodeGen",method.getName()+"("+ Arrays.toString(method.getParameterTypes())+")");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Log.d("JsCodeGen",method.getName()+"("+ Arrays.toString(method.getParameters())+")");
                }

            }
        }

    }
}
