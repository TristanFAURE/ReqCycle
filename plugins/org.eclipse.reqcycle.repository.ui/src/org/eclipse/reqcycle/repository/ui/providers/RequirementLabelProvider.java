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

package org.eclipse.reqcycle.repository.ui.providers;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import RequirementSourceConf.RequirementSource;
import RequirementSourceData.AbstractElement;

public class RequirementLabelProvider extends LabelProvider {


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
	 */
	@Override
	public String getText(Object element) {

		if(element instanceof RequirementSource) {
			return ((RequirementSource)element).getName() + " (" + ((RequirementSource)element).getRepositoryUri() + " )";
		}

		if(element instanceof AbstractElement) {
			String name = ((AbstractElement)element).getText();
			EList<EStructuralFeature> structuralFeatures = ((AbstractElement)element).eClass().getEStructuralFeatures();
			String attr = " [ id : " + ((AbstractElement)element).getId() + " ]" + "[ text : " + ((AbstractElement)element).getText() + " ]";

			for(EStructuralFeature eStructuralFeature : structuralFeatures) {
				attr += "[ " + eStructuralFeature.getName() + " : " + ((AbstractElement)element).eGet(eStructuralFeature) + "]";
			}
			return name + attr;
		}

		return super.getText(element);
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object)
	 */
	@Override
	public Image getImage(Object element) {

		//		if(element instanceof RequirementSource) {
		//			String connectorId = ((RequirementSource)element).getConnectorID();
		//			return manager.getImage(connectorId, 20, 20);
		//		}
		//		
		return super.getImage(element);
	}

}
