package org.eclipse.reqcycle.repository.connector.local.ui.editor.provider;

import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;
import org.eclipse.reqcycle.repository.data.IDataModelManager;
import org.eclipse.reqcycle.repository.data.types.IRequirementType;
import org.eclipse.ziggurat.inject.ZigguratInject;

import RequirementSourceConf.RequirementSource;
import RequirementSourceConf.RequirementSourceConfPackage;
import RequirementSourceData.RequirementSourceDataFactory;
import RequirementSourceData.RequirementSourceDataPackage;
import RequirementSourceData.RequirementsContainer;
import RequirementSourceData.provider.RequirementsContainerItemProvider;



public class CustomRequirementsContainerItemProvider extends RequirementsContainerItemProvider {

	@Inject
	IDataModelManager dataManager;

	@Inject
	@Named("confResourceSet")
	private ResourceSet rs;

	public CustomRequirementsContainerItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
		ZigguratInject.inject(this);
	}

	/*
	 * ECrossReferenceAdapter c = ECrossReferenceAdapter.getCrossReferenceAdapter(this);
	 * 
	 * if(c == null) {
	 * c = new ECrossReferenceAdapter();
	 * Resource r = this.eResource();
	 * if(r != null) {
	 * ResourceSet rs = r.getResourceSet();
	 * if(rs != null) {
	 * c.setTarget(rs);
	 * } else {
	 * c.setTarget(r);
	 * }
	 * } else {
	 * c.setTarget(this);
	 * }
	 * }
	 * 
	 * 
	 * EList<EObject> res = new BasicEList<EObject>();
	 * Collection<Setting> settings = c.getInverseReferences(this, true);
	 * for(Setting s : settings) {
	 * if(oppositeRef.equals(s.getEStructuralFeature())) {
	 * res.add(s.getEObject());
	 * }
	 * }
	 * return res;
	 */

	@Override
	public String getText(Object object) {
		if(object instanceof RequirementsContainer) {
			ECrossReferenceAdapter c = ECrossReferenceAdapter.getCrossReferenceAdapter((RequirementsContainer)object);
			if(c == null) {
				c = new ECrossReferenceAdapter();
			}
			c.setTarget(rs);
			Collection<Setting> settings = c.getInverseReferences((RequirementsContainer)object, true);
			for(Setting s : settings) {
				if(RequirementSourceConfPackage.Literals.REQUIREMENT_SOURCE__CONTENTS.equals(s.getEStructuralFeature())) {
					return ((RequirementSource)s.getEObject()).getName();
				}
			}
		}
		return super.getText(object);
	}



	@Override
	protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object) {
		//FIXME : Use element Data Model to get possible children
		//Gets Dynamic Data Model possible children
		for(IRequirementType type : dataManager.getAllRequirementTypes()) {
			newChildDescriptors.add(createChildParameter(RequirementSourceDataPackage.Literals.REQUIREMENTS_CONTAINER__REQUIREMENTS, type.createInstance()));
		}
		newChildDescriptors.add(createChildParameter(RequirementSourceDataPackage.Literals.REQUIREMENTS_CONTAINER__REQUIREMENTS, RequirementSourceDataFactory.eINSTANCE.createSection()));
	}

}
