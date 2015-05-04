package org.elasticsearch.index.engine.internal;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.engine.Engine.Create;
import org.elasticsearch.index.settings.IndexSettings;
import org.elasticsearch.index.shard.AbstractIndexShardComponent;
import org.elasticsearch.index.shard.ShardId;
import org.elasticsearch.index.translog.Translog;

public class AddTranslogThread extends AbstractIndexShardComponent implements Runnable {

	private Translog translog;

	private final LiveVersionMap versionMap;

	private final LinkedBlockingQueue<Create> queue;

	int size = 33554432;

	// private final DB db;
	// private final Map mapdb;

	volatile boolean b = true;

	public AddTranslogThread(ShardId shardId, @IndexSettings Settings indexSettings,Translog translog, LiveVersionMap versionMap) {
		super(shardId, indexSettings);
		
		this.translog = translog;
		this.versionMap = versionMap;

		queue = new LinkedBlockingQueue<>(size);

		/*
		 * File[] fs= ((FsTranslog)translog).locations();
		 * 
		 * File fdb=new File("/test/mapdb");
		 * 
		 * db = DBMaker.newFileDB(fdb) .transactionDisable() .asyncWriteEnable()
		 * //.asyncWriteFlushDelay(100) .compressionEnable()
		 * .mmapFileEnableIfSupported().make(); mapdb= db.getTreeMap("es");
		 */
	}

	public void add(Create create) {

		int n = queue.size();
		if (n >= 1000) {
		    logger.info("Queue length is too long [{}]", n);

		}

		queue.add(create);

	}

	@Override
	public void run() {

		while (b) {
			Create create = queue.poll();
			if (create == null) {
				try {
					TimeUnit.MILLISECONDS.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				continue;

			} else {

				// mapdb.put(create.id(), create.source());
				org.elasticsearch.index.translog.Translog.Create operation = new Translog.Create(create);
				Translog.Location translogLocation = translog.add(operation);
				//versionMap.putUnderLock(create.uid().bytes(), new VersionValue(create.version(), translogLocation));
			}

		}

	}

	public void close() {
		while (!queue.isEmpty()) {
			try {
				TimeUnit.MILLISECONDS.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		b = false;

		// db.commit();
		// db.close();

	}

}
