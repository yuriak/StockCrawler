package org.yuriak.main;

import java.awt.font.NumericShaper;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.jws.Oneway;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.junit.runners.Parameterized.Parameters;
import org.yuriak.bean.StockBean;
import org.yuriak.crawler.StockIDCrawler;
import org.yuriak.crawler.StockInfoCrawler;
import org.yuriak.dao.StockDao;
import org.yuriak.util.MyChartUtil;
import org.yuriak.util.MyDBUtil;
import org.yuriak.util.MyFileUtil;
import org.yuriak.util.MyTimeUtil;

public class MainTest {
	
	public static void main(String[] args) throws Exception {
//		StockDao stockDao=new StockDao();
		collect();
//		throw new IllegalArgumentException("数据不合法");
//		showOne("002156");
//		showOneDay(MyTimeUtil.convertStringDateToDate("2015年6月5日"));
//		showOneDay(MyTimeUtil.convertStringDateToDate("2015年6月8日"));
//		showData(MyTimeUtil.convertStringDateToDate("2015年6月8日"));
//		ArrayList<StockBean> stocks=MyFileUtil.readInfoFromFile("data/stockInfo2015-07-17.txt");
//		stockDao.SaveOneDayStockInfoToDB(stocks);
//		System.out.println("done");
//		ArrayList<StockBean> stocks=stockDao.findOneDayStockInfo(MyTimeUtil.convertStringDateToDate("2015-07-17", false));
//		MyFileUtil.writeInfoToFile(stocks);
//		System.out.println("done");
//		showOne("");
//		showOneDayData(MyTimeUtil.convertStringDateToDate("2015-06-24", false));
//		showOne("603002",0);
//		showOne("青鸟华光",1);
//		showOneDay(MyTimeUtil.convertStringDateToDate("2015-07-17", false));
	}
	
	public static void showOneDayData(Date day){
		StockDao stockDao=new StockDao();
		ArrayList<StockBean> stocks=stockDao.findOneDayStockInfo(day);
		for (StockBean stockBean : stocks) {
			System.out.println(stockBean.getID()+"|"+stockBean.getName()+"|"+stockBean.getPrice()+"|"+stockBean.getScore()+"|"+stockBean.getAdvice()+"|"+stockBean.getDate());
		}
	}
	
	public void initSystem() throws DocumentException{
		SAXReader reader=new SAXReader();
		FileInputStream inputStream=(FileInputStream) this.getClass().getResourceAsStream("conf/config.xml");
		Document document=reader.read(inputStream);
		System.out.println(document);
	}
	public static void collect() throws Exception{
		System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
		StockDao stockDao=new StockDao();
		StockIDCrawler crawler=new StockIDCrawler("data");
		StockInfoCrawler infoCrawler=new StockInfoCrawler("data");
		ArrayList<StockBean> stocks=infoCrawler.getStockInfo(crawler.getAllStock());
		Collections.sort(stocks);
//		for (StockBean stock : stocks) {
//			System.out.println(stock.getID()+"|"+stock.getName()+"|"+stock.getPrice()+"|"+stock.getScore()+"|"+stock.getAdvice()+"|"+stock.getDate());
//		}
		stockDao.SaveOneDayStockInfoToDB(stocks);
		MyFileUtil.writeInfoToFile(stocks);
		MyChartUtil.drawAllStockXYLineChartInOneDay(stocks);
	}
	
	public static void showOneDay(Date day){
		StockDao stockDao=new StockDao();
		ArrayList<StockBean> stocks=stockDao.findOneDayStockInfo(day);
		MyChartUtil.drawAllStockXYLineChartInOneDay(stocks);
	}
	
	/**
	 * 
	 * @param idOrName ID or Name
	 * @param para 0 is ID,1 is Name 
	 */
	public static void showOne(String idOrName,int para){
		StockDao stockDao=new StockDao();
		ArrayList<StockBean> stocks=new ArrayList<>();
		switch (para) {
		case 0:
			stocks=stockDao.findStockByIDAndSortByDate(idOrName);
			break;
		case 1:
			stocks=stockDao.findStockByNameAndSortByDate(idOrName);
		default:
			break;
		}
		MyChartUtil.drawOneStockTimeTrendScoreChart(stocks);
	}
	
	
	public static double calC(int n,int m){
		if (n>m) {
			return (double)factorial(n)/(double)factorial(m)*(double)factorial(n-m);
		}else {
			return 0.0;
		}
	}
	
	public static int factorial(int number){
		if (number>1) {
			return number*factorial(number-1);
		}else {
			return number;
		}
	}
	
}
