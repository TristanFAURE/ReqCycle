/*******************************************************************************
 * Copyright (c) 2012 Original authors and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Original authors and others - initial API and implementation
 ******************************************************************************/
package org.eclipse.reqcycle.traceability.table.model;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.reqcycle.traceability.model.Link;
import org.eclipse.reqcycle.traceability.storage.IStorageProvider;
import org.eclipse.reqcycle.traceability.types.ITraceabilityAttributesManager;
import org.eclipse.reqcycle.traceability.ui.LinkPropertySource;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.IPropertySource2;

/**
 * Creating a new link for deletion purpose.
 * @author omelois
 *
 */
public class TransverseLink extends Link implements IAdaptable {

	@Inject
	@Named("RDF")
	protected IStorageProvider provider;

	@Inject 
	protected ITraceabilityAttributesManager attributeManager;
	
	private IProject project;
	
	public TransverseLink(Link link, IProject project) {
		super(link.getId(), link.getKind(), link.getSources(), link.getTargets());
		this.project = project; 
	}
	
	public IProject getProject(){
		return project;
	}

	@Override
	public Object getAdapter(Class adapter) {
		if (IPropertySource.class.equals(adapter) || IPropertySource2.class.equals(adapter)){
			return new LinkPropertySource(this, null);
		}
		return null;
	}
	
}
