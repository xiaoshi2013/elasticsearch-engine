package com.elasticsearch.test;

import java.util.ArrayDeque;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;



public class TestQueue {
	

	
	static void testBlockingQueue() throws InterruptedException{
		 final int n=10_000_0000;
		// final ArrayDeque<String> queue = new ArrayDeque(n);
		// final LinkedBlockingDeque<String> queue=new LinkedBlockingDeque<>(n);
		 final LinkedBlockingQueue<String> queue=new LinkedBlockingQueue<>(n);
		// final ArrayBlockingQueue<String> queue=new ArrayBlockingQueue<>(n);
		 
		 //final ConcurrentLinkedDeque<String> queue=new ConcurrentLinkedDeque<>(); // 最快 17秒之内
		// final ConcurrentLinkedQueue<String> queue=new ConcurrentLinkedQueue<>(); // 第二快
		 
		 final boolean b=true;
		
		 final AtomicInteger atom=new AtomicInteger();
		
	
		 final long start=System.currentTimeMillis();

		 Thread t=new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(b){
					if(atom.get()==n){
						 System.out.println("---- "+(System.currentTimeMillis()-start)+" ms");
						break;
					}
					if(queue.isEmpty()){
						try {
						//System.out.println("-- "+atom);
							TimeUnit.MILLISECONDS.sleep(10);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
			
					String str=queue.poll();
					if(str!=null){
						atom.incrementAndGet();

					}

				
				}
				
			}
		});
		 
		 t.start();
		 for (int i = 0; i < n; i++) {
			 
			 try {
				 if(queue.size()>=1_000_000){
					 System.out.println("queue.size > "+1_000_000);
					 TimeUnit.MILLISECONDS.sleep(50);

				 }
				 
					queue.add(i+"sdesf334");

			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		 
		 System.out.println((System.currentTimeMillis()-start)+" ms");
		 System.out.println(atom);

		
		 
	}
	public static void main(String[] args) throws InterruptedException {
		testBlockingQueue();
	}

}
