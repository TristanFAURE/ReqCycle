package org.eclipse.reqcycle.traceability.ui.providers;

import java.util.Collection;
import java.util.concurrent.Callable;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.reqcycle.traceability.model.Link;
import org.eclipse.reqcycle.traceability.ui.LinkPropertySource;
import org.eclipse.reqcycle.traceability.ui.views.TraceabilityViewer;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IDeferredWorkbenchAdapter;
import org.eclipse.ui.progress.IElementCollector;
import org.eclipse.ui.views.properties.IPropertySource;

public class BusinessDeffered implements IDeferredWorkbenchAdapter, IAdaptable {
	private Object businessElement;
	private RequestContentProvider requestContentProvider;
	RequestLabelProvider lp = new RequestLabelProvider();
	private int level;

	public BusinessDeffered(Object o,
			RequestContentProvider requestContentProvider) {
		this(o, requestContentProvider, 1);
	}

	public BusinessDeffered(Object o,
			RequestContentProvider requestContentProvider, int level) {
		this.businessElement = o;
		this.requestContentProvider = requestContentProvider;
		this.level = level;
	}

	public Object getBusinessElement() {
		return businessElement;
	}

	final ISchedulingRule mutexRule = new ISchedulingRule() {
		public boolean isConflicting(ISchedulingRule rule) {
			return rule == mutexRule;
		}

		public boolean contains(ISchedulingRule rule) {
			return rule == mutexRule;
		}
	};

	@Override
	public Object[] getChildren(Object o) {
		return null;
		// Collection<Object> child = requestContentProvider.doGetChildren(o);
		// return Lists.newArrayList(
		// transform(child, new Function<Object, BusinessDeffered>() {
		//
		// @Override
		// public BusinessDeffered apply(Object arg0) {
		// return new BusinessDeffered(arg0,
		// requestContentProvider);
		// }
		//
		// })).toArray();
	}

	@Override
	public ImageDescriptor getImageDescriptor(Object object) {
		return null;
	}

	@Override
	public String getLabel(Object o) {
		return lp.getText(o);
	}

	@Override
	public Object getParent(Object o) {
		return null;
	}

	@Override
	public void fetchDeferredChildren(Object object,
			IElementCollector collector, IProgressMonitor monitor) {
		Object objectToBrowse = object;
		if (object instanceof BusinessDeffered) {
			BusinessDeffered busi = (BusinessDeffered) object;
			objectToBrowse = busi.getBusinessElement();
			Collection<Object> child = requestContentProvider
					.doGetChildren(objectToBrowse);
			for (Object o : child) {
				BusinessDeffered element = new BusinessDeffered(o,
						requestContentProvider, busi.getLevel() + 1);
				collector.add(element, monitor);
			}
		}
		collector.done();
	}

	@Override
	public boolean isContainer() {
		return true;
	}

	@Override
	public ISchedulingRule getRule(Object object) {
		return mutexRule;
	}

	public int getLevel() {
		return level;
	}

	@Override
	public Object getAdapter(Class adapter) {
		if (getBusinessElement() instanceof Link) {
			if (adapter == IPropertySource.class) {
				Callable<?> callback = new Callable<Object>(){
					@Override
					public Object call() throws Exception {
						for(IViewReference vr : PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getViewReferences()) {
							if(vr.getId().equals(TraceabilityViewer.ID)) {
								IWorkbenchPart part = vr.getPart(false);
								if(part != null && PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().isPartVisible(part)) {
									TraceabilityViewer v = (TraceabilityViewer)part;
									v.refreshElement(BusinessDeffered.this);
								}
							}
						}
						return null;
					}
				};
				return new LinkPropertySource((Link) getBusinessElement(), callback);
			}
		}
		return null;
	}

}
