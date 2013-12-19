/**
 */
package RequirementSourceConf.impl;

import MappingModel.MappingModelPackage;
import RequirementSourceConf.RequirementSource;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import RequirementSourceData.RequirementSourceDataPackage;
import ScopeConf.ScopeConfPackage;
import org.eclipse.emf.ecore.EAttribute;
import RequirementSourceConf.RequirementSources;
import RequirementSourceConf.RequirementSourceConfFactory;
import RequirementSourceConf.RequirementSourceConfPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * 
 * @generated
 */
public class RequirementSourceConfPackageImpl extends EPackageImpl implements RequirementSourceConfPackage {

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass requirementSourcesEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass requirementSourceEClass = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the
	 * package
	 * package URI value.
	 * <p>
	 * Note: the correct way to create the package is via the static factory method {@link #init init()}, which also performs initialization of the
	 * package, or returns the registered package, if one already exists. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see RequirementSourceConf.RequirementSourceConfPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private RequirementSourceConfPackageImpl() {
		super(eNS_URI, RequirementSourceConfFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 * 
	 * <p>
	 * This method is used to initialize {@link RequirementSourceConfPackage#eINSTANCE} when that field is accessed. Clients should not invoke it
	 * directly. Instead, they should simply access that field to obtain the package. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static RequirementSourceConfPackage init() {
		if(isInited)
			return (RequirementSourceConfPackage)EPackage.Registry.INSTANCE.getEPackage(RequirementSourceConfPackage.eNS_URI);

		// Obtain or create and register package
		RequirementSourceConfPackageImpl theRequirementSourceConfPackage = (RequirementSourceConfPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof RequirementSourceConfPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new RequirementSourceConfPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		MappingModelPackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theRequirementSourceConfPackage.createPackageContents();

		// Initialize created meta-data
		theRequirementSourceConfPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theRequirementSourceConfPackage.freeze();


		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(RequirementSourceConfPackage.eNS_URI, theRequirementSourceConfPackage);
		return theRequirementSourceConfPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EClass getRequirementSources() {
		return requirementSourcesEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EReference getRequirementSources_RequirementSources() {
		return (EReference)requirementSourcesEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getRequirementSource() {
		return requirementSourceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getRequirementSource_Contents() {
		return (EReference)requirementSourceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getRequirementSource_Name() {
		return (EAttribute)requirementSourceEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getRequirementSource_Properties() {
		return (EReference)requirementSourceEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getRequirementSource_ConnectorId() {
		return (EAttribute)requirementSourceEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getRequirementSource_Mappings() {
		return (EReference)requirementSourceEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getRequirementSource_DataModelURI() {
		return (EAttribute)requirementSourceEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getRequirementSource_DefaultScope() {
		return (EReference)requirementSourceEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public RequirementSourceConfFactory getRequirementSourceConfFactory() {
		return (RequirementSourceConfFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package. This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void createPackageContents() {
		if(isCreated)
			return;
		isCreated = true;

		// Create classes and their features
		requirementSourcesEClass = createEClass(REQUIREMENT_SOURCES);
		createEReference(requirementSourcesEClass, REQUIREMENT_SOURCES__REQUIREMENT_SOURCES);

		requirementSourceEClass = createEClass(REQUIREMENT_SOURCE);
		createEReference(requirementSourceEClass, REQUIREMENT_SOURCE__CONTENTS);
		createEAttribute(requirementSourceEClass, REQUIREMENT_SOURCE__NAME);
		createEReference(requirementSourceEClass, REQUIREMENT_SOURCE__PROPERTIES);
		createEAttribute(requirementSourceEClass, REQUIREMENT_SOURCE__CONNECTOR_ID);
		createEReference(requirementSourceEClass, REQUIREMENT_SOURCE__MAPPINGS);
		createEAttribute(requirementSourceEClass, REQUIREMENT_SOURCE__DATA_MODEL_URI);
		createEReference(requirementSourceEClass, REQUIREMENT_SOURCE__DEFAULT_SCOPE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model. This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void initializePackageContents() {
		if(isInitialized)
			return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Obtain other dependent packages
		RequirementSourceDataPackage theRequirementSourceDataPackage = (RequirementSourceDataPackage)EPackage.Registry.INSTANCE.getEPackage(RequirementSourceDataPackage.eNS_URI);
		EcorePackage theEcorePackage = (EcorePackage)EPackage.Registry.INSTANCE.getEPackage(EcorePackage.eNS_URI);
		MappingModelPackage theMappingModelPackage = (MappingModelPackage)EPackage.Registry.INSTANCE.getEPackage(MappingModelPackage.eNS_URI);
		ScopeConfPackage theScopeConfPackage = (ScopeConfPackage)EPackage.Registry.INSTANCE.getEPackage(ScopeConfPackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes

		// Initialize classes, features, and operations; add parameters
		initEClass(requirementSourcesEClass, RequirementSources.class, "RequirementSources", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getRequirementSources_RequirementSources(), this.getRequirementSource(), null, "requirementSources", null, 0, -1, RequirementSources.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(requirementSourceEClass, RequirementSource.class, "RequirementSource", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getRequirementSource_Contents(), theRequirementSourceDataPackage.getRequirementsContainer(), null, "contents", null, 0, 1, RequirementSource.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRequirementSource_Name(), ecorePackage.getEString(), "name", null, 0, 1, RequirementSource.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getRequirementSource_Properties(), theEcorePackage.getEStringToStringMapEntry(), null, "properties", null, 0, -1, RequirementSource.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRequirementSource_ConnectorId(), ecorePackage.getEString(), "connectorId", null, 0, 1, RequirementSource.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getRequirementSource_Mappings(), theMappingModelPackage.getMappingElement(), null, "mappings", null, 0, -1, RequirementSource.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRequirementSource_DataModelURI(), ecorePackage.getEString(), "DataModelURI", null, 0, 1, RequirementSource.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getRequirementSource_DefaultScope(), theScopeConfPackage.getScope(), null, "DefaultScope", null, 0, 1, RequirementSource.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Create resource
		createResource(eNS_URI);
	}

} //RequirementSourceConfPackageImpl
