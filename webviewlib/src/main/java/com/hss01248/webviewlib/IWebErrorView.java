package com.hss01248.webviewlib;

import android.content.Context;
import android.view.View;

public interface IWebErrorView {


   default View getView(Context context){
       return null;
   }

   default void setErrorMsg(String url,int code,String desMsg){

   }


}
