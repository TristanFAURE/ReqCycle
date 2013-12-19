/*******************************************************************************
 * Copyright (c) 2012 Original authors and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Original authors and others - initial API and implementation
 ******************************************************************************/
package org.eclipse.reqcycle.traceability.table.view;

import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.reqcycle.traceability.engine.ITraceabilityEngine;
import org.eclipse.reqcycle.traceability.model.Link;
import org.eclipse.reqcycle.traceability.model.TType;
import org.eclipse.reqcycle.traceability.storage.IStorageProvider;
import org.eclipse.reqcycle.traceability.storage.ITraceabilityStorageTopics;
import org.eclipse.reqcycle.traceability.table.filters.TableFilter;
import org.eclipse.reqcycle.traceability.table.menus.actions.AllLinksAction;
import org.eclipse.reqcycle.traceability.table.menus.actions.ExplicitLinksAction;
import org.eclipse.reqcycle.traceability.table.model.TableController;
import org.eclipse.reqcycle.traceability.table.providers.LinkLabelProvider;
import org.eclipse.reqcycle.traceability.table.providers.TraceabilityLazyContentProvider;
import org.eclipse.reqcycle.traceability.types.ui.IStylePredicateProvider;
import org.eclipse.reqcycle.traceability.ui.TraceabilityUtils;
import org.eclipse.reqcycle.uri.model.Reachable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.internal.PartSite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ziggurat.inject.ZigguratInject;

import com.google.common.collect.Iterables;

@SuppressWarnings("restriction")
public class TraceabilityTableView extends ViewPart {

	private static final String VIEW_ID = "org.eclipse.reqcycle.traceability.table.partdescriptor.traceability.table"; //$NON-NLS-1$

	@Inject
	protected ITraceabilityEngine engine;

	@Inject
	@Named("RDF")
	protected IStorageProvider provider;

	@Inject
	protected IStylePredicateProvider styleProvider;

	protected TableViewer viewer;

	protected TableController tableControl;

	protected Text filterText;

	protected Refresher refresher = new Refresher();
	
	public TraceabilityTableView() {
		ZigguratInject.inject(this);
	}

	@Inject
	@Optional
	void reactOnNewTraceaLinkCreation(@UIEventTopic(ITraceabilityStorageTopics.NEW) Reachable object) {
		refresher.scheduleRefresh();
	}

	@Inject
	@Optional
	void reactOnTraceaLinkRemoval(@UIEventTopic(ITraceabilityStorageTopics.REMOVE) Reachable object) {
		refresher.scheduleRefresh();
	}

