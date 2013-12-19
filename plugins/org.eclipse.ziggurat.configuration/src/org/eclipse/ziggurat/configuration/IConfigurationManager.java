/*******************************************************************************
 * Copyright (c) 2013 Atos.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Atos - initial API and implementation
 ******************************************************************************/
package org.eclipse.ziggurat.configuration;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import org.eclipse.core.resources.IResource;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;

public interface IConfigurationManager {

	public enum Scope {
		WORKSPACE, PROJECT
	}

	/**
	 * Retrieve a simple key-value representation of a configuration.
	 * The configuration MUST has been save using saveSimpleConfiguration.
	 * 
	 * @param context
	 *        can be null, the workspace conf will be used in this case
	 * @param scope
	 *        can be null : in this case project conf will be returned if available, otherwise workspace conf will be returned
	 * @param id
	 * @return the configuration or null if not found
	 */
	Map<String, Object> getSimpleConfiguration(IResource context, Scope scope, String id, boolean reload);

	/**
	 * Retrieve a configuration EObject.
	 * The object should has been save using saveConfiguration(EObject, IResource, Scope, String) :
	 * if the object is not self contained its outside references will not be resolvable.
	 * 
	 * @param context
	 *        can be null, the workspace conf will be used in this case
	 * @param scope
	 *        can be null : in this case project conf will be returned if available, otherwise workspace conf will be returned
	 * @param id
	 * @return the configuration or null if not found
	 */
	Collection<EObject> getConfiguration(IResource context, Scope scope, String id, boolean reload);

	/**
	 * Retrieve a configuration EObject using the provided resource set to load the corresponding configuration resource.
	 * You can use getConfigurationResourceExtension to filter configuration resources when displaying your resource set.
	 * 
	 * @param context
	 *        can be null, the workspace conf will be used in this case
	 * @param scope
	 *        can be null : in this case project conf will be returned if available, otherwise workspace conf will be returned
	 * @param id
	 * @param resourceSet
	 * @return the configuration or null if not found
	 */
	Collection<EObject> getConfiguration(IResource context, Scope scope, String id, ResourceSet resourceSet, boolean reload);

	/**
	 * Save an EObject as a configuration.
	 * The object can have references to elements outside of itself in the provided resource set,
	 * but it implies that the configuration will be saved and loaded using a configuration Resource in the provided resource set.
	 * You can use getConfigurationResourceExtension to filter configuration resources when displaying your resource set.
	 * 
	 * @param conf
	 * @param context
	 *        can be null if the scope is workspace
	 * @param scope
	 *        should not be null
	 * @param id
	 * @param resourceSet
	 * @throws IOException
	 */
	void saveConfiguration(EObject conf, IResource context, Scope scope, String id, ResourceSet resourceSet) throws IOException;

	/**
	 * Save a Collection of EObjects as a configuration.
	 * Objects can have references to elements outside themselves in the provided resource set,
	 * but it implies that the configuration will be saved and loaded using a configuration Resource in the provided resource set.
	 * You can use getConfigurationResourceExtension to filter configuration resources when displaying your resource set.
	 * 
	 * @param conf
	 * @param context
	 *        can be null if the scope is workspace
	 * @param scope
	 *        should not be null
	 * @param id
	 * @param resourceSet
	 * @throws IOException
	 */
	void saveConfiguration(Collection<? extends EObject> conf, IResource context, Scope scope, String id, ResourceSet resourceSet) throws IOException;
	
	/**
	 * Save an EObject as a configuration.
	 * The object should be self contained (no references outside of itself or its children),
	 * use saveConfiguration(EObject, IResource, Scope, String, ResourceSet) otherwise.
	 * 
	 * @param conf
	 * @param context
	 *        can be null if the scope is workspace
	 * @param scope
	 *        should not be null
	 * @param id
	 * @throws IOException
	 */
	void saveConfiguration(EObject conf, IResource context, Scope scope, String id) throws IOException;

	/**
	 * Save a simple key-value representation of a configuration.
	 * The values should be either primitive java objects (Integer, Long, Float, Double, String, Boolean)
	 * or a coherent collection of primitive objects.
	 * 
	 * @param conf
	 *        key-value configuration
	 * @param context
	 *        can be null if the scope is workspace
	 * @param scope
	 *        should not be null
	 * @param id
	 * @throws IOException
	 */
	void saveSimpleConfiguration(Map<String, Object> conf, IResource context, Scope scope, String id) throws IOException;

	/**
	 * @return the extension used for configuration resources
	 */
	String getConfigurationResourceExtension();
}