package org.eclipse.reqcycle.repository.connector.local;

import java.util.concurrent.Callable;

import javax.inject.Inject;

import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.reqcycle.repository.connector.local.ui.LocalSettingPage;
import org.eclipse.reqcycle.repository.connector.ui.wizard.IConnectorWizard;
import org.eclipse.reqcycle.repository.data.IDataManager;
import org.eclipse.reqcycle.repository.data.types.IDataModel;
import org.eclipse.reqcycle.repository.data.util.IRequirementSourceProperties;

import RequirementSourceConf.RequirementSource;
import RequirementSourceData.RequirementsContainer;
import ScopeConf.Scope;

public class LocalConnector extends Wizard implements IConnectorWizard {

	public final static String LOCAL_CONNECTOR_ID = "org.eclipse.reqcycle.repository.connector.local.connectorCore";

	@Inject
	IDataManager manager;

	private LocalSettingPage localSettingPage;

	private IDataModel dataModel;

	private Scope scope;

	private String destination;

	public LocalConnector() {
	}

	@Override
	public void initializeWithRequirementSource(RequirementSource requirementSource) {
	}

	@Override
	public Callable<RequirementSource> createRequirementSource() {
		return new Callable<RequirementSource>() {

			@Override
			public RequirementSource call() throws Exception {
				RequirementSource source = manager.createRequirementSource();
				RequirementsContainer rc = manager.createRequirementsContainer(URI.createPlatformResourceURI(destination, true));
				source.setContents(rc);
				source.setDataModelURI(dataModel.getDataModelURI());
				source.setDefaultScope(scope);
				source.setProperty(IRequirementSourceProperties.IS_LOCAL, "true");
				return source;
			}
		};
	}

	@Override
	public void init(ISelection selection) {
		// Don't need to init
	}

	@Override
	public boolean performFinish() {
		if(localSettingPage == null) {
			return false;
		}
		dataModel = localSettingPage.bean.getDataModel();
		scope = localSettingPage.bean.getScope();
		destination = localSettingPage.bean.getDestination();
		return true;
	}

	@Override
	public void addPages() {
		localSettingPage = new LocalSettingPage("Setting Page");
		addPage(localSettingPage);
	}

}
