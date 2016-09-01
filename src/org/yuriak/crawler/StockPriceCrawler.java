package org.yuriak.crawler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;
import org.yuriak.bean.StockBean;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatum;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.net.HttpRequest;
import cn.edu.hfut.dmic.webcollector.net.HttpResponse;
import cn.edu.hfut.dmic.webcollector.plugin.ram.RamCrawler;

public class StockPriceCrawler extends RamCrawler {
	List<StockBean> stocks;
	Map<String, Double> priceMap;
	Set<String> retryStock;
	int retryTimes;
	@Override
	public void visit(Page page, CrawlDatums arg1) {
		if (page.getResponse().getCode()!=200) {
			retryStock.add(page.getCrawlDatum().getKey());
			return;
		}
		JSONObject jsonObject=new JSONObject(page.getHtml());
		String priceString=jsonObject.getString("xj");
		double price=Double.valueOf(priceString);
		if (price!=0.0) {
			priceMap.put(page.getCrawlDatum().getKey(), price);
		}
	}
	
	public List<StockBean> getPrice(List<StockBean> stocks) throws Exception{
		this.stocks=stocks;
		priceMap=new HashMap<String, Double>();
		retryStock=new HashSet<String>();
		retryTimes=0;
		for (StockBean stockBean : stocks) {
			CrawlDatum crawlDatum=new CrawlDatum();
			if (stockBean==null||stockBean.getID()==null||stockBean.getID().trim().equals("")) {
				continue;
			}
			crawlDatum.setUrl("http://stockpage.10jqka.com.cn/spService/"+stockBean.getID()+"/Header/realHeader");
			crawlDatum.setKey(stockBean.getID());
			this.addSeed(crawlDatum);
		}
		this.setAutoParse(false);
		this.start(5);
		this.forcedSeeds.clear();
		System.out.println(retryStock.size()+"stock price need to refetch");
		while (retryTimes<3||retryStock.size()>5) {
			this.forcedSeeds.clear();
			for (String retryID : retryStock) {
				CrawlDatum crawlDatum=new CrawlDatum();
				crawlDatum.setUrl("http://stockpage.10jqka.com.cn/spService/"+retryID+"/Header/realHeader");
				crawlDatum.setKey(retryID);
				this.forcedSeeds.add(crawlDatum);
			}
			System.out.println("retry crawl price times:"+retryTimes);
			this.start(5);
			retryTimes++;
		}
		for (StockBean stockBean : stocks) {
			if (priceMap.containsKey(stockBean.getID())) {
				stockBean.setPrice(priceMap.get(stockBean.getID()));
			}
		}
		return this.stocks;
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

}
