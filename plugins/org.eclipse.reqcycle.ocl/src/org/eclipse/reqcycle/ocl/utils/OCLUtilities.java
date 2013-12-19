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
package org.eclipse.reqcycle.ocl.utils;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IResourceStatus;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.ocl.examples.pivot.utilities.BaseResource;
import org.eclipse.ocl.examples.xtext.base.baseCST.PrimitiveTypeRefCS;
import org.eclipse.ocl.examples.xtext.base.baseCST.TypedRefCS;
import org.eclipse.ocl.examples.xtext.completeocl.CompleteOCLStandaloneSetup;
import org.eclipse.ocl.examples.xtext.completeocl.completeOCLCST.ClassifierContextDeclCS;
import org.eclipse.ocl.examples.xtext.completeocl.completeOCLCST.CompleteOCLDocumentCS;
import org.eclipse.ocl.examples.xtext.completeocl.completeOCLCST.ContextDeclCS;
import org.eclipse.ocl.examples.xtext.completeocl.completeOCLCST.DefCS;
import org.eclipse.ocl.examples.xtext.completeocl.completeOCLCST.DefOperationCS;
import org.eclipse.reqcycle.ocl.ReqcycleOCLPlugin;
import org.eclipse.reqcycle.repository.data.types.IAttribute;
import org.eclipse.reqcycle.repository.data.types.IRequirementType;
import org.eclipse.ziggurat.ocl.OCLEvaluator;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;


public class OCLUtilities {

	protected static Map<String, String> ecoreToOCLPrimitiveTypes = new HashMap<String, String>();
	static {
		ecoreToOCLPrimitiveTypes.put("EString", "String"); //$NON-NLS-1$
		ecoreToOCLPrimitiveTypes.put("EBoolean", "Boolean"); //$NON-NLS-1$
		ecoreToOCLPrimitiveTypes.put("EFloat", "Real"); //$NON-NLS-1$
		ecoreToOCLPrimitiveTypes.put("EInt", "Integer"); //$NON-NLS-1$
	}

	public static BaseResource loadOCLResource(ResourceSet resourceSet, URI oclURI) {
		BaseResource xtextResource = null;
		CompleteOCLStandaloneSetup.init();
		try {
			xtextResource = (BaseResource)resourceSet.getResource(oclURI, true);
		} catch (WrappedException e) {
			URI retryURI = null;
			Throwable cause = e.getCause();
			if(cause instanceof CoreException) {
				IStatus status = ((CoreException)cause).getStatus();
				if((status.getCode() == IResourceStatus.RESOURCE_NOT_FOUND) && status.getPlugin().equals(ResourcesPlugin.PI_RESOURCES)) {
					if(oclURI.isPlatformResource()) {
						retryURI = URI.createPlatformPluginURI(oclURI.toPlatformString(false), false);
					}
				}
			}
			if(retryURI != null) {
				xtextResource = (BaseResource)resourceSet.getResource(retryURI, true);
			} else {
				throw e;
			}
		}
		return xtextResource;
	}

	public static Collection<DefOperationCS> getOperations(BaseResource resource) {
		Collection<DefOperationCS> result = Lists.newArrayList();
		EList<EObject> contents = resource.getContents();
		if(contents.size() == 1) {
			EObject root = contents.get(0);
			if(root instanceof CompleteOCLDocumentCS) {
				EList<ContextDeclCS> contexts = ((CompleteOCLDocumentCS)root).getContexts();
				for(ContextDeclCS context : contexts) {
					if(context instanceof ClassifierContextDeclCS) {
						EList<DefCS> definitions = ((ClassifierContextDeclCS)context).getDefinitions();
						Iterables.addAll(result, Iterables.filter(definitions, DefOperationCS.class));
					}
				}
			}
		}
		return result;
	}

	/**
	 * Checks whether an OCL resource contains an operation allowing to test whether an uml element can
	 * be associated to a given data type. The operation should be named "isX" where "X" is the name
	 * of the data type.
	 */
	public static IStatus isOperationPresent(final IRequirementType type, BaseResource resource) {
		if(Iterables.size(getMatchingOperations(type, resource)) > 0) {
			return Status.OK_STATUS;
		};
		return new Status(IStatus.ERROR, ReqcycleOCLPlugin.PLUGIN_ID, "Required operation : " + OCLUtilities.getOperationRequiredSignature(type) + " could not be found");
	}

	/**
	 * Checks whether an OCL resource contains an operation allowing to retrieve a requirement's attribute
	 * of a given data type. The operation should be named "isX" where "X" is the name
	 * of the data type.
	 */
	public static IStatus isOperationPresent(final IAttribute attribute, BaseResource resource) {
		String attributeTypeName = attribute.getName();
		if(ecoreToOCLPrimitiveTypes.get(attributeTypeName) == null) {
			return new Status(IStatus.WARNING, ReqcycleOCLPlugin.PLUGIN_ID, "Type " + attributeTypeName + " cannot be used in OCL.");
		}
		if(Iterables.size(getMatchingOperations(attribute, resource)) > 0) {
			return Status.OK_STATUS;
		};
		return new Status(IStatus.ERROR, ReqcycleOCLPlugin.PLUGIN_ID, "Required operation : " + OCLUtilities.getOperationRequiredSignature(attribute) + " could not be found.");
	}

