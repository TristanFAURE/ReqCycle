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

/**
 * 
 */
package org.eclipse.reqcycle.repository.connector.ui.wizard;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.reqcycle.repository.connector.ui.Activator;
import org.eclipse.reqcycle.repository.data.types.IAttribute;
import org.eclipse.reqcycle.repository.data.types.IEnumerationType;
import org.eclipse.reqcycle.repository.data.types.IRequirementType;
import org.eclipse.reqcycle.repository.data.util.DataUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;


public abstract class MappingComposite extends Composite {

	private TreeViewer sourceViewer;

	private Button btnLink;

	private TreeViewer targetViewer;

	private TreeViewer resultViewer;

	private Button btnRemoveLink;

	private Object sourceSelection;

	protected Object targetSelection;

	private Collection<EObject> result = new ArrayList<EObject>();

	private Label lblSource;

	private Label lblTarget;

	private Label lblMapping;

	private WizardPage page;

	private Button btnEditLink;

	private Button btnAutoMap;

	public static ITreeContentProvider contentProvider = new ITreeContentProvider() {

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
			return ArrayContentProvider.getInstance().getElements(inputElement);
		}

		@Override
		public Object[] getChildren(Object parentElement) {
			return null;
		}
	};

	public MappingComposite(Composite parent, int style, WizardPage page) {
		super(parent, style);
		setLayout(new GridLayout(1, false));
		this.page = page;

		Composite selectionComposite = new Composite(this, SWT.NONE);
		selectionComposite.setLayout(new GridLayout(3, false));
		selectionComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		lblSource = new Label(selectionComposite, SWT.NONE);

		String sourceDetail = getSourceDetail();
		if(sourceDetail != null && !sourceDetail.isEmpty()) {
			lblSource.setText("Source (" + sourceDetail + ")");
		} else {
			lblSource.setText("Source");
		}

		new Label(selectionComposite, SWT.NONE);

		lblTarget = new Label(selectionComposite, SWT.NONE);

		String targetDetail = getTargetDetail();
		if(targetDetail != null && !targetDetail.isEmpty()) {
			lblTarget.setText("Target (" + targetDetail + ")");
		} else {
			lblTarget.setText("Target");
		}

		sourceViewer = new TreeViewer(selectionComposite, SWT.BORDER);
		sourceViewer.getTree().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		sourceViewer.setLabelProvider(getSourceLabelProvider());
		sourceViewer.setContentProvider(getSourceContentProvider());
		sourceViewer.setInput(getSourceInput());

		btnLink = new Button(selectionComposite, SWT.NONE);
		btnLink.setImage(Activator.getImageDescriptor("icons/link_add.png").createImage());
		btnLink.setToolTipText("Link");
		btnLink.setEnabled(false);

		targetViewer = new TreeViewer(selectionComposite, SWT.BORDER);
		targetViewer.getTree().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		targetViewer.setLabelProvider(getTargetLabelProvider());
		targetViewer.setContentProvider(getTargetContentProvider());
		targetViewer.setInput(getTargetInput());

		Composite resultComposite = new Composite(this, SWT.NONE);
		resultComposite.setLayout(new GridLayout(2, false));
		resultComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		lblMapping = new Label(resultComposite, SWT.NONE);

		String resultDetail = getResultDetail();
		if(resultDetail != null && !resultDetail.isEmpty()) {
			lblMapping.setText("Mapping (" + resultDetail + ")");
		} else {
			lblMapping.setText("Mapping");
		}
		new Label(resultComposite, SWT.NONE);

		resultViewer = new TreeViewer(resultComposite, SWT.BORDER);
		resultViewer.getTree().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 4));

		resultViewer.setLabelProvider(getResultLabelProvider());
		resultViewer.setContentProvider(getResultContentProvider());
		init(result);
		resultViewer.setInput(result);

		btnRemoveLink = new Button(resultComposite, SWT.NONE);
		btnRemoveLink.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, false, false, 1, 1));
		btnRemoveLink.setToolTipText("Remove Link");
		btnRemoveLink.setImage(Activator.getImageDescriptor("icons/link_break.png").createImage());
		btnRemoveLink.setEnabled(false);

		//TODO : created when it's possible to edit link
		btnEditLink = new Button(resultComposite, SWT.NONE);
		btnEditLink.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, false, false, 1, 1));
		btnEditLink.setToolTipText("Edit Link");
		btnEditLink.setImage(Activator.getImageDescriptor("icons/link_edit.png").createImage());
		btnEditLink.setEnabled(getCanEditLink());
		btnEditLink.setVisible(getCanEditLink());

		btnAutoMap = new Button(resultComposite, SWT.NONE);
		btnAutoMap.setText("auto");
		btnAutoMap.setEnabled(false);
		btnAutoMap.setVisible(false);
		btnAutoMap.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, false, false, 1, 1));

		hookListeners();
	}

	/**
	 * @param result
	 */
	protected void init(Collection<EObject> result) {
	}

	protected void hookListeners() {
		sourceViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				ISelection selection = event.getSelection();
				if(selection instanceof IStructuredSelection) {
					sourceSelection = ((IStructuredSelection)selection).getFirstElement();
				}
				btnLink.setEnabled(!targetViewer.getSelection().isEmpty() && !selection.isEmpty());
			}
		});

		targetViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				ISelection selection = event.getSelection();
				if(selection instanceof IStructuredSelection) {
					targetSelection = ((IStructuredSelection)selection).getFirstElement();
				}
				btnLink.setEnabled(!event.getSelection().isEmpty() && !sourceViewer.getSelection().isEmpty());
			}
		});

		resultViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				ISelection selection = event.getSelection();
				btnRemoveLink.setEnabled(!selection.isEmpty());
			}
		});

		btnLink.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				EObject linkedElement = linkElements(sourceSelection, targetSelection);
				if(linkedElement != null) {
					result.add(linkedElement);
					resultViewer.refresh();
					if(page != null) {
						page.setPageComplete(true);
					}
				}
			}
		});

		btnRemoveLink.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				ISelection selection = resultViewer.getSelection();
				if(selection instanceof IStructuredSelection) {
					Object fElement = ((IStructuredSelection)selection).getFirstElement();
					result.remove(fElement);
					resultViewer.refresh();
					if(page != null && result.isEmpty()) {
						page.setPageComplete(false);
					}
				}
			}
		});
	}

	protected IContentProvider getResultContentProvider() {
		return contentProvider;
	}

	protected IBaseLabelProvider getResultLabelProvider() {
		return DataUtil.labelProvider;
	}

	protected abstract String getResultDetail();

	protected abstract Object getTargetInput();

	protected IContentProvider getTargetContentProvider() {
		return contentProvider;
	}

	protected IBaseLabelProvider getTargetLabelProvider() {
		return new LabelProvider() {

			@Override
			public String getText(Object element) {
				if(element instanceof IRequirementType) {
					return ((IRequirementType)element).getName();
				}
				if(element instanceof IEnumerationType) {
					return ((IEnumerationType)element).getName();
				}
				if(element instanceof IAttribute) {
					IAttribute attr = (IAttribute)element;
					String typeName = attr.getAttributeType().getName();
					return attr.getName() + " : " + typeName;
				}
				return super.getText(element);
			}
		};
	}

	protected abstract Object getSourceInput();

	protected abstract IContentProvider getSourceContentProvider();

	protected abstract IBaseLabelProvider getSourceLabelProvider();

	protected abstract String getTargetDetail();

	protected abstract String getSourceDetail();

	public abstract EObject linkElements(Object sourceSelection, Object targetSelection);

	protected abstract boolean getCanEditLink();

	public Collection<EObject> getResult() {
		return result;
	}

	public void addToResult(EObject element) {
		result.add(element);
		if(resultViewer != null) {
			resultViewer.refresh();
			page.setPageComplete(true);
		}
	}

	public void addToResult(Collection<EObject> elements) {
		result.addAll(elements);
		if(resultViewer != null) {
			resultViewer.refresh();
			page.setPageComplete(true);
		}
	}

}
