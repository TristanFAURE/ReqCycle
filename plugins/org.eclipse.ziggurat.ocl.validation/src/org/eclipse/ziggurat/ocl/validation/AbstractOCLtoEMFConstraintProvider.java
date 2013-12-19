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

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.validation.model.IModelConstraint;
import org.eclipse.emf.validation.service.IModelConstraintProvider;
import org.eclipse.ocl.examples.pivot.Constraint;
import org.eclipse.ocl.examples.pivot.OCL;
import org.eclipse.ocl.examples.pivot.utilities.PivotEnvironmentFactory;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;


/**
 * Implementation of the IModelConstraintProvider that converts constraints from the 
 * ocl Pivot metamodel to EMF constraints (IModelConstraint). 
 */
public abstract class AbstractOCLtoEMFConstraintProvider implements IModelConstraintProvider {

	/**
	 * The OCL that is used to evaluate the constraints.
	 */
	protected OCL ocl;
	
	/**
	 * A list to keep the constraints provided by this object.
	 */
	protected Collection<IModelConstraint> allConstraints = Lists.newArrayList();
	
	public AbstractOCLtoEMFConstraintProvider() {
		//Using the default registry
		EPackage.Registry registry = EPackage.Registry.INSTANCE ;
		PivotEnvironmentFactory environmentFactory = new PivotEnvironmentFactory(registry, null);
		this.ocl = OCL.newInstance(environmentFactory);
		for (URI uri : getOclUris()){
			loadConstraintsFromCompleteOCL(uri);
		}
	}
	
	
	/**
	 * @see org.eclipse.emf.validation.service.IModelConstraintProvider#getBatchConstraints(org.eclipse.emf.ecore.EObject, java.util.Collection)
	 */
	@Override
	public Collection<IModelConstraint> getBatchConstraints(final EObject eObject, Collection<IModelConstraint> constraints) {
		Collection<IModelConstraint> result = Collections2.filter(allConstraints, new Predicate<IModelConstraint>(){

			@Override
			public boolean apply(IModelConstraint arg0) {
				return arg0.getDescriptor().targetsTypeOf(eObject);
			}
		});
		if (constraints != null){
			constraints.addAll(result);
			return constraints;
		}
		return result;
	}

	/**
	 * @see org.eclipse.emf.validation.service.IModelConstraintProvider#getLiveConstraints(org.eclipse.emf.common.notify.Notification, java.util.Collection)
	 */
	@Override
	public Collection<IModelConstraint> getLiveConstraints(final Notification notification, Collection<IModelConstraint> constraints) {
		Collection<IModelConstraint> result = Collections2.filter(allConstraints, new Predicate<IModelConstraint>(){

			@Override
			public boolean apply(IModelConstraint arg0) {
				return arg0.getDescriptor().targetsEvent(notification);
			}
		});
		if (constraints != null){
			constraints.addAll(result);
			return constraints;
		}
		return result;
	}
	
	/**
	 * Loads the constraints from an ocl model, converts them to EMF constraints and stores them.
	 * @param uri the uri of an ocl model.
	 */
	protected void loadConstraintsFromCompleteOCL(URI uri){
		final Resource oclResource= ocl.parse(uri);
		Iterable<Constraint> oclConstraints = Iterables.filter(new Iterable<EObject>(){
			@Override
			public Iterator<EObject> iterator() {
				return oclResource.getAllContents();
			}
		}, Constraint.class);
		
		Iterable<IModelConstraint> emfConstraints = Iterables.transform(oclConstraints, new Function<Constraint, IModelConstraint>(){

			@Override
			public IModelConstraint apply(Constraint arg0) {
				return new OCLtoEMFConstraintWrapper(ocl, arg0);
			}
			
		});
		Iterables.addAll(allConstraints, emfConstraints);
	}
	
	/**
	 * Retrieves the uri of the ocl resources that should be parsed to get the constraints
	 * associated with this provider. 
	 * @return a possibly unmodifiable iterable of files that are compliant with the CompleteOCL metamodel.
	 */
	protected abstract Iterable<URI> getOclUris();
	
}
