/**
 */
package ScopeConf.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;

import RequirementSourceData.AbstractElement;
import RequirementSourceData.RequirementSourceDataPackage;
import ScopeConf.Scope;
import ScopeConf.ScopeConfPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Scope</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link ScopeConf.impl.ScopeImpl#getName <em>Name</em>}</li>
 * <li>{@link ScopeConf.impl.ScopeImpl#getRequirements <em>Requirements</em>}</li>
 * <li>{@link ScopeConf.impl.ScopeImpl#getDataModelURI <em>Data Model URI</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class ScopeImpl extends MinimalEObjectImpl.Container implements Scope {

	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getDataModelURI() <em>Data Model URI</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getDataModelURI()
	 * @generated
	 * @ordered
	 */
	protected static final String DATA_MODEL_URI_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDataModelURI() <em>Data Model URI</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getDataModelURI()
	 * @generated
	 * @ordered
	 */
	protected String dataModelURI = DATA_MODEL_URI_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected ScopeImpl() {
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
		return ScopeConfPackage.Literals.SCOPE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if(eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScopeConfPackage.SCOPE__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated not
	 */
	@Override
	public EList<AbstractElement> getRequirements() {
		return (EList<AbstractElement>)getOppositeObjs(RequirementSourceDataPackage.Literals.ABSTRACT_ELEMENT__SCOPES);
	}

	//	/**
	//	 * <!-- begin-user-doc -->
	//	 * <!-- end-user-doc -->
	//	 * 
	//	 * @generated not
	//	 */
	//	@Override
	//	public EList<AbstractElement> getRequirements() {
	//		return (EList<AbstractElement>)getOppositeObjs(RequirementSourceDataPackage.Literals.ABSTRACT_ELEMENT__SCOPES);
	//	}

	public EList<? extends EObject> getOppositeObjs(EReference oppositeRef) {

		ECrossReferenceAdapter c = ECrossReferenceAdapter.getCrossReferenceAdapter(this);
		if(c == null) {
			c = new ECrossReferenceAdapter();
		}

		if(c.getTarget() == null) {
			Resource r = this.eResource();
			if(r != null) {
				ResourceSet rs = r.getResourceSet();
				if(rs != null) {
					c.setTarget(rs);
				} else {
					c.setTarget(r);
				}
			} else {
				c.setTarget(this);
			}
		}

		EList<EObject> res = new BasicEList<EObject>();
		Collection<Setting> settings = c.getInverseReferences(this, true);
		for(Setting s : settings) {
			if(oppositeRef.equals(s.getEStructuralFeature())) {
				res.add(s.getEObject());
			}
		}
		return res;
	}



	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated not
	 */
	@Override
	public boolean isSetRequirements() {
		return true;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String getDataModelURI() {
		return dataModelURI;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setDataModelURI(String newDataModelURI) {
		String oldDataModelURI = dataModelURI;
		dataModelURI = newDataModelURI;
		if(eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScopeConfPackage.SCOPE__DATA_MODEL_URI, oldDataModelURI, dataModelURI));
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
		case ScopeConfPackage.SCOPE__NAME:
			return getName();
		case ScopeConfPackage.SCOPE__REQUIREMENTS:
			return getRequirements();
		case ScopeConfPackage.SCOPE__DATA_MODEL_URI:
			return getDataModelURI();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch(featureID) {
		case ScopeConfPackage.SCOPE__NAME:
			setName((String)newValue);
			return;
		case ScopeConfPackage.SCOPE__DATA_MODEL_URI:
			setDataModelURI((String)newValue);
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
		case ScopeConfPackage.SCOPE__NAME:
			setName(NAME_EDEFAULT);
			return;
		case ScopeConfPackage.SCOPE__DATA_MODEL_URI:
			setDataModelURI(DATA_MODEL_URI_EDEFAULT);
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
		case ScopeConfPackage.SCOPE__NAME:
			return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
		case ScopeConfPackage.SCOPE__REQUIREMENTS:
			return isSetRequirements();
		case ScopeConfPackage.SCOPE__DATA_MODEL_URI:
			return DATA_MODEL_URI_EDEFAULT == null ? dataModelURI != null : !DATA_MODEL_URI_EDEFAULT.equals(dataModelURI);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String toString() {
		if(eIsProxy())
			return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (name: ");
		result.append(name);
		result.append(", DataModelURI: ");
		result.append(dataModelURI);
		result.append(')');
		return result.toString();
	}

} //ScopeImpl
