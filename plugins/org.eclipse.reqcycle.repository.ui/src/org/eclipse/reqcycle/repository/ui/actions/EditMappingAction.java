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

import java.util.concurrent.Callable;

import javax.inject.Inject;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.reqcycle.core.ILogger;
import org.eclipse.reqcycle.repository.connector.ConnectorDescriptor;
import org.eclipse.reqcycle.repository.connector.IConnector;
import org.eclipse.reqcycle.repository.connector.IConnectorManager;
import org.eclipse.reqcycle.repository.connector.ui.Activator;
import org.eclipse.reqcycle.repository.connector.ui.wizard.IConnectorWizard;
import org.eclipse.reqcycle.repository.data.IDataManager;
import org.eclipse.reqcycle.repository.data.IDataModelManager;
import org.eclipse.swt.widgets.Display;

import RequirementSourceConf.RequirementSource;

/**
 * Action to change the requirementSourceMapping mapping
 */
public class EditMappingAction extends Action {

	@Inject
	IConnectorManager connectorManager;

	@Inject
	IDataModelManager dataModelManager;

	@Inject
	ILogger logger;

	@Inject
	IDataManager requirementSourceManager;

	private IConnector connector;

	private TreeViewer viewer;

	public EditMappingAction(TreeViewer viewer) {
		this.viewer = viewer;
	}

	@Override
	public void run() {
		ISelection selection = viewer.getSelection();
		if(selection instanceof IStructuredSelection) {
			Object element = ((IStructuredSelection)selection).getFirstElement();
			if(element instanceof RequirementSource) {
				try {
					RequirementSource requirementSource = (RequirementSource)element;

					//Gets and init the connector 
					String connectorID = requirementSource.getConnectorId();
					ConnectorDescriptor connectorDescriptor = connectorManager.get(connectorID);
					connector = connectorDescriptor.createConnector();
					connector.initializeWithRequirementSource(requirementSource);

					Callable<RequirementSource> callable = null;

					if(connector instanceof IConnectorWizard) {
						WizardDialog wd = new WizardDialog(Display.getDefault().getActiveShell(), (IConnectorWizard)connector);
						wd.setHelpAvailable(false);
						if(wd.open() == Window.OK) {
							callable = this.connector.createRequirementSource();
						}
					} else {
						callable = this.connector.createRequirementSource();
					}

					if(callable != null) {
						callable.call();


						//						EList<ElementMapping> mappings = requirementSource.getMappings();
						//						
						//						ResourceSet rs = new ResourceSetImpl();
						//						for(ElementMapping elementMapping : mappings) {
						//							rs.getResources().add(elementMapping.getTargetElement().eResource());
						//							for(AttributeMapping attributeMapping : elementMapping.getAttributes()) {
						//								rs.getResources().add(attributeMapping.getTargetAttribute().eResource());
						//							}
						//						}
						requirementSourceManager.addRequirementSource(requirementSource);

					}
				} catch (CoreException e) {
					logger.log(e.getStatus());
				} catch (Exception e) {
					logger.log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Could not modify the requirement source"));
				}

			}
		}
	}

}
