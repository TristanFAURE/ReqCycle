package org.eclipse.reqcycle.repository.connector.local.ui.dialog;

import javax.inject.Inject;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.reqcycle.repository.data.IDataManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ziggurat.inject.ZigguratInject;

import RequirementSourceConf.RequirementSource;


public class AddToSourceComposite extends Composite {


	protected Composite compositeAddToSource;

	protected ComboViewer cvSources;

	protected Combo cSources;

	@Inject
	IDataManager dataManager;

	public AddToSourceComposite(Composite parent, int style) {
		super(parent, style);
		ZigguratInject.inject(this);
		createAddComposite(this);
	}

	protected void createAddComposite(Composite compositeMain) {
		//		btnAddToSource = new Button(compositeMain, SWT.RADIO);
		//		btnAddToSource.setText("Add to an existing Requirement Source :");
		//		btnAddToSource.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		compositeAddToSource = new Composite(compositeMain, SWT.NONE);
		compositeAddToSource.setLayout(new GridLayout(2, false));
		compositeAddToSource.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		Label lblSource = new Label(compositeAddToSource, SWT.NONE);
		lblSource.setText("Requirement Source :");
		lblSource.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));

		cvSources = new ComboViewer(compositeAddToSource);
		cSources = cvSources.getCombo();
		cSources.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		cvSources.setContentProvider(ArrayContentProvider.getInstance());
		cvSources.setLabelProvider(new LabelProvider() {

			@Override
			public String getText(Object element) {
				if(element instanceof RequirementSource) {
					return ((RequirementSource)element).getName();
				}
				return super.getText(element);
			}
		});
		cvSources.setInput(dataManager.getRequirementSources());
	}

	public static class Bean {

		protected RequirementSource source;

		public RequirementSource getSource() {
			return source;
		}

		public void setSource(RequirementSource source) {
			this.source = source;
		}

	}

}
