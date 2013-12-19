package org.eclipse.reqcycle.repository.connector.ui.wizard;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.reqcycle.repository.connector.IConnector;

/**
 * If the connector's configuration is made through a wizard, this interface should be used.
 */
public interface IConnectorWizard extends IConnector, IWizard {

	/** Initialize the wizard with the user selection */
	public void init(ISelection selection);

}
