package com.elasticsearch.test;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

import org.elasticsearch.action.WriteConsistencyLevel;
import org.elasticsearch.action.admin.indices.settings.get.GetSettingsResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.replication.ReplicationType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.joda.time.DateTime;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;

public class IndexTest {
	
	private Client client = null;

	public IndexTest(String clusteranme,String... esHost) {
		Settings settings = ImmutableSettings.settingsBuilder()
				.put("client.transport.ping_timeout", 10000)
				 .put("client.transport.sniff", true)
				 .put("cluster.name", clusteranme)
				.build();

		TransportClient tc = new TransportClient(settings);
		System.out.println(Arrays.toString(esHost));
		for (int i = 0; i < esHost.length; i++) {
			tc.addTransportAddress(new InetSocketTransportAddress(esHost[i], 9300));

		}

		this.client = tc;
	}

	public void test() throws IOException{
		
		final List<Map> list=Utils.getListMap();

		final int size=list.size();
		
		
		final CountDownLatch down=new CountDownLatch(8);
		
		long start0=System.currentTimeMillis();

		for (int i1 = 0; i1 < 8; i1++) {
			Thread t=new Thread(new Runnable() {
				
				@Override
				public void run() {
					for (int j = 0; j < 1250; j++) {
						long start=System.currentTimeMillis();
						
						/*String pid=new DateTime().getMillis()+"";
						map.put("@timestamp", new DateTime().getMillis());

						
						 client.prepareIndex("msg-test2","branch").setSource(map).setId(pid).execute().actionGet();*/
						 

						BulkRequestBuilder bulkRequest = client.prepareBulk();

						for (int i = 0; i < size; i++) {
							Map<String, Object> map=list.get(i);
							DateTime dt= new  DateTime( map.get("@timestamp"));
							map.put("@timestamp", new DateTime( (dt.toLocalDate().minusDays(ThreadLocalRandom.current().nextInt(100)).toDate().getTime()-1)));
							
							bulkRequest.add( client.prepareIndex("notemplate-test1","employee").setSource(map))
							.setConsistencyLevel(WriteConsistencyLevel.ONE)
							.setReplicationType(ReplicationType.ASYNC)
							;

						}
						
						BulkResponse bulkResponse = bulkRequest.execute().actionGet();
					//	if(bulkResponse.getTookInMillis()>=100){
							System.out.println(Thread.currentThread().getName()+ "  Took "+bulkResponse.getTookInMillis()+"ms   "+(System.currentTimeMillis()-start)+" ms");

					//	}
					}
					down.countDown();
				}
			});
			t.start();

		
			
		}

		

		try {
			down.await();
			System.out.println("over  "+(System.currentTimeMillis()-start0)+" ms");

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	
	public void testQuery(){
		
	/*SearchResponse res=	client.prepareSearch("msg-test1").setSize(0)
			.addAggregation(AggregationBuilders.terms("tim").field("@timestamp")
					.order(Order.count(false)).size(1000)).execute().actionGet();*/
	
	
	/*SearchResponse res=	client.prepareSearch("msg-test1").setSize(0)
			.addAggregation(AggregationBuilders.dateHistogram("tim").field("@timestamp").interval(Interval.DAY)
					.order(org.elasticsearch.search.aggregations.bucket.histogram.Histogram.Order.KEY_ASC))
					.execute().actionGet();*/
	
	
		SearchResponse res=	client.prepareSearch("kafka-test4").setQuery(QueryBuilders.matchAllQuery())
				.addSort(SortBuilders.fieldSort("@timestamp"))
				.execute().actionGet();
		
	
	System.out.println(res);
		
	
	}
	
	
	public void testAgg(){
		SearchResponse res=	client.prepareSearch("msg-test1")
				.addAggregation(AggregationBuilders.extendedStats("tim").field("@timestamp"))
						.execute().actionGet();
				
			
			System.out.println(res);
	}
	
	
	public void testMapping(String index){
	/*	  String mapping = "{";
	        Map<String, String> rootTypes = new HashMap<>();
	        //just pick some example from DocumentMapperParser.rootTypeParsers
	        rootTypes.put(SizeFieldMapper.NAME, "{\"enabled\" : true}");
	        rootTypes.put(IndexFieldMapper.NAME, "{\"enabled\" : true}");
	        rootTypes.put(SourceFieldMapper.NAME, "{\"enabled\" : true}");
	        rootTypes.put(TypeFieldMapper.NAME, "{\"store\" : true}");
	        rootTypes.put("include_in_all", "true");
	        rootTypes.put("index_analyzer", "\"standard\"");
	        rootTypes.put("search_analyzer", "\"standard\"");
	        rootTypes.put("analyzer", "\"standard\"");
	        rootTypes.put("dynamic_date_formats", "[\"yyyy-MM-dd\", \"dd-MM-yyyy\"]");
	        rootTypes.put("numeric_detection", "true");
	        rootTypes.put("dynamic_templates", "[]");
	        for (String key : rootTypes.keySet()) {
	            mapping += "\"" + key+ "\"" + ":" + rootTypes.get(key) + ",\n";
	        }
	        mapping += "\"properties\":{}}" ;
	        
	        System.out.println(mapping);*/
	        
	        GetSettingsResponse  res=	client.admin().indices().prepareGetSettings(index).execute().actionGet();
	        
	        
	        System.out.println(res.getIndexToSettings().get(index).toDelimitedString(','));

	        
	        
	}
	

	public static void main(String[] args) throws IOException {
		// DateTimeZone.setDefault(DateTimeZone.forID("Asia/Shanghai"));  
		//String[] ips = { "localhost" };
		String[] ips = { "192.168.6.207" };

		
		//IndexTest test=new IndexTest("es-monitor", ips);
		IndexTest test=new IndexTest("test", ips);

		test.test();
/*for (int i = 0; i < 100; i++) {
	test.testQuery();
}
	*/
		
		//test.testAgg();
		test.client.close();
		
	}
}
