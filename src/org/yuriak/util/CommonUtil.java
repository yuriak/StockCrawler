package org.yuriak.util;

public class CommonUtil {
	public static int convertAdviceWord2Number(String advice){
		switch (advice) {
		case "买入":
			return 4;
		case "增持":
			return 3;
		case "中性":
			return 2;
		case "减持":
			return 1;
		case "卖出":
			return 0;
		default:
			return -1;
		}
	}
	
//	public static void main(String[] args) {
//		String advice="买入";
//		System.out.println(convertAdviceWord2Number(advice));
//	}
}
