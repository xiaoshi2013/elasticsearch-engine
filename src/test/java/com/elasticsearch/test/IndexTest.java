package com.elasticsearch.test;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.elasticsearch.action.admin.indices.settings.get.GetSettingsResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.joda.time.DateTime;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.search.aggregations.AggregationBuilders;

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

		int size=list.size();
		
		
		Random random=new Random();

		
		
		for (int j = 0; j < 30; j++) {
			long start=System.currentTimeMillis();
			
			/*String pid=new DateTime().getMillis()+"";
			map.put("@timestamp", new DateTime().getMillis());

			
			 client.prepareIndex("msg-test2","branch").setSource(map).setId(pid).execute().actionGet();*/
			 

			BulkRequestBuilder bulkRequest = client.prepareBulk();

			for (int i = 0; i < size; i++) {
				/*DateTime dt=new DateTime();
				//System.out.println(dt);
				//System.out.println("dt "+new DateTime(  dt.toLocalDate().minusDays(3).toDate().getTime()).toString());
				map.put("@timestamp", new DateTime( (dt.toLocalDate().minusDays(random.nextInt(100)).toDate().getTime()-1)).getMillis());
				map.put("message", "中华人民共和国成立了,我是中国人 ，南京市长江大桥");
				map.put("size", i);
				//bulkRequest.add( client.prepareIndex("msg-test2","employee").setSource(map).setParent(pid));
				bulkRequest.add( client.prepareIndex("kafka-test2","employee").setSource(map));*/
				
				Map<String, Object> map=list.get(i);

				
				bulkRequest.add( client.prepareIndex("kafka-test3","employee").setSource(map));

				

			}
			
			BulkResponse bulkResponse = bulkRequest.execute().actionGet();

			System.out.println("Took "+bulkResponse.getTookInMillis()+"ms");
			System.out.println((System.currentTimeMillis()-start)+" ms");
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
	
	
		SearchResponse res=	client.prepareSearch("msg-test1").setSize(0)
		.addAggregation(AggregationBuilders.stats("tim").field("@timestamp"))
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
		//String[] ips = { "192.168.6.203" };
		String[] ips = { "localhost" };

		
		IndexTest test=new IndexTest("es-monitor", ips);
		
		test.test();

		//test.testQuery();
		
		//test.testAgg();
		test.client.close();
		
	}
}
