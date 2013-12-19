package org.eclipse.reqcycle.repository.data.reachables;

import org.eclipse.emf.common.util.URI;
import org.eclipse.reqcycle.emf.handlers.EMFReachableObject;
import org.eclipse.reqcycle.emf.handlers.EMFURIHandler;
import org.eclipse.reqcycle.uri.model.Reachable;


public class ConfReachableHandler extends EMFURIHandler {

	@Override
	public boolean handlesURI(URI uri) {
		return super.handlesURI(uri) && (uri.path().endsWith("emfconf") || uri.path().endsWith("reqcycle"));
	}

	@Override
	protected EMFReachableObject doGetReachableObject(Reachable t) {
		return new ConfReachableObject(t);
	}

}
