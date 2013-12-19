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

package org.eclipse.reqcycle.repository.connector.rmf.provider;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.rmf.reqif10.SpecType;
import org.eclipse.swt.graphics.Image;

/**
 * Connector Label Provider
 */
public class RMFLabelProvider extends LabelProvider {

	public String getText(Object obj) {
		if(obj instanceof SpecType) {
			return ((SpecType)obj).getLongName();
		}

		return obj.toString();
	}

	public Image getImage(Object obj) {
		return null;
	}
}
