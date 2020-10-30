package com.hss01248.webviewlib;

import android.view.View;

public interface IWebErrorView {


   default View getView(){
       return null;
   }

   default void setErrorMsg(String url,int code,String desMsg){

   }


}
