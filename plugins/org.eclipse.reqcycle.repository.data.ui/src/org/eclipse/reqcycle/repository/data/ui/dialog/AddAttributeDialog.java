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

import java.util.Collection;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.viewers.ViewerProperties;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.reqcycle.repository.data.types.IAttributeType;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public class AddAttributeDialog extends NameDialog {

	/**
	 * The popup bean
	 */
	public class Bean extends NameBean {

		private IAttributeType type;

		public Bean(Listener listener) {
			super(listener);
		}

		public IAttributeType getType() {
			return type;
		}

		public void setType(IAttributeType type) {
			this.type = type;
			listener.handleEvent(new Event());
		}
	}

	/** Attribute types */
	private Collection<IAttributeType> types;

	/** Attribute Types Combo Viewer */
	private ComboViewer cvAttribute;

	public AddAttributeDialog(Shell parentShell, String title, Collection<IAttributeType> types) {
		super(parentShell, title);
		setBean(new Bean(this));
		this.types = types;
	}

	@Override
	protected void doCreateDialogArea(Composite parent) {
		Label typelbl = new Label(parent, SWT.None);
		typelbl.setText("Type :");
		typelbl.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));

		cvAttribute = new ComboViewer(parent);
		cvAttribute.getCombo().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		cvAttribute.setLabelProvider(new LabelProvider() {

			@Override
			public String getText(Object element) {
				if(element instanceof IAttributeType) {

					return ((IAttributeType)element).getName();
					//						String nsURI = ((IAttributeType)element).getEPackage().getNsURI();
					//						if(EcoreFactory.eINSTANCE.getEPackage().getNsURI().equals(nsURI) && name.startsWith("E")) {
					//							name = name.replaceFirst("E", "");
					//						}
					//						return name + "  [" + nsURI + "]";
				}
				return super.getText(element);
			}
		});
		cvAttribute.setContentProvider(ArrayContentProvider.getInstance());
		cvAttribute.setInput(types);
	};

	@Override
	protected Button createButton(Composite parent, int id, String label, boolean defaultButton) {
		Button button = super.createButton(parent, id, label, defaultButton);
		if(OK == id) {
			button.setEnabled(false);
		}
		return button;
	}

	@Override
	public String getName() {
		return bean.getName();
	}

	@Override
	public boolean doHandleEvent(Event event) {
		if(((Bean)bean).getType() == null) {
			return false;
		}
		return true;
	}

	@Override
	protected void doInitDataBindings(DataBindingContext bindingContext) {
		IObservableValue observeSingleSelectionAttrCV = ViewerProperties.singleSelection().observe(cvAttribute);
		IObservableValue typeBeanObserveValue = PojoProperties.value("type").observe(bean);
		bindingContext.bindValue(observeSingleSelectionAttrCV, typeBeanObserveValue, null, null);
	}

	/**
	 * @return chosen attribue type
	 */
	public IAttributeType getType() {
		return ((Bean)bean).getType();
	}
}