	/**
	 * Gets operations that could be used to match uml elements to a data type. These operations
	 * must have a specific name and signature (no parameter, return boolean).
	 */
	public static Iterable<DefOperationCS> getMatchingOperations(final IRequirementType type, BaseResource resource) {
		Collection<DefOperationCS> operations = getOperations(resource);
		if(operations == null || Iterables.isEmpty(operations)) {
			return Collections.emptyList();
		}
		return Iterables.filter(operations, new Predicate<DefOperationCS>() {

			@Override
			public boolean apply(DefOperationCS arg0) {
				TypedRefCS operationReturnType = arg0.getOwnedType();
				if(!arg0.getParameters().isEmpty()) {
					return false;
				}
				if(operationReturnType instanceof PrimitiveTypeRefCS) {
					String returnType = ((PrimitiveTypeRefCS)operationReturnType).getName();
					if(!"Boolean".equals(returnType)) { //$NON-NLS-1$
						return false;
					}
				}
				return arg0.getName().equalsIgnoreCase(getOperationRequiredName(type));
			}
		});
	}

	/**
	 * Gets operations that could be used to match uml elements to a data type. These operations
	 * must have a specific name and signature (no parameter, return boolean).
	 */
	public static Iterable<DefOperationCS> getMatchingOperations(final IAttribute attribute, BaseResource resource) {
		Collection<DefOperationCS> operations = getOperations(resource);
		if(operations == null || Iterables.isEmpty(operations)) {
			return Collections.emptyList();
		}
		return Iterables.filter(operations, new Predicate<DefOperationCS>() {

			@Override
			public boolean apply(DefOperationCS arg0) {
				TypedRefCS operationReturnType = arg0.getOwnedType();
				if(!arg0.getParameters().isEmpty()) {
					return false;
				}
				if(operationReturnType instanceof PrimitiveTypeRefCS) {
					String returnType = ((PrimitiveTypeRefCS)operationReturnType).getName();
					String attributeTypeName = attribute.getName();
					String lookupResult = ecoreToOCLPrimitiveTypes.get(attributeTypeName);
					if(lookupResult == null || !lookupResult.equals(returnType)) {
						return false;
					}
				}
				return arg0.getName().equalsIgnoreCase(getOperationRequiredName(attribute));
			}
		});
	}

	public static String getOperationRequiredName(IRequirementType type) {
		StringBuilder builder = new StringBuilder("is"); //$NON-NLS-1$
		String dataTypeName = type.getName();
		builder.append(Character.toUpperCase(dataTypeName.charAt(0))).append(dataTypeName.substring(1));
		return builder.toString();
	}

	public static String getOperationRequiredName(IAttribute attribute) {
		StringBuilder builder = new StringBuilder("get"); //$NON-NLS-1$
		String dataTypeName = attribute.getName();
		builder.append(Character.toUpperCase(dataTypeName.charAt(0))).append(dataTypeName.substring(1));
		return builder.toString();
	}

	public static String getOperationRequiredSignature(IRequirementType type) {
		return getOperationRequiredName(type) + "() : Boolean"; //$NON-NLS-1$
	}

	public static String getOperationRequiredSignature(IAttribute attribute) {
		String returnTypeName = attribute.getName();
		String lookupType = ecoreToOCLPrimitiveTypes.get(returnTypeName);
		return getOperationRequiredName(attribute) + "() : " + lookupType; //$NON-NLS-1$
	}

	public static boolean isDataType(OCLEvaluator evaluator, EObject eObject, IRequirementType type) {
		String operationName = OCLUtilities.getOperationRequiredName(type);
		EOperation eOperation = evaluator.getCompiledOperation(operationName, eObject);
		if(eOperation != null) {
			Object result = evaluator.evaluateOperation(eOperation, eObject, new Object[0]);
			if(result instanceof Boolean) {
				return (Boolean)result;
			}
		}
		return false;
	}

	public static Object getAttributeValue(OCLEvaluator evaluator, EObject eObject, IAttribute attribute) {
		String operationName = OCLUtilities.getOperationRequiredName(attribute);
		EOperation eOperation = evaluator.getCompiledOperation(operationName, eObject);
		if(eOperation != null) {
			Object result = evaluator.evaluateOperation(eOperation, eObject, new Object[0]);
			
			EDataType type = getDataType(attribute);
			
			if(type == null) {
				return null;
			}
			
			if(type.isInstance(result)) {
				return result;
			} else {
				Object converted = convertResult(result, attribute);
				if(type.isInstance(converted)) {
					return converted;
				}
			}
		}
		return null;
	}

	private static EDataType getDataType(IAttribute attribute) {
		EDataType type = null;
		
		if(attribute instanceof IAdaptable) {
			type = (EDataType)((IAdaptable)attribute).getAdapter(EDataType.class);
		}
		return type;
	}

	protected static Object convertResult(Object result, IAttribute attribute) {
		EDataType type = getDataType(attribute);
		
		if(type == null) {
			return null;
		}
		
		String instanceClass = type.getInstanceClassName();
		if("float".equalsIgnoreCase(instanceClass) && result instanceof Double) { //$NON-NLS-1$
			return ((Double)result).floatValue();
		}
		return null;
	}


}
