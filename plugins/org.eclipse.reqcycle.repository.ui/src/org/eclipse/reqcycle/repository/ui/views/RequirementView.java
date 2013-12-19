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
package org.eclipse.reqcycle.repository.ui.views;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.inject.Inject;

import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.IPostSelectionProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.reqcycle.core.ILogger;
import org.eclipse.reqcycle.dnd.DragRequirementSourceAdapter;
import org.eclipse.reqcycle.predicates.core.api.IPredicate;
import org.eclipse.reqcycle.repository.data.util.DataUtil;
import org.eclipse.reqcycle.repository.ui.Activator;
import org.eclipse.reqcycle.repository.ui.actions.SelectPredicatesFiltersAction;
import org.eclipse.reqcycle.repository.ui.actions.SelectRequirementSourcesAction;
import org.eclipse.reqcycle.repository.ui.providers.DummyInputContentProvider;
import org.eclipse.reqcycle.repository.ui.providers.DummyInputContentProvider.DummyInput;
import org.eclipse.reqcycle.repository.ui.providers.DummyInputContentProvider.DummyObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.DrillDownAdapter;
import org.eclipse.ui.part.PluginTransfer;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ziggurat.inject.ZigguratInject;

import RequirementSourceConf.RequirementSource;

public class RequirementView extends ViewPart implements Listener {


	/** View ID */
	public static final String VIEW_ID = "org.eclipse.reqcycle.repository.ui.views.requirements";

	@Inject
	ILogger logger;

	/** Requirement repositories TreeViewer */
	protected TreeViewer viewer;

	protected SelectPredicatesFiltersAction selectPredicatesFilterAction;

	protected SelectRequirementSourcesAction selectRequirementSourcesAction;

	protected Collection<IPredicate> predicates;

	protected Collection<RequirementSource> sources = Collections.emptyList();

	protected Collection<DummyInput> input = new ArrayList<DummyInput>();

	protected Action newInstanceAction;

	protected DrillDownAdapter drillDownAdapter;

	protected ISelectionProvider selectionProvider;

	public RequirementView() {
		super();
		ZigguratInject.inject(this);
	}

	@Override
	public void createPartControl(Composite parent) {
		this.viewer = new TreeViewer(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		final ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		final DummyInputContentProvider dummyInputContentProvider = new DummyInputContentProvider(adapterFactory);
		getViewer().setContentProvider(dummyInputContentProvider);
		getViewer().setLabelProvider(new AdapterFactoryLabelProvider(adapterFactory) {

			@Override
			public String getText(Object element) {
				if(element instanceof DummyInput) {
					return ((DummyInput)element).getDisplayName();
				} else if(element instanceof DummyObject) {
					return super.getText(((DummyObject)element).getEobj());
				}
				return DataUtil.getLabel(element);
			}

			@Override
			public Image getImage(Object object) {
				if(object instanceof DummyObject) {
					return super.getImage(((DummyObject)object).getEobj());
				}
				return super.getImage(object);
			}
		});

		getViewer().setInput(input);

		drillDownAdapter = new DrillDownAdapter(viewer);

		int dndOperations = DND.DROP_COPY | DND.DROP_MOVE;

		Transfer[] transfers;
		transfers = new Transfer[]{ PluginTransfer.getInstance() };

		DragRequirementSourceAdapter listener = new DragRequirementSourceAdapter(getViewer());
		ZigguratInject.inject(listener);
		getViewer().addDragSupport(dndOperations, transfers, listener);
		getViewSite().setSelectionProvider(getViewer());

		makeSelectionProvider();
		makeActions();
		hookContextMenu();
		contributeToActionBars();
		hookListeners();
	}

	/**
	 * Create The context menu
	 */
	protected void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {

			@Override
			public void menuAboutToShow(IMenuManager manager) {
				fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);

		getSite().registerContextMenu(menuMgr, selectionProvider != null ? selectionProvider : viewer);
	}

	protected void makeSelectionProvider() {
		if(viewer == null) {
			return;
		}
		selectionProvider = new IPostSelectionProvider() {

			@Override
			public void addSelectionChangedListener(ISelectionChangedListener listener) {
				((IPostSelectionProvider)viewer).addSelectionChangedListener(listener);
			}

			@Override
			public ISelection getSelection() {
				ISelection selection = ((IPostSelectionProvider)viewer).getSelection();
				if(selection instanceof IStructuredSelection) {
					Object element = ((IStructuredSelection)selection).getFirstElement();
					if(element instanceof DummyObject) {
						return new StructuredSelection(((DummyObject)element).getEobj());
					}
				}
				return selection;
			}

			@Override
			public void removeSelectionChangedListener(ISelectionChangedListener listener) {
				((IPostSelectionProvider)viewer).removeSelectionChangedListener(listener);
			}

			@Override
			public void setSelection(ISelection selection) {
				((IPostSelectionProvider)viewer).setSelection(selection);
			}

			@Override
			public void addPostSelectionChangedListener(ISelectionChangedListener listener) {
				((IPostSelectionProvider)viewer).addPostSelectionChangedListener(listener);
			}

			@Override
			public void removePostSelectionChangedListener(ISelectionChangedListener listener) {
				((IPostSelectionProvider)viewer).removePostSelectionChangedListener(listener);
			}

		};
		getSite().setSelectionProvider(selectionProvider);
	}

	/**
	 * Fills the context menu
	 * 
	 * @param manager
	 *        the context menu manager
	 */
	protected void fillContextMenu(IMenuManager manager) {
		drillDownAdapter.addNavigationActions(manager);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	@Override
	public void setFocus() {
		getViewer().getControl().setFocus();
	}

	protected static IViewPart createNewView() {
		IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		int nbView = 0;
		for(IViewReference ref : activePage.getViewReferences()) {
			if(ref.getId().startsWith(VIEW_ID)) {
				nbView++;
			}
		}
		// increment to have the second view named #2
		nbView++;
		IViewPart view = null;
		try {
			view = activePage.showView(VIEW_ID, VIEW_ID + "_" + nbView, IWorkbenchPage.VIEW_ACTIVATE);
			// view.
		} catch (PartInitException e) {
			e.printStackTrace();
		}
		return view;

	}

	/**
	 * @param sources
	 * @param predicates
	 *        - The collection of predicates to use for filtering the same input.
	 */
	public static void openNewRequirementView(final Collection<RequirementSource> sources, final Collection<IPredicate> predicates) {

		if(!sources.isEmpty()) {

			IViewPart view = createNewView();
			if(view == null) {
				return;
			}

			RequirementView reqView = (RequirementView)view;

			final ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);

			final DummyInputContentProvider dummyInputContentProvider = new DummyInputContentProvider(adapterFactory);

			reqView.getViewer().setContentProvider(dummyInputContentProvider);

			reqView.setPredicates(predicates);
			reqView.setSources(sources);
			reqView.refresh();

		}
	}

	protected void refresh() {
		if(viewer != null) {
			viewer.refresh();
		}
	}

	public void setSources(Collection<RequirementSource> sources) {
		if(sources == null) {
			sources = Collections.emptyList();
		}
		this.sources = sources;
		if(selectRequirementSourcesAction != null) {
			selectRequirementSourcesAction.setInitialSelection(sources);
		}

		DummyInput dummy;
		if(input.isEmpty()) {
			dummy = new DummyInput(sources);
			input.add(dummy);
		} else {
			for(DummyInput d : input) {
				d.setInput(sources);
			}
		}
	}

	public TreeViewer getViewer() {
		return viewer;
	}

	public Collection<IPredicate> getPredicates() {
		return predicates;
	}

	public void setPredicates(Collection<IPredicate> predicates) {
		if(predicates == null) {
			predicates = Collections.emptyList();
		}
		this.predicates = predicates;
		if(selectPredicatesFilterAction != null) {
			selectPredicatesFilterAction.setInitialSelection(predicates);
		}

		input.clear();

		DummyInput dummy;

		for(IPredicate predicate : predicates) {
			dummy = new DummyInput(sources);
			dummy.setPredicate(predicate);
			input.add(dummy);
		}
	}

	/**
	 * Fills the action Bars
	 */
	protected void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalToolBar(bars.getToolBarManager());
	}

