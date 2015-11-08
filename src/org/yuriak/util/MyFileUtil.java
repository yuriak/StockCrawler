package org.yuriak.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.yuriak.bean.StockBean;

public class MyFileUtil {
	public static void writeInfoToFile(ArrayList<StockBean> stocks) throws Exception{
		JSONObject mainData=new JSONObject();
		JSONArray stockArray=new JSONArray();
		mainData.put("StockInfo", stockArray);
		for (StockBean stock : stocks) {
			JSONObject stockObject=new JSONObject();
			stockObject.put("ID", stock.getID());
			stockObject.put("name", stock.getName());
			stockObject.put("price", stock.getPrice());
			stockObject.put("score", stock.getScore());
			stockObject.put("advice", stock.getAdvice());
			stockObject.put("evaluation", stock.getEvaluation());
			stockObject.put("date", stock.getDate());
			stockArray.put(stockObject);
		}
		String time=new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Timestamp(System.currentTimeMillis()));
		FileUtils.write(new File(System.getProperty("user.dir")+File.separator+"data/stockInfo"+time+".txt"), mainData.toString(), "UTF-8");
	}
	
	public static ArrayList<StockBean> readInfoFromFile(String fileName){
		ArrayList<StockBean> stockList=new ArrayList<>();
		String content="";
		try {
			File file=new File(fileName);
			content=new String(FileUtils.readFileToByteArray(file),"utf-8");
		} catch (Exception e) {
			e.printStackTrace();
			return stockList;
		}
		JSONObject mainData=new JSONObject(content);
		JSONArray stockArray=mainData.getJSONArray("StockInfo");
		for (int i = 0; i < stockArray.length(); i++) {
			JSONObject object=stockArray.getJSONObject(i);
			StockBean bean=new StockBean();
			bean.setID(object.getString("ID"));
			bean.setName(object.getString("name"));
			bean.setPrice(object.getDouble("price"));
			bean.setScore(object.getDouble("score"));
			bean.setAdvice(object.getInt("advice"));
			bean.setEvaluation(object.getString("evaluation"));
			bean.setDate(MyTimeUtil.convertStringDateToDate(object.getString("date"),false));
			stockList.add(bean);
		}
		return stockList;
	}
}
