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
import java.util.Properties;

import javax.jws.Oneway;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.yuriak.bean.StockBean;
import org.yuriak.config.CommonValue;
import org.yuriak.crawler.StockIDCrawler;
import org.yuriak.crawler.StockInfoCrawler;
import org.yuriak.dao.StockDao;
import org.yuriak.util.MyDBUtil;
import org.yuriak.util.MyFileUtil;
import org.yuriak.util.MyTimeUtil;

public class Launcher {
	
	public static void main(String[] args) throws Exception {
		if (args.length==0) {
			System.out.println("Please provide command and param");
			return;
		}
		Launcher launcher=new Launcher();
		try {
//			launcher.initSystem();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		String command=args[0];
		switch (command) {
		case "collect":
			switch (args[1]) {
			case "file":
				launcher.collect(0);
				break;
			case "DB":
				launcher.collect(1);
				break;
			case "fileAndDB":
				launcher.collect(2);
				break;
			default:
				break;
			}
			break;
		case "showoneday":
			if (args[1].isEmpty()||args[1].equals("")||!args[1].matches("\\d{4}-\\d{2}-\\d{2}")) {
				System.out.println("Please input correct date like: 1970-01-01");
				return;
			}
			String dateParam=args[1];
			launcher.showOneDayData(MyTimeUtil.convertStringDateToDate(dateParam, false));
			break;
		case "showonestock":
			if (args[1].isEmpty()||args[1].equals("")||!args[1].matches("\\d{6}")) {
				System.out.println("Please input correct id like: 000001");
				return;
			}
			String idParam=args[1];
			launcher.showOne(idParam, 0);
			break;
		}
//		StockDao stockDao=new StockDao();
//		collect();
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
//		showOne("东风汽车",1);
//		showOneDay(MyTimeUtil.convertStringDateToDate("2015-07-17", false));
	}
	
	public void showOneDayData(Date day){
		StockDao stockDao=new StockDao();
		ArrayList<StockBean> stocks=stockDao.findOneDayStockInfo(day);
		System.out.println("  ID  "+"|"+"Name"+"|"+"Price"+"|"+"Score"+"|"+"Advice"+"|"+"Date");
		for (StockBean stockBean : stocks) {
			System.out.println(stockBean.getID()+"|"+stockBean.getName()+"|"+stockBean.getPrice()+"|"+stockBean.getScore()+"|"+stockBean.getAdvice()+"|"+stockBean.getDate());
		}
	}
	
	public void initSystem() throws Exception{
		Properties prop = new Properties();
		prop.load(new FileInputStream(new File("conf/conf.properties")));
		CommonValue.DB_URL=prop.getProperty("DBUrl");
		CommonValue.DB_USERNAME=prop.getProperty("DBUsername");
		CommonValue.DB_PASSWORD=prop.getProperty("DBPassword");
	}
	/**
	 * 
	 * @param saveMethod 0:file,1:db,2:file and db
	 * @throws Exception
	 */
	public void collect(int saveMethod) throws Exception{
		StockDao stockDao=new StockDao();
		StockIDCrawler crawler=new StockIDCrawler(System.getProperty("user.dir")+File.separator+"crawldb");
		StockInfoCrawler infoCrawler=new StockInfoCrawler(System.getProperty("user.dir")+File.separator+"crawldb");
		ArrayList<StockBean> stocks=infoCrawler.getStockInfo(crawler.getAllStock());
		Collections.sort(stocks);
		switch (saveMethod) {
		case 0:
			MyFileUtil.writeInfoToFile(stocks);
			break;
		case 1:
			stockDao.SaveOneDayStockInfoToDB(stocks);
			break;
		case 2:
			MyFileUtil.writeInfoToFile(stocks);
			stockDao.SaveOneDayStockInfoToDB(stocks);
			break;
		}
		
//		MyChartUtil.drawAllStockXYLineChartInOneDay(stocks);
	}
	
//	public static void showOneDay(Date day){
//		StockDao stockDao=new StockDao();
//		ArrayList<StockBean> stocks=stockDao.findOneDayStockInfo(day);
//		for (StockBean stockBean : stocks) {
//			
//		}
////		MyChartUtil.drawAllStockXYLineChartInOneDay(stocks);
//	}
	
	/**
	 * 
	 * @param idOrName ID or Name
	 * @param para 0 is ID,1 is Name 
	 */
	public void showOne(String idOrName,int para){
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
		System.out.println("  ID  "+"|"+"Name"+"|"+"Price"+"|"+"Score"+"|"+"Advice"+"|"+"Date");
		for (StockBean stockBean : stocks) {
			System.out.println(stockBean.getID()+"|"+stockBean.getName()+"|"+stockBean.getPrice()+"|"+stockBean.getScore()+"|"+stockBean.getAdvice()+"|"+stockBean.getDate());
		}
	}
	
	
	public double calC(int n,int m){
		if (n>m) {
			return (double)factorial(n)/(double)factorial(m)*(double)factorial(n-m);
		}else {
			return 0.0;
		}
	}
	
	public int factorial(int number){
		if (number>1) {
			return number*factorial(number-1);
		}else {
			return number;
		}
	}
	
}
