package org.eclipse.reqcycle.repository.data.types;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.reqcycle.emf.types.EMFTypeChecker;
import org.eclipse.reqcycle.repository.data.IDataModelManager;
import org.eclipse.reqcycle.types.IInjectedTypeChecker;
import org.eclipse.reqcycle.uri.IReachableManager;
import org.eclipse.reqcycle.uri.exceptions.IReachableHandlerException;
import org.eclipse.reqcycle.uri.exceptions.VisitableException;
import org.eclipse.reqcycle.uri.model.Reachable;
import org.eclipse.reqcycle.uri.model.ReachableObject;
import org.eclipse.reqcycle.uri.visitors.IVisitor;

import RequirementSourceData.AbstractElement;
import ScopeConf.Scope;


public class ReqcycleTypeChecker implements IInjectedTypeChecker {

	@Inject
	IReachableManager manager;

	@InjectValueName(type = IDataModel.class)
	String dataModel;

	@InjectValueName(type = Scope.class)
	String requirementScope;

	@InjectValueName(type = IRequirementType.class)
	String requirementType;

	@Inject
	IDataModelManager dataModelManager;

	@Inject
	@Named("confResourceSet")
	ResourceSet rs;

	@Override
	public boolean apply(Reachable reachable) {
		EMFTypeChecker emfTypeChecker = new EMFTypeChecker();
		if(emfTypeChecker.apply(reachable)) {
			try {
				ReachableObject object = manager.getHandlerFromReachable(reachable).getFromReachable(reachable);
				IsRequirementTypeVisitor visitor = new IsRequirementTypeVisitor();
				object.getVisitable().accept(visitor);
				return visitor.getResult();
			} catch (IReachableHandlerException e) {
				e.printStackTrace();
			} catch (VisitableException e) {
				e.printStackTrace();
			}
		}

		return false;
	}

	class IsRequirementTypeVisitor implements IVisitor {

		boolean found = false;

		@Override
		public void start(IAdaptable adaptable) {
		}

		@Override
		public boolean visit(Object o, IAdaptable adaptable) {
			found = false;
			if(o instanceof AbstractElement) {
				AbstractElement type = (AbstractElement)o;
				found = true;

				if(dataModel != null) {

					String dataModelName = type.eClass().getEPackage().getName();
					if(!dataModelName.equals(dataModel)) {
						found = false;
					}

					if(found && requirementScope != null) {
						found = false;
						for(Scope s : type.getScopes()) {
							if(s.eIsProxy()) {
								EObject newObj = EcoreUtil.resolve(s, rs);
								if(newObj instanceof Scope) {
									s = (Scope)newObj;
								}
							}
							if(requirementScope.contains(":")) {
								found = requirementScope.equals(dataModelName + "::" + s.getName());
							} else {
								found = requirementScope.equals(s.getName());
							}
						}
					}

					if(found && requirementType != null) {
						found = false;
						String className = type.eClass().getName();
						if(requirementType.contains(":")) {
							found = requirementType.equals(dataModelName + "::" + className);
						} else {
							found = className.equals(requirementType);
						}
					}
				}
			}
			return found;
		}

		@Override
		public void end(IAdaptable adaptable) {
		}

		boolean getResult() {
			return found;
		}

	}

}
