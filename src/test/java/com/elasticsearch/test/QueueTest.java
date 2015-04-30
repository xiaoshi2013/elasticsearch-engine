package com.elasticsearch.test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.common.collect.Lists;
import org.elasticsearch.common.collect.Maps;
import org.elasticsearch.common.joda.time.DateTime;

import net.openhft.chronicle.Chronicle;
import net.openhft.chronicle.ChronicleQueueBuilder;
import net.openhft.chronicle.ExcerptAppender;
import net.openhft.chronicle.ExcerptTailer;

public class QueueTest {

	public static void main(String[] args) throws IOException {
		
		File f = new File("test");
		final List<String> lines = Files.readAllLines(Paths.get(f.toURI()), Charset.forName("UTF-8"));
		final int len = lines.size();

		List<Map> list=Lists.newArrayList();
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


			list.add(map);


		}
		
		
		String basePath = "/test1/queue";
		long start0=System.currentTimeMillis();

				Chronicle chronicle = ChronicleQueueBuilder.indexed(basePath)
					//	.indexBlockSize(32 << 20)
			          //  .dataBlockSize(128 << 20)
						.build();
				// Obtain an ExcerptAppender
				ExcerptAppender appender = chronicle.createAppender();
				System.out.println("init "+(System.currentTimeMillis()-start0)+"  ms");

				
				// Configure the appender to write up to 100 bytes 

				long start=System.currentTimeMillis();

				
				int size=list.size();
				for (int l = 0; l < 10000; l++) {
					for (int i = 0; i < size; i++) {
						appender.startExcerpt(); 
						appender.writeMap(list.get(i));
						appender.finish();

					}
				}
				

				// Commit 
				
				
				ExcerptTailer reader = chronicle.createTailer();

				
				System.out.println(System.currentTimeMillis()-start +"ms" +" queue"+reader.size());
				
				appender.close();
				reader.close();
				chronicle.close();
	}
}
