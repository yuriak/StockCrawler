package org.yuriak.util;

import java.util.ArrayList;

import org.yuriak.bean.StockBean;

public class SortUtil {

	public static void main(String[] args){  
        int score[] = {67, 69, 75, 87, 89, 90, 99, 100,99,90,69};  
        for (int i = 0; i < score.length -1; i++){    //最多做n-1趟排序  
            for(int j = 0 ;j < score.length - i - 1; j++){    //对当前无序区间score[0......length-i-1]进行排序(j的范围很关键，这个范围是在逐步缩小的)  
                if(score[j] < score[j + 1]){    //把小的值交换到后面  
                    int temp = score[j];  
                    score[j] = score[j + 1];  
                    score[j + 1] = temp;  
                }  
            }              
            System.out.print("第" + (i + 1) + "次排序结果：");  
            for(int a = 0; a < score.length; a++){  
                System.out.print(score[a] + "\t");  
            }  
            System.out.println("");  
        }  
            System.out.print("最终排序结果：");  
            for(int a = 0; a < score.length; a++){  
                System.out.print(score[a] + "\t");  
       }  
    }  
	
	
	public static ArrayList<StockBean> sort(ArrayList<StockBean> stockBeans){
		for (int i = 0; i < stockBeans.size()-1; i++) {
			for (int j = 0; j < stockBeans.size()-i-1; j++) {
				if (stockBeans.get(j).getScore()<stockBeans.get(j+1).getScore()) {
					StockBean temp=stockBeans.get(j);
					stockBeans.set(j, stockBeans.get(j+1));
					stockBeans.set(j+1, temp);
				}
			}
		}
		return stockBeans;
	}
	
}
