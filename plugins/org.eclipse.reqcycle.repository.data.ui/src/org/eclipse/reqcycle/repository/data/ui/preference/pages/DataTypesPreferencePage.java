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
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.dialogs.MessageDialog;
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
import org.eclipse.reqcycle.repository.data.types.IAttribute;
import org.eclipse.reqcycle.repository.data.types.IAttributeType;
import org.eclipse.reqcycle.repository.data.types.IDataModel;
import org.eclipse.reqcycle.repository.data.types.IEnumerationType;
import org.eclipse.reqcycle.repository.data.types.IEnumerator;
import org.eclipse.reqcycle.repository.data.types.IRequirementType;
import org.eclipse.reqcycle.repository.data.ui.Activator;
import org.eclipse.reqcycle.repository.data.ui.dialog.AddAttributeDialog;
import org.eclipse.reqcycle.repository.data.ui.dialog.AddTypeDialog;
import org.eclipse.reqcycle.repository.data.ui.dialog.NameDialog;
import org.eclipse.reqcycle.repository.data.ui.preference.PreferenceUiUtil;
import org.eclipse.reqcycle.repository.data.util.DataUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;

import com.google.common.base.Function;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;

public class DataTypesPreferencePage extends DataModelsPreferencePage {

	/** Types Viewer Elements */
	protected TableViewer tvTypes;

	protected Table tTypes;

	protected TableViewerColumn tvcTypesNames;

	protected TableViewerColumn tvcTypesTypes;

	/** Attributes Viewer Elements */
	protected TableViewer tvAttributes;

	protected Table tAttributes;

	protected TableViewerColumn tvcAttributesNames;

	protected TableViewerColumn tvcAttributesTypes;

	/** Add Buttons */
	protected Button btnAddType;

	protected Button btnAddAttribute;

	/** Edit Buttons */
	protected Button btnEditAttribute;

	protected Button btnEditType;

	/** Viewers Inputs */
	protected Collection<Object> inputTypes = new ArrayList<Object>();

	protected Collection<Object> inputAttributes = new ArrayList<Object>();

	/** Viewers Selected Items */
	protected IDataModel selectedModel;

	protected Object selectedType;

	private Button btnDeleteAttribute;

	private Button btnDeleteType;

	private Boolean dirty = false;


	public DataTypesPreferencePage() {
	}

	@Override
	public void handleEvent(Event event) {
		super.handleEvent(event);

		inputTypes.clear();
		if(tvTypes != null) {
			tvTypes.refresh();
		}

		inputAttributes.clear();
		if(tvAttributes != null) {
			tvAttributes.refresh();
		}
	}

	@Override
	public void doCreateContents(Composite control) {
		//Data Type group
		Group typeGrp = PreferenceUiUtil.createGroup(control, "Data Types");
		createTypesUi(typeGrp);

		//Attribute group
		Group attributesGrp = PreferenceUiUtil.createGroup(control, "Attributes");
		createAttribuesUi(attributesGrp);
	}

