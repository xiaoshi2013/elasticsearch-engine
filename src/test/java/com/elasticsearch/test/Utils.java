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

import com.alibaba.fastjson.JSON;

public class Utils {

	public static List<String> getMsgList() throws IOException {

		File f = new File("test");
		final List<String> lines = Files.readAllLines(Paths.get(f.toURI()), Charset.forName("UTF-8"));
		final int len = lines.size();

		List<String> list = Lists.newArrayList();
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

			// map.put("@timestamp", dt);
			DateTime dtc = new DateTime();
			// System.out.println(dtc);
			// map.put("@timestamp_c",dtc);

			map.put("status_code", StringUtils.split(msg)[3].split("/")[0]);
			map.put("status", StringUtils.split(msg)[3].split("/")[1]);
			map.put("size", Integer.valueOf(StringUtils.split(msg)[4]));
			map.put("method", StringUtils.split(msg)[5]);

			String k = JSON.toJSONString(map);
			list.add(k);

		}
		return list;

	}
	
	public static List<Map> getListMap() throws IOException {

		File f = new File("test");
		final List<String> lines = Files.readAllLines(Paths.get(f.toURI()), Charset.forName("UTF-8"));
		final int len = lines.size();

		List<Map> list = Lists.newArrayList();
		for (int i = 0; i < len; i++) {

			String msg = lines.get(i);
			Map map = Maps.newHashMap();

			map.put("@message", msg);
			map.put("response", Integer.valueOf(StringUtils.split(msg)[1]));
			map.put("srcip", StringUtils.split(msg)[2]);
			String time = StringUtils.substring(msg, 0, 14);
			time = time.replaceAll("\\.", "");
			DateTime dt = new DateTime(Long.valueOf(time));
			dt = dt.withYear(2015);

			map.put("@timestamp", dt);
			DateTime dtc = new DateTime();
			// System.out.println(dtc);
			// map.put("@timestamp_c",dtc);

			map.put("status_code", StringUtils.split(msg)[3].split("/")[0]);
			map.put("status", StringUtils.split(msg)[3].split("/")[1]);
			map.put("size", Integer.valueOf(StringUtils.split(msg)[4]));
			map.put("method", StringUtils.split(msg)[5]);

			list.add(map);

		}
		return list;

	}
}
