package org.yuriak.dao;

import java.security.interfaces.RSAKey;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.yuriak.bean.StockBean;
import org.yuriak.config.CommonValue;
import org.yuriak.util.CommonUtil;
import org.yuriak.util.MyDBUtil;
import org.yuriak.util.MyFileUtil;

public class StockDao {
	
	private MyDBUtil myDBUtil;
	private Connection connection;
	private Statement statement;
	public StockDao(){
		myDBUtil=new MyDBUtil();
	}
	
	public ArrayList<StockBean> findStockByIDAndSortByDate(String StockID){
		try {
			ArrayList<StockBean> stockList=new ArrayList<StockBean>();
			connection=myDBUtil.getConnection();
			statement=connection.createStatement();
			ResultSet resultSet=statement.executeQuery("select * from "+CommonValue.TABLE_NAME+" where stockid='"+StockID+"' order by stockdate desc");
			while (resultSet.next()) {
				StockBean stock=new StockBean();
				stock.setID(resultSet.getString("stockid"));
				stock.setName(resultSet.getString("stockname"));
				stock.setPrice(resultSet.getDouble("stockprice"));
				stock.setScore(resultSet.getDouble("stockscore"));
				stock.setAdvice(resultSet.getInt("stockadvice"));
				stock.setEvaluation(resultSet.getString("stockevaluation"));
				stock.setDate(resultSet.getDate("stockdate"));
				stockList.add(stock);
			}
			resultSet.close();
			statement.close();
			connection.close();
			return stockList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally{
		      //finally block used to close resources
		      try{
		         if(statement!=null)
		        	 statement.close();
		      }catch(SQLException se2){
		      }// nothing we can do
		      try{
		         if(connection!=null)
		        	 connection.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
		   }
	}
	
	public ArrayList<StockBean> findStockByNameAndSortByDate(String StockName){
		try {
			ArrayList<StockBean> stockList=new ArrayList<StockBean>();
			connection=myDBUtil.getConnection();
			statement=connection.createStatement();
			ResultSet resultSet=statement.executeQuery("select * from "+CommonValue.TABLE_NAME+" where stockname='"+StockName+"' order by stockdate desc");
			while (resultSet.next()) {
				StockBean stock=new StockBean();
				stock.setID(resultSet.getString("stockid"));
				stock.setName(resultSet.getString("stockname"));
				stock.setPrice(resultSet.getDouble("stockprice"));
				stock.setScore(resultSet.getDouble("stockscore"));
				stock.setAdvice(resultSet.getInt("stockadvice"));
				stock.setEvaluation(resultSet.getString("stockevaluation"));
				stock.setDate(resultSet.getDate("stockdate"));
				stockList.add(stock);
			}
			resultSet.close();
			statement.close();
			connection.close();
			return stockList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally{
		      //finally block used to close resources
		      try{
		         if(statement!=null)
		        	 statement.close();
		      }catch(SQLException se2){
		      }// nothing we can do
		      try{
		         if(connection!=null)
		        	 connection.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
		   }
	}
	
	public ArrayList<StockBean> findOneDayStockInfo(Date date){
		try {
			ArrayList<StockBean> stockList=new ArrayList<StockBean>();
			connection=myDBUtil.getConnection();
			statement=connection.createStatement();
			ResultSet resultSet=statement.executeQuery("select * from "+CommonValue.TABLE_NAME+" where stockdate='"+date+"' order by stockscore desc");
			while (resultSet.next()) {
				StockBean stock=new StockBean();
				stock.setID(resultSet.getString("stockid"));
				stock.setName(resultSet.getString("stockname"));
				stock.setPrice(resultSet.getDouble("stockprice"));
				stock.setScore(resultSet.getDouble("stockscore"));
				stock.setAdvice(resultSet.getInt("stockadvice"));
				stock.setEvaluation(resultSet.getString("stockevaluation"));
				stock.setDate(resultSet.getDate("stockdate"));
				stockList.add(stock);
			}
			resultSet.close();
			statement.close();
			connection.close();
			return stockList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally{
		      //finally block used to close resources
		      try{
		         if(statement!=null)
		        	 statement.close();
		      }catch(SQLException se2){
		    	  se2.printStackTrace();
		      }// nothing we can do
		      try{
		         if(connection!=null)
		        	 connection.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
		   }
	}
	
	public void SaveOneDayStockInfoToDB(ArrayList<StockBean> stockList){
		try {
			connection=myDBUtil.getConnection();
			statement=connection.createStatement();
			for (StockBean stockBean : stockList) {
				String sql = "insert into stocktable(stockid,stockname,stockprice,stockscore,stockadvice,stockdate,stockevaluation)"
						+ " values('"+stockBean.getID()+"','"
						+stockBean.getName()+"',"
						+stockBean.getPrice()+","
						+stockBean.getScore()+","
						+stockBean.getAdvice()+",'"
						+stockBean.getDate()+"','"
						+stockBean.getEvaluation()+"')";
				statement.executeUpdate(sql);
			}
			statement.close();
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
		      try{
		         if(statement!=null)
		        	 statement.close();
		      }catch(SQLException se2){
		      }// nothing we can do
		      try{
		         if(connection!=null)
		        	 connection.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
		   }
	}
	
	public ArrayList<StockBean> getAllStockID(){
		try {
			ArrayList<StockBean> beans=new ArrayList<>();
			connection=myDBUtil.getConnection();
			statement=connection.createStatement();
			String sql="select * from "+CommonValue.STOCK_ID_TABLE_NAME;
			ResultSet resultSet=statement.executeQuery(sql);
			while (resultSet.next()) {
				StockBean bean=new StockBean();
				bean.setID(resultSet.getString("stockid"));
				beans.add(bean);
			}
			resultSet.close();
			statement.close();
			connection.close();
			return beans;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public void updateNewStock(ArrayList<StockBean> beans){
		ArrayList<StockBean> idBeans=getAllStockID();
		for (StockBean stockBean : beans) {
			if (!containID(stockBean, idBeans)) {
				try {
					connection=myDBUtil.getConnection();
					statement=connection.createStatement();
					String sql="insert into "+CommonValue.STOCK_ID_TABLE_NAME+"(stockid) values('"+stockBean.getID()+"')";
					statement.execute(sql);
					statement.close();
					connection.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
	}
	
	private boolean containID(StockBean bean,ArrayList<StockBean> Beans){
		for (StockBean stockBean : Beans) {
			if (stockBean.getID().equals(bean.getID())) {
				return true;
			}
		}
		return false;
	}
	
}
