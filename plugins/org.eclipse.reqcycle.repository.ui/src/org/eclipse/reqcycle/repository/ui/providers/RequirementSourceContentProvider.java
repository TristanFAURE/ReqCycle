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
import org.eclipse.reqcycle.repository.data.IDataManager;
import org.eclipse.ziggurat.inject.ZigguratInject;

import RequirementSourceConf.RequirementSource;

public class RequirementSourceContentProvider implements ITreeContentProvider, IStructuredContentProvider {


	private @Inject
	IDataManager requirementSourceManager = ZigguratInject.make(IDataManager.class);

	@Override
	public void inputChanged(Viewer v, Object oldInput, Object newInput) {
	}

	@Override
	public void dispose() {
	}

	@Override
	public Object[] getElements(Object parent) {
		if(parent instanceof Set<?>) {
			return ((Set<?>)parent).toArray();
		}
		return getChildren(parent);
	}

	@Override
	public Object getParent(Object child) {
		return null;
	}

	@Override
	public Object[] getChildren(Object parent) {
		Set<RequirementSource> repositories = requirementSourceManager.getRequirementSources((String)parent);
		return repositories.toArray();
	}

	@Override
	public boolean hasChildren(Object parent) {

		if(parent instanceof String) {
			return !requirementSourceManager.getRequirementSources((String)parent).isEmpty();
		}
		return false;
	}
}
