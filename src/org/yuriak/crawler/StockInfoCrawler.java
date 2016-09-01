package org.yuriak.crawler;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jsoup.nodes.Document;
import org.yuriak.bean.StockBean;
import org.yuriak.util.CommonUtil;
import org.yuriak.util.MyTimeUtil;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatum;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.net.HttpRequest;
import cn.edu.hfut.dmic.webcollector.net.HttpResponse;
import cn.edu.hfut.dmic.webcollector.plugin.ram.RamCrawler;

public class StockInfoCrawler extends RamCrawler {

	List<StockBean> stocks;

	@Override
	public void visit(Page page, CrawlDatums crawlDatums) {
		if (page.getResponse().getCode()==404) {
			System.out.println("invalid stock");
			return;
		}
		if (page.getResponse().getUrl().equals("http://doctor.10jqka.com.cn/client.html")) {
			System.out.println("invalid stock");
			return;
		}
		Document document=page.doc();
		if (document.select(".title").get(0).text().contains("停牌")) {
			System.out.println("stopped");
			return;
		}
		StockBean stock=new StockBean();
		
		String name=document.select(".stockname").text().split("（")[0];
		String id=document.select(".stockname").text().split("（")[1].split("）")[0];
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
		Date d=MyTimeUtil.convertStringDateToDate(date,MyTimeUtil.CHN_MODE);
		stock.setDate(date==null?new Date(System.currentTimeMillis()):MyTimeUtil.convertStringDateToDate(date,MyTimeUtil.CHN_MODE));
		stocks.add(stock);
	}
	
	public List<StockBean> getAllStockInfo(List<String> stockID) throws Exception{
		stocks=new ArrayList<StockBean>();
		for (String string : stockID) {
			CrawlDatum crawlDatum=new CrawlDatum();
			crawlDatum.setUrl("http://doctor.10jqka.com.cn/"+string);
			crawlDatum.setKey(string);
			this.addSeed(crawlDatum);
		}
		this.setAutoParse(false);
		this.start(5);
		return stocks;
	}
	
	@Override
	public HttpResponse getResponse(CrawlDatum crawlDatum) throws Exception {
		HttpRequest httpRequest=new HttpRequest(crawlDatum.getUrl());
		httpRequest.setTimeoutForRead(100000);
		httpRequest.setTimeoutForConnect(100000);
		httpRequest.setFollowRedirects(false);
		httpRequest.setMethod("GET");
		return httpRequest.getResponse();
	}
	
	public static void main(String[] args) throws Exception {
		StockInfoCrawler crawler=new StockInfoCrawler();
		StockIDCrawler idCrawler=new StockIDCrawler();
		List<String> idList=new ArrayList<String>();
		idList.add("000001");
		idList.add("000002");
		idList.add("000003");
		idList.add("000004");
		crawler.getAllStockInfo(idList);
	}
}
