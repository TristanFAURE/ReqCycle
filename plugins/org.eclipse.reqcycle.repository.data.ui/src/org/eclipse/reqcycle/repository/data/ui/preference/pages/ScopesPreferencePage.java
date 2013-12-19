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
package org.eclipse.reqcycle.repository.data.ui.preference.pages;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.window.Window;
import org.eclipse.reqcycle.repository.data.types.IDataModel;
import org.eclipse.reqcycle.repository.data.ui.Activator;
import org.eclipse.reqcycle.repository.data.ui.dialog.NameDialog;
import org.eclipse.reqcycle.repository.data.ui.preference.PreferenceUiUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;

import ScopeConf.Scope;

public class ScopesPreferencePage extends DataModelsPreferencePage {

	public ScopesPreferencePage() {
	}

	/** Table Viewer Ui Elements */
	protected TableViewer tvScopes;

	protected Table tScopes;

	protected TableViewerColumn tvcScopesNames;

	/** Selected Model */
	protected IDataModel selectedModel;

	/** Scopes Table Input */
	protected Collection<Scope> inputScopes = new ArrayList<Scope>();

	/** Buttons */
	protected Button btnAddScope;

	protected Button btnEditScope;

	private Button btnDeleteScope;

	@Override
	protected void performDefaults() {
		super.performDefaults();

		if(inputScopes != null) {
			inputScopes.clear();
		}

		if(tvScopes != null) {
			tvScopes.refresh();
		}
	}


	@Override
	public void doCreateContents(Composite control) {
		Group scopesGrp = PreferenceUiUtil.createGroup(control, "Scopes");
		createScopesUi(scopesGrp);
	}

	@Override
	public void hookListeners() {
		super.hookListeners();

		tvModels.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				btnAddScope.setEnabled(false);
				inputScopes.clear();

				ISelection selection = event.getSelection();
				if(selection instanceof IStructuredSelection) {
					Object obj = ((IStructuredSelection)selection).getFirstElement();
					if(obj instanceof IDataModel) {
						selectedModel = (IDataModel)obj;
						btnAddScope.setEnabled(true);
						inputScopes.addAll(dataModelManager.getScopes(selectedModel));
					}

				}

				tvScopes.refresh();
			}
		});

		btnAddScope.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				NameDialog dialog = new NameDialog(e.display.getActiveShell(), "Add Scope");
				if(dialog.open() == Window.OK) {
					String name = dialog.getName();
					Scope scope = dataModelManager.createScope(name, selectedModel);
					dataModelManager.addScopes(selectedModel, scope);
					inputScopes.add(scope);
					tvScopes.setInput(inputScopes);
					tvScopes.refresh();
				}
			}
		});

	}

	/**
	 * Creates Scope Ui
	 * 
	 * @param parent
	 *        Prent Composite
	 */
	protected void createScopesUi(Group parent) {
		//Table Viewer
		Composite viewerComposite = new Composite(parent, SWT.None);
		viewerComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		TableColumnLayout dataTypeTVLayout = new TableColumnLayout();
		viewerComposite.setLayout(dataTypeTVLayout);

		tvScopes = new TableViewer(viewerComposite);
		tvScopes.setContentProvider(ArrayContentProvider.getInstance());
		tScopes = tvScopes.getTable();
		tScopes.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tScopes.setLinesVisible(true);

		//Columns
		tvcScopesNames = PreferenceUiUtil.createTableViewerColumn(tvScopes, "Name", SWT.None);
		tvcScopesNames.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {
				if(element instanceof Scope) {
					return ((Scope)element).getName();
				}
				return super.getText(element);
			}
		});
		dataTypeTVLayout.setColumnData(tvcScopesNames.getColumn(), new ColumnWeightData(20, 100, true));

		tvScopes.setInput(inputScopes);

		Composite btnComposite = new Composite(parent, SWT.None);
		btnComposite.setLayout(new GridLayout());
		btnComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true));

		btnAddScope = PreferenceUiUtil.createButton(btnComposite, "Add Scope", Activator.getImageDescriptor("/icons/add.gif").createImage());
		btnAddScope.setEnabled(false);

		btnEditScope = PreferenceUiUtil.createButton(btnComposite, "Edit Scope", Activator.getImageDescriptor("/icons/edit.png").createImage());
		btnEditScope.setEnabled(false);

		btnDeleteScope = PreferenceUiUtil.createButton(btnComposite, "Delete Scope", Activator.getImageDescriptor("/icons/delete.gif").createImage());
		btnDeleteScope.setEnabled(false);
	}

}
