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
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.SelectObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;


public class AddTypeDialog extends NameDialog {

	/**
	 * Element Types
	 */
	static enum TYPE {
		REQUIREMENT, ENUMERATION
	}

	/**
	 * The popup Bean
	 */
	public class Bean extends NameBean {

		private TYPE type;

		public Bean(Listener listener) {
			super(listener);
		}

		public void setType(TYPE type) {
			this.type = type;
			listener.handleEvent(new Event());
		}

		public TYPE getType() {
			return type;
		}
	}


	private Button btnReqRadio;

	private Button btnEnumRadio;

	public AddTypeDialog(Shell parentShell, String title) {
		super(parentShell, title);
		setBean(new Bean(this));
	}

	@Override
	protected void doCreateDialogArea(Composite parent) {
		Label typelbl = new Label(parent, SWT.None);
		typelbl.setText("Type :");
		typelbl.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));

		Composite radioComposite = new Composite(parent, SWT.None);
		radioComposite.setLayout(new GridLayout(1, false));
		radioComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));

		btnReqRadio = new Button(radioComposite, SWT.RADIO);
		btnReqRadio.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		btnReqRadio.setText("Requirement");

		btnEnumRadio = new Button(radioComposite, SWT.RADIO);
		btnEnumRadio.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		btnEnumRadio.setText("Enumeration");

		btnReqRadio.setSelection(true);
		((Bean)bean).setType(TYPE.REQUIREMENT);
	};


	/**
	 * @return true if Requirement has been chosen instead of Enumeration
	 */
	public boolean isRequirement() {
		return TYPE.REQUIREMENT.equals(((Bean)bean).getType());
	}

	/**
	 * @return true if Enumeration has been chosen instead of Requirement
	 */
	public boolean isEnumeration() {
		return TYPE.ENUMERATION.equals(((Bean)bean).getType());
	}

	@Override
	protected void doInitDataBindings(DataBindingContext bindingContext) {
		//Create the Select Observable for our enum type  
		SelectObservableValue typeRadioObservable = new SelectObservableValue(TYPE.class);

		//bind the requirement radio button selection to the right enum value  
		IObservableValue btnRequirementObserverSelection = SWTObservables.observeSelection(btnReqRadio);
		typeRadioObservable.addOption(TYPE.REQUIREMENT, btnRequirementObserverSelection);

		//bind the enumeration radio button selection to the right enum value 
		IObservableValue btnEnumerationObserverSelection = SWTObservables.observeSelection(btnEnumRadio);
		typeRadioObservable.addOption(TYPE.ENUMERATION, btnEnumerationObserverSelection);

		bindingContext.bindValue(typeRadioObservable, PojoObservables.observeValue(bean, "type"));
	}

	@Override
	public boolean doHandleEvent(Event event) {
		if(((Bean)bean).getType() == null) {
			return false;
		}
		return true;
	}

}
