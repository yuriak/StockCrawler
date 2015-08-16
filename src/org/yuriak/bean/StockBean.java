package org.yuriak.bean;

import java.sql.Date;
import java.sql.Timestamp;

public class StockBean implements Comparable<StockBean> {

	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getEvaluation() {
		return evalution;
	}
	public void setEvaluation(String evaluation) {
		this.evalution = evaluation;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Double getScore() {
		return score;
	}
	public void setScore(Double score) {
		this.score = score;
	}
	public int getAdvice() {
		return advice;
	}
	public void setAdvice(int advice) {
		this.advice = advice;
	}
	private String ID;
	private String name;
	private Double price;
	private Double score;
	private int advice;
	private String evalution;
	private Date date;
	@Override
	public int compareTo(StockBean o) {
		if (this.score>o.score) {
			return -1;
		}else if(this.score==o.score) {
			return 0;
		}else if (this.score<o.score) {
			return 1;
		}else {
			return 1;
		}
	}
	
	
	
	
}
