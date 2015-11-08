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
	private HashSet<String> stocks;
	
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
			for (int i = 0; i<elements.size(); i++) {
				String title=elements.get(i).select("span.l").text();
				Elements list=elements.get(i).select("li");
				if (title.equals("沪市")||title.equals("深市")) {
					for (int j = 0; j < list.size(); j++) {
						jqStock.add(list.get(j).select("a").attr("href").split(",")[1]);
					}
				}
			}
		}
		else if (page.getUrl().equals("http://quote.eastmoney.com/stocklist.html")) {
			Document document=page.getDoc();
			Elements elements=document.select("#quotesearch").get(0).getElementsByTag("li");
			for (int i = 0; i < elements.size(); i++) {
				String idAndName=elements.get(i).select("a").last().text();
				quStock.add(idAndName.split("\\(")[1].split("\\)")[0]);
			}
		}
		stocks.addAll(jqStock);
		stocks.addAll(quStock);
		return null;
	}
	
	public Set<String> getAllStock() throws Exception{
		this.stocks=new HashSet<>();
		this.addSeed("http://bbs.10jqka.com.cn/codelist.html");
		this.addSeed("http://quote.eastmoney.com/stocklist.html");
		this.start(1);
		return this.stocks;
	}
	
	public static void main(String[] args) throws Exception {
		StockIDCrawler crawler=new StockIDCrawler("data");
		crawler.getAllStock();
	}
	
}
