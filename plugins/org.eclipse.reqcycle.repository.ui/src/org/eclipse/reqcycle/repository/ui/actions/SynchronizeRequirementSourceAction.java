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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.workspace.util.WorkspaceSynchronizer;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.reqcycle.core.ILogger;
import org.eclipse.reqcycle.repository.data.util.IRequirementSourceProperties;
import org.eclipse.ziggurat.inject.ZigguratInject;

import RequirementSourceConf.RequirementSource;

/**
 * Example of Action to Synchronize a Requirement Resource
 * FIXME : use a synchronize module in a different plugin and remove this one
 */
public class SynchronizeRequirementSourceAction extends Action {

	@Inject
	ILogger logger;

	/** Requirement repositories TreeViewer */
	private TreeViewer viewer;

	/**
	 * Constructor
	 * 
	 * @param viewer
	 *        The requirement Resource Viewer
	 */
	public SynchronizeRequirementSourceAction(TreeViewer viewer) {
		this.viewer = viewer;
		ZigguratInject.inject(this);
	}

	@Override
	public void run() {
		ISelection selection = viewer.getSelection();
		if(selection instanceof IStructuredSelection) {
			Object element = ((IStructuredSelection)selection).getFirstElement();
			if(element instanceof RequirementSource) {
				RequirementSource source = (RequirementSource)element;
				if(Boolean.parseBoolean(source.getProperty(IRequirementSourceProperties.IS_LOCAL))) {

					//					try {
					//						long[] resultReqs = null;
					//						resultReqs = SVNUtils.synchronizeSVNSource(source);
					//
					//						if(resultReqs != null && resultReqs.length > 0 && resultReqs[0] != SVNRevision.INVALID_REVISION_NUMBER) {
					//							MessageDialog.openInformation(Display.getDefault().getActiveShell(), "Synchronize Requirement Source", "Synchronize Requirement Source finished without errors.");
					//						} else if(resultReqs != null && resultReqs.length == 0) {
					//							MessageDialog.openInformation(Display.getDefault().getActiveShell(), "Synchronize Requirement Source", "Nothing to Commit.");
					//						} else {
					//							MessageDialog.openError(Display.getDefault().getActiveShell(), "Synchronize Requirement Source", "Commit error.");
					//							return;
					//						}
					//
					//						
					//						 if(!MessageDialog.openQuestion(Display.getDefault().getActiveShell(), "Synchronize Traceability",
					//						  "Would you like to synchronize project traceability?")) {
					//						  return;
					//						  }
					//						  
					//						  if(!isTraceabilityAvailable(source)) {
					//						  MessageDialog.openError(Display.getDefault().getActiveShell(), "Synchronize Traceability",
					//						  "Can't find traceability file. The traceability file must be in the same project as the Requirement Source file.");
					//						  return;
					//						  }
					//						  
					//						  long[] resultTracea = null;
					//						  resultTracea = SVNUtils.synchronizeSVNTraceability(source);
					//						  
					//						  if(resultTracea != null && resultTracea.length > 0 && resultTracea[0] != SVNRevision.INVALID_REVISION_NUMBER) {
					//						  MessageDialog.openInformation(Display.getDefault().getActiveShell(), "Synchronize Traceability",
					//						  "Synchronize Traceability finished without errors");
					//						  } else if(resultTracea != null && resultTracea.length == 0) {
					//						  MessageDialog.openInformation(Display.getDefault().getActiveShell(), "Synchronize Traceability", "Nothing to Commit.");
					//						  } else {
					//						  MessageDialog.openError(Display.getDefault().getActiveShell(), "Synchronize Traceability", "Commit error.");
					//						  return;
					//						  }
					//						 
					//					} catch (SVNConnectorException e) {
					//						//FIXME : Use logger
					//						e.printStackTrace();
					//						logger.trace(e.getMessage());
					//						MessageDialog.openError(Display.getDefault().getActiveShell(), "ReqCycle Synchronize", "SVN Error while syncing.\n" + e.getMessage());
					//						return;
					//					} catch (IOException e) {
					//						//FIXME : Use logger
					//						e.printStackTrace();
					//						logger.trace(e.getMessage());
					//						MessageDialog.openError(Display.getDefault().getActiveShell(), "ReqCycle Synchronize", "Error while syncing.\n" + e.getMessage());
					//						return;
					//					}
				}
			}
		}
	}

	private boolean isTraceabilityAvailable(RequirementSource source) {
		Resource resource = source.getContents().eResource();
		IFile file = WorkspaceSynchronizer.getFile(resource);
		IProject project = file.getProject();

		IFile fileTracea = project.getFile(new Path("./.traceability.rdf"));
		return fileTracea.exists();
	}

}
