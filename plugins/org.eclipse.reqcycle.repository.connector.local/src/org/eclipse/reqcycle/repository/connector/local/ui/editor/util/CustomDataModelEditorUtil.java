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
package org.eclipse.reqcycle.repository.connector.local.ui.editor.util;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;


/**
 * The Class CustomDataModelEditorUtil.
 */
public class CustomDataModelEditorUtil {

	/**
	 * Checks if it is an instance of the given class
	 * 
	 * @param selection
	 *        the selection
	 * @param clazz
	 *        the clazz
	 * @return true, if it is instance
	 */
	public static boolean isInstance(ISelection selection, Class<?> clazz) {
		return selection instanceof IStructuredSelection && clazz != null && clazz.isInstance(((IStructuredSelection)selection).getFirstElement());
	}
}
