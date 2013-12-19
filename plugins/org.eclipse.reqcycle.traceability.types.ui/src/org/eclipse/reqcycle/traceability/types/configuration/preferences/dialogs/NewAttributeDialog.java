package org.eclipse.reqcycle.traceability.types.configuration.preferences.dialogs;

import java.util.ArrayList;

import javax.inject.Inject;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.reqcycle.traceability.types.ITraceTypesManager;
import org.eclipse.reqcycle.traceability.types.configuration.typeconfiguration.Attribute;
import org.eclipse.reqcycle.traceability.types.configuration.typeconfiguration.RegisteredAttribute;
import org.eclipse.reqcycle.traceability.types.configuration.typeconfiguration.StdAttribute;
import org.eclipse.reqcycle.traceability.types.configuration.typeconfiguration.TypeConfigContainer;
import org.eclipse.reqcycle.traceability.types.configuration.typeconfiguration.TypeconfigurationFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.google.common.collect.Lists;

public class NewAttributeDialog extends TitleAreaDialog {

	private TypeConfigContainer container;
	@Inject
	ITraceTypesManager manager;

	private StdAttribute attribute;
	private Button btnNewAttribute;
	private Button btnRegisteredAttribute;
	private Group grpExistingAttribute;

	AttributeComposite registeredComposite;
	private ComboViewer comboViewer;

	private RegisteredAttribute NullRegistered = TypeconfigurationFactory.eINSTANCE
			.createRegisteredAttribute();

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 * @param container
	 */
	public NewAttributeDialog(Shell parentShell, TypeConfigContainer container) {
		super(parentShell);
		setShellStyle(SWT.CLOSE | SWT.RESIZE | SWT.TITLE);
		this.container = container;
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		setMessage("Attribute Creation");
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(1, false));

		Group grpTypeOfAttribute = new Group(container, SWT.NONE);
		FillLayout fl_grpTypeOfAttribute = new FillLayout(SWT.VERTICAL);
		fl_grpTypeOfAttribute.marginWidth = 5;
		fl_grpTypeOfAttribute.marginHeight = 5;
		grpTypeOfAttribute.setLayout(fl_grpTypeOfAttribute);
		grpTypeOfAttribute.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 1, 1));
		grpTypeOfAttribute.setText("Type of attribute");

		btnNewAttribute = new Button(grpTypeOfAttribute, SWT.RADIO);
		btnNewAttribute.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setNewOne();
			}
		});
		btnNewAttribute.setSelection(true);
		btnNewAttribute.setText("New Attribute");

		btnRegisteredAttribute = new Button(grpTypeOfAttribute, SWT.RADIO);
		btnRegisteredAttribute.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setRegistered();
			}
		});
		btnRegisteredAttribute.setText("Registered Attribute");

		grpExistingAttribute = new Group(container, SWT.NONE);
		grpExistingAttribute.setLayout(new GridLayout(2, false));
		grpExistingAttribute.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				true, true, 1, 1));
		grpExistingAttribute.setText("Attribute");

		Label lblNewLabel = new Label(grpExistingAttribute, SWT.NONE);
		lblNewLabel.setBounds(0, 0, 55, 15);
		lblNewLabel.setText("id : ");

		comboViewer = new ComboViewer(grpExistingAttribute, SWT.READ_ONLY);
		comboViewer.setLabelProvider(new LabelProvider() {

			@Override
			public String getText(Object element) {
				if (element == NullRegistered) {
					return "no registered attributes";
				}
				if (element instanceof RegisteredAttribute) {
					RegisteredAttribute att = (RegisteredAttribute) element;
					return att.getName() + " (" + att.getId() + ")";
				}
				return super.getText(element);
			}

		});
		comboViewer.setContentProvider(new ArrayContentProvider());
		ArrayList<RegisteredAttribute> newArrayList = Lists
				.newArrayList(manager.getAllAttributes());
		if (newArrayList.isEmpty()) {
			newArrayList.add(NullRegistered);
		}
		comboViewer.setInput(newArrayList);
		Combo combo = comboViewer.getCombo();
		combo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				registeredComposite
						.displayAttribute((Attribute) ((IStructuredSelection) comboViewer
								.getSelection()).getFirstElement());
			}
		});
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1,
				1));
		combo.setBounds(0, 0, 91, 23);

		registeredComposite = new AttributeComposite(grpExistingAttribute);
		registeredComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				true, true, 2, 1));
		registeredComposite.setEditable(false);
		setNewOne();
		
		recursiveSetEnabled(container, false);
		
		return container;
	}

	public void recursiveSetEnabled(Control ctrl, boolean enabled) {
		   if (ctrl instanceof Composite) {
		      Composite comp = (Composite) ctrl;
		      for (Control c : comp.getChildren()) {
		         recursiveSetEnabled(c, enabled);
		         c.setEnabled(enabled);
		      }
		   } else {
		      ctrl.setEnabled(enabled);
		   }
		}
	
	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		Button okButton = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		okButton.setEnabled(false);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 491);
	}

	@Override
	protected void okPressed() {
		if (btnRegisteredAttribute.getSelection()) {
			attribute = (StdAttribute) ((IStructuredSelection) comboViewer
					.getSelection()).getFirstElement();
		} else {
			registeredComposite.saveInAttribute(attribute);
		}
		if (attribute == NullRegistered || attribute == null
				|| attribute.getName() == null || attribute.getType() == null
				|| !registeredComposite.isValid()) {
			setErrorMessage("Please verify your fields");
			return;
		}
		super.okPressed();
	}

	public Attribute getAttribute() {
		return attribute;
	}

	public void setNewOne() {
		display(true);
		comboViewer.setSelection(null);
	}

	public void display(boolean newOne) {
		btnNewAttribute.setSelection(newOne);
		btnRegisteredAttribute.setSelection(!newOne);
		if (comboViewer.getCombo() != null) {
			comboViewer.getCombo().setEnabled(!newOne);
		}
		if (newOne) {
			attribute = TypeconfigurationFactory.eINSTANCE.createStdAttribute();
		} else {
			attribute = null;
		}
		registeredComposite.setEditable(newOne);
	}

	public void setRegistered() {
		display(false);
	}
}
