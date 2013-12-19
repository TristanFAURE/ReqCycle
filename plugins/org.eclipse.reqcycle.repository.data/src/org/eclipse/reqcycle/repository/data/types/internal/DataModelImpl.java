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
package org.eclipse.reqcycle.repository.data.types.internal;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.reqcycle.repository.data.IDataModelManager;
import org.eclipse.reqcycle.repository.data.types.IDataModel;
import org.eclipse.reqcycle.repository.data.types.IEnumerationType;
import org.eclipse.reqcycle.repository.data.types.IRequirementType;

import ScopeConf.Scope;

/**
 * The Class DataModelImpl.
 */
public class DataModelImpl implements IDataModel, IAdaptable {

	/** The ePackage. */
	protected EPackage ePackage;

	/** The subPackages. */
	protected Collection<IDataModel> subPackages = new ArrayList<IDataModel>();

	/** The scopes. */
	protected Collection<Scope> scopes = new ArrayList<Scope>();

	/** The requirement types. */
	protected Collection<IRequirementType> requirementTypes = new ArrayList<IRequirementType>();

	/** The enumeration types. */
	protected Collection<IEnumerationType> enumerationTypes = new ArrayList<IEnumerationType>();

	/**
	 * Instantiates a new data model.
	 */
	public DataModelImpl() {
		ePackage = EcoreFactory.eINSTANCE.createEPackage();
		ePackage.setName("");
		ePackage.setNsPrefix("");
		ePackage.setNsURI(IDataModelManager.MODEL_NS_URI);
	}

	/**
	 * Instantiates a new data model.
	 * 
	 * @param name
	 *        the data model name
	 */
	public DataModelImpl(String name) {
		ePackage = EcoreFactory.eINSTANCE.createEPackage();
		ePackage.setName(name);
		ePackage.setName(name);
		ePackage.setNsPrefix(name);
		ePackage.setNsURI(IDataModelManager.MODEL_NS_URI + "/" + name);
	}

