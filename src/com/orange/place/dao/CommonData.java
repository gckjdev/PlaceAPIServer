package com.orange.place.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.prettyprint.hector.api.beans.ColumnSlice;
import me.prettyprint.hector.api.beans.HColumn;

public class CommonData {

	Map<String, String> keyValueList;
	Map<String, String> actionCounterMap;

	public Map<String, String> getActionCounterMap() {
		return actionCounterMap;
	}

	public void setActionCounterMap(Map<String, String> actionCounterMap) {
		this.actionCounterMap = actionCounterMap;
	}

	public void setActionCounterMap(List<HColumn<String, String>> columns) {
		for (HColumn<String, String> column : columns) {
			actionCounterMap.put(column.getName(), column.getValue());
		}
	}

	private CommonData() {

	}

	public CommonData(List<HColumn<String, String>> columnValues) {
		keyValueList = new HashMap<String, String>();
		actionCounterMap = new HashMap<String, String>();
		for (HColumn<String, String> data : columnValues) {
			keyValueList.put(data.getName(), data.getValue());
		}
	}

	public CommonData(Map<String, String> mapColumnValues) {
		this.keyValueList = mapColumnValues;
	}

	public String getKey(String key) {
		String value = keyValueList.get(key);
		return (value == null) ? "" : value;
	}

	public void addValues(List<HColumn<String, String>> columnValues) {
		for (HColumn<String, String> data : columnValues) {
			keyValueList.put(data.getName(), data.getValue());
		}
	}

	public void addValues(String key, int value) {
		keyValueList.put(key, String.valueOf(value));
	}

	public void addValues(String key, String values) {
		keyValueList.put(key, values);
	}

	public void removeData(String key) {
		keyValueList.remove(key);
	}

	public boolean editData(String key, String value) {
		if (!keyValueList.containsKey(key))
			return false;
		removeData(key);
		addValues(key, value);
		return true;
	}

	public void printData() {
		System.out.println(keyValueList.toString());
	}
}
