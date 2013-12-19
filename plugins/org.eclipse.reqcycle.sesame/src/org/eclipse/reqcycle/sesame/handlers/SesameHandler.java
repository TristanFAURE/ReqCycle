package org.eclipse.reqcycle.sesame.handlers;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.core.resources.IFile;
import org.eclipse.reqcycle.sesame.graph.SailBusinessOperations;
import org.eclipse.reqcycle.uri.IReachableCreator;
import org.eclipse.reqcycle.uri.model.IObjectHandler;
import org.eclipse.reqcycle.uri.model.IReachableHandler;
import org.eclipse.reqcycle.uri.model.Reachable;
import org.eclipse.reqcycle.uri.model.ReachableObject;
import org.eclipse.ziggurat.inject.ZigguratInject;
import org.openrdf.rio.RDFWriterRegistry;

import com.tinkerpop.blueprints.Vertex;

public class SesameHandler implements IObjectHandler, IReachableHandler {

	SailBusinessOperations op = null;

	@Inject
	IReachableCreator creator;

	@PostConstruct
	public void init() {
		op = new SailBusinessOperations();
		ZigguratInject.inject(op);
	}

	@Override
	public ReachableObject getFromObject(Object object) {
		ReachableObject result = null;
		if(object instanceof Vertex) {
			Vertex vertext = (Vertex)object;
			if(vertext.getId() instanceof String) {
				result = new SesameReachableObject(op.getReachable(vertext), op);
			}
		} else if(object instanceof File) {
			result = new SesameReachableObject(creator.getReachable(((File)object).toURI(), object), op);
		} else if(object instanceof IFile) {
			try {
				result = new SesameReachableObject(creator.getReachable(new URI("platform:" + ((IFile)object).getFullPath().toString()), object), op);
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
		if(result != null) {
			ZigguratInject.inject(result);
		}
		return result;
	}

	@Override
	public boolean handlesObject(Object object) {
		if(object instanceof Vertex) {
			return true;
		} else if(object instanceof File) {
			return RDFWriterRegistry.getInstance().getFileFormatForFileName(((File)object).getName()) != null;
		} else if(object instanceof IFile) {
			return RDFWriterRegistry.getInstance().getFileFormatForFileName(((IFile)object).getName()) != null;
		}
		return false;
	}

	@Override
	public ReachableObject getFromReachable(Reachable t) {
		SesameReachableObject sesameReachableObject = new SesameReachableObject(t, op);
		ZigguratInject.inject(sesameReachableObject);
		return sesameReachableObject;
	}

	@Override
	public boolean handlesReachable(Reachable t) {
		return t.getPath() != null && ! "reqcycleStd".equals(t.getScheme()) && RDFWriterRegistry.getInstance().getFileFormatForFileName(t.getPath()) != null;
	}

}
