package org.elasticsearch.index.engine;

import org.elasticsearch.common.inject.AbstractModule;
import org.elasticsearch.index.engine.internal.AsynchronousEngine;

/**
 *
 */
public class AsynchEngineModule extends AbstractModule  {
	
	   

	
	 @Override
	    protected void configure() {
		 System.out.println("AsynchEngineModule.configure()");
	        bind(Engine.class).to(AsynchronousEngine.class).asEagerSingleton();
	    }
}
