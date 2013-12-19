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
package org.eclipse.reqcycle.repository.data.types;

import java.util.Collection;

import RequirementSourceData.Requirement;

/**
 * The Interface RequirementType.
 */
public interface IRequirementType {

	/**
	 * Gets the name.
	 * 
	 * @return the Requirement name
	 */
	public String getName();

	/**
	 * Gets the attributes.
	 * 
	 * @return the requirement types attributes
	 */
	public Collection<IAttribute> getAttributes();

	/**
	 * Adds the attribute type.
	 * 
	 * @param type
	 *        the attribute type
	 */
	public void addAttributeType(IAttribute type);

	/**
	 * Creates an instance.
	 * 
	 * @return requirement element
	 */
	public Requirement createInstance();

	public IDataModel getDataModel();
}
