package org.eclipse.reqcycle.repository.connector.local;

import java.io.IOException;
import java.util.Iterator;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.reqcycle.repository.connector.local.ui.dialog.UseAsSourceDialog;
import org.eclipse.reqcycle.repository.data.IDataManager;
import org.eclipse.reqcycle.repository.data.util.IRequirementSourceProperties;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ziggurat.inject.ZigguratInject;

import RequirementSourceConf.RequirementSource;
import RequirementSourceData.RequirementsContainer;

public class UseAsSourceHandler extends AbstractHandler {

	@Inject
	@Named("confResourceSet")
	ResourceSet rs;

	@Inject
	IDataManager dataManager;

	public UseAsSourceHandler() {
		super();
		ZigguratInject.inject(this);
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection activeMenuSelection = HandlerUtil.getActiveMenuSelection(event);
		if(activeMenuSelection instanceof IStructuredSelection) {
			Object firstElement = ((IStructuredSelection)activeMenuSelection).getFirstElement();
			if(firstElement instanceof IFile) {
				IFile file = (IFile)firstElement;
				Resource resource = rs.getResource(URI.createURI(file.getFullPath().toString()), true);
				EList<EObject> contents = resource.getContents();
				Iterator<EObject> iter = contents.iterator();
				while(iter.hasNext()) {
					EObject eObject = iter.next();
					if(eObject instanceof RequirementsContainer) {

						UseAsSourceDialog dialog = new UseAsSourceDialog(HandlerUtil.getActiveShell(event));
						dialog.init(file.getLocation().removeFileExtension().lastSegment());

						if(Window.OK == dialog.open()) {
							RequirementSource source;
							source = dataManager.createRequirementSource();
							source.setName(dialog.bean.getSourceName());
							source.setDataModelURI(dialog.bean.getDataModel().getDataModelURI());
							source.setDefaultScope(dialog.bean.getScope());
							source.setConnectorId(LocalConnector.LOCAL_CONNECTOR_ID);
							source.setContents((RequirementsContainer)eObject);
							try {
								source.setProperty(IRequirementSourceProperties.IS_LOCAL, "true");
							} catch (Exception e) {
								//FIXME : use logger
								e.printStackTrace();
							}
							dataManager.addRequirementSource(source);
						}
					}
				}
				try {
					dataManager.save();
				} catch (IOException e) {
					//FIXME : Use logger
					e.printStackTrace();
				}
			}
		}
		return null;
	}

}
