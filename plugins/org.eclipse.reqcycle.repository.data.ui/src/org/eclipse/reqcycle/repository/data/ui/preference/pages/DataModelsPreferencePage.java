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
import java.util.Iterator;

import javax.inject.Inject;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.window.Window;
import org.eclipse.reqcycle.repository.data.IDataModelManager;
import org.eclipse.reqcycle.repository.data.types.IDataModel;
import org.eclipse.reqcycle.repository.data.ui.Activator;
import org.eclipse.reqcycle.repository.data.ui.dialog.NameDialog;
import org.eclipse.reqcycle.repository.data.ui.preference.PreferenceUiUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ziggurat.inject.ZigguratInject;


public class DataModelsPreferencePage extends PreferencePage implements IWorkbenchPreferencePage, Listener {

	@Inject
	IDataModelManager dataModelManager;

	/** Models table viewer */
	protected TableViewer tvModels;

	/** Models table */
	protected Table tModels;

	/** Add Model Button */
	protected Button btnAddModel;

	/** Edit Model Button */
	protected Button btnEditModel;

	protected Button btnDeleteModel;

	protected Collection<IDataModel> inputModels;


	/**
	 * @wbp.parser.constructor
	 */
	public DataModelsPreferencePage() {
		super();
		ZigguratInject.inject(this);
	}

	@Override
	protected void performDefaults() {
	}


	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);
		Button defaultButton = getDefaultsButton();
		//Disable default button
		if(defaultButton != null) {
			defaultButton.setVisible(false);
			defaultButton.setEnabled(false);
		}
	}

	@Override
	public boolean performOk() {
		dataModelManager.save();
		return super.performOk();
	}

	@Override
	public boolean performCancel() {
		dataModelManager.discardUnsavedChanges();
		return super.performCancel();
	}


	@Override
	protected Control createContents(Composite parent) {
		SashForm control = new SashForm(parent, SWT.VERTICAL);
		control.setLayout(new GridLayout(1, false));
		control.setLayoutData(new GridData());

		//Data type Packages group
		Group packagesGrp = PreferenceUiUtil.createGroup(control, "Data Models");
		createModelsUi(packagesGrp);

		doCreateContents(control);

		hookListeners();
		return control;
	}

	/**
	 * Override this method to add element UI
	 * 
	 * @param control
	 */
	public void doCreateContents(Composite control) {
	}


	/**
	 * Create Model Viewer UI
	 * 
	 * @param parent
	 *        Composite parent
	 */
	protected void createModelsUi(Composite parent) {
		//Table Viewer
		Composite viewerComposite = new Composite(parent, SWT.None);
		viewerComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		TableColumnLayout packagesTVLayout = new TableColumnLayout();
		viewerComposite.setLayout(packagesTVLayout);

		tvModels = createDataModelTableViewer(viewerComposite, packagesTVLayout);
		tModels = tvModels.getTable();

		Composite btnComposite = new Composite(parent, SWT.None);
		btnComposite.setLayout(new GridLayout());
		btnComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true));

		btnAddModel = PreferenceUiUtil.createButton(btnComposite, "Add Data Model", Activator.getImageDescriptor("/icons/add.gif").createImage());

		btnEditModel = PreferenceUiUtil.createButton(btnComposite, "Edit Data Model", Activator.getImageDescriptor("/icons/edit.png").createImage());
		btnEditModel.setEnabled(false);

		btnDeleteModel = PreferenceUiUtil.createButton(btnComposite, "Delete Data Model", Activator.getImageDescriptor("/icons/delete.gif").createImage());
		//		btnDeleteModel.setEnabled(false);

	}

	public TableViewer createDataModelTableViewer(Composite parent, TableColumnLayout packagesTVLayout) {

		//Table Viewer
		TableViewer tvModels = new TableViewer(parent);
		tvModels.setContentProvider(ArrayContentProvider.getInstance());
		Table tModels = tvModels.getTable();
		tModels.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tModels.setLinesVisible(true);

		//Columns
		TableViewerColumn tvcModelsNames = PreferenceUiUtil.createTableViewerColumn(tvModels, "Name", SWT.None);
		tvcModelsNames.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {
				if(element instanceof IDataModel) {
					return ((IDataModel)element).getName();
				}
				return super.getText(element);
			}
		});
		packagesTVLayout.setColumnData(tvcModelsNames.getColumn(), new ColumnWeightData(20, 100, true));

		initInput(dataModelManager);
		tvModels.setInput(inputModels);

		return tvModels;
	}

	private void initInput(IDataModelManager dataModelManager) {
		inputModels = new ArrayList<IDataModel>();
		inputModels.addAll(dataModelManager.getAllDataModels());
	}

	public void addDataModels(IDataModel... models) {
		for(IDataModel model : models) {
			inputModels.add(model);
		}
	}

	public void removeDataModels(IDataModel... models) {
		for(IDataModel model : models) {
			inputModels.remove(model);
		}
	}

	/**
	 * Add Listeners
	 */
	public void hookListeners() {

		btnAddModel.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				NameDialog dialog = new NameDialog(e.display.getActiveShell(), "Add Data Model") {

					@Override
					protected void okPressed() {
						String name = getName();
						//FIXME : use exception mecanism instead of using message dialog.
						if(dataModelManager.getDataModel(name) != null) {
							MessageDialog.openError(getShell(), "Add Data Model", "A Data Model with the same name already exists. Please choose a differente one.");
							return;
						}
						super.okPressed();
					}
				};
				if(dialog.open() == Window.OK) {
					String name = dialog.getName();
					addDataModels(dataModelManager.createDataModel(name));
					tvModels.refresh();
				}
			}
		});

		btnDeleteModel.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				ISelection selection = tvModels.getSelection();
				if(selection instanceof IStructuredSelection) {
					Object firstElement = ((IStructuredSelection)selection).getFirstElement();
					if(firstElement instanceof IDataModel) {
						IDataModel dataModel = (IDataModel)firstElement;
						if(!isEmpty(dataModel)) {
							if(!MessageDialog.openQuestion(getShell(), "Delete Data Model", "The Data Model you are trying to remove is not empty. Would you like to continue ?")) {
								return;
							}
						}
						if(isUsed(dataModel)) {
							MessageDialog.openError(getShell(), "Delete Data Model", "The Data Model you are trying to remove is used. Used Data Models can't be deleted");
							return;
						} else {
							removeDataModels(dataModel);
							dataModelManager.removeDataModel(dataModel);
							tvModels.refresh();
						}
					}

				}
			}
		});

	}

	protected boolean isEmpty(IDataModel dataModel) {
		return dataModelManager.isEmpty(dataModel);
	}

	protected Boolean isUsed(IDataModel dataModel) {
		return dataModelManager.isDataModelUsed(dataModel);
	}

	@Override
	public void handleEvent(Event event) {
		if(tvModels != null) {
			tvModels.refresh();
		}
	}

	@Override
	public void init(IWorkbench workbench) {
	}

	@Override
	public void setVisible(boolean visible) {
		if(visible && tvModels != null && inputModels != null) {
			Collection<IDataModel> dataModels = dataModelManager.getAllDataModels();
			Iterator<IDataModel> iter = inputModels.iterator();
			Collection<IDataModel> toRemove = new ArrayList<IDataModel>();
			while(iter.hasNext()) {
				IDataModel dataModel = iter.next();
				if(!dataModels.contains(dataModel)) {
					toRemove.add(dataModel);
				}
			}

			removeDataModels(toRemove.toArray(new IDataModel[toRemove.size()]));
			tvModels.refresh();
		}
		super.setVisible(visible);
	}

}
