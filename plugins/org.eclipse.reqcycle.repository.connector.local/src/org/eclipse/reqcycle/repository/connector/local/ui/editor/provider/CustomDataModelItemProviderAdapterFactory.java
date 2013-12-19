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
package org.eclipse.reqcycle.repository.connector.local.ui.editor.provider;

import org.eclipse.emf.common.notify.Adapter;

import RequirementSourceData.provider.RequirementSourceDataItemProviderAdapterFactory;


public class CustomDataModelItemProviderAdapterFactory extends RequirementSourceDataItemProviderAdapterFactory {

	@Override
	public Adapter createRequirementsContainerAdapter() {
		if(requirementsContainerItemProvider == null) {
			//Use Custom Requirements Container item provider
			requirementsContainerItemProvider = new CustomRequirementsContainerItemProvider(this);
		}
		return requirementsContainerItemProvider;
	}

	@Override
	public Adapter createSimpleRequirementAdapter() {
		if(simpleRequirementItemProvider == null) {
			//Use Custom Requirement Item Provider
			simpleRequirementItemProvider = new CustomSimpleRequirementItemProvider(this);
		};
		return simpleRequirementItemProvider;
	}

	@Override
	public Adapter createSectionAdapter() {
		if(sectionItemProvider == null) {
			//Use Custom Section Item Provider
			sectionItemProvider = new CustomSectionItemProvider(this);
		}
		return sectionItemProvider;
	}

	@Override
	public Adapter createRequirementAdapter() {
		if(requirementItemProvider == null) {
			//Use Custom Requirement Section Item Provider
			requirementItemProvider = new CustomRequirementItemProvider(this);
		}
		return requirementItemProvider;
	}

	@Override
	public boolean isFactoryForType(Object type) {
		return super.isFactoryForType(type);
	}

}
