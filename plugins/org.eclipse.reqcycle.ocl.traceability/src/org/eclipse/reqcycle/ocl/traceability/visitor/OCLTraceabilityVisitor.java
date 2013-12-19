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
package org.eclipse.reqcycle.ocl.traceability.visitor;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.reqcycle.ocl.traceability.OCLTraceabilityPlugin;
import org.eclipse.reqcycle.ocl.traceability.types.OCLVolatileType;
import org.eclipse.reqcycle.traceability.builder.ITraceabilityBuilder.IBuilderCallBack;
import org.eclipse.reqcycle.uri.visitors.IVisitor;
import org.eclipse.ui.statushandlers.StatusManager;
import org.eclipse.ziggurat.ocl.OCLEvaluator;
import org.eclipse.ziggurat.ocl.ZigguratOCLPlugin;


public class OCLTraceabilityVisitor implements IVisitor {

	private OCLEvaluator evaluator;

	@Override
	public void start(IAdaptable adaptable) {
	}

	@Override
	public boolean visit(Object o, IAdaptable adaptable) {
		IBuilderCallBack callBack = (IBuilderCallBack)adaptable.getAdapter(IBuilderCallBack.class);
		if(o instanceof Resource) {
			Resource resource = (Resource)o;
			URI uri = resource.getURI();
			if(uri != null && uri.fileExtension() != null && uri.fileExtension().equals("uml")) {
				IStatus initializationStatus = initializeOCLEvaluator(uri);
				if(!initializationStatus.isOK()) {
					StatusManager.getManager().handle(initializationStatus, StatusManager.LOG);
					return false;
				}
				go(resource, callBack);
			}
		}
		return false;
	}

	@Override
	public void end(IAdaptable adaptable) {
	}

	private void go(Resource resource, IBuilderCallBack callback) {
		TreeIterator<EObject> allContents = resource.getAllContents();
		while(allContents.hasNext()) {
			EObject eObject = allContents.next();
			EOperation operation = evaluator.getCompiledOperation("getLinkTypes", eObject); //$NON-NLS-1$
			if(operation != null) {
				Object result = evaluator.evaluateOperation(operation, eObject, new Object[0]);
				if(result instanceof Collection<?>) {
					for(Object o : (Collection<?>)result) {
						if(o instanceof String) {
							computeTraceability((String)o, eObject, callback);
						}
					}
				} else if(result instanceof String) {
					computeTraceability((String)result, eObject, callback);
				}
			}
		}
	}

	/**
	 * Retrieves the right operation to trace links from the object, executes it,
	 * and serialize the links.
	 * 
	 * @param callBack
	 */
	private IStatus computeTraceability(String linkType, EObject from, IBuilderCallBack callBack) {
		OCLVolatileType ttype = new OCLVolatileType(linkType);
		String operationName = ttype.getOperationName();
		EOperation traceaOperation = evaluator.getCompiledOperation(operationName, from);
		if(traceaOperation != null) {
			Object result = evaluator.evaluateOperation(traceaOperation, from, new Object[0]);
			if(result instanceof Collection<?>) {
				for(Object o : ((Collection<?>)result)) {
					UUID uniqueID = UUID.randomUUID();
					callBack.newUpwardRelation(uniqueID.toString(), from.eResource(), o, Collections.singletonList(from), ttype);
				}
				return Status.OK_STATUS;
			} else if(result != null) {
				//Downward relation from "from" to "result" == upward relation from "result" to "from";
				UUID uniqueID = UUID.randomUUID();
				callBack.newUpwardRelation(uniqueID.toString(), from.eResource(), result, Collections.singletonList(from), ttype);
				return Status.OK_STATUS;
			} else {
				return new Status(IStatus.ERROR, OCLTraceabilityPlugin.PLUGIN_ID, "Traceability operation -" + traceaOperation.getName() + "- returned null");
			}
		}
		return new Status(IStatus.ERROR, OCLTraceabilityPlugin.PLUGIN_ID, "Could not find a matching operation named " + operationName + " for object " + from.toString());
	}

	/**
	 * Initializes the ocl evaluator by compiling the file "traceability.ocl" that should be present at the
	 * root of the workspace.
	 */
	private IStatus initializeOCLEvaluator(URI uri) {
		if(uri.isPlatformResource()) {
			String[] segments = uri.segments();
			if(segments.length > 1) {
				String projectName = segments[1];
				IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
				IResource resource = project.findMember("traceability.ocl");
				if(resource instanceof IFile && resource.exists()) {
					String location = resource.getFullPath().toOSString();
					URI oclURI = URI.createPlatformResourceURI(location, true);
					try {
						this.evaluator = ZigguratOCLPlugin.compileOCL(new ResourceSetImpl(), oclURI);
						return Status.OK_STATUS;
					} catch (Exception e) {
						e.printStackTrace();
						if(e instanceof CoreException) {
							return ((CoreException)e).getStatus();
						}
						return new Status(IStatus.ERROR, OCLTraceabilityPlugin.PLUGIN_ID, e.getMessage());
					}
				}
				return new Status(IStatus.ERROR, OCLTraceabilityPlugin.PLUGIN_ID, "An ocl file named 'traceability.ocl' should be present at the root of the project");
			}
		}

		return Status.OK_STATUS;
	}

}
