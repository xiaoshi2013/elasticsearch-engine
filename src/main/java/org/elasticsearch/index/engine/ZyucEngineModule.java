package org.elasticsearch.index.engine;

import org.elasticsearch.common.inject.AbstractModule;
import org.elasticsearch.index.engine.internal.ZyucInternalEngine;

/**
 *
 */
public class ZyucEngineModule extends AbstractModule  {
	
	   

	
	 @Override
	    protected void configure() {
		 System.out.println("ZyucEngineModule.configure()");
	        bind(Engine.class).to(ZyucInternalEngine.class).asEagerSingleton();
	    }
}
