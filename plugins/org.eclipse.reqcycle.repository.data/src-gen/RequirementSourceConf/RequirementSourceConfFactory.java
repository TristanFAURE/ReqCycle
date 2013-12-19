/**
 */
package RequirementSourceConf;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * 
 * @see RequirementSourceConf.RequirementSourceConfPackage
 * @generated
 */
public interface RequirementSourceConfFactory extends EFactory {

	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	RequirementSourceConfFactory eINSTANCE = RequirementSourceConf.impl.RequirementSourceConfFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Requirement Sources</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Requirement Sources</em>'.
	 * @generated
	 */
	RequirementSources createRequirementSources();

	/**
	 * Returns a new object of class '<em>Requirement Source</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Requirement Source</em>'.
	 * @generated
	 */
	RequirementSource createRequirementSource();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the package supported by this factory.
	 * @generated
	 */
	RequirementSourceConfPackage getRequirementSourceConfPackage();

} //RequirementSourceConfFactory
