package org.yuriak.util;

import java.awt.Font;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickMarkPosition;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.LineRenderer3D;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.Hour;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.ui.TextAnchor;
import org.yuriak.bean.StockBean;

import com.orsoncharts.Chart3DFactory;

public class MyChartUtil {
	public static void drawAllStockXYLineChartInOneDay(ArrayList<StockBean> stockList){
		if (stockList==null||stockList.size()<=0) {
			return;
		}
		DefaultXYDataset dataset=new DefaultXYDataset();
		JFreeChart chart=ChartFactory.createXYLineChart("stock"+stockList.get(0).getDate(), "count", "price", dataset);
		double[] y=new double[stockList.size()];
		double[] x=new double[stockList.size()];
		for (int i = 0; i < stockList.size(); i++) {
			x[i]=i;
			y[i]=stockList.get(i).getPrice();
		}
		double[][] xy={x,y};
		dataset.addSeries("All", xy);
		ChartFrame frame=new ChartFrame("stockChartFrame", chart);
		frame.pack();
		frame.setVisible(true);
	}
	
	public static void drawOneStockTimeTrendScoreChart(ArrayList<StockBean> stockList){
		if (stockList==null||stockList.size()<=0) {
			return;
		}
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		TimeSeries scoreTimeSeries=new TimeSeries("Score",Day.class);
		TimeSeries priceTimeSeries=new TimeSeries("Price",Day.class);
		TimeSeries adviceTimeSeries=new TimeSeries("Advice",Day.class);
		TimeSeriesCollection dataset=new TimeSeriesCollection();
		dataset.setDomainIsPointsInTime(true);
		for (int i = 0; i < stockList.size(); i++) {
			Day day=new Day(stockList.get(i).getDate());
			scoreTimeSeries.add(day,stockList.get(i).getScore()*10);
			priceTimeSeries.add(day,stockList.get(i).getPrice());
			adviceTimeSeries.add(day,stockList.get(i).getAdvice()*10);
		}
		dataset.addSeries(scoreTimeSeries);
		dataset.addSeries(priceTimeSeries);
		dataset.addSeries(adviceTimeSeries);
		JFreeChart chart=ChartFactory.createTimeSeriesChart(stockList.get(0).getName(), "time", "price/score", dataset);
		chart.getTitle().setFont(new Font("宋体",Font.BOLD,20));
		XYLineAndShapeRenderer renderer=new XYLineAndShapeRenderer();
		renderer.setBaseItemLabelGenerator(new StandardXYItemLabelGenerator());
		renderer.setBaseItemLabelsVisible(true);
		renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.CENTER, TextAnchor.BOTTOM_CENTER));
		renderer.setItemLabelAnchorOffset(10D);
		XYPlot plot=chart.getXYPlot();
		plot.setRenderer(renderer);
		DateAxis dateAxis=(DateAxis)plot.getDomainAxis();
		dateAxis.setTickUnit(new DateTickUnit(DateTickUnit.HOUR, 1,df));
		dateAxis.setVerticalTickLabels(true);
		dateAxis.setAutoTickUnitSelection(true); // 由于横轴标签过多，这里设置为自动格式 。  
        dateAxis.setDateFormatOverride(df);  
        dateAxis.setTickMarkPosition(DateTickMarkPosition.START); 
		ValueAxis rangeAxis=plot.getRangeAxis();  
		rangeAxis.setTickLabelFont(new Font("宋体",Font.BOLD,10));//设置y轴坐标上的字体  
		rangeAxis.setLabelFont(new Font("宋体",Font.BOLD,10));//设置y轴坐标上的标题的字体  
		ChartFrame frame=new ChartFrame("timeTrendChart", chart);
		frame.pack();
		frame.setVisible(true);
	}
	
}
