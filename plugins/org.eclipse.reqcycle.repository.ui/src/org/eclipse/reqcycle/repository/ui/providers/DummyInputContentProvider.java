package org.eclipse.reqcycle.repository.ui.providers;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.reqcycle.predicates.core.api.IPredicate;

import RequirementSourceConf.RequirementSource;
import RequirementSourceData.AbstractElement;
import RequirementSourceData.Section;
import RequirementSourceData.SimpleRequirement;

import com.google.common.base.Function;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;

public class DummyInputContentProvider extends AdapterFactoryContentProvider {

	public DummyInputContentProvider(ComposedAdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	@Override
	public Object[] getElements(Object inputElement) {
		if(inputElement instanceof Collection) {
			Iterator<?> iter = ((Collection<?>)inputElement).iterator();
			if(iter.hasNext()) {
				Object obj = iter.next();
				if(obj instanceof DummyInput) {
					return ArrayContentProvider.getInstance().getElements(inputElement);
				}
			}
		}
		return super.getElements(inputElement);
	}

	@Override
	public Object[] getChildren(final Object object) {
		if(object instanceof DummyInput) {
			Object[] children = Collections2.transform(((DummyInput)object).getInput(), new Function<RequirementSource, DummyObject>() {

				@Override
				public DummyObject apply(RequirementSource reqSource) {
					return new DummyObject(((DummyInput)object).getPredicate(), reqSource);
				};
			}).toArray();
			return children;
		}
		if(object instanceof DummyObject) {
			final DummyObject dummyObject = (DummyObject)object;
			EObject obj = dummyObject.getEobj();
			Collection<AbstractElement> elements = Collections.emptyList();
			if(obj instanceof RequirementSource) {
				elements = ((RequirementSource)obj).getRequirements();
			}
			if(obj instanceof Section) {
				elements = ((Section)obj).getChildren();
			}

			Collection<DummyObject> transform = Collections2.transform(elements, new Function<EObject, DummyObject>() {

				@Override
				public DummyObject apply(EObject eObj) {
					IPredicate predicate = dummyObject.getPredicate();
					DummyObject dObj = new DummyObject(predicate, eObj);
					if(dObj.getEobj() instanceof Section && !(dObj.getEobj() instanceof SimpleRequirement)) {
						return dObj; // do not use predicate filter for
										// sections which are not
										// requirements
					}
					if(predicate != null) {
						return predicate.match(eObj) ? dObj : null;
					} else {
						return dObj;
					}
				}
			});

			Iterable<DummyObject> result = Iterables.filter(transform, Predicates.notNull());
			return Iterables.toArray(result, DummyObject.class);
		}
		return super.getChildren(object);
	}

	@Override
	public Object getParent(Object object) {
		if(object instanceof DummyInput) {
			return null;
		} else if(object instanceof DummyObject) {
			return super.getParent(((DummyObject)object).getEobj());
		}
		return super.getParent(object);
	}

	@Override
	public boolean hasChildren(Object object) {
		if(object instanceof DummyInput) {
			return true;
		} else if(object instanceof DummyObject) {
			EObject eobj = ((DummyObject)object).getEobj();
			if(eobj instanceof RequirementSource) {
				return !((RequirementSource)eobj).getRequirements().isEmpty();
			}
			if(eobj instanceof Section) {
				return !((Section)eobj).getChildren().isEmpty();
			}
			return false;
		}
		return super.hasChildren(object);
	}

	public static class DummyInput {

		private Collection<RequirementSource> input;

		private IPredicate predicate;

		public DummyInput(Collection<RequirementSource> input) {
			this.input = input;
		}

		public Collection<RequirementSource> getInput() {
			return this.input;
		}

		public void setInput(Collection<RequirementSource> input) {
			this.input = input;
		}

		public IPredicate getPredicate() {
			return predicate;
		}

		public void setPredicate(IPredicate predicate) {
			this.predicate = predicate;
		}

		public String getDisplayName() {
			return predicate == null ? null : predicate.getDisplayName();
		}
	}

	public class DummyObject implements IAdaptable {

		private IPredicate predicate;

		private EObject eobj;

		public EObject getEobj() {
			return this.eobj;
		}

		public void setEobj(EObject eobj) {
			this.eobj = eobj;
		}

		public DummyObject(IPredicate predicate, EObject eobj) {
			this.predicate = predicate;
			this.eobj = eobj;
		}

		public IPredicate getPredicate() {
			return predicate;
		}

		@SuppressWarnings("rawtypes")
		@Override
		public Object getAdapter(Class adapter) {
			if(adapter == null) {
				return null;
			}
			if(adapter.isInstance(this.eobj)) {
				return this.eobj;
			}
			return null;
		}
	}

}
