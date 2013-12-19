package org.eclipse.reqcycle.repository.connector.local;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.jface.viewers.IStructuredSelection;

import RequirementSourceConf.RequirementSource;


public class EditRequirementsPropertyTester extends PropertyTester {

	public static final String IS_LOCAL = "isLocal";

	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		if(IS_LOCAL.equals(property) && receiver instanceof IStructuredSelection) {
			Object element = ((IStructuredSelection)receiver).getFirstElement();
			if(element instanceof RequirementSource) {
				return LocalConnector.LOCAL_CONNECTOR_ID.equals(((RequirementSource)element).getConnectorId());
			}
		}
		return false;
	}

}
