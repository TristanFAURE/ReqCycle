/*******************************************************************************
 * Copyright (c) 2013 Atos.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Atos - initial API and implementation
 ******************************************************************************/
package org.eclipse.reqcycle.ocl.ui;

import java.util.Collection;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.reqcycle.ocl.utils.OCLUtilities;
import org.eclipse.reqcycle.repository.connector.ui.wizard.IConnectorWizard;
import org.eclipse.reqcycle.repository.data.IDataManager;
import org.eclipse.reqcycle.repository.data.IDataModelManager;
import org.eclipse.reqcycle.repository.data.types.IAttribute;
import org.eclipse.reqcycle.repository.data.types.IDataModel;
import org.eclipse.reqcycle.repository.data.types.IRequirementType;
import org.eclipse.reqcycle.repository.data.util.IRequirementSourceProperties;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ziggurat.ocl.OCLEvaluator;
import org.eclipse.ziggurat.ocl.ZigguratOCLPlugin;

import MappingModel.MappingElement;
import RequirementSourceConf.RequirementSource;
import RequirementSourceData.AbstractElement;
import RequirementSourceData.Requirement;
import ScopeConf.Scope;

import com.google.common.collect.Iterables;

public class OCLConnector extends Wizard implements IConnectorWizard, Listener {

	protected SettingBean bean = new SettingBean();

	private RequirementSource requirementSource = null;

	@Inject
	IDataModelManager manager;

	@Inject
	IDataManager dataManager;

	@Override
	public void addPages() {
		addPage(new SettingPage(bean));
		addPage(new OCLPage(bean));
		super.addPages();
	}

	@Override
	public Callable<RequirementSource> createRequirementSource() {
		return new Callable<RequirementSource>() {

			@Override
			public RequirementSource call() throws Exception {
				RequirementSource result = null;

				if(OCLConnector.this.requirementSource != null) {
					result = OCLConnector.this.requirementSource;
				} else {
					result = dataManager.createRequirementSource();
				}

				result.setProperty(IRequirementSourceProperties.PROPERTY_URI, bean.getUri());
				fillRequirements(result);
				return result;
			}
		};
	}

	protected void fillRequirements(RequirementSource requirementSource) throws Exception {
		requirementSource.clearContent();
		Collection<MappingElement> mapping = requirementSource.getMappings();
		ResourceSet resourceSet = new ResourceSetImpl();

		String repositoryUri = requirementSource.getRepositoryUri();

		Resource resource = resourceSet.getResource(URI.createPlatformResourceURI(repositoryUri, true), true);
		OCLEvaluator evaluator = ZigguratOCLPlugin.compileOCL(resourceSet, URI.createPlatformResourceURI(bean.getOclUri(), true));

		TreeIterator<EObject> contents = resource.getAllContents();
		Collection<IRequirementType> requirementTypes = bean.getDataPackage().getRequirementTypes();
		while(contents.hasNext()) {
			EObject eObject = contents.next();
			for(IRequirementType reqType : requirementTypes) {
				if(OCLUtilities.isDataType(evaluator, eObject, reqType)) {
					AbstractElement requirement = createRequirement(evaluator, mapping, eObject, reqType);
					dataManager.addElementsToSource(requirementSource, requirement);
				}
			}
		}
	}

	protected AbstractElement createRequirement(OCLEvaluator evaluator, Collection<MappingElement> mappings, EObject eObject, IRequirementType reqType) throws Exception {
		Requirement requirement = reqType.createInstance();
		for(IAttribute attribute : Iterables.filter(reqType.getAttributes(), IAttribute.class)) {
			Object value = OCLUtilities.getAttributeValue(evaluator, eObject, attribute);
			if(value != null) {
				dataManager.addAttribute(requirement, attribute, value);
			}
		}
		return requirement;
	}

	@Override
	public void initializeWithRequirementSource(RequirementSource requirementSource) {
		this.requirementSource = requirementSource;
	}

	@Override
	public boolean performFinish() {
		return true;
	}

	protected class SettingBean {

		private String uri = "";

		private String oclUri = "";

		private IDataModel dataPackage;

		private Scope scope;

		public SettingBean() {
		}

		public String getUri() {
			return uri;
		}

		public void setUri(String uri) {
			this.uri = uri;
			notifyChange();
		}

		public String getOclUri() {
			return oclUri;
		}

		public void setOclUri(String oclUri) {
			this.oclUri = oclUri;
			notifyChange();
		}

		public IDataModel getDataPackage() {
			return dataPackage;
		}

		public void setDataPackage(IDataModel dataPackage) {
			this.dataPackage = dataPackage;
			notifyChange();
		}

		public Scope getScope() {
			return scope;
		}

		public void setScope(Scope scope) {
			this.scope = scope;
			notifyChange();
		}

		public void notifyChange() {
			IWizardPage[] pages = getPages();
			if(pages != null) {
				for(int i = 0; i < pages.length; i++) {
					IWizardPage iWizardPage = pages[i];
					iWizardPage.getWizard().getContainer().updateButtons();
					if(iWizardPage instanceof Listener) {
						((Listener)iWizardPage).handleEvent(new Event());
					}
				}
			}
		}
	}

	@Override
	public boolean canFinish() {
		return bean != null && bean.getOclUri() != null && bean.getDataPackage() != null && bean.getUri() != null && !bean.getOclUri().isEmpty() && !bean.getUri().isEmpty();
	}

	@Override
	public void handleEvent(Event event) {
		getContainer().updateButtons();
	}

	@Override
	public void init(ISelection selection) {
		// TODO Auto-generated method stub
	}

}
