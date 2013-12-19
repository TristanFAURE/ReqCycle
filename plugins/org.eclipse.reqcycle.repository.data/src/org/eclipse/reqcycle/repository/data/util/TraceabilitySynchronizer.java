/*****************************************************************************
 * Copyright (c) 2013 AtoS.
 *
 *    
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Anass RADOUANI (AtoS) anass.radouani@atos.net - Initial API and implementation
 *
 *****************************************************************************/
package org.eclipse.reqcycle.repository.data.util;

import java.io.InputStream;
import java.util.Collection;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Path;
import org.eclipse.reqcycle.traceability.engine.ITraceabilityEngine.DIRECTION;
import org.eclipse.reqcycle.traceability.model.Link;
import org.eclipse.reqcycle.traceability.model.Pair;
import org.eclipse.reqcycle.traceability.storage.IOneFileStorageProvider;
import org.eclipse.reqcycle.traceability.storage.ITraceabilityStorage;
import org.eclipse.reqcycle.uri.functions.URIFunctions;
import org.eclipse.reqcycle.uri.model.Reachable;
import org.eclipse.ziggurat.inject.ZigguratInject;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;


/**
 * The Class TraceabilitySynchronizer.
 * FIXME : replace by an svn plugin for traceability
 */
public class TraceabilitySynchronizer {

	/** The Constant RDF_FILE. */
	private static final String RDF_FILE = "./.traceability.rdf"; //$NON-NLS-1$

	/** The provider. */
	@Inject
	@Named("RDF")
	protected IOneFileStorageProvider provider;

	/** The file. */
	protected final IFile file;


	/**
	 * Instantiates a new traceability synchronizer.
	 * 
	 * @param project
	 *        the project
	 */
	public TraceabilitySynchronizer(IProject project) {
		file = project.getFile(new Path(RDF_FILE));
	}


	/**
	 * Gets the file.
	 * 
	 * @return the file
	 */
	public IFile getFile() {
		return file;
	}

	/**
	 * Gets the traceability links.
	 * 
	 * @param project
	 *        the project
	 * @return the traceability links
	 */
	public Iterable<Link> getTraceabilityLinks(final IProject project) {
		IFile file = project.getFile(new Path(RDF_FILE));
		if(file != null && file.exists()) {
			// get the storage for the file path
			String uri = file.getLocationURI().getPath();
			ZigguratInject.inject(provider);
			ITraceabilityStorage storage = provider.getStorage(uri);
			Iterable<Pair<Link, Reachable>> allTraceabilityLinks = storage.getAllTraceability(DIRECTION.DOWNWARD);

			return Iterables.transform(allTraceabilityLinks, new Function<Pair<Link, Reachable>, Link>() {

				@Override
				public Link apply(Pair<Link, Reachable> arg0) {
					return arg0.getFirst();
				}
			});
		}
		return Lists.newArrayList();
	}

	/**
	 * Gets the traceability links.
	 * 
	 * @param inputStream
	 *        the input stream
	 * @return the traceability links
	 */
	public Iterable<Link> getTraceabilityLinks(final InputStream inputStream) {
		// get the storage for the file path
		ITraceabilityStorage storage = provider.getStorageReader(inputStream);
		Iterable<Pair<Link, Reachable>> allTraceabilityLinks = storage.getAllTraceability(DIRECTION.DOWNWARD);

		return Iterables.transform(allTraceabilityLinks, new Function<Pair<Link, Reachable>, Link>() {

			@Override
			public Link apply(Pair<Link, Reachable> arg0) {
				return arg0.getFirst();
			}
		});
	}

	/**
	 * Adds the new links.
	 * 
	 * @param rdfFile
	 *        the rdf file
	 * @param newLinks
	 *        the new links
	 */
	public void addNewLinks(IFile rdfFile, Collection<Link> newLinks) {
		String uri = rdfFile.getLocationURI().getPath();
		ZigguratInject.inject(provider);
		ITraceabilityStorage storage = provider.getStorage(uri);
		for(Link link : newLinks) {
			Set<Reachable> targets = link.getTargets();
			Reachable[] sources = link.getSources().toArray(new Reachable[link.getSources().size()]);
			Reachable target = null;
			if(targets.size() > 0) {
				target = targets.iterator().next();
			}
			Function<Object, Reachable> obj2RO = URIFunctions.newObject2ReachableFunction();
			Reachable resourceReachable = obj2RO.apply(rdfFile);
			storage.addOrUpdateUpwardRelationShip(link.getKind(), link.getId(), resourceReachable, target, sources);
		}
		storage.save();
	}

}
