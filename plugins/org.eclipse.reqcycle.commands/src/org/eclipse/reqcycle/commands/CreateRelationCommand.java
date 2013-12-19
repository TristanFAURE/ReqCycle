package org.eclipse.reqcycle.commands;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.reqcycle.traceability.storage.IStorageProvider;
import org.eclipse.reqcycle.traceability.storage.ITraceabilityStorage;
import org.eclipse.reqcycle.traceability.types.ITraceTypesManager;
import org.eclipse.reqcycle.traceability.types.configuration.typeconfiguration.Relation;
import org.eclipse.reqcycle.uri.IReachableCreator;
import org.eclipse.reqcycle.uri.IReachableManager;
import org.eclipse.reqcycle.uri.exceptions.IReachableHandlerException;
import org.eclipse.reqcycle.uri.model.Reachable;

public class CreateRelationCommand implements Command {

	@Inject
	@Named("RDF")
	IStorageProvider provider;

	@Inject
	ITraceTypesManager tTypesManager;

	@Inject
	IReachableManager manager;

	@Inject
	IReachableCreator creator;

	private Relation relation;

	private Reachable source;

	private Reachable target;

	private IResource res;

	public CreateRelationCommand(Relation relation, Reachable source, Reachable target, IResource res) {
		this.relation = relation;
		this.source = source;
		this.target = target;
		this.res = res;
	}

	public void execute() {
		redo();

	}

	public void redo() {
		// handle project traceability path
		IProject p = res.getProject();
		IFile traceaFile = p.getFile(new Path("./.traceability.rdf"));//$NON-NLS-1$
		IFile attributeFile = p.getFile(new Path("./.t-attributes.rdf")); //$NON-NLS-1$
		
		// get the storage for the file path
		String traceaUri = traceaFile.getLocationURI().getPath();
		String attributeURI = attributeFile.getLocationURI().getPath();
		ITraceabilityStorage traceaStorage = provider.getStorage(traceaUri);
		ITraceabilityStorage attributeStorage = provider.getStorage(attributeURI);
		try {
			Reachable container = manager.getHandlerFromObject(traceaFile).getFromObject(traceaFile).getReachable(traceaFile);
			Object id = new Object[]{ container, getNextId() };
			Reachable tracea = manager.getHandlerFromObject(id).getFromObject(id).getReachable(id);
			traceaStorage.startTransaction();
			attributeStorage.startTransaction();
			// FIX ME
			// for (TType type : relation.getAgregated()) {
			// storage.addOrUpdateUpwardRelationShip(type, tracea, container,
			// source,
			// new Reachable[] { target });
			// }
			traceaStorage.addOrUpdateUpwardRelationShip(relation.getTType(), tracea, container, source, new Reachable[]{ target });
			attributeStorage.addUpdateProperty(tracea, "relationKind", relation.getKind()); //$NON-NLS-1$
			traceaStorage.commit();
			attributeStorage.commit();
			traceaStorage.save();
			attributeStorage.save();
		} catch (RuntimeException e) {
			e.printStackTrace();
			attributeStorage.rollback();
			traceaStorage.rollback();
		} catch (IReachableHandlerException e) {
			e.printStackTrace();
		} finally {
			attributeStorage.dispose();
			traceaStorage.dispose();
		}
		try {
			traceaFile.refreshLocal(IResource.DEPTH_ONE, new NullProgressMonitor());
			attributeFile.refreshLocal(IResource.DEPTH_ONE, new NullProgressMonitor());
			if(traceaFile.exists()) {
				traceaFile.setHidden(false);
			}
			if (attributeFile.exists()){
				attributeFile.setHidden(false);
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}

	}

	private String getNextId() {
		return EcoreUtil.generateUUID();
	}
}
