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
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.reqcycle.repository.data.types.IAttributeType;
import org.eclipse.reqcycle.repository.data.types.IEnumerationType;
import org.eclipse.reqcycle.repository.data.types.IEnumerator;


public class EnumerationTypeImpl implements IEnumerationType, IAdaptable {

	protected EEnum eEnum;

	protected Collection<IEnumerator> enumerators = new ArrayList<IEnumerator>();

	protected AttributeTypeImpl type;

	public EnumerationTypeImpl(String name) {
		eEnum = EcoreFactory.eINSTANCE.createEEnum();
		eEnum.setName(name);
		type = new AttributeTypeImpl(name, eEnum);
	}

	protected EnumerationTypeImpl(EEnum eEnum) {
		this.eEnum = eEnum;
		for(EEnumLiteral eLiteral : eEnum.getELiterals()) {
			enumerators.add(new EnumeratorImpl(eLiteral));
		}
		this.type = new AttributeTypeImpl(eEnum.getName(), eEnum);
	}

	@Override
	public void addEnumerator(IEnumerator enumerator) {
		EEnumLiteral eEnumLiteral = null;
		if(enumerator instanceof IAdaptable) {
			eEnumLiteral = (EEnumLiteral)((IAdaptable)enumerator).getAdapter(EEnumLiteral.class);
		}
		if(eEnumLiteral != null) {
			eEnum.getELiterals().add(eEnumLiteral);
			enumerators.add(enumerator);
		}
	}

	@Override
	public String getName() {
		return eEnum.getName();
	}

	@Override
	public Collection<IEnumerator> getEnumerators() {
		return enumerators;
	}

	public String getModelNsURI() {
		return eEnum.getEPackage() != null ? eEnum.getEPackage().getNsURI() : null;
	}

	/**
	 * Gets the EDataType.
	 * 
	 * @return the EDataType
	 * @deprecated use getAdapter
	 */
	@Deprecated
	public EDataType getEDataType() {
		return eEnum;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class adapter) {
		if(adapter == EEnum.class || adapter == EDataType.class) {
			return eEnum;
		}
		if(adapter == IAttributeType.class) {
			return type;
		}
		return null;
	}
}
