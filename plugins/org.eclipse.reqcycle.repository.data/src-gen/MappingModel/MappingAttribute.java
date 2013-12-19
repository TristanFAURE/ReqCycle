/**
 */
package MappingModel;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Mapping Attribute</b></em>'.
 * <!-- end-user-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link MappingModel.MappingAttribute#getTargetAttribute <em>Target Attribute</em>}</li>
 * <li>{@link MappingModel.MappingAttribute#getDescription <em>Description</em>}</li>
 * <li>{@link MappingModel.MappingAttribute#getSourceId <em>Source Id</em>}</li>
 * </ul>
 * </p>
 * 
 * @see MappingModel.MappingModelPackage#getMappingAttribute()
 * @model
 * @generated
 */
public interface MappingAttribute extends EObject {

	/**
	 * Returns the value of the '<em><b>Target Attribute</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Target Attribute</em>' reference isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Target Attribute</em>' reference.
	 * @see #setTargetAttribute(EAttribute)
	 * @see MappingModel.MappingModelPackage#getMappingAttribute_TargetAttribute()
	 * @model required="true"
	 * @generated
	 */
	EAttribute getTargetAttribute();

	/**
	 * Sets the value of the '{@link MappingModel.MappingAttribute#getTargetAttribute <em>Target Attribute</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @param value
	 *        the new value of the '<em>Target Attribute</em>' reference.
	 * @see #getTargetAttribute()
	 * @generated
	 */
	void setTargetAttribute(EAttribute value);

	/**
	 * Returns the value of the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Description</em>' attribute isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Description</em>' attribute.
	 * @see #setDescription(String)
	 * @see MappingModel.MappingModelPackage#getMappingAttribute_Description()
	 * @model
	 * @generated
	 */
	String getDescription();

	/**
	 * Sets the value of the '{@link MappingModel.MappingAttribute#getDescription <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @param value
	 *        the new value of the '<em>Description</em>' attribute.
	 * @see #getDescription()
	 * @generated
	 */
	void setDescription(String value);

	/**
	 * Returns the value of the '<em><b>Source Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Source Id</em>' attribute isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Source Id</em>' attribute.
	 * @see #setSourceId(String)
	 * @see MappingModel.MappingModelPackage#getMappingAttribute_SourceId()
	 * @model
	 * @generated
	 */
	String getSourceId();

	/**
	 * Sets the value of the '{@link MappingModel.MappingAttribute#getSourceId <em>Source Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @param value
	 *        the new value of the '<em>Source Id</em>' attribute.
	 * @see #getSourceId()
	 * @generated
	 */
	void setSourceId(String value);

} // MappingAttribute