	/**
	 * Create Attributes Ui Elements
	 * 
	 * @param parent
	 *        Composite parent
	 */
	protected void createAttribuesUi(Composite parent) {
		//Table Viewer
		Composite viewerComposite = new Composite(parent, SWT.NONE);
		viewerComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		TableColumnLayout attributeTVLayout = new TableColumnLayout();
		viewerComposite.setLayout(attributeTVLayout);

		tvAttributes = new TableViewer(viewerComposite);
		tvAttributes.setContentProvider(ArrayContentProvider.getInstance());
		tAttributes = tvAttributes.getTable();
		tAttributes.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tAttributes.setHeaderVisible(true);
		tAttributes.setLinesVisible(true);

		//Columns
		tvcAttributesNames = PreferenceUiUtil.createTableViewerColumn(tvAttributes, "Name", SWT.None);
		tvcAttributesNames.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {
				if(element instanceof IAttribute) {
					return ((IAttribute)element).getName();
				} else if(element instanceof IEnumerator) {
					return ((IEnumerator)element).getName();
				}
				return super.getText(element);
			}
		});
		attributeTVLayout.setColumnData(tvcAttributesNames.getColumn(), new ColumnWeightData(20, 100, true));

		tvcAttributesTypes = PreferenceUiUtil.createTableViewerColumn(tvAttributes, "Type", SWT.None);
		tvcAttributesTypes.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {
				if(element instanceof IAttribute) {
					return ((IAttribute)element).getAttributeType().getName();
				}
				return "";
			}
		});
		attributeTVLayout.setColumnData(tvcAttributesTypes.getColumn(), new ColumnWeightData(20, 100, true));

		tvAttributes.setInput(inputAttributes);

		Composite btnComposite = new Composite(parent, SWT.None);
		btnComposite.setLayout(new GridLayout());
		btnComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true));

		btnAddAttribute = PreferenceUiUtil.createButton(btnComposite, "Add Attribute", Activator.getImageDescriptor("/icons/add.gif").createImage());
		btnAddAttribute.setEnabled(false);

		btnEditAttribute = PreferenceUiUtil.createButton(btnComposite, "Edit Attribute", Activator.getImageDescriptor("/icons/edit.png").createImage());
		btnEditAttribute.setEnabled(false);

		btnDeleteAttribute = PreferenceUiUtil.createButton(btnComposite, "Delete Attribute", Activator.getImageDescriptor("/icons/delete.gif").createImage());
		btnDeleteAttribute.setEnabled(false);
	}


	/**
	 * Create Types Attributes
	 * 
	 * @param parent
	 *        Composite parent
	 */
	protected void createTypesUi(Composite parent) {
		//Table viewer
		Composite viewerComposite = new Composite(parent, SWT.NONE);
		viewerComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		TableColumnLayout typesTVLayout = new TableColumnLayout();
		viewerComposite.setLayout(typesTVLayout);

		tvTypes = new TableViewer(viewerComposite, SWT.BORDER);
		tvTypes.setContentProvider(ArrayContentProvider.getInstance());

		tTypes = tvTypes.getTable();
		tTypes.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tTypes.setHeaderVisible(true);
		tTypes.setLinesVisible(true);

		//Columns
		tvcTypesNames = PreferenceUiUtil.createTableViewerColumn(tvTypes, "Name", SWT.None);
		tvcTypesNames.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {

				if(element instanceof IRequirementType) {
					return ((IRequirementType)element).getName();
				}

				if(element instanceof IEnumerationType) {
					return ((IEnumerationType)element).getName();
				}

				return super.getText(element);
			}
		});
		typesTVLayout.setColumnData(tvcTypesNames.getColumn(), new ColumnWeightData(20, 100, true));

		tvcTypesTypes = PreferenceUiUtil.createTableViewerColumn(tvTypes, "Type", SWT.None);
		tvcTypesTypes.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {
				if(element instanceof IEnumerationType) {
					return "Enumeration";
				} else {
					return "Requirement";
				}
			}
		});
		typesTVLayout.setColumnData(tvcTypesTypes.getColumn(), new ColumnWeightData(20, 100, true));

		tvTypes.setInput(inputTypes);

		Composite btnComposite = new Composite(parent, SWT.None);
		btnComposite.setLayout(new GridLayout());
		btnComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true));

		btnAddType = PreferenceUiUtil.createButton(btnComposite, "Add Data Type", Activator.getImageDescriptor("/icons/add.gif").createImage());
		btnAddType.setEnabled(false);

		btnEditType = PreferenceUiUtil.createButton(btnComposite, "Edit Data Type", Activator.getImageDescriptor("/icons/edit.png").createImage());
		btnEditType.setEnabled(false);

		btnDeleteType = PreferenceUiUtil.createButton(btnComposite, "Delete Data Type", Activator.getImageDescriptor("/icons/delete.gif").createImage());
		btnDeleteType.setEnabled(false);
	}

	@Override
	public void hookListeners() {

		super.hookListeners();

		tvModels.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				btnAddType.setEnabled(false);
				btnAddAttribute.setEnabled(false);
				inputTypes.clear();
				inputAttributes.clear();

				ISelection selection = event.getSelection();
				if(selection instanceof IStructuredSelection) {
					Object obj = ((IStructuredSelection)selection).getFirstElement();
					if(obj instanceof IDataModel) {
						selectedModel = (IDataModel)obj;
						btnAddType.setEnabled(true);
						inputTypes.addAll(selectedModel.getRequirementTypes());
						inputTypes.addAll(selectedModel.getEnumerationTypes());
					}
				}
				tvTypes.refresh();
				tvAttributes.refresh();
			}
		});

		tvTypes.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				btnAddAttribute.setEnabled(false);
				inputAttributes.clear();

				ISelection selection = event.getSelection();
				if(selection instanceof IStructuredSelection) {
					Object obj = ((IStructuredSelection)selection).getFirstElement();
					selectedType = obj;
					if(obj instanceof IRequirementType) {
						inputAttributes.addAll(((IRequirementType)obj).getAttributes());
						if(!tvcAttributesTypes.getColumn().getResizable()) {

							int width = (tvAttributes.getTable().getBounds().width) / 2;

							tvcAttributesTypes.getColumn().setWidth(width - tvAttributes.getTable().getBorderWidth());
							tvcAttributesTypes.getColumn().setResizable(true);
							tvcAttributesNames.getColumn().setWidth(width - tvAttributes.getTable().getBorderWidth());
						}
					}
					if(obj instanceof IEnumerationType) {
						inputAttributes.addAll(((IEnumerationType)obj).getEnumerators());
						if(tvcAttributesTypes.getColumn().getResizable()) {
							tvcAttributesTypes.getColumn().setWidth(0);
							tvcAttributesTypes.getColumn().setResizable(false);
							tvcAttributesNames.getColumn().setWidth(tvAttributes.getTable().getBounds().width - tvAttributes.getTable().getBorderWidth() * 2);
						}
					}
					btnAddAttribute.setEnabled(true);
				}
				tvAttributes.refresh();
			}
		});


		btnAddType.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				AddTypeDialog dialog = new AddTypeDialog(e.display.getActiveShell(), "Add Data Type");
				if(dialog.open() == Window.OK) {
					String name = dialog.getName();
					boolean isReq = dialog.isRequirement();
					Object element;

					if(isReq) {
						element = dataModelManager.createRequirementType(name, selectedModel);
						selectedModel.addRequirementType((IRequirementType)element);
					} else {
						element = dataModelManager.createEnumerationType(name);
						selectedModel.addEnumerationType((IEnumerationType)element);
					}

					inputTypes.add(element);
					tvTypes.refresh();
					dirty = true;
				}
			}
		});

		btnAddAttribute.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				Object attribute = null;

				if(selectedType instanceof IEnumerationType) {

					NameDialog dialog = new NameDialog(e.display.getActiveShell(), "Add Enumeration Value");
					if(dialog.open() == Window.OK) {
						attribute = dataModelManager.createEnumerator(dialog.getName());
						((IEnumerationType)selectedType).addEnumerator((IEnumerator)attribute);
					}

				} else if(selectedType instanceof IRequirementType) {

					Set<IAttributeType> values = new HashSet<IAttributeType>();
					values.addAll(DataUtil.eDataTypes);
					Collection<IEnumerationType> enumerations = selectedModel.getEnumerationTypes();
					values.addAll(Collections2.transform(enumerations, new Function<IEnumerationType, IAttributeType>() {

						@Override
						public IAttributeType apply(IEnumerationType arg0) {
							IAttributeType attributeType = null;
							if(arg0 instanceof IAdaptable) {
								attributeType = (IAttributeType)((IAdaptable)arg0).getAdapter(IAttributeType.class);
							}
							return attributeType;
						}
					}));

					Collection<IAttributeType> types = Collections2.filter(values, Predicates.notNull());

					AddAttributeDialog dialog = new AddAttributeDialog(e.display.getActiveShell(), "Add Attribute", types);

					if(Window.OK == dialog.open()) {
						String name = dialog.getName();
						IAttributeType type = dialog.getType();
						attribute = dataModelManager.createAttribute(name, type);
						((IRequirementType)selectedType).addAttributeType((IAttribute)attribute);
					}
				}
				if(attribute != null) {
					inputAttributes.add(attribute);
					tvAttributes.refresh();
					dirty = true;
				}

			}

		});
	}

	@Override
	public boolean performOk() {
		boolean result = super.performOk();
		if(dirty) {
			MessageDialog.openWarning(getShell(), "Eclipse Restart", "Please relaunch Eclipse to use newly added Requirements types and attributes");
			dirty = false;
		}

		return result;
	}
}
