package org.eclipse.reqcycle.repository.data.util;

import java.util.Collection;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.reqcycle.repository.data.IDataModelManager;
import org.eclipse.reqcycle.repository.data.types.IDataModel;
import org.eclipse.reqcycle.repository.data.types.IRequirementType;
import org.eclipse.ziggurat.inject.ZigguratInject;

import ScopeConf.Scope;


public class DataTypesAdapterFactory implements IAdapterFactory {

	@Override
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if(adaptableObject instanceof IDataModel) {
			if(String.class == adapterType) {
				return ((IDataModel)adaptableObject).getName();
			}
		}
		if(adaptableObject instanceof IRequirementType) {
			if(String.class == adapterType) {
				IRequirementType requirementType = (IRequirementType)adaptableObject;
				IDataModel dataModel = requirementType.getDataModel();
				return dataModel.getName() + "::" + requirementType.getName();
			}
		}
		if(adaptableObject instanceof Scope) {
			if(String.class == adapterType) {
				Scope scope = (Scope)adaptableObject;
				String dataModelURI = scope.getDataModelURI();
				IDataModelManager dataModelManager = ZigguratInject.make(IDataModelManager.class);
				Collection<IDataModel> dataModels = dataModelManager.getDataModelByURI(dataModelURI);
				IDataModel dataModel = null;
				if(dataModels.size() > 0) {
					dataModel = dataModels.iterator().next();
				}
				return dataModel != null ? dataModel.getName() + "::" + scope.getName() : scope.getName();
			}
		}
		return null;
	}

	@Override
	public Class[] getAdapterList() {
		return null;
	}

}
