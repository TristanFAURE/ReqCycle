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

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.reqcycle.repository.data.types.IAttribute;
import org.eclipse.reqcycle.repository.data.types.IAttributeType;

/**
 * The Class AttributeImpl.
 */
public class AttributeImpl implements IAttribute, IAdaptable {

	/** The eAttribute. */
	protected EAttribute eAttribute;

	/** The type. */
	protected IAttributeType type;


	/**
	 * Instantiates a new attribute.
	 * 
	 * @param name
	 *        the attribute name
	 * @param type
	 *        the attribute type
	 */
	public AttributeImpl(String name, IAttributeType type) {

		EDataType eDataType = null;
		if(type instanceof IAdaptable) {
			eDataType = (EDataType)((IAdaptable)type).getAdapter(EDataType.class);
		}
		EAttribute eAttribute = EcoreFactory.eINSTANCE.createEAttribute();
		eAttribute.setEType(eDataType);
		eAttribute.setName(name);
		this.eAttribute = eAttribute;
		this.type = type;
	}

	/**
	 * Instantiates a new attribute.
	 * 
	 * @param name
	 *        the attribute name
	 * @param type
	 *        the attribute type
	 */
	protected AttributeImpl(String name, EDataType type) {

		EAttribute eAttribute = EcoreFactory.eINSTANCE.createEAttribute();
		eAttribute.setEType(type);
		eAttribute.setName(name);
		this.eAttribute = eAttribute;
		this.type = new AttributeTypeImpl(name, type);
	}

	/**
	 * Instantiates a new attribute.
	 * 
	 * @param eAttribute
	 *        the EAttribute
	 */
	protected AttributeImpl(EAttribute eAttribute) {
		this.eAttribute = eAttribute;
		this.type = new AttributeTypeImpl(eAttribute.getEAttributeType());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.reqcycle.repository.data.types.IAttribute#getName()
	 */
	@Override
	public String getName() {
		return eAttribute != null ? eAttribute.getName() : null;
	}

	/**
	 * Gets the type.
	 * 
	 * @return the type
	 * @deprecated use getAdapter()
	 */
	@Deprecated
	public EDataType getType() {
		return eAttribute.getEAttributeType();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.reqcycle.repository.data.types.IAttribute#isHidden()
	 */
	@Override
	public boolean isHidden() {
		if(eAttribute.getEAnnotation("hidden") != null) {
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class adapter) {
		if(adapter == EAttribute.class) {
			return eAttribute;
		}
		if(adapter == EDataType.class) {
			return eAttribute.getEType();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.reqcycle.repository.data.types.IAttribute#getAttributeType()
	 */
	@Override
	public IAttributeType getAttributeType() {
		return type;
	}

}
