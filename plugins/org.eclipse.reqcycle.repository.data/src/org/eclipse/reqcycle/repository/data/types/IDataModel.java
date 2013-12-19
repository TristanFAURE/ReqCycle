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

import ScopeConf.Scope;

/**
 * The Interface DataModel.
 */
public interface IDataModel {

	/**
	 * Gets the name.
	 * 
	 * @return the data model name
	 */
	public String getName();

	/**
	 * Adds the scope to the data model.
	 * 
	 * @param scope
	 *        the scope to add
	 */
	public void addScope(Scope scope);

	/**
	 * Adds the requirement type.
	 * 
	 * @param type
	 *        the requirement type to add
	 */
	public void addRequirementType(IRequirementType type);

	/**
	 * Adds the Enumeration type.
	 * 
	 * @param type
	 *        the Enumeration type to add
	 */
	public void addEnumerationType(IEnumerationType type);

	/**
	 * Gets the enumeration type.
	 * 
	 * @param name
	 *        the enumeration type name
	 * @return the enumeration type
	 */
	public IEnumerationType getEnumerationType(String name);

	/**
	 * Gets the requirement type.
	 * 
	 * @param name
	 *        the requirement type name
	 * @return the requirement type
	 */
	public IRequirementType getRequirementType(String name);

	/**
	 * Gets the scope.
	 * 
	 * @param name
	 *        the scope name
	 * @return the scope
	 */
	public Scope getScope(String name);

	/**
	 * Gets the requirement types.
	 * 
	 * @return the requirement types
	 */
	public Collection<IRequirementType> getRequirementTypes();

	/**
	 * Gets the enumeration types.
	 * 
	 * @return the enumeration types
	 */
	public Collection<IEnumerationType> getEnumerationTypes();

	/**
	 * Gets the scopes.
	 * 
	 * @return the scopes
	 */
	public Collection<Scope> getScopes();

	/**
	 * Gets the data model uri.
	 * 
	 * @return the data model uri
	 */
	public String getDataModelURI();

}
