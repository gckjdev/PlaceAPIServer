package com.orange.common.cassandra;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.ObjectSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.serializers.UUIDSerializer;
import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.Serializer;
import me.prettyprint.hector.api.beans.ColumnSlice;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.beans.Rows;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.MutationResult;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.ColumnQuery;
import me.prettyprint.hector.api.query.MultigetSliceQuery;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;
import me.prettyprint.hector.api.query.SliceQuery;

public class CassandraClient {
	Cluster  cluster;
	Keyspace keyspace;
	
    final static StringSerializer ss = StringSerializer.get();
    final static LongSerializer ls = LongSerializer.get();
    final static UUIDSerializer us = UUIDSerializer.get();
    
    final static int MAX_COUNT_FOR_MULTI_ROW = 50;
	
	public CassandraClient(String serverNameAndPort, String clusterName, String keyspaceName){
		this.initServer(serverNameAndPort, clusterName);
		this.initKeyspace(keyspaceName);
		
		assert(cluster != null && keyspace != null);
	}
	
	public void initServer(String serverNameAndPort, String clusterName){	// e.g. "localhost:9160"
		cluster = HFactory.getOrCreateCluster(clusterName, new CassandraHostConfigurator(serverNameAndPort));		
	}
	
	public void initKeyspace(String keyspaceName){
		if (cluster == null){
			return;
		}
		keyspace = HFactory.createKeyspace(keyspaceName, cluster);				
	}
	
	
	public boolean insert(String columnFamilyName, String key, String columnName, String columnValue){
		Mutator<String> mutator = HFactory.createMutator(keyspace, StringSerializer.get());
		mutator.addInsertion(key, columnFamilyName, HFactory.createStringColumn(columnName, columnValue));
		mutator.execute();
		return true;
	}
	
	public boolean insert(String columnFamilyName, String key, String[] columnNames, String[] columnValues){
		Mutator<String> mutator = HFactory.createMutator(keyspace, StringSerializer.get());
		int len = columnNames.length;
		for (int i=0; i<len; i++){
			String columnName = columnNames[i];
			String columnValue = columnValues[i];
			if (columnValue == null){
				mutator.addInsertion(key, columnFamilyName, HFactory.createStringColumn(columnName, ""));				
			}
			else{
				mutator.addInsertion(key, columnFamilyName, HFactory.createStringColumn(columnName, columnValue));
			}
		}
		mutator.execute();
		return true;
	}
	
	public boolean insert(String columnFamilyName, String key, Map<String, String> columnValueMap){
		
		Mutator<String> mutator = HFactory.createMutator(keyspace, StringSerializer.get());
		for (Map.Entry<String, String> entry : columnValueMap.entrySet()) {
			String columnName = entry.getKey();
			String columnValue = entry.getValue();		
			if (columnValue == null){
				mutator.addInsertion(key, columnFamilyName, HFactory.createStringColumn(columnName, ""));				
			}
			else{
				mutator.addInsertion(key, columnFamilyName, HFactory.createStringColumn(columnName, columnValue));
			}
		}
		
		mutator.execute();
		return true;
	}
	
	public boolean insert(String columnFamilyName, String key, UUID columnName, String columnValue){		
		Mutator<String> mutator = HFactory.createMutator(keyspace, ss);
		HColumn<UUID, String> column = HFactory.createColumn(columnName, columnValue, us, ss);
		mutator.insert(key, columnFamilyName, column);
		return true;
	}

	public String getColumnValue(String columnFamilyName, String key, String columnName){
		ColumnQuery<String, String, String> columnQuery = HFactory.createStringColumnQuery(keyspace);
		if (columnQuery == null)
			return null;
		
		columnQuery.setColumnFamily(columnFamilyName).setKey(key).setName(columnName); 		
		QueryResult<HColumn<String, String>> result = columnQuery.execute();        
		if (result == null){
			return null;
		}
		
		HColumn<String, String> columnNameValue = result.get();
		if (columnNameValue == null)
			return null;
		
        return columnNameValue.getValue();		
	}

	public List<HColumn<String, String>> getColumnKey(String columnFamilyName, String key, String... columnNames){
		StringSerializer se = StringSerializer.get();
		SliceQuery<String, String, String> q = HFactory.createSliceQuery(keyspace, se, se, se);		
		if (q == null){
			return null;
		}
		
		q.setColumnFamily(columnFamilyName).setKey(key).setColumnNames(columnNames);
		
		QueryResult<ColumnSlice<String, String>> r = q.execute();
		if (r == null){
			return null;
		}
		
		ColumnSlice<String, String> slices = r.get();
		if (slices == null){
			return null;
		}
		
		List<HColumn<String, String>> result = slices.getColumns();
		
		// print for test TODO rem the code
		System.out.println("get data result size="+result.size());
		for (HColumn<String, String> data : result){
			System.out.println("column["+data.getName()+"]="+data.getValue());
		}
		
        return result;		
	}
	
