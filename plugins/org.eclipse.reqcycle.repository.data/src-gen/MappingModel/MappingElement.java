/**
 */
package MappingModel;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Mapping Element</b></em>'.
 * <!-- end-user-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link MappingModel.MappingElement#getDescription <em>Description</em>}</li>
 * <li>{@link MappingModel.MappingElement#getTargetElement <em>Target Element</em>}</li>
 * <li>{@link MappingModel.MappingElement#getAttributes <em>Attributes</em>}</li>
 * <li>{@link MappingModel.MappingElement#getSourceQualifier <em>Source Qualifier</em>}</li>
 * </ul>
 * </p>
 * 
 * @see MappingModel.MappingModelPackage#getMappingElement()
 * @model
 * @generated
 */
public interface MappingElement extends EObject {

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
	 * @see MappingModel.MappingModelPackage#getMappingElement_Description()
	 * @model
	 * @generated
	 */
	String getDescription();

	/**
	 * Sets the value of the '{@link MappingModel.MappingElement#getDescription <em>Description</em>}' attribute.
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
	 * Returns the value of the '<em><b>Target Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Target Element</em>' reference isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Target Element</em>' reference.
	 * @see #setTargetElement(EClass)
	 * @see MappingModel.MappingModelPackage#getMappingElement_TargetElement()
	 * @model required="true"
	 * @generated
	 */
	EClass getTargetElement();

	/**
	 * Sets the value of the '{@link MappingModel.MappingElement#getTargetElement <em>Target Element</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @param value
	 *        the new value of the '<em>Target Element</em>' reference.
	 * @see #getTargetElement()
	 * @generated
	 */
	void setTargetElement(EClass value);

	/**
	 * Returns the value of the '<em><b>Attributes</b></em>' containment reference list.
	 * The list contents are of type {@link MappingModel.MappingAttribute}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Attributes</em>' containment reference list isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Attributes</em>' containment reference list.
	 * @see MappingModel.MappingModelPackage#getMappingElement_Attributes()
	 * @model containment="true"
	 * @generated
	 */
	EList<MappingAttribute> getAttributes();

	/**
	 * Returns the value of the '<em><b>Source Qualifier</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Source Qualifier</em>' attribute isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Source Qualifier</em>' attribute.
	 * @see #setSourceQualifier(String)
	 * @see MappingModel.MappingModelPackage#getMappingElement_SourceQualifier()
	 * @model
	 * @generated
	 */
	String getSourceQualifier();

	/**
	 * Sets the value of the '{@link MappingModel.MappingElement#getSourceQualifier <em>Source Qualifier</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @param value
	 *        the new value of the '<em>Source Qualifier</em>' attribute.
	 * @see #getSourceQualifier()
	 * @generated
	 */
	void setSourceQualifier(String value);

} // MappingElement
