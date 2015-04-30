package com.elasticsearch.test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.base.Charsets;
import org.elasticsearch.common.collect.Lists;
import org.elasticsearch.common.collect.Maps;
import org.elasticsearch.common.joda.time.DateTime;
import org.mapdb.DB;
import org.mapdb.DBMaker;

import com.alibaba.fastjson.JSON;

public class MapDBTest {
	
	public static 	List<String> getMsgList() throws IOException{
		
		File f = new File("test");
		final List<String> lines = Files.readAllLines(Paths.get(f.toURI()), Charset.forName("UTF-8"));
		final int len = lines.size();

		List<String> list=Lists.newArrayList();
		for (int i = 0; i < len; i++) {

			String msg = lines.get(i);
			Map map = Maps.newHashMap();

			map.put("message", msg);
			map.put("response", Integer.valueOf(StringUtils.split(msg)[1]));
			map.put("srcip", StringUtils.split(msg)[2]);
			String time = StringUtils.substring(msg, 0, 14);
			time = time.replaceAll("\\.", "");
			DateTime dt = new DateTime(Long.valueOf(time));
			dt = dt.withYear(2015);
			


			//map.put("@timestamp", dt);
			DateTime dtc= new DateTime();
			//System.out.println(dtc);
			//map.put("@timestamp_c",dtc);
			
			map.put("status_code", StringUtils.split(msg)[3].split("/")[0]);
			map.put("status", StringUtils.split(msg)[3].split("/")[1]);
			map.put("size", Integer.valueOf(StringUtils.split(msg)[4]));
			map.put("method", StringUtils.split(msg)[5]);

			String k=JSON.toJSONString( map);
			list.add(k);


		}
		return list;
		
	}
	
	public static void main(String[] args) throws IOException {
		long start0=System.currentTimeMillis();
		File fdb=new File("/test1/mapdb");
		DB db = DBMaker.newFileDB(fdb)
				.compressionEnable()
	            .transactionDisable()
	            .closeOnJvmShutdown()
	            .asyncWriteEnable()
	            //.asyncWriteFlushDelay(100)
	            .compressionEnable()
	            .mmapFileEnableIfSupported().make();
		
		Map es= db.getTreeMap("es");
	
		System.out.println("init "+(System.currentTimeMillis()-start0)+"  ms "+es.size());
		
	
		final List<String> list=getMsgList();
		
		long start=System.currentTimeMillis();
		int size=list.size();
		System.out.println("size "+size);
		
		
		for (int l = 0; l < 1000; l++) {
			
			for (int i = 0; i <size ; i++) {
				es.put(Strings.base64UUID(), list.get(i));

			}
			
		}
		System.out.println(":: will commit es "+es.size());
		System.out.println((System.currentTimeMillis()-start) +"ms" +" es "+es.size());
		System.out.println(" es "+es.size());

		db.commit();
		System.out.println((System.currentTimeMillis()-start) +"ms" +" es "+es.size());

		db.close();
		   System.out.println("close");
		System.out.println(System.currentTimeMillis()-start +"ms");

	
	}

}
