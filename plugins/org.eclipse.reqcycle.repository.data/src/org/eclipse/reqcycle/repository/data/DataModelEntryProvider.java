package org.eclipse.reqcycle.repository.data;

import org.eclipse.reqcycle.repository.data.util.EntryUtil;
import org.eclipse.reqcycle.traceability.types.configuration.typeconfiguration.Entry;
import org.eclipse.reqcycle.traceability.types.ui.IEntryCompositeProvider;
import org.eclipse.reqcycle.types.IType.FieldDescriptor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ziggurat.inject.ZigguratInject;


public class DataModelEntryProvider implements IEntryCompositeProvider {

	IDataModelManager dataModelManager = ZigguratInject.make(IDataModelManager.class);

	/**
	 * @wbp.parser.entryPoint
	 */
	@Override
	public Entry createEntryComposite(Composite parent, int style, FieldDescriptor fd) {

		return EntryUtil.createComboViewer(parent, fd, dataModelManager.getAllDataModels());
	}

}
