/*******************************************************************************
 * Copyright (c) 2013 Olivier Mélois.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Olivier Mélois - initial API and implementation
 ******************************************************************************/
package org.eclipse.ziggurat.ocl.validation;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.ConstraintSeverity;
import org.eclipse.emf.validation.model.EvaluationMode;
import org.eclipse.emf.validation.model.IModelConstraint;
import org.eclipse.emf.validation.service.AbstractConstraintDescriptor;
import org.eclipse.ocl.examples.pivot.Constraint;
import org.eclipse.ocl.examples.pivot.ExpressionInOCL;
import org.eclipse.ocl.examples.pivot.Namespace;
import org.eclipse.ocl.examples.pivot.OCL;
import org.eclipse.ocl.examples.pivot.OCLExpression;
import org.eclipse.ocl.examples.pivot.OpaqueExpression;
import org.eclipse.ocl.examples.pivot.ParserException;
import org.eclipse.ocl.examples.pivot.helper.OCLHelper;

/**
 * Wraps an OCL Pivot constraint into an {@link IModelConstraint} to be used in live or batch validation. Also 
 * extends {@link AbstractConstraintDescriptor} so this object can refer to itself as a descriptor.
 */
public class OCLtoEMFConstraintWrapper extends AbstractConstraintDescriptor implements IModelConstraint {

	private final OCL ocl;

	private final Constraint constraint;

	public OCLtoEMFConstraintWrapper(OCL ocl, Constraint constraint) {
		super();
		this.ocl = ocl;
		this.constraint = constraint;
	}

	@Override
	public IStatus validate(IValidationContext ctx) {
		EObject target = ctx.getTarget();
		boolean constraintResult = ocl.check(target, constraint);
		if(constraintResult == false) {
			String message = String.format("Object %s does not comply with constraint %s", target.toString(), getDescriptor().getName()); //$NON-NLS-1$
			OpaqueExpression specification = constraint.getSpecification();
			if(specification instanceof ExpressionInOCL) {
				OCLExpression messageExpression = ((ExpressionInOCL)specification).getMessageExpression();
				ExpressionInOCL messageQuery = null;
				if(messageExpression instanceof ExpressionInOCL) {
					messageQuery = (ExpressionInOCL)messageExpression;
				} else if(messageExpression != null) {
					OCLHelper helper = ocl.createOCLHelper(target.eClass());
					try {
						messageQuery = helper.createQuery(messageExpression.toString());
					} catch (ParserException e) {
						e.printStackTrace();
					}
				}
				if(messageQuery != null) {
					Object queryResult = ocl.evaluate(target, messageQuery);
					if(queryResult instanceof String) {
						message = (String)queryResult;
					}
				}
			}
			return ctx.createFailureStatus(message);
		}
		return ctx.createSuccessStatus();
	}

	@Override
	public boolean targetsTypeOf(EObject object) {
		Namespace context = constraint.getContext();
		EClassifier eClassifier = ocl.getMetaModelManager().getEcoreOfPivot(EClass.class, context);
		boolean result = false;
		if(eClassifier != null) {
			if(!result) {
				EObject eContainer = eClassifier.eContainer();
				if(eContainer instanceof EPackage) {
					String nsURI = ((EPackage)eContainer).getNsURI();
					EPackage ePackage = EPackage.Registry.INSTANCE.getEPackage(nsURI);
					EClassifier eClassifier2 = ePackage.getEClassifier(eClassifier.getName());
					result = eClassifier2.isInstance(object);
				}
			}
		}
		return result;
	}

	@Override
	public boolean targetsEvent(Notification notification) {
		Object notifier = notification.getNotifier();
		if(notifier instanceof EObject) {
			return targetsTypeOf((EObject)notifier);
		}
		return false;
	}

	@Override
	public int getStatusCode() {
		return 0;
	}

	@Override
	public ConstraintSeverity getSeverity() {
		return ConstraintSeverity.ERROR;
	}

	@Override
	public String getPluginId() {
		return ZigguratOCLValidationPlugin.PLUGIN_ID;
	}

	@Override
	public String getName() {
		return constraint.getName(); //$NON-NLS-1$
	}

	@Override
	public String getMessagePattern() {
		return "{0}"; //$NON-NLS-1$
	}

	@Override
	public String getId() {
		Resource eResource = constraint.eResource();
		return eResource.getURI().lastSegment() + "#" + constraint.getName(); //$NON-NLS-1$
	}

	@Override
	public EvaluationMode<?> getEvaluationMode() {
		//Note : LIVE also works as batch. 
		return EvaluationMode.LIVE;
	}
	
	@Override
	public String getDescription() {
		//TODO : find how to generate description from the comments in the ocl source file.
		return null;
	}

	@Override
	public String getBody() {
		return null;
	}
}
