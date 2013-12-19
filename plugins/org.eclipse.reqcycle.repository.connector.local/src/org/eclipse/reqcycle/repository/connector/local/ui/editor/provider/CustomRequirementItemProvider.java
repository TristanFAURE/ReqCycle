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
import java.util.List;

import javax.inject.Inject;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.reqcycle.repository.data.IDataModelManager;
import org.eclipse.reqcycle.repository.data.types.IRequirementType;
import org.eclipse.ziggurat.inject.ZigguratInject;

import RequirementSourceConf.RequirementSource;
import RequirementSourceData.AbstractElement;
import RequirementSourceData.Requirement;
import RequirementSourceData.RequirementSourceDataFactory;
import RequirementSourceData.RequirementSourceDataPackage;
import RequirementSourceData.RequirementsContainer;
import RequirementSourceData.provider.RequirementItemProvider;
import ScopeConf.Scope;

/**
 * The Class CustomRequirementItemProvider.
 */
public class CustomRequirementItemProvider extends RequirementItemProvider {

	@Inject
	IDataModelManager manager;

	protected Collection<IItemPropertyDescriptor> customItemPropertyDescriptors;

	/**
	 * Instantiates a new custom requirement section item provider.
	 * 
	 * @param adapterFactory
	 *            the adapter factory
	 */
	public CustomRequirementItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
		ZigguratInject.inject(this);
	}

	@Override
	public String getText(Object object) {
		String result = "";

		String id = ((Requirement) object).getId();
		String text = ((Requirement) object).getText();

		if (id != null && !id.isEmpty()) {
			result += "[ id : " + id;
		}

		if (text != null && !text.isEmpty()) {
			result += result.isEmpty() ? "[ " : " | ";
			result += "text : " + text;
		}

		if (!result.isEmpty()) {
			result += " ]";
		}

		return "Requirement " + result;
	}

	@Override
	public void setPropertyValue(Object object, String property, Object value) {
		// TODO Auto-generated method stub
		super.setPropertyValue(object, property, value);
	}

	@Override
	public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object) {
		super.getPropertyDescriptors(object);
		// if(itemPropertyDescriptors == null) {
		customItemPropertyDescriptors = new ArrayList<IItemPropertyDescriptor>();
		EList<EStructuralFeature> features = ((Requirement) object).eClass()
				.getEStructuralFeatures();
		for (EStructuralFeature eStructuralFeature : features) {
			customItemPropertyDescriptors.add(createItemPropertyDescriptor(
					((ComposeableAdapterFactory) adapterFactory)
							.getRootAdapterFactory(), getResourceLocator(),
					eStructuralFeature.getName(), "", eStructuralFeature, true,
					false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
					null, null));
		}
		itemPropertyDescriptors.addAll(customItemPropertyDescriptors);
		return itemPropertyDescriptors;
		// }
		// return itemPropertyDescriptors;
	}

	@Override
	protected void collectNewChildDescriptors(
			Collection<Object> newChildDescriptors, Object object) {
		// FIXME : Use element Data Model to get possible children
		// Gets Dynamic Data Model possible children

		Scope scope = getScope(object);

		for (IRequirementType type : manager.getAllRequirementTypes()) {
			Requirement instance = type.createInstance();
			instance.getScopes().add(scope);
			newChildDescriptors.add(createChildParameter(
					RequirementSourceDataPackage.Literals.SECTION__CHILDREN,
					instance));
		}
		newChildDescriptors.add(createChildParameter(
				RequirementSourceDataPackage.Literals.SECTION__CHILDREN,
				RequirementSourceDataFactory.eINSTANCE.createSection()));
	}

	private Scope getScope(Object object) {
		if (object instanceof AbstractElement) {
			RequirementsContainer rc = getRequirementContainer((AbstractElement) object);
			if (rc == null) {
				return null;
			}
			RequirementSource source = rc.getRequirementSource();
			return source.getDefaultScope();
		}
		return null;
	}

	private RequirementsContainer getRequirementContainer(AbstractElement object) {
		EObject container = object.eContainer();
		if (container instanceof RequirementsContainer) {
			return (RequirementsContainer) container;
		}

		if (container instanceof AbstractElement) {
			return getRequirementContainer((AbstractElement) container);
		}

		return null;
	}
}
