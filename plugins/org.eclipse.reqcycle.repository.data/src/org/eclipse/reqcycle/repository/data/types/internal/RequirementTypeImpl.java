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
package org.eclipse.reqcycle.repository.data.types.internal;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.reqcycle.repository.data.types.IAttribute;
import org.eclipse.reqcycle.repository.data.types.IDataModel;
import org.eclipse.reqcycle.repository.data.types.IRequirementType;

import RequirementSourceData.Requirement;
import RequirementSourceData.RequirementSourceDataPackage;


public class RequirementTypeImpl implements IRequirementType, IAdaptable {

	protected EClass eClass;

	protected Collection<IAttribute> attributes = new ArrayList<IAttribute>();

	private IDataModel dataModel;

	public RequirementTypeImpl(String name, IDataModel dataModel) {
		eClass = EcoreFactory.eINSTANCE.createEClass();
		eClass.setName(name);
		eClass.getESuperTypes().add(RequirementSourceDataPackage.Literals.REQUIREMENT);
		for(EAttribute eAttribute : eClass.getEAllAttributes()) {
			attributes.add(new AttributeImpl(eAttribute));
		}
		this.dataModel = dataModel;
	}

	public RequirementTypeImpl(EClass eClass, IDataModel dataModel) {
		this.eClass = eClass;
		for(EAttribute attribute : eClass.getEAllAttributes()) {
			attributes.add(new AttributeImpl(attribute));
		}
		this.dataModel = dataModel;
	}

	@Override
	public void addAttributeType(IAttribute attributeType) {
		EAttribute eAttribute = null;
		if(attributeType instanceof IAdaptable) {
			eAttribute = (EAttribute)((IAdaptable)attributeType).getAdapter(EAttribute.class);
		}
		if(eAttribute != null) {
			attributes.add(attributeType);
			eClass.getEStructuralFeatures().add(eAttribute);
		}
	}

	@Override
	public String getName() {
		return eClass.getName();
	}

	/**
	 * Gets the EClass.
	 * 
	 * @return the EClass
	 * @deprecated use getAdapter
	 */
	@Deprecated
	public EClass getEClass() {
		return eClass;
	}

	@Override
	public Collection<IAttribute> getAttributes() {
		return attributes;
	}

	@Override
	public Requirement createInstance() {
		EPackage ePackage = eClass.getEPackage();
		if(ePackage != null) {
			return (Requirement)ePackage.getEFactoryInstance().create(eClass);
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class adapter) {
		if(adapter == EClass.class) {
			return eClass;
		}
		return null;
	}

	@Override
	public IDataModel getDataModel() {
		return dataModel;
	}

}
