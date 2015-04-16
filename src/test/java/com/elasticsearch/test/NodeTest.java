package com.elasticsearch.test;

import org.elasticsearch.cluster.ClusterName;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.index.engine.ZyucEngineModule;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;


public class NodeTest {
	
	
	public void node(){
		
		//System.out.println("NodeTest.node() "+ NRTFsIndexStoreModule.class.getName());
		 Node build = NodeBuilder.nodeBuilder().data(true).settings(ImmutableSettings.builder()
	                .put(ClusterName.SETTING, "es-monitor")
	               // .put("discovery.zen.ping.multicast.enabled",false )
	                //.put("node.name", "zl")
	                .put("http.cors.enabled", true)
	                .put(IndexMetaData.SETTING_NUMBER_OF_SHARDS, 1)
	                .put(IndexMetaData.SETTING_NUMBER_OF_REPLICAS, 1)
	                //.put(EsExecutors.PROCESSORS, 1) // limit the number of threads created
	                .put("http.enabled", true)
	                //.put("index.store.type", NRTFsIndexStoreModule.class.getName())
	               // .put("index.engine.type", ZyucEngineModule.class.getName())
	               // .put("config.ignore_system_properties", true) // make sure we get what we set :)
	               // .put("gateway.type", "none")
	                )
	                
	                .build();
	        build.start();
		
	}
	
	
	public static void main(String[] args) {
		NodeTest test=new NodeTest();
		test.node();
		
		
		
	}

}
