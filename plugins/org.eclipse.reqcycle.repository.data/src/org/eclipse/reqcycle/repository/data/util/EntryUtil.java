package org.eclipse.reqcycle.repository.data.util;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.reqcycle.repository.data.Activator;
import org.eclipse.reqcycle.repository.data.IDataModelManager;
import org.eclipse.reqcycle.repository.data.types.IDataModel;
import org.eclipse.reqcycle.repository.data.types.IRequirementType;
import org.eclipse.reqcycle.traceability.types.configuration.typeconfiguration.Entry;
import org.eclipse.reqcycle.traceability.types.configuration.typeconfiguration.TypeconfigurationFactory;
import org.eclipse.reqcycle.types.IType.FieldDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ziggurat.inject.ZigguratInject;

import ScopeConf.Scope;

/*
 * FIXME : remove entry (Traceability element)
 */
public class EntryUtil {

	static IDataModelManager dataModelManager = ZigguratInject.make(IDataModelManager.class);

	private static final String AN_ENTRY = "entry";

	private static final String CLEAR_IMG_PATH = "/icons/delete.gif";

	public static Entry createComboViewer(Composite parent, FieldDescriptor fd, Object input) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		final ComboViewer comboViewer = new ComboViewer(composite);
		Combo combo = comboViewer.getCombo();
		combo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		final LabelProvider labelProvider = new LabelProvider() {

			@Override
			public String getText(Object element) {
				if(Platform.getAdapterManager().hasAdapter(element, String.class.getName())) {
					Object adapter = Platform.getAdapterManager().getAdapter(element, String.class);
					if(adapter instanceof String) {
						return ((String)adapter);
					}
				}
				return super.getText(element);
			}
		};
		comboViewer.setLabelProvider(labelProvider);
		comboViewer.setContentProvider(new ArrayContentProvider());
		comboViewer.setInput(input);

		final Entry entry = TypeconfigurationFactory.eINSTANCE.createEntry();
		entry.setName(fd.name);
		comboViewer.setData(AN_ENTRY, entry);
		comboViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				ISelection selection = event.getSelection();
				if(selection instanceof IStructuredSelection) {
					Object firstElement = ((IStructuredSelection)selection).getFirstElement();
					if(firstElement instanceof IDataModel || firstElement instanceof IRequirementType || firstElement instanceof Scope) {
						entry.setValue(labelProvider.getText(firstElement));
					} else {
						entry.setValue(null);
					}
				}
			}
		});

		Button clearCombo = new Button(composite, SWT.PUSH);
		clearCombo.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
		clearCombo.setImage(Activator.getImageDescriptor(CLEAR_IMG_PATH).createImage());
		clearCombo.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if(comboViewer != null) {
					comboViewer.setSelection(StructuredSelection.EMPTY);
				}
			}
		});

		return entry;
	}
}
