/**
 */
package MappingModel;

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
 * @see MappingModel.MappingModelFactory
 * @model kind="package"
 * @generated
 */
public interface MappingModelPackage extends EPackage {

	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNAME = "MappingModel";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNS_URI = "http://www.eclipse.org/ReqCycle/MappingModel";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNS_PREFIX = "MappingModel";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	MappingModelPackage eINSTANCE = MappingModel.impl.MappingModelPackageImpl.init();

	/**
	 * The meta object id for the '{@link MappingModel.impl.MappingElementImpl <em>Mapping Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see MappingModel.impl.MappingElementImpl
	 * @see MappingModel.impl.MappingModelPackageImpl#getMappingElement()
	 * @generated
	 */
	int MAPPING_ELEMENT = 0;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MAPPING_ELEMENT__DESCRIPTION = 0;

	/**
	 * The feature id for the '<em><b>Target Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MAPPING_ELEMENT__TARGET_ELEMENT = 1;

	/**
	 * The feature id for the '<em><b>Attributes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MAPPING_ELEMENT__ATTRIBUTES = 2;

	/**
	 * The feature id for the '<em><b>Source Qualifier</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MAPPING_ELEMENT__SOURCE_QUALIFIER = 3;

	/**
	 * The number of structural features of the '<em>Mapping Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MAPPING_ELEMENT_FEATURE_COUNT = 4;

	/**
	 * The number of operations of the '<em>Mapping Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MAPPING_ELEMENT_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link MappingModel.impl.MappingAttributeImpl <em>Mapping Attribute</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see MappingModel.impl.MappingAttributeImpl
	 * @see MappingModel.impl.MappingModelPackageImpl#getMappingAttribute()
	 * @generated
	 */
	int MAPPING_ATTRIBUTE = 1;

	/**
	 * The feature id for the '<em><b>Target Attribute</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MAPPING_ATTRIBUTE__TARGET_ATTRIBUTE = 0;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MAPPING_ATTRIBUTE__DESCRIPTION = 1;

	/**
	 * The feature id for the '<em><b>Source Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MAPPING_ATTRIBUTE__SOURCE_ID = 2;

	/**
	 * The number of structural features of the '<em>Mapping Attribute</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MAPPING_ATTRIBUTE_FEATURE_COUNT = 3;

	/**
	 * The number of operations of the '<em>Mapping Attribute</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MAPPING_ATTRIBUTE_OPERATION_COUNT = 0;


	/**
	 * Returns the meta object for class '{@link MappingModel.MappingElement <em>Mapping Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Mapping Element</em>'.
	 * @see MappingModel.MappingElement
	 * @generated
	 */
	EClass getMappingElement();

	/**
	 * Returns the meta object for the attribute '{@link MappingModel.MappingElement#getDescription <em>Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Description</em>'.
	 * @see MappingModel.MappingElement#getDescription()
	 * @see #getMappingElement()
	 * @generated
	 */
	EAttribute getMappingElement_Description();

	/**
	 * Returns the meta object for the reference '{@link MappingModel.MappingElement#getTargetElement <em>Target Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference '<em>Target Element</em>'.
	 * @see MappingModel.MappingElement#getTargetElement()
	 * @see #getMappingElement()
	 * @generated
	 */
	EReference getMappingElement_TargetElement();

	/**
	 * Returns the meta object for the containment reference list '{@link MappingModel.MappingElement#getAttributes <em>Attributes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '<em>Attributes</em>'.
	 * @see MappingModel.MappingElement#getAttributes()
	 * @see #getMappingElement()
	 * @generated
	 */
	EReference getMappingElement_Attributes();

	/**
	 * Returns the meta object for the attribute '{@link MappingModel.MappingElement#getSourceQualifier <em>Source Qualifier</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Source Qualifier</em>'.
	 * @see MappingModel.MappingElement#getSourceQualifier()
	 * @see #getMappingElement()
	 * @generated
	 */
	EAttribute getMappingElement_SourceQualifier();

	/**
	 * Returns the meta object for class '{@link MappingModel.MappingAttribute <em>Mapping Attribute</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Mapping Attribute</em>'.
	 * @see MappingModel.MappingAttribute
	 * @generated
	 */
	EClass getMappingAttribute();

	/**
	 * Returns the meta object for the reference '{@link MappingModel.MappingAttribute#getTargetAttribute <em>Target Attribute</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference '<em>Target Attribute</em>'.
	 * @see MappingModel.MappingAttribute#getTargetAttribute()
	 * @see #getMappingAttribute()
	 * @generated
	 */
	EReference getMappingAttribute_TargetAttribute();

	/**
	 * Returns the meta object for the attribute '{@link MappingModel.MappingAttribute#getDescription <em>Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Description</em>'.
	 * @see MappingModel.MappingAttribute#getDescription()
	 * @see #getMappingAttribute()
	 * @generated
	 */
	EAttribute getMappingAttribute_Description();

	/**
	 * Returns the meta object for the attribute '{@link MappingModel.MappingAttribute#getSourceId <em>Source Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Source Id</em>'.
	 * @see MappingModel.MappingAttribute#getSourceId()
	 * @see #getMappingAttribute()
	 * @generated
	 */
	EAttribute getMappingAttribute_SourceId();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	MappingModelFactory getMappingModelFactory();

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
		 * The meta object literal for the '{@link MappingModel.impl.MappingElementImpl <em>Mapping Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @see MappingModel.impl.MappingElementImpl
		 * @see MappingModel.impl.MappingModelPackageImpl#getMappingElement()
		 * @generated
		 */
		EClass MAPPING_ELEMENT = eINSTANCE.getMappingElement();

		/**
		 * The meta object literal for the '<em><b>Description</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute MAPPING_ELEMENT__DESCRIPTION = eINSTANCE.getMappingElement_Description();

		/**
		 * The meta object literal for the '<em><b>Target Element</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference MAPPING_ELEMENT__TARGET_ELEMENT = eINSTANCE.getMappingElement_TargetElement();

		/**
		 * The meta object literal for the '<em><b>Attributes</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference MAPPING_ELEMENT__ATTRIBUTES = eINSTANCE.getMappingElement_Attributes();

		/**
		 * The meta object literal for the '<em><b>Source Qualifier</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute MAPPING_ELEMENT__SOURCE_QUALIFIER = eINSTANCE.getMappingElement_SourceQualifier();

		/**
		 * The meta object literal for the '{@link MappingModel.impl.MappingAttributeImpl <em>Mapping Attribute</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @see MappingModel.impl.MappingAttributeImpl
		 * @see MappingModel.impl.MappingModelPackageImpl#getMappingAttribute()
		 * @generated
		 */
		EClass MAPPING_ATTRIBUTE = eINSTANCE.getMappingAttribute();

		/**
		 * The meta object literal for the '<em><b>Target Attribute</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference MAPPING_ATTRIBUTE__TARGET_ATTRIBUTE = eINSTANCE.getMappingAttribute_TargetAttribute();

		/**
		 * The meta object literal for the '<em><b>Description</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute MAPPING_ATTRIBUTE__DESCRIPTION = eINSTANCE.getMappingAttribute_Description();

		/**
		 * The meta object literal for the '<em><b>Source Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute MAPPING_ATTRIBUTE__SOURCE_ID = eINSTANCE.getMappingAttribute_SourceId();

	}

} //MappingModelPackage
