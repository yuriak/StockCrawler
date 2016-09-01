package org.yuriak.util;

import java.sql.Timestamp;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.sql.Date;

public class MyTimeUtil {
	public static int CHN_MODE=0;
	public static int SIMPLE_MODE=1;
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
	/**
	 * 
	 * @param date String value
	 * @param fore  true:yyyy年mm月dd日  false:yyyy-MM-dd
	 * @return
	 */

	public static Date convertStringDateToDate(String date,int mode){
		try {
			Format format;
			if (mode==CHN_MODE) {
				format=new SimpleDateFormat("yyyy年MM月dd日");
			}else if (mode==SIMPLE_MODE) {
				format=new SimpleDateFormat("yyyy-MM-dd");
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