	@Override
	public void createPartControl(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));

		filterText = new Text(composite, SWT.BORDER);
		filterText.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));

		viewer = new TableViewer(composite, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER | SWT.VIRTUAL);
		viewer.getTable().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		TraceabilityLazyContentProvider<Link> provider = TraceabilityLazyContentProvider.create(Link.class, viewer);
		viewer.setContentProvider(provider);

		//Creating the control.
		tableControl = new TableController(viewer);
		ZigguratInject.inject(tableControl);

		// special settings for the lazy Content provider
		viewer.setUseHashlookup(true);

		final Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		//Creating the columns.
		createModel();

		hookMenu(table);
		hookActions();
		hookListeners();

		//Setting the input.
		tableControl.displayAllLinks();
	}

	private void hookListeners() {
		TableFilter filter = new TableFilter();
		viewer.addFilter(filter);
		filterText.addModifyListener(new ModifyListenerImplementation(filter));;
	}

	private void hookMenu(final Table table) {
		MenuManager popupMenu = new MenuManager(null, VIEW_ID);
		Menu menu = popupMenu.createContextMenu(table);
		table.setMenu(menu);
		getSite().setSelectionProvider(viewer);
		getSite().registerContextMenu(popupMenu, viewer);
	}

	private void createModel() {
		createTableViewerColumn("Link type", 50, 0).setLabelProvider(new LinkLabelProvider(styleProvider) {

			@Override
			public String getText(Object element) {
				if(element instanceof Link) {
					TType kind = ((Link)element).getKind();
					StringBuilder builder = new StringBuilder(kind.getLabel());
					TType superKind = kind.getSuperType();
					if(superKind != null) {
						builder.append(String.format(" [Transverse : %s]", superKind.getLabel()));
					}
					return builder.toString();
				}
				return super.getText(element);
			}
		});

		createTableViewerColumn("Upstream", 200, 1).setLabelProvider(new LinkLabelProvider(styleProvider) {

			@Override
			public String getText(Object element) {
				if(element instanceof Link) {
					Set<Reachable> set = ((Link)element).getSources();
					if(set != null && set.size() == 1) {
						Reachable reachable = Iterables.get(set, 0);
						return TraceabilityUtils.getText(reachable);
					}
				}
				return super.getText(element);
			}
		});

		createTableViewerColumn("Downstream", 200, 2).setLabelProvider(new LinkLabelProvider(styleProvider) {

			@Override
			public String getText(Object element) {
				if(element instanceof Link) {
					Set<Reachable> set = ((Link)element).getTargets();
					if(set != null && set.size() == 1) {
						Reachable reachable = Iterables.get(set, 0);
						return TraceabilityUtils.getText(reachable);
					}
				}
				return super.getText(element);
			}
		});
	}

	private void hookActions() {
		IActionBars bars = ((PartSite)getSite()).getActionBars();
		ExplicitLinksAction explicitAction = new ExplicitLinksAction(viewer, tableControl);
		AllLinksAction implicitAction = new AllLinksAction(tableControl);
		ZigguratInject.inject(explicitAction, implicitAction);
		RefreshAction refreshViewAction = new RefreshAction(refresher);
		bars.getToolBarManager().add(explicitAction);
		bars.getToolBarManager().add(implicitAction);
		bars.getToolBarManager().add(refreshViewAction);
	}

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	private TableViewerColumn createTableViewerColumn(String title, int bound, final int colNumber) {
		final TableViewerColumn viewerColumn = new TableViewerColumn(viewer, SWT.NONE);
		final TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setWidth(bound);
		column.setResizable(true);
		column.setMoveable(false);
		return viewerColumn;
	}

	/**
	 * Search is performed after a delay (avoids computation every time the user presses a key)
	 * 
	 * @author omelois
	 */
	private final class ModifyListenerImplementation implements ModifyListener {

		Timer t = new Timer();

		TimerTask tt;

		TableFilter filter;

		ModifyListenerImplementation(TableFilter filter) {
			this.filter = filter;
		}

		@Override
		public void modifyText(ModifyEvent e) {
			if(tt != null) {
				tt.cancel(); //This cancels the timer as well.
				t.purge();
				tt = null;
			}

			tt = new TimerTask() {

				//The timer thread will yield to the display thread to apply the filter.
				@Override
				public void run() {
					Display.getDefault().syncExec(new Runnable() {

						public void run() {
							String searchText = TraceabilityTableView.this.filterText.getText();
							ModifyListenerImplementation.this.filter.setSearchText(searchText);
							TraceabilityTableView.this.tableControl.refreshViewerVisuals();
						}
					});
				}
			};

			t.schedule(tt, 800);
		}
	
	}
	
	private final class Refresher{ 
		Timer t = new Timer(); 
		TimerTask tt;
		
		public void scheduleRefresh(){
			if(tt != null) {
				tt.cancel(); //This cancels the timer as well.
				t.purge();
				tt = null;
			}
			tt = new TimerTask() {

				//The timer thread will yield to the display thread to apply the filter.
				@Override
				public void run() {
					Display display = getSite().getShell().getDisplay();
					display.syncExec(new Runnable() {
						public void run() {
							tableControl.refreshViewerData();
						}
					});
				}
			};

			t.schedule(tt, 1500);
			
		}
		
	}
	
	
	private final class RefreshAction extends Action {
		
		private Refresher refresher;

		public RefreshAction(Refresher refresher) {
			this.refresher = refresher;
			setText("Refresh");
			setToolTipText("Refresh Links list");
		}
		
		@Override
		public void run() {
			this.refresher.scheduleRefresh();
		}
	}

	public TableController getController() {
		return this.tableControl;
	}

}
