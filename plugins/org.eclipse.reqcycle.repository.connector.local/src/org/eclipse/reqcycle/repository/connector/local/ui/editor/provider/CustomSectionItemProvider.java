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

import java.util.Collection;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.reqcycle.repository.data.IDataModelManager;
import org.eclipse.reqcycle.repository.data.types.IRequirementType;
import org.eclipse.ziggurat.inject.ZigguratInject;

import RequirementSourceData.RequirementSourceDataFactory;
import RequirementSourceData.RequirementSourceDataPackage;
import RequirementSourceData.Section;
import RequirementSourceData.provider.SectionItemProvider;


/**
 * The Class CustomSectionItemProvider.
 */
public class CustomSectionItemProvider extends SectionItemProvider {

	IDataModelManager manager = ZigguratInject.make(IDataModelManager.class);

	/**
	 * Instantiates a new custom section item provider.
	 * 
	 * @param adapterFactory
	 *        the adapter factory
	 */
	public CustomSectionItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	@Override
	public String getText(Object object) {
		String result = "";

		String id = ((Section)object).getId();
		String text = ((Section)object).getText();

		if(id != null && !id.isEmpty()) {
			result += "[ id : " + id;
		}

		if(text != null && !text.isEmpty()) {
			result += result.isEmpty() ? "[ " : " | ";
			result += "text : " + text;
		}

		if(!result.isEmpty()) {
			result += " ]";
		}

		return "Section " + result;
	}

	@Override
	protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object) {
		//FIXME : Use element Data Model to get possible children
		//Gets Dynamic Data Model possible children
		for(IRequirementType type : manager.getAllRequirementTypes()) {
			newChildDescriptors.add(createChildParameter(RequirementSourceDataPackage.Literals.SECTION__CHILDREN, type.createInstance()));
		}
		newChildDescriptors.add(createChildParameter(RequirementSourceDataPackage.Literals.SECTION__CHILDREN, RequirementSourceDataFactory.eINSTANCE.createSection()));
	}

}
