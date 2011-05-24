package com.orange.place.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.prettyprint.hector.api.beans.HColumn;

public class CommonData {

	Map<String, String>	keyValueList;
	
	private CommonData(){
		
	}
	
	public CommonData(List<HColumn<String, String>> columnValues){
		keyValueList = new HashMap<String, String>();
		for (HColumn<String, String> data : columnValues){
			keyValueList.put(data.getName(), data.getValue());
		}
	}
	
	public CommonData(Map<String, String> mapColumnValues){
		this.keyValueList = mapColumnValues;
	}
	
	public String getKey(String key){
		return keyValueList.get(key);
	}
}
