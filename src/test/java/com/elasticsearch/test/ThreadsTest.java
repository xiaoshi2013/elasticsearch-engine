package com.elasticsearch.test;

import static org.fusesource.leveldbjni.JniDBFactory.bytes;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.util.RamUsageEstimator;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.base.Charsets;

import static org.fusesource.leveldbjni.JniDBFactory.*;

public class ThreadsTest {

	public static void main(String[] args) {
		
		
		System.out.println(32 << 20);
		System.out.println(128 << 20);
		
		System.out.println( 4 << 20);
		
		long start=System.currentTimeMillis();
		int l=5000_000;
		for (int i = 0; i <l ; i++) {
			bytes(Strings.base64UUID());
			//Strings.base64UUID().getBytes(Charsets.UTF_8);
		}
		
		System.out.println((System.currentTimeMillis()-start)+" ms");
	}
}
