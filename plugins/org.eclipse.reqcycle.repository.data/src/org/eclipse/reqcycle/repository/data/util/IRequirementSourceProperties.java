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
package org.eclipse.reqcycle.repository.data.util;

/**
 * The Interface IRequirementSourceProperties.
 */
public interface IRequirementSourceProperties {

	/** Constant for repository Connector Id property. */
	public static final String PROPERTY_CONNECTOR_ID = "id";

	/** Constant for unknown properties. */
	public static final String TYPE_UNKNOWN = "<unknown>";

	/** Constant for repository label property. */
	public static final String PROPERTY_LABEL = "label";

	/** Constant for Connector username property. */
	public static final String PROPERTY_USERNAME = "username";

	/** Constant for Connector password property. */
	public static final String PROPERTY_PASSWORD = "password";

	/** Constant for Connector URL property. */
	public static final String PROPERTY_URI = "uri";

	/** The Constant IS_COPY for requirement source with requirement copy. */
	public static final String IS_LOCAL = "isCopy";

	/** The Constant Requirement Remote Repository. */
	public static final String REQUIREMENT_REPO = "requirementRepository";

	/** The Constant Traceability Remote Repository. */
	public static final String TRACEABILITY_REPO = "traceabilityRepository";



}
