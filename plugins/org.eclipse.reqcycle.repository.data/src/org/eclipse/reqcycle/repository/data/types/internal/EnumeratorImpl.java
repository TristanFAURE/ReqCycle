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
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.reqcycle.repository.data.types.IEnumerator;


public class EnumeratorImpl implements IEnumerator, IAdaptable {

	protected EEnumLiteral eEnumLiteral;

	public EnumeratorImpl(String name) {
		eEnumLiteral = EcoreFactory.eINSTANCE.createEEnumLiteral();
		eEnumLiteral.setName(name);
	}

	protected EnumeratorImpl(EEnumLiteral eEnumLiteral) {
		this.eEnumLiteral = eEnumLiteral;
	}

	@Override
	public String getName() {
		return eEnumLiteral.getName();
	}

	/**
	 * Gets the EEnumLiteral.
	 * 
	 * @return the EEnumLiteral
	 * @deprecated use getAdapter
	 */
	@Deprecated
	protected EEnumLiteral getEEnumLiteral() {
		return eEnumLiteral;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class adapter) {
		if(adapter == EEnumLiteral.class) {
			return eEnumLiteral;
		}
		return null;
	}

}
