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

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.reqcycle.repository.data.IDataModelManager;
import org.eclipse.reqcycle.repository.data.types.IRequirementType;
import org.eclipse.ziggurat.inject.ZigguratInject;

import RequirementSourceConf.RequirementSource;
import RequirementSourceConf.RequirementSourceConfPackage;
import RequirementSourceConf.provider.RequirementSourceItemProvider;
import RequirementSourceData.RequirementSourceDataFactory;
import RequirementSourceData.Section;


/**
 * The Class CustomRequirementSourceItemProvider.
 */
public class CustomRequirementSourceItemProvider extends RequirementSourceItemProvider {

	IDataModelManager manager = ZigguratInject.make(IDataModelManager.class);

	/**
	 * Instantiates a new custom requirement source item provider.
	 * 
	 * @param adapterFactory
	 *        the adapter factory
	 */
	public CustomRequirementSourceItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	@Override
	public Collection<? extends EStructuralFeature> getChildrenFeatures(Object object) {
		if(childrenFeatures == null) {
			childrenFeatures = new ArrayList<EStructuralFeature>();
			childrenFeatures.add(RequirementSourceConfPackage.Literals.REQUIREMENT_SOURCE__CONTENTS);
		}
		return childrenFeatures;
	}

	@Override
	public String getText(Object object) {
		String text = "";

		String label = ((RequirementSource)object).getName();
		if(label != null && !label.isEmpty()) {
			text += label;
		}

		String uri = ((RequirementSource)object).getRepositoryUri();
		if(uri != null && !uri.isEmpty()) {
			text += " [ " + uri + " ] ";
		}

		return text;
	}

	@Override
	protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object) {
		//FIXME : Use element Data Model to get possible children
		//Gets Dynamic Data Model possible children
		for(IRequirementType type : manager.getAllRequirementTypes()) {
			newChildDescriptors.add(createChildParameter(RequirementSourceConfPackage.Literals.REQUIREMENT_SOURCE__CONTENTS, type.createInstance()));
		}
		Section section = RequirementSourceDataFactory.eINSTANCE.createSection();
		section.setId("");
		newChildDescriptors.add(createChildParameter(RequirementSourceConfPackage.Literals.REQUIREMENT_SOURCE__CONTENTS, section));
	}

}
