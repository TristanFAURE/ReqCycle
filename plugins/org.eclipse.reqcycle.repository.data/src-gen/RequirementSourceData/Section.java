/**
 */
package RequirementSourceData;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Section</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link RequirementSourceData.Section#getChildren <em>Children</em>}</li>
 * </ul>
 * </p>
 *
 * @see RequirementSourceData.RequirementSourceDataPackage#getSection()
 * @model
 * @generated
 */
public interface Section extends AbstractElement {

	/**
	 * Returns the value of the '<em><b>Children</b></em>' containment reference list.
	 * The list contents are of type {@link RequirementSourceData.AbstractElement}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Children</em>' containment reference list isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Children</em>' containment reference list.
	 * @see RequirementSourceData.RequirementSourceDataPackage#getSection_Children()
	 * @model containment="true"
	 * @generated
	 */
	EList<AbstractElement> getChildren();

} // Section
