package uk.co.alt236.webviewdebugsampleapp;

/**
 * time:2019/12/1
 * author:hss
 * desription:
 */
public class MethodTest {

    public String callPhone(String num){
        return "phone called "+num;
    }

    public String callPhone2(String num,int time){
        return "phone called 2 "+num +" last times:"+time;
    }
}
