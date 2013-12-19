package org.eclipse.reqcycle.traceability.types.ui;

import org.eclipse.reqcycle.traceability.types.configuration.typeconfiguration.Entry;
import org.eclipse.reqcycle.types.IType.FieldDescriptor;
import org.eclipse.swt.widgets.Composite;


public interface IEntryCompositeProvider {

	public Entry createEntryComposite(Composite parent, int style, FieldDescriptor fd);
	
}
