package org.elasticsearch.plugin.engine;

import java.util.Collection;

import org.elasticsearch.common.component.LifecycleComponent;
import org.elasticsearch.common.inject.Module;
import org.elasticsearch.index.CloseableIndexComponent;
import org.elasticsearch.index.engine.EngineModule;
import org.elasticsearch.plugins.AbstractPlugin;

public class EnginePlugin extends AbstractPlugin  {

	@Override
	public String name() {
		  return "es-engine";
	}

	@Override
	public String description() {
	    return "es engine";
	}
	
	  /* @Override
	    public Collection<Class<? extends Module>> modules() {
	        Collection<Class<? extends Module>> modules = Lists.newArrayList();
	        modules.add(ZyucEngineModule.class);
	        return modules;
	    }*/
	   
	   @Override public void processModule(Module module) {
		   
		   if(module instanceof EngineModule){
			   
		   }
	    //  System.out.println("-------EnginePlugin.processModule() "+module.getClass());
		   
	    }
	   
	   @Override
	public Collection<Class<? extends CloseableIndexComponent>> shardServices() {
		//System.out.println("EnginePlugin.shardServices()*********");
		   Collection<Class<? extends CloseableIndexComponent>> modules= super.shardServices();
		 //  System.out.println(modules);
		return super.shardServices();
	}

	   @Override
	public Collection<Class<? extends LifecycleComponent>> services() {
	//System.out.println("EnginePlugin.services()  ==========");
		return super.services();
	}
}
