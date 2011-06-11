package com.orange.common.cassandra;
  
import me.prettyprint.cassandra.connection.ConcurrentHClientPool;
import me.prettyprint.cassandra.connection.HThriftClient;
import me.prettyprint.cassandra.service.CassandraHost;
public class CassandraTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CassandraHost host = new CassandraHost("192.168.1.101", 9160);
		ConcurrentHClientPool pool = new ConcurrentHClientPool(host);
		HThriftClient client = pool.borrowClient();
		CassandraClient cclient ;

///		CassandraClientPool pool = CassandraClientPoolFactory.INSTANCE.get(); 
	}

}
;