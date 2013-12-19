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
package org.eclipse.reqcycle.repository.data;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.eclipse.reqcycle.repository.data.types.IAttribute;

import RequirementSourceConf.RequirementSource;
import RequirementSourceData.AbstractElement;
import RequirementSourceData.Requirement;
import RequirementSourceData.RequirementsContainer;
import RequirementSourceData.Section;


public interface IDataManager {

	/**
	 * Creates a requirement source.
	 * 
	 * @param name
	 *        the requirement source name
	 * @param connectorId
	 *        the requirement source connector id
	 * @return the requirement source
	 */
	public RequirementSource createRequirementSource(String name, String connectorId);

	/**
	 * Creates an empty requirement source.
	 * 
	 * @return the requirement source
	 */
	public RequirementSource createRequirementSource();

	/**
	 * Creates an empty requirements Container.
	 * 
	 * @return the requirements container
	 */
	public RequirementsContainer createRequirementsContainer(URI uri);

	/**
	 * Adds a newly created repository to repositories list
	 * 
	 * @param requirementSource
	 *        the repository to add
	 */
	public void addRequirementSource(final RequirementSource requirementSource);

	/**
	 * Remove a repository from repositories list
	 * 
	 * @param requirementSource
	 */
	public void removeRequirementSource(final RequirementSource requirementSource, boolean removeFromWS);

	/**
	 * Creates a section.
	 * 
	 * @param id
	 *        the section id
	 * @param name
	 *        the section name
	 * @param uri
	 *        the section uri
	 * @return the section
	 */
	public Section createSection(String id, String name, String uri);

	/**
	 * Gets an existing repository
	 * 
	 * @param kind
	 *        the repository kind
	 * @param urlString
	 *        the repository url
	 * @return
	 */
	public RequirementSource getRequirementSource(String kind, String urlString);

	/**
	 * Gets repositories for a connector Id
	 * 
	 * @param connectorId
	 *        the connector Id
	 * @return Set of repositories
	 */
	public Set<RequirementSource> getRequirementSources(String connectorId);

	/**
	 * Gets all repositories
	 * 
	 * @return Repositories collection
	 */
	public Set<RequirementSource> getRequirementSources();

	/**
	 * Gets Connector id to repositories map
	 * 
	 * @return map Connector id to repositories
	 */
	public Map<String, Set<RequirementSource>> getRepositoryMap();

	/**
	 * Removes a connector repositories
	 * 
	 * @param connectorId
	 *        the connector id
	 */
	public void removeRequirementSources(String connectorId);

	/**
	 * Save the Data Model
	 * 
	 * @throws IOException
	 *         Signals that an I/O exception has occurred.
	 */
	public void save() throws IOException;

	/**
	 * Notify change.
	 * 
	 * @param event
	 *        the event
	 * @param data
	 *        the data
	 */
	public void notifyChange(String event, Object data);

	/**
	 * Adds the attribute to the given element.
	 * 
	 * @param element
	 *        the element
	 * @param attribute
	 *        the attribute to add
	 * @param value
	 *        the value to set
	 */
	public void addAttribute(AbstractElement element, IAttribute attribute, Object value);

	/**
	 * Adds the elements to the requirement source.
	 * 
	 * @param source
	 *        the requirement source
	 * @param elements
	 *        the elements to add
	 * @return true, if successful
	 */
	public boolean addElementsToSource(RequirementSource source, AbstractElement... elements);


	/**
	 * Adds an element to a section.
	 * 
	 * @param section
	 *        the section
	 * @param element
	 *        the element to add
	 * @return true, if successful
	 */
	public boolean addElementsToSection(Section section, AbstractElement... element);

	/**
	 * Adds an element to a requirement.
	 * 
	 * @param requirement
	 *        the requirement
	 * @param element
	 *        the element to add
	 * @return true, if successful
	 */
	public boolean addElementsToRequirement(Requirement requirement, AbstractElement... element);

	public void load();

}