	/**
	 * Fills the local ToolBar
	 * 
	 * @param manager
	 *        The tool Bar manager
	 */
	protected void fillLocalToolBar(IToolBarManager manager) {
		manager.add(selectPredicatesFilterAction);
		manager.add(selectRequirementSourcesAction);
		manager.add(newInstanceAction);
		manager.add(new Separator());
	}

	protected void makeActions() {
		selectPredicatesFilterAction = new SelectPredicatesFiltersAction();
		ZigguratInject.inject(selectPredicatesFilterAction);
		selectPredicatesFilterAction.setText("Select Predicates Filters");
		selectPredicatesFilterAction.setToolTipText("Select predicates to use for filtering");
		selectPredicatesFilterAction.setImageDescriptor(Activator.getImageDescriptor("/icons/editFilterList.png"));
		selectPredicatesFilterAction.addListener(this);
		selectPredicatesFilterAction.setEnabled(false);

		selectRequirementSourcesAction = new SelectRequirementSourcesAction();
		ZigguratInject.inject(selectRequirementSourcesAction);
		selectRequirementSourcesAction.setText("Select Requirement Sources");
		selectRequirementSourcesAction.setToolTipText("Select requirement sources to filter");
		selectRequirementSourcesAction.setImageDescriptor(Activator.getImageDescriptor("/icons/editRepoList.png"));
		selectRequirementSourcesAction.addListener(this);

		newInstanceAction = new Action("New Instance") {

			@Override
			public void run() {
				createNewView();
			}
		};
		newInstanceAction.setImageDescriptor(Activator.getImageDescriptor("icons/newView.gif"));
	}

	/**
	 * Adds all components listeners
	 */
	protected void hookListeners() {
	}

	@Override
	public void handleEvent(Event event) {
		if(event instanceof SelectRequirementSourcesAction.SourcesChangeEvent) {
			setSources(((SelectRequirementSourcesAction.SourcesChangeEvent)event).getSources());
		}

		if(event instanceof SelectPredicatesFiltersAction.PredicatesChangeEvent) {
			setPredicates(((SelectPredicatesFiltersAction.PredicatesChangeEvent)event).getPredicates());
		}
		refresh();
	}

	@Override
	public void dispose() {
		selectPredicatesFilterAction.removeListener(this);
		selectRequirementSourcesAction.removeListener(this);
		super.dispose();
	}

}
