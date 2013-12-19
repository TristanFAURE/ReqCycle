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
 *  Papa Issa DIAKHATE (AtoS) papa-issa.diakhate@atos.net - Initial API and implementation
 *  Anass RADOUANI (AtoS) anass.radouani@atos.net - Use a container object instead of retrieving data 
 *  												from the configuration at each get
 *  											  - Singleton Manager
 *
 *****************************************************************************/
package org.eclipse.reqcycle.predicates.persistance.util;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.reqcycle.predicates.core.api.IPredicate;
import org.eclipse.reqcycle.predicates.persistance.PredicatesConfFactory;
import org.eclipse.reqcycle.predicates.persistance.api.PredicatesConf;
import org.eclipse.ziggurat.configuration.IConfigurationManager;

/**
 * This class is an utility that helps to manage predicates configuration (persistence, removal, and so forth ...)
 * 
 * @author Papa Issa DIAKHATE
 */
@Singleton
public class PredicatesConfManager implements IPredicatesConfManager {

	/**
	 * The id of the configuration file which contains the name of the stored predicates.
	 */
	public static final String PREDICATES_ENTRIES_CONF_ID = "org.eclipse.reqcycle.predicates.entries";

	public PredicatesConf predicates;

	@Inject
	IConfigurationManager confManager;

	@Inject
	@Named("confResourceSet")
	ResourceSet rs;

	@Inject
	public PredicatesConfManager(@Named("confResourceSet") ResourceSet rs, IConfigurationManager confManager) {
		this.rs = rs;
		this.confManager = confManager;

		predicates = getConf(true);
		if(predicates == null) {
			predicates = PredicatesConfFactory.eINSTANCE.createPredicatesConf();
		}
	}

	/**
	 * Stores a new predicate into the workspace.
	 * 
	 * @param predicate
	 * 
	 * @return <code>true</code> if the persisting operation is done correctly, <code>false</code> otherwise.
	 * 
	 * @see #isPredicateNameAlreadyUsed(String)
	 */
	@Override
	public boolean storePredicate(final IPredicate predicate) {
		boolean added = false;
		try {
			added = predicates.getPredicates().add(predicate);
			confManager.saveConfiguration(predicates, null, null, PREDICATES_ENTRIES_CONF_ID, rs);
		} catch (IOException ioe) {
			// TODO log ...
			ioe.printStackTrace();
			added = false;
		}
		return added;
	}

	@Override
	public void save() {
		try {
			confManager.saveConfiguration(predicates, null, null, PREDICATES_ENTRIES_CONF_ID, rs);
		} catch (IOException e) {
			//TODO log...
			e.printStackTrace();
		}
	}

	/**
	 * @return The stored predicates.
	 */
	protected Collection<IPredicate> getStoredPredicates() {
		try {
			predicates = this.getConf(true);
			if(predicates == null) {
				predicates = PredicatesConfFactory.eINSTANCE.createPredicatesConf();
				confManager.saveConfiguration(predicates, null, null, PREDICATES_ENTRIES_CONF_ID, rs);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return predicates.getPredicates();
	}

	@Override
	public Collection<IPredicate> getPredicates(boolean reload) {
		if(reload) {
			return getStoredPredicates();
		}
		return predicates != null ? predicates.getPredicates() : new LinkedList<IPredicate>();
	}

	/**
	 * @param predicateName
	 * @return <code>true</code> if the name is already used by another persisted predicate, <code>false</code> otherwise.
	 */
	@Override
	public boolean isPredicateNameAlreadyUsed(final String predicateName) {
		boolean alreadyUsed = false;
		if(predicateName != null && predicates != null) {
			for(IPredicate p : predicates.getPredicates()) {
				if(predicateName.equals(p.getDisplayName())) {
					alreadyUsed = true;
					break;
				}
			}
		}
		return alreadyUsed;
	}

	/**
	 * @param predicateName
	 *        - The name of the predicate to retrieve.
	 * @return The stored predicate having the specified name or <code>null</code> if not found or if the specified
	 *         predicate's name is <code>null</code>.
	 */
	@Override
	public IPredicate getPredicateByName(final String predicateName) {
		if(predicateName != null) {
			for(IPredicate p : getPredicates(false)) {
				if(predicateName.equals(p.getDisplayName())) {
					return p;
				}
			}
		}
		return null;
	}

	/**
	 * Removes the first predicate having the specified name.
	 * 
	 * @return <code>true</code> if the removal operation is done correctly, <code>false</code> otherwise.
	 * 
	 * @param predicateName
	 * 
	 */
	@Override
	public boolean removePredicate(String predicateName) {
		IPredicate predicate = getPredicateByName(predicateName);
		if(predicate != null) {
			return removePredicate(predicate);
		}
		return false;
	}

	/**
	 * Removes the predicate
	 * 
	 * @return <code>true</code> if the removal operation is done correctly, <code>false</code> otherwise.
	 * 
	 * @param predicate
	 *        the predicate to remove
	 * 
	 */
	@Override
	public boolean removePredicate(IPredicate predicate) {
		if(predicates != null) {
			return predicates.getPredicates().remove(predicate);
		}
		return false;
	}

	protected PredicatesConf getConf(boolean reload) {
		Collection<EObject> conf = confManager.getConfiguration(null, null, PREDICATES_ENTRIES_CONF_ID, rs, reload);
		EObject element = null;
		if(conf != null && !conf.isEmpty()) {
			element = conf.iterator().next();
		}
		if(element instanceof PredicatesConf) {
			return (PredicatesConf)element;
		}
		return null;
	}

}
