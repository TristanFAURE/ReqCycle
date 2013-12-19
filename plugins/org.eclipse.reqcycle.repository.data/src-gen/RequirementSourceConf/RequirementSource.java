/**
 */
package RequirementSourceConf;

import java.beans.PropertyChangeListener;
import java.util.Collection;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import MappingModel.MappingElement;
import RequirementSourceData.AbstractElement;
import RequirementSourceData.RequirementsContainer;
import ScopeConf.Scope;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Requirement Source</b></em>'.
 * <!-- end-user-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link RequirementSourceConf.RequirementSource#getContents <em>Contents</em>}</li>
 * <li>{@link RequirementSourceConf.RequirementSource#getName <em>Name</em>}</li>
 * <li>{@link RequirementSourceConf.RequirementSource#getProperties <em>Properties</em>}</li>
 * <li>{@link RequirementSourceConf.RequirementSource#getConnectorId <em>Connector Id</em>}</li>
 * <li>{@link RequirementSourceConf.RequirementSource#getMappings <em>Mappings</em>}</li>
 * <li>{@link RequirementSourceConf.RequirementSource#getDataModelURI <em>Data Model URI</em>}</li>
 * <li>{@link RequirementSourceConf.RequirementSource#getDefaultScope <em>Default Scope</em>}</li>
 * </ul>
 * </p>
 * 
 * @see RequirementSourceConf.RequirementSourceConfPackage#getRequirementSource()
 * @model
 * @generated
 */
public interface RequirementSource extends EObject {

	/**
	 * Returns the value of the '<em><b>Contents</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Contents</em>' reference isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Contents</em>' reference.
	 * @see #setContents(RequirementsContainer)
	 * @see RequirementSourceConf.RequirementSourceConfPackage#getRequirementSource_Contents()
	 * @model
	 * @generated
	 */
	RequirementsContainer getContents();

	/**
	 * Sets the value of the '{@link RequirementSourceConf.RequirementSource#getContents <em>Contents</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @param value
	 *        the new value of the '<em>Contents</em>' reference.
	 * @see #getContents()
	 * @generated
	 */
	void setContents(RequirementsContainer value);

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
	 * @see RequirementSourceConf.RequirementSourceConfPackage#getRequirementSource_Name()
	 * @model transient="true" volatile="true" derived="true"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link RequirementSourceConf.RequirementSource#getName <em>Name</em>}' attribute.
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
	 * Returns the value of the '<em><b>Properties</b></em>' map.
	 * The key is of type {@link java.lang.String},
	 * and the value is of type {@link java.lang.String},
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Properties</em>' map isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Properties</em>' map.
	 * @see RequirementSourceConf.RequirementSourceConfPackage#getRequirementSource_Properties()
	 * @model mapType="org.eclipse.emf.ecore.EStringToStringMapEntry<org.eclipse.emf.ecore.EString, org.eclipse.emf.ecore.EString>"
	 * @generated
	 */
	EMap<String, String> getProperties();

	/**
	 * Returns the value of the '<em><b>Connector Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Connector Id</em>' attribute isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Connector Id</em>' attribute.
	 * @see #setConnectorId(String)
	 * @see RequirementSourceConf.RequirementSourceConfPackage#getRequirementSource_ConnectorId()
	 * @model transient="true" volatile="true" derived="true"
	 * @generated
	 */
	String getConnectorId();

	/**
	 * Sets the value of the '{@link RequirementSourceConf.RequirementSource#getConnectorId <em>Connector Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @param value
	 *        the new value of the '<em>Connector Id</em>' attribute.
	 * @see #getConnectorId()
	 * @generated
	 */
	void setConnectorId(String value);

	/**
	 * Returns the value of the '<em><b>Mappings</b></em>' containment reference list.
	 * The list contents are of type {@link MappingModel.MappingElement}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Mappings</em>' containment reference list isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Mappings</em>' containment reference list.
	 * @see RequirementSourceConf.RequirementSourceConfPackage#getRequirementSource_Mappings()
	 * @model containment="true"
	 * @generated
	 */
	EList<MappingElement> getMappings();

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
	 * @see RequirementSourceConf.RequirementSourceConfPackage#getRequirementSource_DataModelURI()
	 * @model
	 * @generated
	 */
	String getDataModelURI();

	/**
	 * Sets the value of the '{@link RequirementSourceConf.RequirementSource#getDataModelURI <em>Data Model URI</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @param value
	 *        the new value of the '<em>Data Model URI</em>' attribute.
	 * @see #getDataModelURI()
	 * @generated
	 */
	void setDataModelURI(String value);

	/**
	 * Returns the value of the '<em><b>Default Scope</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Default Scope</em>' reference isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Default Scope</em>' reference.
	 * @see #setDefaultScope(Scope)
	 * @see RequirementSourceConf.RequirementSourceConfPackage#getRequirementSource_DefaultScope()
	 * @model
	 * @generated
	 */
	Scope getDefaultScope();

	/**
	 * Sets the value of the '{@link RequirementSourceConf.RequirementSource#getDefaultScope <em>Default Scope</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @param value
	 *        the new value of the '<em>Default Scope</em>' reference.
	 * @see #getDefaultScope()
	 * @generated
	 */
	void setDefaultScope(Scope value);

	EList<AbstractElement> getRequirements();

	void setProperty(String property, String newValue) throws Exception;

	String getRepositoryUri();

	boolean hasProperty(String propertyKey);

	String getProperty(String propertyKey);

	void removeProperty(String key);

	void removeChangeListeners(PropertyChangeListener listener);

	void addChangeListeners(PropertyChangeListener listener);

	void store();

	void dispose();

	Collection<EClass> getTargetEPackage();

	boolean contains(AbstractElement abstractElement);

	void clearContent();

	void setRequirementsResourceURI(URI uri);

} // RequirementSource
