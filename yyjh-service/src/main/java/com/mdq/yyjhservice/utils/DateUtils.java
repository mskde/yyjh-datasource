package com.mdq.yyjhservice.utils;

public class DateUtils {
    public static String setDateFormat(String test,int index){
        StringBuffer time=new StringBuffer(test);

        String year=time.substring(0,time.indexOf("-"));
        time.delete(0,time.indexOf("-")+1);

        String month=time.substring(0,time.indexOf("-"));
        time.delete(0,time.indexOf("-")+1);

        String date=time.substring(0,time.indexOf("-"));
        time.delete(0,time.indexOf("-")+1);

        String hour=time.substring(0,time.indexOf("-"));
        time.delete(0,time.indexOf("-")+1);

        String minute=time.substring(0,time.indexOf("-"));
        time.delete(0,time.indexOf("-")+1);

        String second=time.toString();


        String newtime="";
        //年月日时分秒
        //日月年时分秒
        //年月日
        //日月年
        //时分秒
        if(index==1){
            newtime=year+"-"+month+"-"+date+" "+hour+":"+minute+":"+second;
        }else if(index==2){
            newtime=date+"-"+month+"-"+year+" "+hour+":"+minute+":"+second;
        }else if(index==3){
            newtime=year+"-"+month+"-"+date;
        }else if(index==4){
            newtime=date+"-"+month+"-"+year;
        }else if(index==5){
            newtime=hour+":"+minute+":"+second;
        }
        return newtime;
    }
}
