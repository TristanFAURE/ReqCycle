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
package org.eclipse.reqcycle.repository.connector.rmf.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.ui.dialogs.ResourceDialog;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.viewers.ViewerProperties;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.reqcycle.repository.data.IDataModelManager;
import org.eclipse.reqcycle.repository.data.types.IDataModel;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ziggurat.inject.ZigguratInject;

import RequirementSourceConf.RequirementSource;
import ScopeConf.Scope;

public class RMFSettingPage extends WizardPage implements Listener {

	private Text fileURIText;

	private Button browseFileBtn;

	private String uri;

	private Button btnSkipMapping;

	private RMFSettingPageBean bean;

	private ComboViewer cvDataModel;

	private ComboViewer cvScope;

	private Button btnReferenceImport;

	private Button btnCopyImport;

	@Inject
	IDataModelManager dataManager;

	private Combo cScope;

	private Combo cDataModel;

	private Collection<Scope> inputScope = new ArrayList<Scope>();

	private Text txtFile;

	private Button btnBrowseCopyFile;


	/**
	 * @param title
	 *        Page title
	 * @param description
	 *        page description
	 * @wbp.parser.constructor
	 */
	public RMFSettingPage(String title, String description) {
		super(title);
		ZigguratInject.inject(this);
		setTitle(title);
		setDescription(description);
		this.bean = new RMFSettingPageBean(this);
	}

	/**
	 * @param title
	 *        Page title
	 * @param description
	 *        Page description
	 * @param uri
	 *        input uri
	 */
	public RMFSettingPage(String title, String description, String uri) {
		this(title, description);
		this.uri = uri;
	}

	@Override
	public boolean canFlipToNextPage() {
		return isPageComplete() && !bean.skipMapping;
	}

