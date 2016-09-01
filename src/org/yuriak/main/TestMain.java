package org.yuriak.main;

import java.util.ArrayList;
import java.util.Collections;

import org.yuriak.bean.StockBean;
import org.yuriak.util.MyFileUtil;

public class TestMain {

	public static void main(String[] args) {
		ArrayList<StockBean> beans=MyFileUtil.readInfoFromFile("data/stockInfo.txt");
		Collections.sort(beans);
		for (StockBean stockBean : beans) {
			System.out.println(stockBean.getScore());
		}
	}
}
