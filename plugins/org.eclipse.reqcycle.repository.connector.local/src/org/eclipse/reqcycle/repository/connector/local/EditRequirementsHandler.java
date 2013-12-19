package org.eclipse.reqcycle.repository.connector.local;

import javax.inject.Inject;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.reqcycle.repository.connector.local.ui.editor.CustomDataModelEditor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ziggurat.configuration.IConfigurationManager;
import org.eclipse.ziggurat.inject.ZigguratInject;

import RequirementSourceConf.RequirementSource;

public class EditRequirementsHandler extends AbstractHandler {

	@Inject
	IConfigurationManager confManager;

	public EditRequirementsHandler() {
		ZigguratInject.inject(this);
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		IEditorReference[] editorReferences = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getEditorReferences();
		for(IEditorReference iEditorReference : editorReferences) {
			if(CustomDataModelEditor.EDITOR_ID.equals(iEditorReference.getId())) {
				MessageDialog.openInformation(Display.getDefault().getActiveShell(), "Open Requiement Editor", "Please close the opened Requirements Editor before beginning a new edition.");
				return null;
			}
		}

		ISelection selection = HandlerUtil.getCurrentSelection(event);
		if(selection instanceof IStructuredSelection) {
			Object firstElement = ((IStructuredSelection)selection).getFirstElement();
			if(firstElement instanceof RequirementSource) {
				RequirementSource requirementSource = (RequirementSource)firstElement;
				String connectorId = requirementSource.getConnectorId();
				if(LocalConnector.LOCAL_CONNECTOR_ID.equals(connectorId)) {
					Resource eResource = requirementSource.getContents().eResource();
					// Resource eResource = requirementSource.eResource();
					// URI uri =
					// ((ConfigurationManagerImpl)confManager).getConfigurationFileUri(null,
					// null, DataManagerImpl.ID + "." +
					// requirementSource.getName());
					// uri =
					// uri.appendFragment(eResource.getURIFragment(requirementSource));
					CustomDataModelEditor.openEditor(eResource.getURI());
				} else {
					MessageDialog.openInformation(Display.getDefault().getActiveShell(), "Open Requiement Editor", "Can't Edit this Requirement Source. Only Local Requirement Source can be edited.");
					return null;
				}
			}
		}

		return null;
	}
}
