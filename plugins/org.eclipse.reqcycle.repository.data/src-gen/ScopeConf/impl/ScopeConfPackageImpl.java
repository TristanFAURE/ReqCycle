/**
 */
package ScopeConf.impl;

import RequirementSourceData.RequirementSourceDataPackage;

import RequirementSourceData.impl.RequirementSourceDataPackageImpl;
import ScopeConf.Scope;
import ScopeConf.Scopes;
import ScopeConf.ScopeConfFactory;
import ScopeConf.ScopeConfPackage;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * 
 * @generated
 */
public class ScopeConfPackageImpl extends EPackageImpl implements ScopeConfPackage {

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass scopesEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass scopeEClass = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the
	 * package
	 * package URI value.
	 * <p>
	 * Note: the correct way to create the package is via the static factory method {@link #init init()}, which also performs initialization of the
	 * package, or returns the registered package, if one already exists. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see ScopeConf.ScopeConfPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private ScopeConfPackageImpl() {
		super(eNS_URI, ScopeConfFactory.eINSTANCE);
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
	 * This method is used to initialize {@link ScopeConfPackage#eINSTANCE} when that field is accessed. Clients should not invoke it directly.
	 * Instead, they should simply access that field to obtain the package. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static ScopeConfPackage init() {
		if(isInited)
			return (ScopeConfPackage)EPackage.Registry.INSTANCE.getEPackage(ScopeConfPackage.eNS_URI);

		// Obtain or create and register package
		ScopeConfPackageImpl theScopeConfPackage = (ScopeConfPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof ScopeConfPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new ScopeConfPackageImpl());

		isInited = true;

		// Obtain or create and register interdependencies
		RequirementSourceDataPackageImpl theRequirementSourceDataPackage = (RequirementSourceDataPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(RequirementSourceDataPackage.eNS_URI) instanceof RequirementSourceDataPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(RequirementSourceDataPackage.eNS_URI) : RequirementSourceDataPackage.eINSTANCE);

		// Create package meta-data objects
		theScopeConfPackage.createPackageContents();
		theRequirementSourceDataPackage.createPackageContents();

		// Initialize created meta-data
		theScopeConfPackage.initializePackageContents();
		theRequirementSourceDataPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theScopeConfPackage.freeze();


		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(ScopeConfPackage.eNS_URI, theScopeConfPackage);
		return theScopeConfPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getScopes() {
		return scopesEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getScopes_Scopes() {
		return (EReference)scopesEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getScope() {
		return scopeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getScope_Name() {
		return (EAttribute)scopeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getScope_Requirements() {
		return (EReference)scopeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getScope_DataModelURI() {
		return (EAttribute)scopeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ScopeConfFactory getScopeConfFactory() {
		return (ScopeConfFactory)getEFactoryInstance();
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
		scopesEClass = createEClass(SCOPES);
		createEReference(scopesEClass, SCOPES__SCOPES);

		scopeEClass = createEClass(SCOPE);
		createEAttribute(scopeEClass, SCOPE__NAME);
		createEReference(scopeEClass, SCOPE__REQUIREMENTS);
		createEAttribute(scopeEClass, SCOPE__DATA_MODEL_URI);
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

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes

		// Initialize classes, features, and operations; add parameters
		initEClass(scopesEClass, Scopes.class, "Scopes", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getScopes_Scopes(), this.getScope(), null, "scopes", null, 0, -1, Scopes.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(scopeEClass, Scope.class, "Scope", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getScope_Name(), ecorePackage.getEString(), "name", null, 0, 1, Scope.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getScope_Requirements(), theRequirementSourceDataPackage.getAbstractElement(), null, "requirements", null, 0, -1, Scope.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEAttribute(getScope_DataModelURI(), ecorePackage.getEString(), "DataModelURI", null, 0, 1, Scope.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Create resource
		createResource(eNS_URI);
	}

} //ScopeConfPackageImpl
