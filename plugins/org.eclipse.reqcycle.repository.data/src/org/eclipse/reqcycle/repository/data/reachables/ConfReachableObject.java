package org.eclipse.reqcycle.repository.data.reachables;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.reqcycle.emf.handlers.EMFReachableObject;
import org.eclipse.reqcycle.emf.utils.EMFUtils;
import org.eclipse.reqcycle.emf.visitors.EMFVisitable;
import org.eclipse.reqcycle.uri.model.Reachable;


public class ConfReachableObject extends EMFReachableObject {

	public ConfReachableObject(Reachable t) {
		super(t);
	}

	@Override
	protected EMFVisitable doGetVisitable(URI uri) {
		return new EMFVisitable(uri) {

			@Override
			protected ResourceSet getResourceSet() {
				return EMFUtils.getFAURSWithPathMaps();
			}
		};
	}
}
