package org.eclipse.reqcycle.jdt.handlers;

import org.agesys.inject.AgesysInject;
import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.internal.core.JavaProject;
import org.eclipse.reqcycle.jdt.model.JDTReachableObject;
import org.eclipse.reqcycle.jdt.utils.JDTUtils;
import org.eclipse.reqcycle.types.ITypeChecker;
import org.eclipse.reqcycle.uri.model.IObjectHandler;
import org.eclipse.reqcycle.uri.model.IReachableHandler;
import org.eclipse.reqcycle.uri.model.Reachable;
import org.eclipse.reqcycle.uri.model.ReachableObject;

public class JDTReachableHandler implements IReachableHandler, IObjectHandler,
		ITypeChecker {

	public JDTReachableHandler() {
	}

	@Override
	public ReachableObject getFromReachable(Reachable t) {
		JDTReachableObject jdtReachableObject = new JDTReachableObject(t);
		AgesysInject.inject(jdtReachableObject);
		return jdtReachableObject;
	}

	@Override
	public boolean handlesReachable(Reachable t) {
		return t.getPath().endsWith("java");
	}

	@Override
	public ReachableObject getFromObject(Object object) {
		if (object instanceof IFile) {
			IFile file = (IFile) object;
			if ("java".equalsIgnoreCase(file.getFileExtension())) {
				return JDTUtils.getReachable(file);
			}
		}
		if (object instanceof IJavaElement) {
			IJavaElement cu = (IJavaElement) object;
			return JDTUtils.getReachable(cu);
		}
		return null;
	}

	@Override
	public boolean handlesObject(Object object) {
		if (object instanceof IFile) {
			IFile file = (IFile) object;
			if ("java".equalsIgnoreCase(file.getFileExtension())) {
				return JavaProject.hasJavaNature(file.getProject());
			}
		}
		return (object instanceof IJavaElement);
	}

	@Override
	public boolean apply(Reachable reachable) {
		return handlesReachable(reachable);
	}

}