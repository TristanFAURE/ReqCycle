package org.eclipse.reqcycle.repository.connector.local.ui.dialog;

import java.util.Collections;

import javax.inject.Inject;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.viewers.ViewerProperties;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.reqcycle.repository.data.IDataModelManager;
import org.eclipse.reqcycle.repository.data.types.IDataModel;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ziggurat.inject.ZigguratInject;

import ScopeConf.Scope;


public class CreateNewSourceComposite extends Composite {

	protected Composite compositeNewSource;

	protected Text txtName;

	protected ComboViewer cvDataModel;

	protected Combo cDataModel;

	protected ComboViewer cvScope;

	protected Combo cScope;

	public Bean bean = new Bean();

	@Inject
	IDataModelManager dataModelManager;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public CreateNewSourceComposite(Composite parent, int style) {
		super(parent, style);
		ZigguratInject.inject(this);
		createNewComposite(this);
		setLayout(new GridLayout());
		setLayoutData(new GridData());
		hookListeners();

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

	}

	protected void createNewComposite(Composite compositeMain) {
		//		btnNewSource = new Button(compositeMain, SWT.RADIO);
		//		btnNewSource.setText("Create a new Requirement Source :");
		//		btnNewSource.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		compositeNewSource = new Composite(compositeMain, SWT.NONE);
		compositeNewSource.setLayout(new GridLayout(2, false));
		compositeNewSource.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		Label lblName = new Label(compositeNewSource, SWT.NONE);
		lblName.setText("Name :");
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));

		txtName = new Text(compositeNewSource, SWT.BORDER);
		txtName.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		Label lblDataModel = new Label(compositeNewSource, SWT.NONE);
		lblDataModel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		lblDataModel.setText("Data Model :");

		cvDataModel = new ComboViewer(compositeNewSource);
		cDataModel = cvDataModel.getCombo();
		cDataModel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
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

		Label lblScope = new Label(compositeNewSource, SWT.NONE);
		lblScope.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		lblScope.setText("Scope :");

		cvScope = new ComboViewer(compositeNewSource);
		cScope = cvScope.getCombo();
		cScope.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
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

		//		Label lblSeparator = new Label(compositeMain, SWT.SEPARATOR | SWT.HORIZONTAL);
		//		lblSeparator.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
	}


	public static class Bean {

		protected Boolean newSource = false;

		protected String sourceName;

		protected IDataModel dataModel;

		protected Scope scope;

		public String getSourceName() {
			return sourceName;
		}

		public void setSourceName(String sourceName) {
			this.sourceName = sourceName;
		}

		public IDataModel getDataModel() {
			return dataModel;
		}

		public void setDataModel(IDataModel dataModel) {
			this.dataModel = dataModel;
		}

		public Scope getScope() {
			return scope;
		}

		public void setScope(Scope scope) {
			this.scope = scope;
		}

	}

	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		IObservableValue observeTextTxtNameObserveWidget = WidgetProperties.text(SWT.Modify).observe(txtName);
		IObservableValue sourceNameBeanObserveValue = PojoProperties.value("sourceName").observe(bean);
		bindingContext.bindValue(observeTextTxtNameObserveWidget, sourceNameBeanObserveValue, null, null);
		//
		IObservableValue observeSingleSelectionCvDataModel = ViewerProperties.singleSelection().observe(cvDataModel);
		IObservableValue dataModelBeanObserveValue = PojoProperties.value("dataModel").observe(bean);
		bindingContext.bindValue(observeSingleSelectionCvDataModel, dataModelBeanObserveValue, null, null);
		//
		IObservableValue observeSingleSelectionCvScope = ViewerProperties.singleSelection().observe(cvScope);
		IObservableValue scopeBeanObserveValue = PojoProperties.value("scope").observe(bean);
		bindingContext.bindValue(observeSingleSelectionCvScope, scopeBeanObserveValue, null, null);
		//
		return bindingContext;
	}

	public void init(String name) {
		if(txtName != null && name != null) {
			txtName.setText(name);
		}
	}

}
