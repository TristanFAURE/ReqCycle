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
package org.eclipse.reqcycle.repository.data.ui.dialog;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * Simple popup requesting name
 */
public class NameDialog extends Dialog implements Listener {


	/**
	 * The popup Bean
	 */
	public class NameBean {

		protected Listener listener;

		private String name;

		public NameBean(Listener listener) {
			this.listener = listener;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
			handleEvent(new Event());
		}

		public void handleEvent(Event event) {
			if(listener != null) {
				listener.handleEvent(event);
			}
		}

		protected Listener getListener() {
			return listener;
		}
	}

	/** popup bean for databinding */
	protected NameBean bean;

	/** popup text field */
	protected Text txtName;

	/** popup title */
	private String title;

	public NameDialog(Shell parentShell, String title) {
		super(parentShell);
		this.title = title;
	}

	public NameDialog(Shell parentShell, String title, NameBean bean) {
		this(parentShell, title);
		this.bean = bean;
	}


	@Override
	protected Control createDialogArea(Composite parent) {

		Shell shell = getShell();
		if(shell != null) {
			shell.setText(title);
		}

		Composite control = (Composite)super.createDialogArea(parent);

		Composite composite = new Composite(control, SWT.None);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		composite.setLayout(new GridLayout(2, false));

		Label lblName = new Label(composite, SWT.None);
		lblName.setText("Name :");

		txtName = new Text(composite, SWT.BORDER);
		txtName.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		doCreateDialogArea(composite);

		initDataBindings();

		return control;
	};


	/**
	 * Override this method to add custom Ui
	 * 
	 * @param control
	 */
	protected void doCreateDialogArea(Composite control) {
	}

	@Override
	protected Button createButton(Composite parent, int id, String label, boolean defaultButton) {
		Button button = super.createButton(parent, id, label, defaultButton);
		if(OK == id) {
			button.setEnabled(false);
		}
		return button;
	}


	/**
	 * @return Entered Name
	 */
	public String getName() {
		return bean.getName();
	}


	/**
	 * Perfom binding between the popup and the bean
	 * 
	 * @return the data binding context
	 */
	protected DataBindingContext initDataBindings() {
		if(bean == null) {
			bean = new NameBean(this);
		}
		DataBindingContext bindingContext = new DataBindingContext();
		//
		IObservableValue observeTextTxtNameObserveWidget = WidgetProperties.text(SWT.Modify).observe(txtName);
		IObservableValue nameBeanObserveValue = PojoProperties.value("name").observe(bean);
		bindingContext.bindValue(observeTextTxtNameObserveWidget, nameBeanObserveValue, null, null);
		//
		doInitDataBindings(bindingContext);
		//
		return bindingContext;
	}

	/**
	 * Override this method to add data binding
	 * 
	 * @param bindingContext
	 *        the binding context
	 */
	protected void doInitDataBindings(DataBindingContext bindingContext) {
	}


	/**
	 * Enables the button OK
	 * 
	 * @param enable
	 */
	private void enableOKbtn(boolean enable) {
		Button okBtn = getButton(OK);
		if(okBtn != null) {
			okBtn.setEnabled(enable);
		}
	}

	@Override
	public void handleEvent(Event event) {
		String name = bean.getName();
		boolean enableOK = true;
		if(name == null || name.isEmpty()) {
			enableOK = false;
		}

		if(!doHandleEvent(event)) {
			enableOK = false;
		}

		enableOKbtn(enableOK);
	}

	/**
	 * Override this method to handle changes modifications events
	 * 
	 * @param event
	 *        the event which occurred
	 * @return return false if OK button have to be disabled
	 */
	public boolean doHandleEvent(Event event) {
		return true;
	}


	/**
	 * Sets the popup bean
	 * 
	 * @param bean
	 *        the bean to set
	 */
	public void setBean(NameBean bean) {
		this.bean = bean;
	}

}
