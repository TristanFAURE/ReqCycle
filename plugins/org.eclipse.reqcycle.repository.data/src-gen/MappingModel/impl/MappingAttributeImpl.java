/**
 */
package MappingModel.impl;

import MappingModel.MappingAttribute;
import MappingModel.MappingModelPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Mapping Attribute</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link MappingModel.impl.MappingAttributeImpl#getTargetAttribute <em>Target Attribute</em>}</li>
 * <li>{@link MappingModel.impl.MappingAttributeImpl#getDescription <em>Description</em>}</li>
 * <li>{@link MappingModel.impl.MappingAttributeImpl#getSourceId <em>Source Id</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class MappingAttributeImpl extends MinimalEObjectImpl.Container implements MappingAttribute {

	/**
	 * The cached value of the '{@link #getTargetAttribute() <em>Target Attribute</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getTargetAttribute()
	 * @generated
	 * @ordered
	 */
	protected EAttribute targetAttribute;

	/**
	 * The default value of the '{@link #getDescription() <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
	protected static final String DESCRIPTION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDescription() <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
	protected String description = DESCRIPTION_EDEFAULT;

	/**
	 * The default value of the '{@link #getSourceId() <em>Source Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getSourceId()
	 * @generated
	 * @ordered
	 */
	protected static final String SOURCE_ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getSourceId() <em>Source Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getSourceId()
	 * @generated
	 * @ordered
	 */
	protected String sourceId = SOURCE_ID_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected MappingAttributeImpl() {
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
		return MappingModelPackage.Literals.MAPPING_ATTRIBUTE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getTargetAttribute() {
		if(targetAttribute != null && targetAttribute.eIsProxy()) {
			InternalEObject oldTargetAttribute = (InternalEObject)targetAttribute;
			targetAttribute = (EAttribute)eResolveProxy(oldTargetAttribute);
			if(targetAttribute != oldTargetAttribute) {
				if(eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, MappingModelPackage.MAPPING_ATTRIBUTE__TARGET_ATTRIBUTE, oldTargetAttribute, targetAttribute));
			}
		}
		return targetAttribute;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute basicGetTargetAttribute() {
		return targetAttribute;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setTargetAttribute(EAttribute newTargetAttribute) {
		EAttribute oldTargetAttribute = targetAttribute;
		targetAttribute = newTargetAttribute;
		if(eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MappingModelPackage.MAPPING_ATTRIBUTE__TARGET_ATTRIBUTE, oldTargetAttribute, targetAttribute));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setDescription(String newDescription) {
		String oldDescription = description;
		description = newDescription;
		if(eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MappingModelPackage.MAPPING_ATTRIBUTE__DESCRIPTION, oldDescription, description));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getSourceId() {
		return sourceId;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setSourceId(String newSourceId) {
		String oldSourceId = sourceId;
		sourceId = newSourceId;
		if(eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MappingModelPackage.MAPPING_ATTRIBUTE__SOURCE_ID, oldSourceId, sourceId));
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
		case MappingModelPackage.MAPPING_ATTRIBUTE__TARGET_ATTRIBUTE:
			if(resolve)
				return getTargetAttribute();
			return basicGetTargetAttribute();
		case MappingModelPackage.MAPPING_ATTRIBUTE__DESCRIPTION:
			return getDescription();
		case MappingModelPackage.MAPPING_ATTRIBUTE__SOURCE_ID:
			return getSourceId();
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
		case MappingModelPackage.MAPPING_ATTRIBUTE__TARGET_ATTRIBUTE:
			setTargetAttribute((EAttribute)newValue);
			return;
		case MappingModelPackage.MAPPING_ATTRIBUTE__DESCRIPTION:
			setDescription((String)newValue);
			return;
		case MappingModelPackage.MAPPING_ATTRIBUTE__SOURCE_ID:
			setSourceId((String)newValue);
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
		case MappingModelPackage.MAPPING_ATTRIBUTE__TARGET_ATTRIBUTE:
			setTargetAttribute((EAttribute)null);
			return;
		case MappingModelPackage.MAPPING_ATTRIBUTE__DESCRIPTION:
			setDescription(DESCRIPTION_EDEFAULT);
			return;
		case MappingModelPackage.MAPPING_ATTRIBUTE__SOURCE_ID:
			setSourceId(SOURCE_ID_EDEFAULT);
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
		case MappingModelPackage.MAPPING_ATTRIBUTE__TARGET_ATTRIBUTE:
			return targetAttribute != null;
		case MappingModelPackage.MAPPING_ATTRIBUTE__DESCRIPTION:
			return DESCRIPTION_EDEFAULT == null ? description != null : !DESCRIPTION_EDEFAULT.equals(description);
		case MappingModelPackage.MAPPING_ATTRIBUTE__SOURCE_ID:
			return SOURCE_ID_EDEFAULT == null ? sourceId != null : !SOURCE_ID_EDEFAULT.equals(sourceId);
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
		result.append(" (description: ");
		result.append(description);
		result.append(", sourceId: ");
		result.append(sourceId);
		result.append(')');
		return result.toString();
	}

} //MappingAttributeImpl
