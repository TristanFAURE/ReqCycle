/**
 */
package DataModel;

import java.beans.PropertyChangeListener;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import MappingModel.ElementMapping;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Requirement Source</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link DataModel.RequirementSource#getRequirements <em>Requirements</em>}</li>
 *   <li>{@link DataModel.RequirementSource#getName <em>Name</em>}</li>
 *   <li>{@link DataModel.RequirementSource#getProperties <em>Properties</em>}</li>
 *   <li>{@link DataModel.RequirementSource#getConnectorId <em>Connector Id</em>}</li>
 *   <li>{@link DataModel.RequirementSource#getMappings <em>Mappings</em>}</li>
 * </ul>
 * </p>
 *
 * @see DataModel.DataModelPackage#getRequirementSource()
 * @model
 * @generated
 */
public interface RequirementSource extends EObject {
	/**
	 * Returns the value of the '<em><b>Requirements</b></em>' containment reference list.
	 * The list contents are of type {@link DataModel.Contained}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Requirements</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Requirements</em>' containment reference list.
	 * @see DataModel.DataModelPackage#getRequirementSource_Requirements()
	 * @model containment="true"
	 * @generated
	 */
	EList<Contained> getRequirements();

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see DataModel.DataModelPackage#getRequirementSource_Name()
	 * @model transient="true" volatile="true" derived="true"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link DataModel.RequirementSource#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Properties</b></em>' map.
	 * The key is of type {@link java.lang.String},
	 * and the value is of type {@link java.lang.String},
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Properties</em>' map isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Properties</em>' map.
	 * @see DataModel.DataModelPackage#getRequirementSource_Properties()
	 * @model mapType="org.eclipse.emf.ecore.EStringToStringMapEntry<org.eclipse.emf.ecore.EString, org.eclipse.emf.ecore.EString>"
	 * @generated
	 */
	EMap<String, String> getProperties();

	/**
	 * Returns the value of the '<em><b>Connector Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Connector Id</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Connector Id</em>' attribute.
	 * @see #setConnectorId(String)
	 * @see DataModel.DataModelPackage#getRequirementSource_ConnectorId()
	 * @model transient="true" volatile="true" derived="true"
	 * @generated
	 */
	String getConnectorId();

	/**
	 * Sets the value of the '{@link DataModel.RequirementSource#getConnectorId <em>Connector Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Connector Id</em>' attribute.
	 * @see #getConnectorId()
	 * @generated
	 */
	void setConnectorId(String value);

	/**
	 * Returns the value of the '<em><b>Mappings</b></em>' containment reference list.
	 * The list contents are of type {@link MappingModel.ElementMapping}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Mappings</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Mappings</em>' containment reference list.
	 * @see DataModel.DataModelPackage#getRequirementSource_Mappings()
	 * @model containment="true"
	 * @generated
	 */
	EList<ElementMapping> getMappings();

	public void setProperty(String property, String newValue) throws Exception;
	
	public String getRepositoryLabel();

	public String getRepositoryUri();

	public boolean hasProperty(String propertyKey);

	public String getProperty(String propertyKey);

	public void removeProperty(String key);

	public void removeChangeListeners(PropertyChangeListener listener);

	public void addChangeListeners(PropertyChangeListener listener);
	
	public void store();

	public void dispose();
	
	public void setTargetEPackage(EPackage ePackage);
	
	public EPackage getTargetEPackage();

} // RequirementSource
