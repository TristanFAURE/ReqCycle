package org.eclipse.reqcycle.repository.data;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.reqcycle.repository.data.types.DataModelLabelProvider;
import org.eclipse.reqcycle.repository.data.types.IDataModel;
import org.eclipse.reqcycle.repository.data.types.IRequirementType;
import org.eclipse.reqcycle.repository.data.types.RequirementTypeLabelProvider;



public class DataModelAdapterFactory extends AdapterFactoryImpl {

	@Override
	public boolean isFactoryForType(Object type) {
		return type instanceof IDataModel || type instanceof IRequirementType;
	}

	@Override
	public Adapter adapt(Notifier target, Object type) {
		return super.adapt(target, type);
	}

	@Override
	protected Adapter createAdapter(Notifier target) {
		if(target instanceof IDataModel) {
			return new DataModelLabelProvider(this);
		}
		if(target instanceof IRequirementType) {
			return new RequirementTypeLabelProvider(this);
		}
		return super.createAdapter(target);
	}


}
