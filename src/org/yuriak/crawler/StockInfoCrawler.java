package org.yuriak.crawler;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.jsoup.nodes.Document;
import org.yuriak.bean.StockBean;
import org.yuriak.util.CommonUtil;
import org.yuriak.util.MyTimeUtil;

import cn.edu.hfut.dmic.webcollector.crawler.DeepCrawler;
import cn.edu.hfut.dmic.webcollector.model.Links;
import cn.edu.hfut.dmic.webcollector.model.Page;

public class StockInfoCrawler extends DeepCrawler {
	private ArrayList<StockBean> stockList;
	
	public StockInfoCrawler(String crawlPath) {
		super(crawlPath);
		
	}

	@Override
	public Links visitAndGetNextLinks(Page page) {
		if (!page.getUrl().equals("http://doctor.10jqka.com.cn/client.html")&&!page.getDoc().title().trim().equals("牛叉诊股-个股分析利器_同花顺金融网")) {
			Document document=page.getDoc();
			StockBean stock=new StockBean();
			String name=document.select(".stockname").text().split("（")[0];
			String id=page.getUrl().split("/")[3];
			stock.setID(id);
			stock.setName(name);
			String score=document.select(".bignum").text()+document.select(".smallnum").text();
			if (score!=null) {
				stock.setScore(Double.valueOf(score));
				
			}else {
				stock.setScore(0.0);
			}
			String advice=document.select(".cur").get(0).text();
			stock.setAdvice(advice==null?-1:CommonUtil.convertAdviceWord2Number(advice));
			String total=document.select(".stocktotal").text();
			stock.setEvaluation(total==null?"":total);
			String value=document.select(".cnt").select("strong").text();
			if (value!=null) {
				stock.setPrice(Double.valueOf(value.substring(0,value.length()-1)));
			}else {
				stock.setPrice(0.0);
			}
			String date=document.select(".date").text();
			date=date.split(" ")[0].substring(6,date.split(" ")[0].length());
			System.out.println(date);
			stock.setDate(date==null?new Date(System.currentTimeMillis()):MyTimeUtil.convertStringDateToDate(date,true));
			this.stockList.add(stock);
		}
		
		return null;
	}
	
	public ArrayList<StockBean> getStockInfo(ArrayList<String> stocks) throws Exception{
		stockList=new ArrayList<>();
		for (String id : stocks) {
			this.addSeed("http://doctor.10jqka.com.cn/"+id+"/");
		}
		this.start(1);
		return this.stockList;
	}
}
