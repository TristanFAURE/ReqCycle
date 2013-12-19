package org.eclipse.reqcycle.traceability.storage.blueprints.storage;

import java.io.InputStream;

import javax.inject.Inject;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.reqcycle.traceability.storage.IOneFileStorageProvider;
import org.eclipse.reqcycle.traceability.storage.ITraceabilityStorage;
import org.eclipse.reqcycle.traceability.storage.blueprints.graph.IBusinessOperationProvider;
import org.eclipse.reqcycle.traceability.storage.blueprints.graph.IGraphProvider;
import org.eclipse.reqcycle.traceability.storage.blueprints.graph.IOneFileGraphProvider;
import org.eclipse.ziggurat.inject.ZigguratInject;

public class GraphStorageProvider implements IOneFileStorageProvider {

	@Inject
	IEventBroker broker;
	
	private IGraphProvider provider;

	@Override
	public ITraceabilityStorage getStorage(String path) {
		GraphStorage graphStorage = null;
		IGraphProvider provider = getProvider();
		IBusinessOperationProvider.IBusinessOperations operations = null;
		if (provider instanceof IBusinessOperationProvider){
			operations = ((IBusinessOperationProvider)provider).getBusinessOperation();
		}
		graphStorage = new GraphStorage(provider.getGraph(path), path, operations);
		ZigguratInject.inject(graphStorage);
		return graphStorage;
	}
	
	protected IGraphProvider getProvider() {
		if(provider == null){
			provider = ZigguratInject.make(IGraphProvider.class);
		}
		return provider;
	}

	@Override
	public void notifyChanged(String event, Object data) {
		//Asynchronousky sending a message.
		broker.post(event, data);
	}

	@Override
	public ITraceabilityStorage getStorageReader(InputStream inputStream) {
		GraphStorage graphStorage = null;
		IGraphProvider provider = getProvider();
		IBusinessOperationProvider.IBusinessOperations operations = null ;
		if (provider instanceof IBusinessOperationProvider){
			operations = ((IBusinessOperationProvider)provider).getBusinessOperation();
		}
		if(provider instanceof IOneFileGraphProvider) {
			IOneFileGraphProvider specProvider = (IOneFileGraphProvider)provider;
			graphStorage = new GraphStorage(specProvider.getGraph(inputStream), null, operations);
		} 
		if(graphStorage != null) {
			ZigguratInject.inject(graphStorage);
		}
		return graphStorage;
	}
}
