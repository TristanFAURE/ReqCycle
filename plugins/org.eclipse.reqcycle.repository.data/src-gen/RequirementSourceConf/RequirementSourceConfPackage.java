/**
 */
package RequirementSourceConf;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 * <li>each class,</li>
 * <li>each feature of each class,</li>
 * <li>each operation of each class,</li>
 * <li>each enum,</li>
 * <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * 
 * @see RequirementSourceConf.RequirementSourceConfFactory
 * @model kind="package"
 * @generated
 */
public interface RequirementSourceConfPackage extends EPackage {

	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNAME = "RequirementSourceConf";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNS_URI = "http://www.eclipse.org/ReqCycle/RequirementSourceConf";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNS_PREFIX = "RequirementSourceConf";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	RequirementSourceConfPackage eINSTANCE = RequirementSourceConf.impl.RequirementSourceConfPackageImpl.init();

	/**
	 * The meta object id for the '{@link RequirementSourceConf.impl.RequirementSourcesImpl <em>Requirement Sources</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see RequirementSourceConf.impl.RequirementSourcesImpl
	 * @see RequirementSourceConf.impl.RequirementSourceConfPackageImpl#getRequirementSources()
	 * @generated
	 */
	int REQUIREMENT_SOURCES = 0;

	/**
	 * The feature id for the '<em><b>Requirement Sources</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REQUIREMENT_SOURCES__REQUIREMENT_SOURCES = 0;

	/**
	 * The number of structural features of the '<em>Requirement Sources</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REQUIREMENT_SOURCES_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Requirement Sources</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REQUIREMENT_SOURCES_OPERATION_COUNT = 0;


	/**
	 * The meta object id for the '{@link RequirementSourceConf.impl.RequirementSourceImpl <em>Requirement Source</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see RequirementSourceConf.impl.RequirementSourceImpl
	 * @see RequirementSourceConf.impl.RequirementSourceConfPackageImpl#getRequirementSource()
	 * @generated
	 */
	int REQUIREMENT_SOURCE = 1;

	/**
	 * The feature id for the '<em><b>Contents</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REQUIREMENT_SOURCE__CONTENTS = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REQUIREMENT_SOURCE__NAME = 1;

	/**
	 * The feature id for the '<em><b>Properties</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REQUIREMENT_SOURCE__PROPERTIES = 2;

	/**
	 * The feature id for the '<em><b>Connector Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REQUIREMENT_SOURCE__CONNECTOR_ID = 3;

	/**
	 * The feature id for the '<em><b>Mappings</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REQUIREMENT_SOURCE__MAPPINGS = 4;

	/**
	 * The feature id for the '<em><b>Data Model URI</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REQUIREMENT_SOURCE__DATA_MODEL_URI = 5;

	/**
	 * The feature id for the '<em><b>Default Scope</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REQUIREMENT_SOURCE__DEFAULT_SCOPE = 6;

	/**
	 * The number of structural features of the '<em>Requirement Source</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REQUIREMENT_SOURCE_FEATURE_COUNT = 7;

	/**
	 * The number of operations of the '<em>Requirement Source</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REQUIREMENT_SOURCE_OPERATION_COUNT = 0;


	/**
	 * Returns the meta object for class '{@link RequirementSourceConf.RequirementSources <em>Requirement Sources</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Requirement Sources</em>'.
	 * @see RequirementSourceConf.RequirementSources
	 * @generated
	 */
	EClass getRequirementSources();

	/**
	 * Returns the meta object for the containment reference list '{@link RequirementSourceConf.RequirementSources#getRequirementSources
	 * <em>Requirement Sources</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '<em>Requirement Sources</em>'.
	 * @see RequirementSourceConf.RequirementSources#getRequirementSources()
	 * @see #getRequirementSources()
	 * @generated
	 */
	EReference getRequirementSources_RequirementSources();

	/**
	 * Returns the meta object for class '{@link RequirementSourceConf.RequirementSource <em>Requirement Source</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Requirement Source</em>'.
	 * @see RequirementSourceConf.RequirementSource
	 * @generated
	 */
	EClass getRequirementSource();

	/**
	 * Returns the meta object for the reference '{@link RequirementSourceConf.RequirementSource#getContents <em>Contents</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference '<em>Contents</em>'.
	 * @see RequirementSourceConf.RequirementSource#getContents()
	 * @see #getRequirementSource()
	 * @generated
	 */
	EReference getRequirementSource_Contents();

