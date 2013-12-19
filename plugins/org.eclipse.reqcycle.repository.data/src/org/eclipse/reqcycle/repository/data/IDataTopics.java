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


/**
 * The Interface IDataTopics.
 */
public interface IDataTopics {

	/** The Constant ALLTOPICS. */
	public static final String ALLTOPICS = "TOPIC_REQCYCLE_DATA/*"; //$NON-NLS-1$

	/** The Constant NEW for all new Elements. */
	public static final String NEW = "TOPIC_REQCYCLE_DATA/NEW/*";

	/** The Constant NEW_ELEMENT for all new elements. */
	public static final String NEW_ELEMENT = "TOPIC_REQCYCLE_DATA/NEW/ELEMENT/*"; //$NON-NLS-1$

	/** The Constant NEW_REQUIREMENT for new Requirements. */
	public static final String NEW_REQUIREMENT = "TOPIC_REQCYCLE_DATA/NEW/ELEMENT/REQUIREMENT"; //$NON-NLS-1$

	/** The Constant NEW_SECTION for new Sections. */
	public static final String NEW_SECTION = "TOPIC_REQCYCLE_DATA/NEW/ELEMENT/SECTION"; //$NON-NLS-1$

	/** The Constant NEW_SOURCE for new requirement sources. */
	public static final String NEW_SOURCE = "TOPIC_REQCYCLE_DATA/NEW/SOURCE"; //$NON-NLS-1$

	/** The Constant REMOVE for all element deletion. */
	public static final String REMOVE = "TOPIC_REQCYCLE_DATA/REMOVE/*"; //$NON-NLS-1$

	/** The Constant REMOVE_ELEMENT for elements deletion. */
	public static final String REMOVE_CONTAINED = "TOPIC_REQCYCLE_DATA/REMOVE/ELEMENT/*"; //$NON-NLS-1$

	/** The Constant REMOVE_REQUIREMENT for Requirements deletion. */
	public static final String REMOVE_REQUIREMENT = "TOPIC_REQCYCLE_DATA/REMOVE/ELEMENT/REQUIREMENT"; //$NON-NLS-1$

	/** The Constant REMOVE_SECTION for Section deletion. */
	public static final String REMOVE_SECTION = "TOPIC_REQCYCLE_DATA/REMOVE/ELEMENT/SECTION"; //$NON-NLS-1$

	/** The Constant REMOVE_SOURCE for source deletion. */
	public static final String REMOVE_SOURCE = "TOPIC_REQCYCLE_DATA/REMOVE/SOURCE"; //$NON-NLS-1$

	/** The Constant UPDATE for all updates. */
	public static final String UPDATE = "TOPIC_REQCYCLE_DATA/UPDATE/*"; //$NON-NLS-1$

	/** The Constant UPDATE_ELEMENT for all elements updates. */
	public static final String UPDATE_ELEMENT = "TOPIC_REQCYCLE_DATA/UPDATE/ELEMENT/*";//$NON-NLS-1$

	/** The Constant UPDATE_REQUIREMENT for Requirements updates. */
	public static final String UPDATE_REQUIREMENT = "TOPIC_REQCYCLE_DATA/UPDATE/ELEMENT/REQUIREMENT"; //$NON-NLS-1$

	/** The Constant UPDATE_SECTION for Sections updates. */
	public static final String UPDATE_SECTION = "TOPIC_REQCYCLE_DATA/UPDATE/ELEMENT/SECTION"; //$NON-NLS-1$

	/** The Constant UPDATE_SOURCE for sources updates. */
	public static final String UPDATE_SOURCE = "TOPIC_REQCYCLE_DATA/UPDATE/SOURCE"; //$NON-NLS-1$

	/** The Constant SAVE for date Saving. */
	public static final String SAVE = "TOPIC_REQCYCLE_DATA/SAVE"; //$NON-NLS-1$

}