	/**
	 * Instantiates a new data model.
	 * 
	 * @param ePackage
	 *        the ePackage
	 */
	public DataModelImpl(EPackage ePackage) {
		this.ePackage = ePackage;

		for(EClassifier classifier : ePackage.getEClassifiers()) {
			if(classifier instanceof EClass) {
				requirementTypes.add(new RequirementTypeImpl((EClass)classifier, this));
			} else if(classifier instanceof EEnum) {
				enumerationTypes.add(new EnumerationTypeImpl((EEnum)classifier));
			}
		}

		for(EPackage subPackage : ePackage.getESubpackages()) {
			subPackages.add(new DataModelImpl(subPackage));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.reqcycle.repository.data.types.IDataModel#getName()
	 */
	@Override
	public String getName() {
		return ePackage != null ? ePackage.getName() : null;
	}

	/**
	 * Gets the factory instance.
	 * 
	 * @return the factory instance
	 */
	EFactory getFactoryInstance() {
		return ePackage.getEFactoryInstance();
	}

	//	//FIXME : Continue
	//	/**
	//	 * Creates the.
	//	 * 
	//	 * @param dataType
	//	 *        the data type
	//	 * @return the requirement section
	//	 */
	//	@Override
	//	public Requirement create(IRequirementType dataType) {
	//		//		return (Requirement)createFactoryInstance().create(((RequirementTypeImpl)dataType).getEClass());
	//		EClass eclass = null;
	//		if(dataType instanceof IAdaptable) {
	//			eclass = (EClass)((IAdaptable)dataType).getAdapter(EClass.class);
	//		}
	//
	//		if(eclass == null) {
	//			return null;
	//		}
	//
	//		for(IDataModel p : getSubDataModels()) {
	//			EPackage pac = null;
	//			if(p instanceof IAdaptable) {
	//				pac = (EPackage)((IAdaptable)p).getAdapter(EPackage.class);
	//			}
	//			if(pac != null && pac.getEClassifiers().contains(eclass)) {
	//				return (Requirement)pac.getEFactoryInstance().create(eclass);
	//			}
	//		}
	//		return null;
	//	}

	/**
	 * Adds the data model.
	 * 
	 * @param dataModel
	 *        the data model to add
	 */
	public void addDataModel(IDataModel dataModel) {
		EPackage ePackage = null;
		if(dataModel instanceof IAdaptable) {
			ePackage = (EPackage)((IAdaptable)dataModel).getAdapter(EPackage.class);
		}
		if(ePackage != null) {
			this.ePackage.getESubpackages().add(ePackage);
			subPackages.add(dataModel);
		}
	}

	/**
	 * Gets the e package.
	 * 
	 * @return the e package
	 * @deprecated use getAdapter
	 */
	@Deprecated
	public EPackage getEPackage() {
		return ePackage;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.reqcycle.repository.data.types.IDataModel#addRequirementType(org.eclipse.reqcycle.repository.data.types.IRequirementType)
	 */
	@Override
	public void addRequirementType(IRequirementType dataType) {
		EClass eClass = null;
		if(dataType instanceof IAdaptable) {
			eClass = (EClass)((IAdaptable)dataType).getAdapter(EClass.class);
		}
		if(eClass != null) {
			ePackage.getEClassifiers().add(eClass);
			requirementTypes.add(dataType);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.reqcycle.repository.data.types.DataModel#add(org.eclipse.reqcycle.repository.data.types.EnumerationType)
	 */
	@Override
	public void addEnumerationType(IEnumerationType enumerationType) {
		EEnum eEnum = null;
		if(enumerationType instanceof IAdaptable) {
			eEnum = (EEnum)((IAdaptable)enumerationType).getAdapter(EEnum.class);
		}
		if(eEnum != null) {
			ePackage.getEClassifiers().add(eEnum);
			enumerationTypes.add(enumerationType);
		}
	}

	/**
	 * Gets the data model.
	 * 
	 * @param name
	 *        the name
	 * @return the data model
	 */
	public IDataModel getSubDataModel(String name) {
		if(name == null) {
			return null;
		}
		for(IDataModel p : subPackages) {
			if(name.equals(p.getName())) {
				return p;
			}
		}
		return null;
	}

	/**
	 * Gets the sub data models.
	 * 
	 * @return the sub data models
	 */
	public Collection<IDataModel> getSubDataModels() {
		return subPackages;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.reqcycle.repository.data.types.IDataModel#getEnumerationType(java.lang.String)
	 */
	@Override
	public IEnumerationType getEnumerationType(String name) {
		for(IEnumerationType enumerationType : enumerationTypes) {
			if(name.equals(enumerationType.getName())) {
				return enumerationType;
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.reqcycle.repository.data.types.IDataModel#getRequirementType(java.lang.String)
	 */
	@Override
	public IRequirementType getRequirementType(String name) {
		for(IRequirementType dataType : requirementTypes) {
			if(name.equals(dataType.getName())) {
				return dataType;
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.reqcycle.repository.data.types.IDataModel#getRequirementTypes()
	 */
	@Override
	public Collection<IRequirementType> getRequirementTypes() {
		return requirementTypes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.reqcycle.repository.data.types.IDataModel#getEnumerationTypes()
	 */
	@Override
	public Collection<IEnumerationType> getEnumerationTypes() {
		return enumerationTypes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.reqcycle.repository.data.types.IDataModel#addScope(DataModel.Scope)
	 * 
	 * @Deprecated Use IDataModelManager to add data models scopes
	 */
	@Deprecated
	@Override
	public void addScope(Scope scope) {

		EAnnotation scopeEAnnotation = ePackage.getEAnnotation("SCOPES");
		if(scopeEAnnotation == null) {
			scopeEAnnotation = EcoreFactory.eINSTANCE.createEAnnotation();
			scopeEAnnotation.setSource("SCOPES");
			ePackage.getEAnnotations().add(scopeEAnnotation);
		}
		scopes.add(scope);
		scopeEAnnotation.getContents().add(scope);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.reqcycle.repository.data.types.IDataModel#getScope(java.lang.String)
	 * 
	 * @Deprecated Use IDataModelManager to retrieve data models scopes
	 */
	@Deprecated
	@Override
	public Scope getScope(String name) {
		for(Scope scope : scopes) {
			if(name.equals(scope.getName())) {
				return scope;
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.reqcycle.repository.data.types.IDataModel#getScopes()
	 * 
	 * @Deprecated Use IDataModelManager to retrieve data models scopes
	 */
	@Deprecated
	@Override
	public Collection<Scope> getScopes() {
		return scopes;
	}

	//	/**
	//	 * Adds the data type.
	//	 * 
	//	 * @param type
	//	 *        the type
	//	 */
	//	public void addDataType(DataType type) {
	//		if(type instanceof IRequirementType) {
	//			addRequirementType((IRequirementType)type);
	//		} else if(type instanceof IEnumerationType) {
	//			addEnumerationType((IEnumerationType)type);
	//		}
	//	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class adapter) {
		if(adapter == EPackage.class) {
			return ePackage;
		}
		return null;
	}

	@Override
	public String getDataModelURI() {
		return ePackage.getNsURI();
	}

	public void removeDataModel(IDataModel p) {
		requirementTypes.removeAll(p.getRequirementTypes());
		enumerationTypes.removeAll(p.getEnumerationTypes());
		subPackages.remove(p);

		EPackage ePackage = null;
		if(p instanceof IAdaptable) {
			ePackage = (EPackage)((IAdaptable)p).getAdapter(EPackage.class);
		}
		if(ePackage != null) {
			this.ePackage.getESubpackages().remove(ePackage);
		}
		EcoreUtil.remove(ePackage);

	}

}
