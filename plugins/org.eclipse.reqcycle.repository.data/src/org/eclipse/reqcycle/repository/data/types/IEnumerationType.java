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

/**
 * The Interface EnumerationType.
 */
public interface IEnumerationType {

	/**
	 * Gets the name.
	 * 
	 * @return the Enumeration type name
	 */
	public String getName();

	/**
	 * Gets the enumerators.
	 * 
	 * @return the enumerators
	 */
	public Collection<IEnumerator> getEnumerators();

	/**
	 * Adds the enumerator.
	 * 
	 * @param enumerator
	 *        the enumerator to add
	 */
	public void addEnumerator(IEnumerator enumerator);

}
