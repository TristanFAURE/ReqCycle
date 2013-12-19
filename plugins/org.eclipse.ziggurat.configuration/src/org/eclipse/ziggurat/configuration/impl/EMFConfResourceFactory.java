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
package org.eclipse.ziggurat.configuration.impl;

import java.io.IOException;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;

public class EMFConfResourceFactory implements Resource.Factory {

	public Resource createResource(URI uri) {
		return new EMFConfResource(uri);
	}

	public class EMFConfResource extends XMIResourceImpl {

		@Override
		protected boolean useUUIDs() {
			return true;
		}

		
		public EMFConfResource() {
		}
		
		public EMFConfResource(URI uri) {
			super(uri);
		}

		@Override
		public void save(Map<?, ?> options) throws IOException {
		}

		public void manualSave(Map<?, ?> options) throws IOException {
			super.save(options);
		}
	}

}