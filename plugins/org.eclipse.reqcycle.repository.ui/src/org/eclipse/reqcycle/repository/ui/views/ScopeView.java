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


import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import javax.inject.Inject;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.reqcycle.dnd.DragRequirementSourceAdapter;
import org.eclipse.reqcycle.repository.data.IRequirementSourceManager;
import org.eclipse.reqcycle.repository.data.IScopeManager;
import org.eclipse.reqcycle.repository.data.util.DataUtil;
import org.eclipse.reqcycle.repository.ui.Activator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.PluginTransfer;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ziggurat.inject.ZigguratInject;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;

import DataModel.Contained;
import DataModel.RequirementSource;
import DataModel.Scope;

public class ScopeView extends ViewPart {

	/** The scope manager */
	private @Inject IScopeManager scopeManager = ZigguratInject.make(IScopeManager.class);
	
	/** Selected Scope */
	private Scope scope;

	/** Requirement Viewer */
	private TreeViewer viewer;

	/** Scope combo viewer */
	private ComboViewer comboViewer;

	/** refresh view button */
	private Button refreshBtn;
	
	private IRequirementSourceManager reqSourceManager = ZigguratInject.make(IRequirementSourceManager.class);
	
	private ResourceSet rs = new ResourceSetImpl(); 

	protected Set<RequirementSource> repositories;
	
	private Collection<Scope> scopes;
	

	
	public static IStructuredContentProvider scopeRequirementContentProvider = new ITreeContentProvider() {
		
		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
		
		@Override
		public void dispose() {
		}
		
		@Override
		public boolean hasChildren(Object element) {
			return false;
		}
		
		@Override
		public Object getParent(Object element) {
			return null;
		}
		
		@Override
		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof Scope) {
				EList<Contained> requirements = ((Scope) inputElement).getRequirements();
				return requirements.toArray();
			}
			return new Object[0];
		}
		
		@Override
		public Object[] getChildren(Object parentElement) {
			return null;
		}
	};
	
	public ScopeView() {
	}

	@Override
	public void createPartControl(Composite parent) {
		
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(3, false));
		
		Label lblScope = new Label(composite, SWT.NONE);
		lblScope.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblScope.setText("Scope :");

		comboViewer = new ComboViewer(composite, SWT.NONE);
		comboViewer.getCombo().setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboViewer.setContentProvider(new ArrayContentProvider());
		
		//TODO : use scope generated label provider 
		comboViewer.setLabelProvider(new LabelProvider(){
			@Override
			public String getText(Object element) {
				return DataUtil.getLabel(element);
			}
		});
		scopes = scopeManager.getAllScopes();
		comboViewer.setInput(scopeManager.getAllScopes());
		
		refreshBtn = new Button(composite, SWT.PUSH);
		refreshBtn.setImage(Activator.getImageDescriptor("icons/refresh.gif").createImage());
		
		refreshBtn.setToolTipText("Refresh View");
		
		viewer = new TreeViewer(composite, SWT.BORDER);
		viewer.getTree().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
		viewer.setContentProvider(scopeRequirementContentProvider);
		viewer.setLabelProvider(new LabelProvider(){
			@Override
			public String getText(Object element) {
				return DataUtil.getLabel(element);
			}
		});
		
		int dndOperations = DND.DROP_COPY | DND.DROP_MOVE;

		Transfer[] transfers;
		transfers = new Transfer[] { PluginTransfer.getInstance() };

		DragRequirementSourceAdapter listener = new DragRequirementSourceAdapter(viewer);
		ZigguratInject.inject(listener);
		viewer.addDragSupport(dndOperations, transfers,
				listener);
		getViewSite().setSelectionProvider(viewer);
		
		hookListeners();
	}

	private void hookListeners() {
		comboViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				ISelection selection = event.getSelection();
				if(selection instanceof IStructuredSelection) {
					Object element = ((IStructuredSelection)selection).getFirstElement();
					if(element instanceof Scope) {
						scope = (Scope)element;
						viewer.setInput(scope);
						viewer.refresh();
					}
				}
			}
		});
		
		refreshBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (viewer != null) {
					initResourceSet();
					repositories = reqSourceManager.getRepositories();
					rs.getResources().addAll(Collections2.transform(repositories, new Function<RequirementSource, Resource>() {
						@Override
						public Resource apply(RequirementSource arg0) {
							return arg0.eResource();
						}
					}));
					viewer.refresh();
				}
			}
		});
	}
	
	//FIXME : remove this method (use the same resource set to load requirement sources)
	protected void initResourceSet() {
		Iterator<Scope> iter = scopes.iterator();
		while(iter.hasNext()) {
			Resource r = ((Scope)iter.next()).eResource();
			if(r != null) {
				if (r.getResourceSet() != null) {
					rs = r.getResourceSet(); 
					break;
				}
			}
			
		}
	}

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}

}
