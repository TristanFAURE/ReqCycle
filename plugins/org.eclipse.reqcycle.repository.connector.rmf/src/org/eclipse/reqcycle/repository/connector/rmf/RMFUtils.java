/*****************************************************************************
 * Copyright (c) 2013 AtoS.
 *
 *    
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Anass RADOUANI (AtoS) anass.radouani@atos.net - Initial API and implementation
 *
 *****************************************************************************/
package org.eclipse.reqcycle.repository.connector.rmf;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.reqcycle.core.ILogger;
import org.eclipse.reqcycle.repository.data.IDataManager;
import org.eclipse.reqcycle.repository.data.IDataModelManager;
import org.eclipse.reqcycle.repository.data.types.IRequirementType;
import org.eclipse.reqcycle.repository.data.types.internal.RequirementTypeImpl;
import org.eclipse.reqcycle.repository.data.util.DataUtil;
import org.eclipse.rmf.reqif10.AttributeValue;
import org.eclipse.rmf.reqif10.AttributeValueEnumeration;
import org.eclipse.rmf.reqif10.EnumValue;
import org.eclipse.rmf.reqif10.ReqIF;
import org.eclipse.rmf.reqif10.ReqIFContent;
import org.eclipse.rmf.reqif10.SpecElementWithAttributes;
import org.eclipse.rmf.reqif10.SpecHierarchy;
import org.eclipse.rmf.reqif10.SpecObject;
import org.eclipse.rmf.reqif10.SpecObjectType;
import org.eclipse.rmf.reqif10.SpecType;
import org.eclipse.rmf.reqif10.Specification;
import org.eclipse.rmf.reqif10.common.util.ReqIF10Util;
import org.eclipse.ziggurat.inject.ZigguratInject;

import MappingModel.MappingAttribute;
import MappingModel.MappingElement;
import RequirementSourceConf.RequirementSource;
import RequirementSourceData.AbstractElement;
import RequirementSourceData.Requirement;
import RequirementSourceData.Section;
import ScopeConf.Scope;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

public class RMFUtils {

	static ILogger logger = ZigguratInject.make(ILogger.class);

	static IDataModelManager dataTypeManager = ZigguratInject.make(IDataModelManager.class);

	static IDataManager dataManager = ZigguratInject.make(IDataManager.class);

	public static Collection<SpecType> getReqIFTypes(ResourceSet resourceSet, String fileLocation) {
		URI uriReqIf = URI.createURI(fileLocation, false);
		Resource reqIfResource = resourceSet.getResource(uriReqIf, true);
		EList<EObject> contents = reqIfResource.getContents();
		if(contents.size() > 0) {
			EObject content = contents.get(0);
			if(content instanceof ReqIF) {
				ReqIFContent coreContent = ((ReqIF)content).getCoreContent();
				EList<SpecType> specTypes = coreContent.getSpecTypes();
				// Gets SpecObjectTypes, specification elements are
				// automatically transformed in sections
				return Collections2.filter(specTypes, new Predicate<SpecType>() {

					@Override
					public boolean apply(SpecType arg0) {
						if(arg0 instanceof SpecObjectType) {
							return true;
						}
						return false;
					}
				});
			}
		}
		return null;
	}

	public static void fillRequirements(RequirementSource requirementSource, IProgressMonitor progressMonitor, Scope scope) {

		Collection<MappingElement> mapping = requirementSource.getMappings();
		if(mapping == null) {
			return;
		}

		ResourceSet resourceSet = new ResourceSetImpl();
		Resource resource = resourceSet.getResource(URI.createURI(requirementSource.getRepositoryUri(), false), true);

		EList<EObject> contents = resource.getContents();
		for(EObject eObject : contents) {
			if(eObject instanceof ReqIF) {
				ReqIF reqIF = (ReqIF)eObject;

				ReqIFContent coreContent = reqIF.getCoreContent();
				EList<Specification> specifications = coreContent.getSpecifications();

				for(Specification specification : specifications) {

					EList<SpecHierarchy> specHierarchies = specification.getChildren();
					Collection<AbstractElement> children = createChildren(specHierarchies, mapping, scope);

					// ElementMapping elementMapping =
					// DataUtil.getElementMapping(mapping,
					// specification.getType().getIdentifier());

					String id = specification.getLongName();// getID(elementMapping,
															// specification);
					String name = specification.getDesc();// getName(elementMapping,
															// specification);

					AbstractElement section = null;
					section = dataManager.createSection(id, name, ReqIF10Util.getSpecType(specification).getIdentifier());
					// section = creator.createSection(id, name,
					// ReqIF10Util.getSpecType(specification).getIdentifier());

					if(section != null) {
						dataManager.addElementsToSource(requirementSource, section);
						// requirementSource.getContents().getRequirements().add(section);
						// addAttributes(elementMapping,
						// specification.getValues(), section);
					}

					if(children != null && !children.isEmpty()) {
						if(section instanceof Section) {
							dataManager.addElementsToSection((Section)section, children.toArray(new AbstractElement[children.size()]));
						} else if(section != null) {
							logger.error("Specification is not mapped with a Section typed element, children can be missed");
						}
					}
				}
			}
		}
	}

