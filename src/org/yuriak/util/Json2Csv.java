package org.yuriak.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Json2Csv {

	public static void main(String[] args) throws JSONException, IOException {
		JSONObject object=new JSONObject(FileUtils.readFileToString(new File("data/stockInfo.txt")));
		JSONArray array=object.getJSONArray("StockInfo");
		System.out.println(CDL.toString(array));
	}
}
