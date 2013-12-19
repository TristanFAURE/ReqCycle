package org.eclipse.reqcycle.traceability.storage;



public interface IStorageProvider {
	/**
	 * Returns a storage able to store traceability information
	 * 
	 * @param path
	 * @return
	 */
	ITraceabilityStorage getStorage(String path);
	
	void notifyChanged(String event, Object data);
}
