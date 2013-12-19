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
package org.eclipse.reqcycle.repository.ui.actions;

import javax.inject.Inject;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.reqcycle.repository.connector.IConnectorManager;
import org.eclipse.reqcycle.repository.data.IDataManager;
import org.eclipse.swt.widgets.Display;

import RequirementSourceConf.RequirementSource;

/**
 * Action to delete a requirement source
 */
public class DeleteRequirementSourceAction extends Action {

	/** Requirement Source TreeViewer */
	private TreeViewer viewer;

	/** Requirement Source Manager */
	@Inject
	IDataManager requirementSourceManager;

	@Inject
	IConnectorManager connectorManager;

	/**
	 * Constructor
	 * 
	 * @param viewer
	 *        The Requirement Resource Viewer
	 */
	public DeleteRequirementSourceAction(TreeViewer viewer) {
		this.viewer = viewer;
	}

	@Override
	public void run() {

		ISelection selection = viewer.getSelection();
		if(selection instanceof IStructuredSelection) {
			Object obj = ((IStructuredSelection)selection).getFirstElement();

			if(obj instanceof RequirementSource) {
				boolean response = MessageDialog.openQuestion(Display.getDefault().getActiveShell(), "Remove Requirement Source", "Would you like to remove Requirement Source file ?");
				requirementSourceManager.removeRequirementSource((RequirementSource)obj, response);
			} else if(obj instanceof String && connectorManager.get((String)obj) != null) {
				requirementSourceManager.removeRequirementSources((String)obj);
			}

			viewer.refresh();
		}
	}
}
