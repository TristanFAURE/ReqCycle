package org.eclipse.reqcycle.ocl.ui;

import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.ui.dialogs.WorkspaceResourceDialog;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.viewers.ViewerProperties;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.reqcycle.ocl.ReqcycleOCLPlugin;
import org.eclipse.reqcycle.ocl.ui.OCLConnector.SettingBean;
import org.eclipse.reqcycle.repository.data.IDataModelManager;
import org.eclipse.reqcycle.repository.data.types.DataTypePackage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;
import org.eclipse.ui.model.BaseWorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ziggurat.inject.ZigguratInject;

import DataModel.Scope;

import com.google.common.collect.Lists;

public class SettingPage extends WizardPage {

	private IDataModelManager dataManager = ZigguratInject.make(IDataModelManager.class);

	private Button browseButton;

	private Text tFile;

	private ComboViewer cvDataModel;

	private ComboViewer cvScope;

	private Combo cScope;

	private Combo cDataModel;

	private List<Scope> inputScope = Lists.newArrayList();

	private SettingBean bean;

	protected SettingPage(SettingBean bean) {
		super("OCL Connector settings");
		this.bean = bean;
		setDescription("Connector settings");
	}

	@Override
	public void createControl(Composite parent) {
		Composite containerComposite = new Composite(parent, SWT.None);
		containerComposite.setLayout(new GridLayout(3, false));
		Label fileLabel = new Label(containerComposite, SWT.NONE);
		fileLabel.setText("UML Model :");

		tFile = new Text(containerComposite, SWT.BORDER | SWT.READ_ONLY);
		tFile.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		tFile.setEnabled(false);
		browseButton = new Button(containerComposite, SWT.NONE);
		browseButton.setText("Browse");

		Label lblSeparator = new Label(containerComposite, SWT.SEPARATOR | SWT.HORIZONTAL);
		lblSeparator.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));

		SashForm sashForm = new SashForm(containerComposite, SWT.NONE);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));

		Label lblDataModel = new Label(containerComposite, SWT.NONE);
		lblDataModel.setText("Data Model :");

		cvDataModel = new ComboViewer(containerComposite);
		cDataModel = cvDataModel.getCombo();
		cDataModel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		cvDataModel.setContentProvider(ArrayContentProvider.getInstance());
		cvDataModel.setLabelProvider(new LabelProvider() {

			@Override
			public String getText(Object element) {
				if(element instanceof DataTypePackage) {
					return ((DataTypePackage)element).getName();
				}
				return super.getText(element);
			}
		});
		cvDataModel.setInput(dataManager.getAllDataTypePackages());

		Label lblScope = new Label(containerComposite, SWT.NONE);
		lblScope.setText("Scope :");

		cvScope = new ComboViewer(containerComposite);
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

		hookListeners();
		initDataBindings();
		setControl(containerComposite);
	}


	/**
	 * Label provider for the workspace resource dialog.
	 */
	protected static ILabelProvider labelProvider = new WorkbenchLabelProvider() {

		@Override
		public Color getForeground(Object element) {
			return Display.getDefault().getSystemColor(SWT.COLOR_BLACK);
		}

		@Override
		public Color getBackground(Object element) {
			return Display.getDefault().getSystemColor(SWT.COLOR_WHITE);
		}
	};

	protected static ViewerFilter filter = new ViewerFilter() {

		@Override
		public boolean select(Viewer viewer, Object parentElement, Object element) {
			return true;
		}
	};

	protected static ISelectionStatusValidator validator = new ISelectionStatusValidator() {

		@Override
		public IStatus validate(Object[] selection) {
			if(selection.length == 1) {
				Object o = selection[0];
				if(o instanceof IFile && "uml".equals(((IFile)o).getFileExtension())) {
					return Status.OK_STATUS;
				}
			}
			return new Status(IStatus.ERROR, ReqcycleOCLPlugin.PLUGIN_ID, "Select a single UML file");
		}
	};

	@Override
	public boolean canFlipToNextPage() {
		return bean.getUri() != null && bean.getDataPackage() != null && bean.getScope() != null;
	}


	protected void hookListeners() {

		cvDataModel.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				inputScope.clear();
				cScope.setEnabled(false);
				ISelection selection = event.getSelection();
				if(selection instanceof IStructuredSelection) {
					Object obj = ((IStructuredSelection)selection).getFirstElement();
					if(obj instanceof DataTypePackage) {
						cScope.setEnabled(true);
						inputScope.addAll(((DataTypePackage)obj).getScopes());
					}

				}
				cvScope.refresh();
			}
		});

		browseButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				BaseWorkbenchContentProvider contentProvider = new BaseWorkbenchContentProvider();
				WorkspaceResourceDialog dialog = new WorkspaceResourceDialog(Display.getCurrent().getActiveShell(), labelProvider, contentProvider);
				dialog.addFilter(filter);
				dialog.setAllowMultiple(false);
				dialog.setInput(ResourcesPlugin.getWorkspace().getRoot());
				dialog.setValidator(validator);
				int open = dialog.open();
				if(open == 0) {
					IFile iFile = dialog.getSelectedFiles()[0];
					String location = iFile.getFullPath().toOSString();
					tFile.setText(location);
				}
			}
		});
	}

	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		IObservableValue observeTextFileURITextObserveWidget = WidgetProperties.text(SWT.Modify).observe(tFile);
		IObservableValue uriBeanObserveValue = PojoProperties.value("uri").observe(bean);
		bindingContext.bindValue(observeTextFileURITextObserveWidget, uriBeanObserveValue, null, null);
		//
		IObservableValue observeSingleSelectionCvDataModel = ViewerProperties.singleSelection().observe(cvDataModel);
		IObservableValue dataPackageBeanObserveValue = PojoProperties.value("dataPackage").observe(bean);
		bindingContext.bindValue(observeSingleSelectionCvDataModel, dataPackageBeanObserveValue, null, null);
		//
		IObservableValue observeSingleSelectionCvScope = ViewerProperties.singleSelection().observe(cvScope);
		IObservableValue scopeBeanObserveValue = PojoProperties.value("scope").observe(bean);
		bindingContext.bindValue(observeSingleSelectionCvScope, scopeBeanObserveValue, null, null);
		//
		return bindingContext;
	}


}