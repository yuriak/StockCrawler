package org.yuriak.crawler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import cn.edu.hfut.dmic.webcollector.crawler.DeepCrawler;
import cn.edu.hfut.dmic.webcollector.model.Links;
import cn.edu.hfut.dmic.webcollector.model.Page;

public class StockIDCrawler extends DeepCrawler {
	private ArrayList<String> stocks;
	
	public StockIDCrawler(String crawlPath) {
		super(crawlPath);
	}

	@Override
	public Links visitAndGetNextLinks(Page page) {
		
		ArrayList<String> jqStock=new ArrayList<>();
		ArrayList<String> quStock=new ArrayList<>();
		if (page.getUrl().equals("http://bbs.10jqka.com.cn/codelist.html")) {
			Document document=page.getDoc();
			Elements elements=document.select(".bbsilst_wei3");
			for (int i = 0; i <= 1; i++) {
				Elements list=elements.get(i).select("li");
				for (int j = 0; j < list.size(); j++) {
					jqStock.add(list.get(j).select("a").attr("href").split(",")[1]);
				}
			}
		}
		else if (page.getUrl().equals("http://quote.eastmoney.com/stocklist.html")) {
			Document document=page.getDoc();
			Elements elements=document.select("#quotesearch").get(0).getElementsByTag("li");
			for (int i = 0; i < elements.size(); i++) {
				quStock.add(elements.get(i).getElementsByTag("a").last().attr("href").split("/")[3].substring(2,8));
			}
		}
		quStock.removeAll(jqStock);
		jqStock.addAll(quStock);
		stocks=jqStock;
		return null;
	}
	
	public ArrayList<String> getAllStock() throws Exception{
		this.stocks=new ArrayList<>();
		this.addSeed("http://bbs.10jqka.com.cn/codelist.html");
		this.addSeed("http://quote.eastmoney.com/stocklist.html");
		this.start(1);
		System.out.println(this.stocks.size());
		return this.stocks;
	}
	
	public static void main(String[] args) throws Exception {
		StockIDCrawler crawler=new StockIDCrawler("data");
		crawler.addSeed("http://quote.eastmoney.com/stocklist.html");
		crawler.start(1);
	}
	
}