	@Override
	public void createControl(Composite parent) {
		Composite compositeContainer = new Composite(parent, SWT.NONE);
		compositeContainer.setLayout(new GridLayout(3, false));
		setControl(compositeContainer);

		Label lblReqIfFile = new Label(compositeContainer, SWT.NONE);
		lblReqIfFile.setText("ReqIF file :");

		fileURIText = new Text(compositeContainer, SWT.BORDER);
		fileURIText.setEditable(false);
		fileURIText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		browseFileBtn = new Button(compositeContainer, SWT.NONE);
		browseFileBtn.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		browseFileBtn.setText("Browse");

		Label lblSeparator = new Label(compositeContainer, SWT.SEPARATOR | SWT.HORIZONTAL);
		lblSeparator.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));

		SashForm sashForm = new SashForm(compositeContainer, SWT.NONE);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));

		Label lblDataModel = new Label(compositeContainer, SWT.NONE);
		lblDataModel.setText("Data Model :");

		cvDataModel = new ComboViewer(compositeContainer);
		cDataModel = cvDataModel.getCombo();
		cDataModel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		cvDataModel.setContentProvider(ArrayContentProvider.getInstance());
		cvDataModel.setLabelProvider(new LabelProvider() {

			@Override
			public String getText(Object element) {
				if(element instanceof IDataModel) {
					return ((IDataModel)element).getName();
				}
				return super.getText(element);
			}
		});
		cvDataModel.setInput(dataManager.getAllDataModels());

		Label lblScope = new Label(compositeContainer, SWT.NONE);
		lblScope.setText("Scope :");

		cvScope = new ComboViewer(compositeContainer);
		cScope = cvScope.getCombo();
		cScope.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		cScope.setEnabled(false);
		cvScope.setContentProvider(ArrayContentProvider.getInstance());
		cvScope.setLabelProvider(new LabelProvider() {

			@Override
			public String getText(Object element) {
				if(element instanceof Scope) {
					return ((Scope)element).getName();
				}
				return super.getText(element);
			}
		});
		cvScope.setInput(inputScope);


		lblSeparator = new Label(compositeContainer, SWT.SEPARATOR | SWT.HORIZONTAL);
		lblSeparator.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));

		Label lblMode = new Label(compositeContainer, SWT.None);
		lblMode.setText("Import Mode :");

		Composite radioBtnComposite = new Composite(compositeContainer, SWT.NONE);
		radioBtnComposite.setLayout(new GridLayout(2, false));
		radioBtnComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));

		btnCopyImport = new Button(radioBtnComposite, SWT.RADIO);
		btnCopyImport.setText("Copy");
		btnCopyImport.setSelection(true);

		Composite compositeCopy = new Composite(radioBtnComposite, SWT.NONE);
		compositeCopy.setLayout(new GridLayout(3, false));
		compositeCopy.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		Label lblCopyFile = new Label(compositeCopy, SWT.NONE);
		lblCopyFile.setText("Destination File :");

		txtFile = new Text(compositeCopy, SWT.BORDER);
		txtFile.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		//		txtFile.setEditable(false);
		txtFile.setEnabled(false);

		btnBrowseCopyFile = new Button(compositeCopy, SWT.NONE);
		btnBrowseCopyFile.setText("Browse");

		btnReferenceImport = new Button(radioBtnComposite, SWT.RADIO);
		btnReferenceImport.setText("Reference");
		btnReferenceImport.setEnabled(false);
		new Label(radioBtnComposite, SWT.NONE);

		lblSeparator = new Label(compositeContainer, SWT.SEPARATOR | SWT.HORIZONTAL);
		lblSeparator.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));

		btnSkipMapping = new Button(compositeContainer, SWT.CHECK);

		Label lblSkipMapping = new Label(compositeContainer, SWT.NONE);
		lblSkipMapping.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		lblSkipMapping.setText("Skip mapping (Perform mapping later)");
		new Label(compositeContainer, SWT.NONE);

		hookListeners();
		init();
		initDataBindings();
	}


	protected void hookListeners() {

		//		fileURIText.addListener(SWT.Modify, this);
		//		btnSkipMapping.addListener(SWT.Selection, this);

		browseFileBtn.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				ResourceDialog dialog = new ResourceDialog(getShell(), "Select ReqIF file", SWT.NONE);
				int res = dialog.open();
				if(res == ResourceDialog.OK) {
					List<URI> uris = dialog.getURIs();
					if(!uris.isEmpty()) {
						fileURIText.setText(uris.get(0).toString());
					}
				}
			}
		});

		cvDataModel.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				inputScope.clear();
				cScope.setEnabled(false);
				ISelection selection = event.getSelection();
				if(selection instanceof IStructuredSelection) {
					Object obj = ((IStructuredSelection)selection).getFirstElement();
					if(obj instanceof IDataModel) {
						cScope.setEnabled(true);
						inputScope.addAll(dataManager.getScopes((IDataModel)obj));
					}
				}
				cvScope.refresh();
			}
		});

		btnBrowseCopyFile.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				SaveAsDialog dialog = new SaveAsDialog(getShell());
				if(Window.OK == dialog.open()) {
					IPath result = dialog.getResult();
					if(!"reqcycle".equals(result.getFileExtension())) {
						result = result.addFileExtension("reqcycle");
					}
					txtFile.setText(result.toString());
				}
			}
		});
	}


	@Override
	public boolean isPageComplete() {
		StringBuffer error = new StringBuffer();
		boolean result = true;
		if(bean.getUri() == null || bean.getUri().isEmpty()) {
			error.append("Choose a ReqIF File.\n");
			result = false;
		}

		if(bean.getDataPackage() == null) {
			error.append("Choose a Data Model\n");
			result = false;
		}

		if(bean.getScope() == null) {
			error.append("Choose a Scope\n");
			result = false;
		}

		if(bean.getIsCopy() && (bean.getDestinationPath() == null || bean.getDestinationPath().isEmpty())) {
			error.append("Choose a destination file for a Copy Import Mode");
			result = false;
		}

		if(!result) {
			setErrorMessage(error.toString());
		} else {
			setErrorMessage(null);
		}

		return result;
	}


	public boolean preFinish(RequirementSource repository) {
		return true;
	}

	private void init() {
		if(uri != null && !uri.isEmpty()) {
			bean.setUri(uri);
			if(fileURIText != null) {
				fileURIText.setText(uri);
			}
		}
	}

	@Override
	public void handleEvent(Event event) {
		getWizard().getContainer().updateButtons();
	}

	public RMFSettingPageBean getBean() {
		return bean;
	}

	public static class RMFSettingPageBean {

		private String uri = "";

		private IDataModel dataPackage;

		private Scope scope;

		private Boolean isCopy = true;

		private boolean skipMapping = false;

		private String destinationPath;

		private Listener listener;


		public RMFSettingPageBean(Listener listener) {
			this.listener = listener;
		}

		public String getUri() {
			return uri;
		}

		public void setUri(String uri) {
			this.uri = uri;
			listener.handleEvent(new Event());
		}

		public boolean getSkipMapping() {
			return skipMapping;
		}

		public void setSkipMapping(boolean skipMapping) {
			this.skipMapping = skipMapping;
			listener.handleEvent(new Event());
		}

		public IDataModel getDataPackage() {
			return dataPackage;
		}

		public void setDataPackage(IDataModel dataPackage) {
			this.dataPackage = dataPackage;
		}

		public Scope getScope() {
			return scope;
		}

		public void setScope(Scope scope) {
			this.scope = scope;
			listener.handleEvent(new Event());
		}

		public Boolean getIsCopy() {
			return isCopy;
		}

		public void setIsCopy(Boolean isCopy) {
			this.isCopy = isCopy;
			listener.handleEvent(new Event());
		}

		public String getDestinationPath() {
			return destinationPath;
		}

		public void setDestinationPath(String destinationPath) {
			this.destinationPath = destinationPath;
			listener.handleEvent(new Event());
		}
	}

	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		IObservableValue observeTextFileURITextObserveWidget = WidgetProperties.text(SWT.Modify).observe(fileURIText);
		IObservableValue uriBeanObserveValue = PojoProperties.value("uri").observe(bean);
		bindingContext.bindValue(observeTextFileURITextObserveWidget, uriBeanObserveValue, null, null);
		//
		IObservableValue observeSelectionBtnCopyImportObserveWidget = WidgetProperties.selection().observe(btnCopyImport);
		IObservableValue isCopyBeanObserveValue = PojoProperties.value("isCopy").observe(bean);
		bindingContext.bindValue(observeSelectionBtnCopyImportObserveWidget, isCopyBeanObserveValue, null, null);
		//
		IObservableValue observeSelectionBtnSkipMappingObserveWidget = WidgetProperties.selection().observe(btnSkipMapping);
		IObservableValue skipMappingBeanObserveValue = PojoProperties.value("skipMapping").observe(bean);
		bindingContext.bindValue(observeSelectionBtnSkipMappingObserveWidget, skipMappingBeanObserveValue, null, null);
		//
		IObservableValue observeSingleSelectionCvDataModel = ViewerProperties.singleSelection().observe(cvDataModel);
		IObservableValue dataPackageBeanObserveValue = PojoProperties.value("dataPackage").observe(bean);
		bindingContext.bindValue(observeSingleSelectionCvDataModel, dataPackageBeanObserveValue, null, null);
		//
		IObservableValue observeSingleSelectionCvScope = ViewerProperties.singleSelection().observe(cvScope);
		IObservableValue scopeBeanObserveValue = PojoProperties.value("scope").observe(bean);
		bindingContext.bindValue(observeSingleSelectionCvScope, scopeBeanObserveValue, null, null);
		//
		IObservableValue observeTextTxtFileObserveWidget = WidgetProperties.text(SWT.Modify).observe(txtFile);
		IObservableValue modelPathBeanObserveValue = PojoProperties.value("destinationPath").observe(bean);
		bindingContext.bindValue(observeTextTxtFileObserveWidget, modelPathBeanObserveValue, null, null);
		//
		return bindingContext;
	}
}
