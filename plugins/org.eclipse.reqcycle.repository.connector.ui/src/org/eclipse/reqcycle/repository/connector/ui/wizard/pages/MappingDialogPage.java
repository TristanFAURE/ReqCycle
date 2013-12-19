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
package org.eclipse.reqcycle.repository.connector.ui.wizard.pages;

import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.reqcycle.repository.connector.ui.wizard.MappingComposite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import MappingModel.MappingAttribute;


public abstract class MappingDialogPage extends Dialog {



	private MappingComposite mappingComposite;

	/**
	 * @param parentShell
	 */
	protected MappingDialogPage(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite)super.createDialogArea(parent);
		Control result = build(composite);
		return result;
	}

	public Control build(Composite parent) {
		mappingComposite = new MappingComposite(parent, SWT.NONE, null) {

			@Override
			public EObject linkElements(Object sourceSelection, Object targetSelection) {
				return MappingDialogPage.this.linkElements(sourceSelection, targetSelection);
			}

			@Override
			protected String getResultDetail() {
				return MappingDialogPage.this.getResultDetail();
			}

			@Override
			protected Object getTargetInput() {
				return MappingDialogPage.this.getTargetInput();
			}

			@Override
			protected Object getSourceInput() {
				return MappingDialogPage.this.getSourceInput();
			}

			@Override
			protected IContentProvider getSourceContentProvider() {
				return MappingDialogPage.this.getSourceContentProvider();
			}

			@Override
			protected IBaseLabelProvider getSourceLabelProvider() {
				return MappingDialogPage.this.getSourceLabelProvider();
			}

			@Override
			protected String getTargetDetail() {
				return MappingDialogPage.this.getTargetDetail();
			}

			@Override
			protected String getSourceDetail() {
				return MappingDialogPage.this.getSourceDetail();
			}

			@Override
			protected boolean getCanEditLink() {
				return MappingDialogPage.this.getCanEditLink();
			}

		};

		return mappingComposite;
	}


	protected abstract String getResultDetail();

	protected abstract Object getTargetInput();

	protected abstract Object getSourceInput();

	protected abstract boolean getCanEditLink();

	protected abstract String getSourceDetail();

	protected abstract String getTargetDetail();

	protected abstract IContentProvider getSourceContentProvider();

	protected abstract MappingAttribute linkElements(Object sourceSelection, Object targetSelection);

	public abstract ILabelProvider getSourceLabelProvider();

	public Collection<EObject> getResult() {
		if(mappingComposite != null) {
			return mappingComposite.getResult();
		}
		return null;
	}

}
