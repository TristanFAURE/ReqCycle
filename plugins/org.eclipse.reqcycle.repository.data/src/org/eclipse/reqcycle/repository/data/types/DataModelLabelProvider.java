package org.eclipse.reqcycle.repository.data.types;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;


public class DataModelLabelProvider extends ItemProviderAdapter implements IItemLabelProvider, Adapter {


	public DataModelLabelProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	@Override
	public String getText(Object object) {
		if(object instanceof IDataModel) {
			return ((IDataModel)object).getName();

		}
		return null;
	}

	@Override
	public Object getImage(Object object) {
		return null;
	}



}
