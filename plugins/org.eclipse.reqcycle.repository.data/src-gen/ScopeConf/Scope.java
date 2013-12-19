/**
 */
package ScopeConf;

import RequirementSourceData.AbstractElement;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Scope</b></em>'.
 * <!-- end-user-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link ScopeConf.Scope#getName <em>Name</em>}</li>
 * <li>{@link ScopeConf.Scope#getRequirements <em>Requirements</em>}</li>
 * <li>{@link ScopeConf.Scope#getDataModelURI <em>Data Model URI</em>}</li>
 * </ul>
 * </p>
 * 
 * @see ScopeConf.ScopeConfPackage#getScope()
 * @model
 * @generated
 */
public interface Scope extends EObject {

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see ScopeConf.ScopeConfPackage#getScope_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link ScopeConf.Scope#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @param value
	 *        the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Requirements</b></em>' reference list.
	 * The list contents are of type {@link RequirementSourceData.AbstractElement}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Requirements</em>' reference list isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Requirements</em>' reference list.
	 * @see #isSetRequirements()
	 * @see ScopeConf.ScopeConfPackage#getScope_Requirements()
	 * @model resolveProxies="false" unsettable="true" transient="true" changeable="false" volatile="true" derived="true"
	 * @generated
	 */
	EList<AbstractElement> getRequirements();

	/**
	 * Returns whether the value of the '{@link ScopeConf.Scope#getRequirements <em>Requirements</em>}' reference list is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Requirements</em>' reference list is set.
	 * @see #getRequirements()
	 * @generated
	 */
	boolean isSetRequirements();

	/**
	 * Returns the value of the '<em><b>Data Model URI</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Data Model URI</em>' attribute isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Data Model URI</em>' attribute.
	 * @see #setDataModelURI(String)
	 * @see ScopeConf.ScopeConfPackage#getScope_DataModelURI()
	 * @model
	 * @generated
	 */
	String getDataModelURI();

	/**
	 * Sets the value of the '{@link ScopeConf.Scope#getDataModelURI <em>Data Model URI</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @param value
	 *        the new value of the '<em>Data Model URI</em>' attribute.
	 * @see #getDataModelURI()
	 * @generated
	 */
	void setDataModelURI(String value);

} // Scope
