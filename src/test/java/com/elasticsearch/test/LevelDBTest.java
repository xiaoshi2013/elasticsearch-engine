package com.elasticsearch.test;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.collect.Lists;
import org.elasticsearch.common.collect.Maps;
import org.elasticsearch.common.joda.time.DateTime;
import org.iq80.leveldb.*;

import com.alibaba.fastjson.JSON;

import static org.fusesource.leveldbjni.JniDBFactory.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class LevelDBTest {
	
	public 	List<String> getMsgList() throws IOException{
		
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

	public void test1() throws IOException {
		long start=System.currentTimeMillis();
		Options options = new Options();
		options.blockSize(16 * 1024);
		//options.writeBufferSize(16*1024*1024);
		//options.cacheSize(100 * 1048576); // 100MB cache
		options.createIfMissing(true);
		final DB	db = factory.open(new File("D:/test/leveldb/db"), options);
		System.out.println("time1 "+(System.currentTimeMillis()-start)+" ms");

		final List<String> list=getMsgList();
		final long start1=System.currentTimeMillis();
		
		//final CountDownLatch down=new CountDownLatch(2);
		try {
		
			final int n=list.size();
			System.out.println("n "+n);
			WriteBatch batch = db.createWriteBatch();

			int l=0;
			for (int i = 0; i < 1000; i++) {

				for (int j = 0; j < n; j++) {
					batch.put(bytes(Strings.base64UUID()), bytes(list.get(j)));
				//	batch.put(Strings.base64UUID().getBytes(Charsets.UTF_8),list.get(j).getBytes(Charsets.UTF_8));
					l++;
				}
				if(l%10000==0){
					 db.write(batch);
					   batch.close();
					   batch = db.createWriteBatch();

				}
				
			}
			 db.write(batch);
			   batch.close();
			System.out.println(Thread.currentThread().getName()+" time2 "+(System.currentTimeMillis()-start1)+" ms");
			
		/*for (int i = 0; i < 2; i++) {
			Thread t=new Thread(new Runnable() {
				
				@Override
				public void run() {
					for (int i = 0; i < 1000; i++) {
						for (int j = 0; j < n; j++) {
							db.put(bytes(Strings.base64UUID()), bytes(list.get(j)));
						}

					}
					System.out.println(Thread.currentThread().getName()+" time2 "+(System.currentTimeMillis()-start1)+" ms");
					//down.countDown();
					
				}
			});
			t.start();
			
		}*/
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {



			try {
				//down.await();
				db.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("time3 "+(System.currentTimeMillis()-start1)+" ms");
		}
	}

	public static void main(String[] args) throws IOException {
		LevelDBTest test=new LevelDBTest();
		
		test.test1();
	}

}
