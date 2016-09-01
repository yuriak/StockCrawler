package org.yuriak.crawler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatum;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.net.HttpRequest;
import cn.edu.hfut.dmic.webcollector.net.HttpResponse;
import cn.edu.hfut.dmic.webcollector.plugin.ram.RamCrawler;

public class StockIDCrawler extends RamCrawler {

	private List<String> stocks;

	@Override
	public void visit(Page page, CrawlDatums arg1) {
		// TODO Auto-generated method stub
		Document document = page.doc();
		Elements elements = document.select("#quotesearch").get(0)
				.getElementsByTag("li");
		for (int i = 0; i < elements.size(); i++) {
			String idAndName = elements.get(i).select("a").last().text();
			stocks.add(idAndName.split("\\(")[1].split("\\)")[0]);
		}
	}

	public List<String> getAllStockID() throws Exception {
		this.stocks = new ArrayList<String>();
		CrawlDatum crawlDatum = new CrawlDatum();
		crawlDatum.setUrl("http://quote.eastmoney.com/stocklist.html");
		this.addSeed(crawlDatum);
		this.start(5);
		System.out.println("fetched " + stocks.size() + " stocks");
		return stocks;
	}

	@Override
	public HttpResponse getResponse(CrawlDatum crawlDatum) throws Exception {
		HttpRequest httpRequest = new HttpRequest(crawlDatum.getUrl());
		httpRequest.setTimeoutForRead(100000);
		httpRequest.setTimeoutForConnect(100000);
		httpRequest.setFollowRedirects(false);
		httpRequest.setMethod("GET");
		return httpRequest.getResponse();
	}

	public static void main(String[] args) throws Exception {
		StockIDCrawler crawler = new StockIDCrawler();
		crawler.getAllStockID();
	}
}