	protected static Collection<AbstractElement> createChildren(EList<SpecHierarchy> specHierarchies, Collection<MappingElement> mapping, Scope scope) {

		Collection<AbstractElement> result = new ArrayList<AbstractElement>();

		for(SpecHierarchy specHierarchy : specHierarchies) {

			Collection<AbstractElement> children = createChildren(specHierarchy.getChildren(), mapping, scope);

			AbstractElement createdObject = null;

			SpecObject specObject = specHierarchy.getObject();
			if(specObject != null) {

				MappingElement elementMapping = DataUtil.getElementMapping(mapping, ReqIF10Util.getSpecType(specObject).getIdentifier());
				if(elementMapping != null) {
					String id = getID(elementMapping, specObject);
					String name = getName(elementMapping, specObject);
					createdObject = createElement(mapping, specObject, ReqIF10Util.getSpecType(specObject).getIdentifier(), id, name);
					if(scope != null && (createdObject instanceof Requirement)) {
						createdObject.getScopes().add(scope);
					}
				}

			} else {
				// createdObject =
				// creator.createSection(specHierarchy.getLongName(),
				// specHierarchy.getDesc(), specHierarchy.getIdentifier());
				createdObject = dataManager.createSection(specHierarchy.getLongName(), specHierarchy.getDesc(), specHierarchy.getIdentifier());
			}

			if(createdObject != null) {
				if(children != null && !children.isEmpty()) {
					if(createdObject instanceof Section) {
						dataManager.addElementsToSection((Section)createdObject, children.toArray(new AbstractElement[children.size()]));
					} else {
						logger.error("The element " + createdObject.getId() + " is not a Section Type, his children will be missed");
					}
				}
				result.add(createdObject);
			} else {
				if(children != null && !children.isEmpty()) {
					logger.error("The element " + specHierarchy.getIdentifier() + " can't be created, his children will be missed");
				}
			}
		}
		return result;
	}

	private static String getName(MappingElement elementMapping, SpecElementWithAttributes element) {
		EList<MappingAttribute> attributes = elementMapping.getAttributes();
		for(MappingAttribute attribute : attributes) {
			if("text".equalsIgnoreCase(attribute.getTargetAttribute().getName())) {
				String sourceId = attribute.getSourceId();
				for(AttributeValue value : element.getValues()) {
					if(sourceId.equals(ReqIF10Util.getAttributeDefinition(value).getIdentifier())) {
						return ReqIF10Util.getTheValue(value).toString();
					}
				}
			}
		}
		return "";
	}

	private static String getID(MappingElement elementMapping, SpecElementWithAttributes element) {
		EList<MappingAttribute> attributes = elementMapping.getAttributes();
		for(MappingAttribute attribute : attributes) {
			if("id".equalsIgnoreCase(attribute.getTargetAttribute().getName())) {
				String sourceId = attribute.getSourceId();
				for(AttributeValue value : element.getValues()) {
					if(sourceId.equals(ReqIF10Util.getAttributeDefinition(value).getIdentifier())) {
						return ReqIF10Util.getTheValue(value).toString();
					}
				}
			}
		}
		return "";
	}

	protected static AbstractElement createElement(Collection<MappingElement> mapping, SpecElementWithAttributes specElement, String sourceQualifier, String id, String name) {
		MappingElement elementMapping = DataUtil.getElementMapping(mapping, sourceQualifier);
		AbstractElement createdObject = null;
		if(elementMapping != null) {
			try {
				IRequirementType type = new RequirementTypeImpl(elementMapping.getTargetElement(), null);
				createdObject = type.createInstance();
				createdObject.setId(id);
				createdObject.setText(name);
				createdObject.setUri(id);
			} catch (Exception e) {
				e.printStackTrace();
				logger.log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage()));
			}
			addAttributes(elementMapping, specElement.getValues(), createdObject);
		}
		return createdObject;
	}

	protected static void addAttributes(MappingElement elementMapping, Collection<AttributeValue> values, AbstractElement element) {
		for(AttributeValue attributeValue : values) {

			MappingAttribute attributeMapping = DataUtil.getAttributeMapping(elementMapping, ReqIF10Util.getAttributeDefinition(attributeValue).getIdentifier());

			if(attributeMapping == null || "id".equalsIgnoreCase(attributeMapping.getTargetAttribute().getName()) || "text".equalsIgnoreCase(attributeMapping.getTargetAttribute().getName())) {

				continue;
			}

			try {
				if(attributeValue instanceof AttributeValueEnumeration) {
					for(EnumValue enumValue : ((AttributeValueEnumeration)attributeValue).getValues()) {
						String name = enumValue.getLongName();
						EAttribute targetAttribute = attributeMapping.getTargetAttribute();
						if(targetAttribute.getEAttributeType() instanceof EEnum) {

							EEnumLiteral enumLiteral = ((EEnum)targetAttribute.getEAttributeType()).getEEnumLiteral(name);
							element.eSet(attributeMapping.getTargetAttribute(), enumLiteral);
							// creator.addAttribute(attributeMapping, element,
							// enumLiteral);
						}
					}
				}
				element.eSet(attributeMapping.getTargetAttribute(), ReqIF10Util.getTheValue(attributeValue));
				// creator.addAttribute(attributeMapping, element,
				// ReqIF10Util.getTheValue(attributeValue));
			} catch (Exception e) {
				logger.error("Can't add the attribute " + ReqIF10Util.getAttributeDefinition(attributeValue).getIdentifier() + " to the element " + element.getText());
			}
		}
	}

	public static ITreeContentProvider contentProvider = new ITreeContentProvider() {

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

		@Override
		public void dispose() {
		}

		@Override
		public boolean hasChildren(Object element) {
			return false;
		}

		@Override
		public Object getParent(Object element) {
			return null;
		}

		@Override
		public Object[] getElements(Object inputElement) {
			return ArrayContentProvider.getInstance().getElements(inputElement);
		}

		@Override
		public Object[] getChildren(Object parentElement) {
			return null;
		}
	};

}
