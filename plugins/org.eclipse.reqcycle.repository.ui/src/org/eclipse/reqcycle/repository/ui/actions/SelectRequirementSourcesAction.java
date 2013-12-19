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
package org.eclipse.reqcycle.repository.ui.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.window.Window;
import org.eclipse.reqcycle.repository.data.IDataManager;
import org.eclipse.reqcycle.ui.components.dialogs.CheckBoxInputDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import RequirementSourceConf.RequirementSource;

public class SelectRequirementSourcesAction extends Action {

	/** Requirement Source Manager */
	@Inject
	IDataManager requirementSourceManager;

	protected Collection<RequirementSource> sources;

	protected Set<Listener> listeners = new HashSet<Listener>();

	public SelectRequirementSourcesAction() {
	}

	public void setInitialSelection(Collection<RequirementSource> sources) {
		this.sources = sources;
	}

	@Override
	public void run() {

		Collection<RequirementSource> selectedSources = openRequirementSourceChooser(sources);
		if(selectedSources != null) {
			SourcesChangeEvent event = new SourcesChangeEvent(selectedSources);
			notifyListeners(event);
		}
	}

	protected Collection<RequirementSource> openRequirementSourceChooser(Collection<RequirementSource> initialSelection) {
		Set<RequirementSource> repositories = requirementSourceManager.getRequirementSources();

		@SuppressWarnings({ "rawtypes", "unchecked" })
		CheckBoxInputDialog dialog = new CheckBoxInputDialog(Display.getDefault().getActiveShell(), "Requirement filtering", "Select Requirement Sources to filter", repositories, null, (Collection)initialSelection);

		if(dialog.open() == Window.OK) {
			Collection<RequirementSource> selectedSources = new ArrayList<RequirementSource>();

			Collection<Object> selection = dialog.getSelectedItems();
			for(Object object : selection) {
				if(object instanceof RequirementSource) {
					selectedSources.add((RequirementSource)object);
				}
			}
			return selectedSources;
		}
		return null;
	}

	public void addListener(Listener listener) {
		listeners.add(listener);
	}

	public void removeListener(Listener listener) {
		listeners.remove(listener);
	}

	protected void notifyListeners(Event event) {
		for(Listener listener : listeners) {
			listener.handleEvent(event);
		}
	}

	public class SourcesChangeEvent extends Event {

		protected Collection<RequirementSource> sources;

		public SourcesChangeEvent(Collection<RequirementSource> sources) {
			this.sources = sources;
		}

		public Collection<RequirementSource> getSources() {
			return sources;
		}

	}

}
