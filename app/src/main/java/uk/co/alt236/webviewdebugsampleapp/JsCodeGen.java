package uk.co.alt236.webviewdebugsampleapp;

import android.os.Build;
import android.util.Log;
import android.webkit.JavascriptInterface;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class JsCodeGen {

    public static void test(){
        Method[] methods =   MyJsObj.class.getDeclaredMethods();

        for (Method method : methods) {
            Object obj = null;
           /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                obj = method.getAnnotation(JavascriptInterface.class);
            }*/
            System.out.println(method+"--->");
            System.out.println(method.getName()+"("+ Arrays.toString(method.getParameters())+")");
            /*if(obj != null){
                //System.out.println(method.getName()+"("+ Arrays.toString(method.getParameterTypes())+")");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    System.out.println(method.getName()+"("+ Arrays.toString(method.getParameters())+")");
                }

            }*/
        }

    }

    /**
     * 一定要有get,set才能正常生成模板
     */
    public static class JsMethod{
        public  String objName;
        public  String methodName;
        public  String params;

        public String getObjName() {
            return objName;
        }

        public void setObjName(String objName) {
            this.objName = objName;
        }

        public String getMethodName() {
            return methodName;
        }

        public void setMethodName(String methodName) {
            this.methodName = methodName;
        }

        public String getParams() {
            return params;
        }

        public void setParams(String params) {
            this.params = params;
        }
    }

    public static void main(String[] args) throws Exception {
        String dir="/Users/hss/github/webviewdebug/html/";
        Configuration conf = new Configuration();
        //加载模板文件(模板的路径)
        conf.setDirectoryForTemplateLoading(new File(dir));
        // 加载模板
        Template template = conf.getTemplate("/freemarker-demo.ftl");
        // 定义数据




        Map root = new HashMap();
        root.put("world", "Hello World");

        List<JsMethod> methodList = new ArrayList<>();


        Method[] methods =   MyJsObj.class.getDeclaredMethods();
        for (Method method : methods) {
            JsMethod method1 = new JsMethod();
            method1.objName = "MyJsObj";
            method1.methodName = method.getName();
            method1.params = Arrays.toString(method.getParameters());
            methodList.add(method1);
            //一定要有get,set方法
        }
        root.put("methods1", methodList);
        System.out.println(methodList.size()+"-size");

        // 定义输出
        Writer out = new FileWriter(dir + "/freemarker.html");
        template.process(root, out);
        System.out.println("转换成功");
        out.flush();
        out.close();
        //JsCodeGen.test();
    }
}
