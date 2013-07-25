/*****************************************************************************
 * Copyright (c) 2013 AtoS.
 *
 *    
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Anass RADOUANI (AtoS) anass.radouani@atos.net - Initial API and implementation
 *
 *****************************************************************************/
package org.eclipse.reqcycle.repository.ui.providers;

import java.util.Set;

import javax.inject.Inject;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.reqcycle.repository.data.IRequirementSourceManager;
import org.eclipse.ziggurat.inject.ZigguratInject;

import DataModel.RequirementSource;

public class RequirementSourceContentProvider implements ITreeContentProvider, IStructuredContentProvider {

	
	private @Inject IRequirementSourceManager requirementSourceManager = ZigguratInject.make(IRequirementSourceManager.class);
	
	public void inputChanged(Viewer v, Object oldInput, Object newInput) {
	}

	public void dispose() {
	}

	public Object[] getElements(Object parent) {
		if(parent instanceof Set<?>) {
			return ((Set<?>)parent).toArray();
		}
		return getChildren(parent);
	}

	public Object getParent(Object child) {
		return null;
	}

	public Object[] getChildren(Object parent) {
		Set<RequirementSource> repositories = requirementSourceManager.getRepositories((String)parent);
		return repositories.toArray();
	}

	public boolean hasChildren(Object parent) {
		
		if(parent instanceof String) {
			return !requirementSourceManager.getRepositories((String)parent).isEmpty();
		}
		return false;
	}
}