	public List<HColumn<UUID, String>> getColumnKeyByRange(String columnFamilyName, String key, UUID start, int size){
		SliceQuery<String, UUID, String> q = HFactory.createSliceQuery(keyspace, ss, us, ss);		
		if (q == null){
			return null;
		}
		
		q.setColumnFamily(columnFamilyName).setKey(key).setRange(start, null, true, size);
		
		QueryResult<ColumnSlice<UUID, String>> r = q.execute();
		if (r == null){
			return null;
		}
		
		List<HColumn<UUID, String>> result = r.get().getColumns();
		
		// print for test TODO rem the code
		System.out.println("get data result size="+result.size());
		for (HColumn<UUID, String> data : result){
			System.out.println("column["+data.getName()+"]="+data.getValue());
		}
		
        return result;		
	}
	
	public Rows<String, String, String> getMultiRow(String columnFamilyName, String[] keys,
			String... columnNames){	
		MultigetSliceQuery<String, String, String> multigetSliceQuery =
		    HFactory.createMultigetSliceQuery(keyspace, ss, ss, ss);
		multigetSliceQuery.setColumnFamily(columnFamilyName);
		multigetSliceQuery.setKeys(keys);
		multigetSliceQuery.setColumnNames(columnNames);
		QueryResult<Rows<String, String, String>> result = multigetSliceQuery.execute();
		Rows<String, String, String> rows = result.get();
		if (rows == null){
			return null;
		}
		
		// for test, TODO rem the code
		for (Row<String, String, String> row : rows){
			System.out.println("row key : "+row.getKey());
			ColumnSlice<String, String> columns = row.getColumnSlice();
			List<HColumn<String, String>> list = columns.getColumns();
			for (HColumn<String, String> data : list){
				System.out.println("column["+data.getName()+"]="+data.getValue());
			}
		}
		
		return rows;
	}

	
	public Rows<String, String, String> getMultiRow(String columnFamilyName, String...keys){	
		MultigetSliceQuery<String, String, String> multigetSliceQuery =
		    HFactory.createMultigetSliceQuery(keyspace, ss, ss, ss);
		multigetSliceQuery.setColumnFamily(columnFamilyName);
		multigetSliceQuery.setKeys(keys);	
		multigetSliceQuery.setRange("", "", true, MAX_COUNT_FOR_MULTI_ROW);
		QueryResult<Rows<String, String, String>> result = multigetSliceQuery.execute();
		Rows<String, String, String> rows = result.get();
		if (rows == null){
			return null;
		}
		
		// for test, TODO rem the code
		for (Row<String, String, String> row : rows){
			System.out.println("row key : "+row.getKey());
			ColumnSlice<String, String> columns = row.getColumnSlice();
			List<HColumn<String, String>> list = columns.getColumns();
			for (HColumn<String, String> data : list){
				System.out.println("column["+data.getName()+"]="+data.getValue());
			}
		}
		
		return rows;
	}

	public Rows<String, String, String> getMultiRow(String columnFamilyName,
			int maxCount) {
		
		RangeSlicesQuery<String, String, String> q =
		    HFactory.createRangeSlicesQuery(keyspace, ss, ss, ss);

		q.setColumnFamily(columnFamilyName);
		q.setKeys("", "");
		q.setRange("", "", true, maxCount);
		QueryResult<OrderedRows<String, String, String>> result = q.execute();
		OrderedRows<String, String, String> rows = result.get();
		if (rows == null){
			return null;
		}
		
		// for test, TODO rem the code
		for (Row<String, String, String> row : rows){
			System.out.println("row key : "+row.getKey());
			ColumnSlice<String, String> columns = row.getColumnSlice();
			List<HColumn<String, String>> list = columns.getColumns();
			for (HColumn<String, String> data : list){
				System.out.println("column["+data.getName()+"]="+data.getValue());
			}
		}		
		
		
		
		return rows;
	}

	public boolean deleteStringColumn(String columnFamilyName, String key, String columnName){
		Mutator<String> mutator = HFactory.createMutator(keyspace, ss);
		mutator.delete(key, columnFamilyName, columnName, ss);
		mutator.execute();
		return true;
	}
	
	public boolean deleteUUIDColumn(String columnFamilyName, String key, UUID uuid){
		Mutator<String> mutator = HFactory.createMutator(keyspace, ss);
		mutator.delete(key, columnFamilyName, uuid, us);
		mutator.execute();
		return true;
	}
}
