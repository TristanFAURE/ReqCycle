package org.eclipse.reqcycle.repository.connector.local.ui;

import java.util.Collections;

import javax.inject.Inject;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.runtime.IPath;
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

import ScopeConf.Scope;

public class LocalSettingPage extends WizardPage implements Listener {

	private DataBindingContext m_bindingContext;

	private ComboViewer cvDataModel;

	private Combo cDataModel;

	private ComboViewer cvScope;

	private Combo cScope;

	public Bean bean;

	@Inject
	IDataModelManager dataModelManager;

	private Text txtDestination;

	private Button btnBrowse;

	public LocalSettingPage(String pageName) {
		super(pageName);
		bean = new Bean(this);
		ZigguratInject.inject(this);
	}

	@Override
	public void createControl(Composite parent) {
		Composite compositeMain = new Composite(parent, SWT.NONE);
		compositeMain.setLayout(new GridLayout(3, false));
		compositeMain.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		setControl(compositeMain);

		Label lblDataModel = new Label(compositeMain, SWT.NONE);
		lblDataModel.setText("Data Model :");

		cvDataModel = new ComboViewer(compositeMain);
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
		cvDataModel.setInput(dataModelManager.getAllDataModels());

		Label lblScope = new Label(compositeMain, SWT.NONE);
		lblScope.setText("Scope :");

		cvScope = new ComboViewer(compositeMain);
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

		Label lblDestination = new Label(compositeMain, SWT.NONE);
		lblDestination.setText("Destination File :");

		txtDestination = new Text(compositeMain, SWT.BORDER);
		txtDestination.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

		btnBrowse = new Button(compositeMain, SWT.PUSH);
		btnBrowse.setText("Browse");
		btnBrowse.setLayoutData(new GridData());

		hookListeners();
		m_bindingContext = initDataBindings();
	}

	protected void hookListeners() {

		cvDataModel.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				cvScope.setInput(Collections.emptyList());
				cScope.setEnabled(false);
				ISelection selection = event.getSelection();
				if(selection instanceof IStructuredSelection) {
					Object obj = ((IStructuredSelection)selection).getFirstElement();
					if(obj instanceof IDataModel) {
						cScope.setEnabled(true);
						cvScope.setInput(dataModelManager.getScopes((IDataModel)obj));
					}
				}
				cvScope.refresh();
			}
		});

		btnBrowse.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				SaveAsDialog dialog = new SaveAsDialog(getShell());
				if(Window.OK == dialog.open()) {
					IPath result = dialog.getResult();
					if(!"reqcycle".equals(result.getFileExtension())) {
						result = result.addFileExtension("reqcycle");
					}
					txtDestination.setText(result.toString());
				}
			}
		});

	}

	public static class Bean {

		private IDataModel dataModel;

		private Scope scope;

		private String destination;

		private Listener listener;

		public Bean(Listener listener) {
			this.listener = listener;
		}

		public IDataModel getDataModel() {
			return dataModel;
		}

		public void setDataModel(IDataModel dataModel) {
			this.dataModel = dataModel;
			listener.handleEvent(new Event());
		}

		public Scope getScope() {
			return scope;
		}

		public void setScope(Scope scope) {
			this.scope = scope;
			listener.handleEvent(new Event());
		}

		public String getDestination() {
			return destination;
		}

		public void setDestination(String destination) {
			this.destination = destination;
			listener.handleEvent(new Event());
		}
	}

	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		IObservableValue observeSingleSelectionCvDataModel = ViewerProperties.singleSelection().observe(cvDataModel);
		IObservableValue dataModelBeanObserveValue = PojoProperties.value("dataModel").observe(bean);
		bindingContext.bindValue(observeSingleSelectionCvDataModel, dataModelBeanObserveValue, null, null);
		//
		IObservableValue observeSingleSelectionCvScope = ViewerProperties.singleSelection().observe(cvScope);
		IObservableValue scopeBeanObserveValue = PojoProperties.value("scope").observe(bean);
		bindingContext.bindValue(observeSingleSelectionCvScope, scopeBeanObserveValue, null, null);
		//
		IObservableValue observeTextTxtDestinationObserveWidget = WidgetProperties.text(SWT.Modify).observe(txtDestination);
		IObservableValue destinationBeanObserveValue = PojoProperties.value("destination").observe(bean);
		bindingContext.bindValue(observeTextTxtDestinationObserveWidget, destinationBeanObserveValue, null, null);
		//
		return bindingContext;
	}

	@Override
	public boolean isPageComplete() {
		StringBuffer error = new StringBuffer();
		boolean result = true;

		if(bean.getDataModel() == null) {
			error.append("Choose a Data Model\n");
			result = false;
		}

		if(bean.getScope() == null) {
			error.append("Choose a Scope\n");
			result = false;
		}

		if(bean.getDestination() == null || bean.getDestination().isEmpty()) {
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

	@Override
	public void handleEvent(Event event) {
		getContainer().updateButtons();

	}
}
