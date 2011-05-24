package com.orange.common.cassandra;

import java.util.List;
import java.util.Map;

import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.ColumnSlice;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.MutationResult;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.ColumnQuery;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.SliceQuery;

public class CassandraClient {
	Cluster  cluster;
	Keyspace keyspace;
	
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
		
		// print for test
		System.out.println("get data result size="+result.size());
		for (HColumn<String, String> data : result){
			System.out.println("column["+data.getName()+"]="+data.getValue());
		}
		
        return result;		
	}
}
