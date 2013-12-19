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
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.reqcycle.repository.data.types.IAttributeType;

/**
 * The Class AttributeTypeImpl.
 */
public class AttributeTypeImpl implements IAttributeType, IAdaptable {

	/** The attribute name. */
	private String name;

	/** The eDataType. */
	private EDataType eDataType;

	/**
	 * Instantiates a new attribute type.
	 * 
	 * @param name
	 *        the attribute name
	 * @param eDataType
	 *        the EDataType
	 */
	public AttributeTypeImpl(EDataType eDataType) {
		this.eDataType = eDataType;
		this.name = getName(eDataType);
	}

	protected String getName(EDataType eDataType) {
		String className = eDataType.getInstanceClassName();
		if(className == null) {
			return eDataType.getName();
		}
		if(className.contains(".")) {
			return className.substring(className.lastIndexOf(".") + 1);
		}
		return className;
	}

	/**
	 * Instantiates a new attribute type.
	 * 
	 * @param name
	 *        the attribute name
	 * @param eDataType
	 *        the EDataType
	 */
	public AttributeTypeImpl(String name, EDataType eDataType) {
		this.name = name;
		this.eDataType = eDataType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.reqcycle.repository.data.types.IAttributeType#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	//	/**
	//	 * Sets the attribute name.
	//	 * 
	//	 * @param name
	//	 *        the attribute name
	//	 */
	//	public void setName(String name) {
	//		this.name = name;
	//	}
	//
	//	/**
	//	 * Gets the EDataType.
	//	 * 
	//	 * @return the EDataType
	//	 */
	//	public EDataType getEDataType() {
	//		return eDataType;
	//	}
	//
	//	/**
	//	 * Sets the EDataType.
	//	 * 
	//	 * @param eDataType
	//	 *        the EDataType
	//	 */
	//	public void setEDataType(EDataType eDataType) {
	//		this.eDataType = eDataType;
	//	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class adapter) {
		if(adapter == EDataType.class) {
			return eDataType;
		}
		return null;
	}

}
