package org.eclipse.reqcycle.uri.impl.handlers;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.reqcycle.uri.exceptions.VisitableException;
import org.eclipse.reqcycle.uri.model.Reachable;
import org.eclipse.reqcycle.uri.model.ReachableObject;
import org.eclipse.reqcycle.uri.visitors.EmptyVisitable;
import org.eclipse.reqcycle.uri.visitors.IVisitable;

public class StandardReachableObject implements ReachableObject {

	private Object object;

	public StandardReachableObject(Object object) {
		this.object = object;
	}

	@Override
	public Object getAdapter(Class adapter) {
		if (ILabelProvider.class.equals(adapter)) {
			return new LabelProvider() {

				@Override
				public String getText(Object element) {
					if (element instanceof Reachable) {
						Reachable reach = (Reachable) element;
						return reach.getSchemeSpecificPart();
					}
					return super.getText(element);
				}

			};
		}
		if (IResource.class.equals(adapter) || IFile.class.equals(adapter)) {
			Reachable reachable = this.getReachable(this.object);
			String path = reachable.getPath();
			return ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(path));
		}
		return null;
	}

	@Override
	public IVisitable getVisitable() throws VisitableException {
		return new EmptyVisitable();
	}

	@Override
	public String getRevisionIdentification() {
		return null;
	}

	@Override
	public Reachable getReachable(Object o) {
		if(object instanceof Reachable) {
			return (Reachable)object;
		} else {
			return StandardUtils.getReachable(o);
		}
	}

}
