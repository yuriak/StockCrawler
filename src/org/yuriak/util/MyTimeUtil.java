package org.yuriak.util;

import java.sql.Timestamp;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.sql.Date;

public class MyTimeUtil {
	public static Timestamp convertStringToTimeStamp(String time){
		try {
			Format f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        Date d = (Date) f.parseObject(time);
	        Timestamp ts = new Timestamp(d.getTime());
	        return ts;
		} catch (Exception e) {
			e.printStackTrace();
			return new Timestamp(System.currentTimeMillis());
		}
	}
	
	public static Date convertStringDateToDate(String date,boolean fore){
		try {
			Format format;
			if (fore) {
				format=new SimpleDateFormat("yyyy年MM月dd日");
			}else {
				format=new SimpleDateFormat("yyyy-MM-dd");
			}
			java.util.Date tmoDate=(java.util.Date) format.parseObject(date);
			Date d=new Date(tmoDate.getTime());
			return d;
		} catch (Exception e) {
			e.printStackTrace();
			return new Date(System.currentTimeMillis());
		}
	}
	
	
	
	public static void main(String[] args) {
		try {
			System.out.println(convertStringToTimeStamp("2015年10月1日 22:33"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
