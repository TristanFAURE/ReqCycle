package org.eclipse.reqcycle.repository.connector.rmf.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.jface.viewers.ComboViewer;


public class ScopeSelectionComposite extends Composite {

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public ScopeSelectionComposite(Composite parent, int style) {
		super(parent, style);

		Composite control = new Composite(parent, SWT.None);
		control.setLayout(new GridLayout(2, false));
		control.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));

		Label lblDataModel = new Label(control, SWT.NONE);
		lblDataModel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
		lblDataModel.setText("Data Model :");

		ComboViewer cvDataModel = new ComboViewer(control, SWT.NONE);
		Combo cDataModel = cvDataModel.getCombo();
		cDataModel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblScope = new Label(control, SWT.NONE);
		lblScope.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
		lblScope.setText("Scope :");

		Combo cScope = new Combo(control, SWT.NONE);
		cScope.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
	}




	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