	/**
	 * Returns the meta object for the attribute '{@link RequirementSourceConf.RequirementSource#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see RequirementSourceConf.RequirementSource#getName()
	 * @see #getRequirementSource()
	 * @generated
	 */
	EAttribute getRequirementSource_Name();

	/**
	 * Returns the meta object for the map '{@link RequirementSourceConf.RequirementSource#getProperties <em>Properties</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the map '<em>Properties</em>'.
	 * @see RequirementSourceConf.RequirementSource#getProperties()
	 * @see #getRequirementSource()
	 * @generated
	 */
	EReference getRequirementSource_Properties();

	/**
	 * Returns the meta object for the attribute '{@link RequirementSourceConf.RequirementSource#getConnectorId <em>Connector Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Connector Id</em>'.
	 * @see RequirementSourceConf.RequirementSource#getConnectorId()
	 * @see #getRequirementSource()
	 * @generated
	 */
	EAttribute getRequirementSource_ConnectorId();

	/**
	 * Returns the meta object for the containment reference list '{@link RequirementSourceConf.RequirementSource#getMappings <em>Mappings</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '<em>Mappings</em>'.
	 * @see RequirementSourceConf.RequirementSource#getMappings()
	 * @see #getRequirementSource()
	 * @generated
	 */
	EReference getRequirementSource_Mappings();

	/**
	 * Returns the meta object for the attribute '{@link RequirementSourceConf.RequirementSource#getDataModelURI <em>Data Model URI</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Data Model URI</em>'.
	 * @see RequirementSourceConf.RequirementSource#getDataModelURI()
	 * @see #getRequirementSource()
	 * @generated
	 */
	EAttribute getRequirementSource_DataModelURI();

	/**
	 * Returns the meta object for the reference '{@link RequirementSourceConf.RequirementSource#getDefaultScope <em>Default Scope</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference '<em>Default Scope</em>'.
	 * @see RequirementSourceConf.RequirementSource#getDefaultScope()
	 * @see #getRequirementSource()
	 * @generated
	 */
	EReference getRequirementSource_DefaultScope();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	RequirementSourceConfFactory getRequirementSourceConfFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 * <li>each class,</li>
	 * <li>each feature of each class,</li>
	 * <li>each operation of each class,</li>
	 * <li>each enum,</li>
	 * <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	interface Literals {

		/**
		 * The meta object literal for the '{@link RequirementSourceConf.impl.RequirementSourcesImpl <em>Requirement Sources</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @see RequirementSourceConf.impl.RequirementSourcesImpl
		 * @see RequirementSourceConf.impl.RequirementSourceConfPackageImpl#getRequirementSources()
		 * @generated
		 */
		EClass REQUIREMENT_SOURCES = eINSTANCE.getRequirementSources();

		/**
		 * The meta object literal for the '<em><b>Requirement Sources</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference REQUIREMENT_SOURCES__REQUIREMENT_SOURCES = eINSTANCE.getRequirementSources_RequirementSources();

		/**
		 * The meta object literal for the '{@link RequirementSourceConf.impl.RequirementSourceImpl <em>Requirement Source</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @see RequirementSourceConf.impl.RequirementSourceImpl
		 * @see RequirementSourceConf.impl.RequirementSourceConfPackageImpl#getRequirementSource()
		 * @generated
		 */
		EClass REQUIREMENT_SOURCE = eINSTANCE.getRequirementSource();

		/**
		 * The meta object literal for the '<em><b>Contents</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference REQUIREMENT_SOURCE__CONTENTS = eINSTANCE.getRequirementSource_Contents();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute REQUIREMENT_SOURCE__NAME = eINSTANCE.getRequirementSource_Name();

		/**
		 * The meta object literal for the '<em><b>Properties</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference REQUIREMENT_SOURCE__PROPERTIES = eINSTANCE.getRequirementSource_Properties();

		/**
		 * The meta object literal for the '<em><b>Connector Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute REQUIREMENT_SOURCE__CONNECTOR_ID = eINSTANCE.getRequirementSource_ConnectorId();

		/**
		 * The meta object literal for the '<em><b>Mappings</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference REQUIREMENT_SOURCE__MAPPINGS = eINSTANCE.getRequirementSource_Mappings();

		/**
		 * The meta object literal for the '<em><b>Data Model URI</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute REQUIREMENT_SOURCE__DATA_MODEL_URI = eINSTANCE.getRequirementSource_DataModelURI();

		/**
		 * The meta object literal for the '<em><b>Default Scope</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference REQUIREMENT_SOURCE__DEFAULT_SCOPE = eINSTANCE.getRequirementSource_DefaultScope();

	}

} //RequirementSourceConfPackage
