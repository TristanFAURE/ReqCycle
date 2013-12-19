/**
 */
package RequirementSourceConf.impl;

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import RequirementSourceConf.RequirementSource;
import RequirementSourceConf.RequirementSourceConfPackage;
import RequirementSourceConf.RequirementSources;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Requirement Sources</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link RequirementSourceConf.impl.RequirementSourcesImpl#getRequirementSources <em>Requirement Sources</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class RequirementSourcesImpl extends MinimalEObjectImpl.Container implements RequirementSources {

	/**
	 * The cached value of the '{@link #getRequirementSources() <em>Requirement Sources</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getRequirementSources()
	 * @generated
	 * @ordered
	 */
	protected EList<RequirementSource> requirementSources;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected RequirementSourcesImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return RequirementSourceConfPackage.Literals.REQUIREMENT_SOURCES;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EList<RequirementSource> getRequirementSources() {
		if(requirementSources == null) {
			requirementSources = new EObjectContainmentEList<RequirementSource>(RequirementSource.class, this, RequirementSourceConfPackage.REQUIREMENT_SOURCES__REQUIREMENT_SOURCES);
		}
		return requirementSources;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch(featureID) {
		case RequirementSourceConfPackage.REQUIREMENT_SOURCES__REQUIREMENT_SOURCES:
			return ((InternalEList<?>)getRequirementSources()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch(featureID) {
		case RequirementSourceConfPackage.REQUIREMENT_SOURCES__REQUIREMENT_SOURCES:
			return getRequirementSources();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch(featureID) {
		case RequirementSourceConfPackage.REQUIREMENT_SOURCES__REQUIREMENT_SOURCES:
			getRequirementSources().clear();
			getRequirementSources().addAll((Collection<? extends RequirementSource>)newValue);
			return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch(featureID) {
		case RequirementSourceConfPackage.REQUIREMENT_SOURCES__REQUIREMENT_SOURCES:
			getRequirementSources().clear();
			return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch(featureID) {
		case RequirementSourceConfPackage.REQUIREMENT_SOURCES__REQUIREMENT_SOURCES:
			return requirementSources != null && !requirementSources.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	@Override
	public void removeRequirementSource(RequirementSource requirementSource) {
		Assert.isNotNull(requirementSource);
		Iterator<RequirementSource> iterator = requirementSources.iterator();
		while(iterator.hasNext()) {
			RequirementSource requirementSourceRepository = iterator.next();
			if(requirementSource == requirementSourceRepository) {
				iterator.remove();
				return;
			}
		}
	}

} //RequirementSourcesImpl
