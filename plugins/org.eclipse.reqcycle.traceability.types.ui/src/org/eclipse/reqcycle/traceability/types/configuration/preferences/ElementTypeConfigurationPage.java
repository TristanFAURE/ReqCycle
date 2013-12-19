package org.eclipse.reqcycle.traceability.types.configuration.preferences;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.reqcycle.traceability.types.configuration.preferences.dialogs.NewCustomTypeDialog;
import org.eclipse.reqcycle.traceability.types.configuration.preferences.providers.PreferenceDialogTypeLabelProvider;
import org.eclipse.reqcycle.traceability.types.configuration.preferences.providers.PreferenceDialogTypesContentProvider;
import org.eclipse.reqcycle.traceability.types.configuration.typeconfiguration.CustomType;
import org.eclipse.reqcycle.traceability.types.configuration.typeconfiguration.Type;
import org.eclipse.reqcycle.traceability.types.configuration.typeconfiguration.TypeConfigContainer;
import org.eclipse.reqcycle.types.IType;
import org.eclipse.reqcycle.types.ITypesManager;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ziggurat.inject.ZigguratInject;

public class ElementTypeConfigurationPage extends AbstractPreferencePage {

	private PreferenceDialogTypesContentProvider preferenceDialogTypesContentProvider;

	@Override
	protected void addAction() {
		if (treeViewer.getSelection() instanceof IStructuredSelection) {
			IStructuredSelection selec = (IStructuredSelection) treeViewer
					.getSelection();
			if (selec.getFirstElement() instanceof Type) {
				Type type = (Type) selec.getFirstElement();
				IType javaType = type.getIType();
				if (javaType != null && javaType.isExtensible()) {
					NewCustomTypeDialog d = new NewCustomTypeDialog(getShell(),
							type);
					ZigguratInject.inject(d);
					if (d.open() == NewCustomTypeDialog.OK) {
						CustomType custom = d.getCustomType();
						container.getTypes().add(custom);
					}
				}
			}
		}
	}

	@Override
	protected boolean removeCondition(EObject firstElement) {
		return firstElement instanceof CustomType;
	}

	@Override
	protected String getAddLabel() {
		return "extend";
	}

	@Override
	protected void addDefaultButton(Composite composite_1) {
		// no default in type
	}

	@Override
	protected IContentProvider getContentProvider() {
		preferenceDialogTypesContentProvider = new PreferenceDialogTypesContentProvider(
				factory, container);
		ZigguratInject.inject(preferenceDialogTypesContentProvider);
		return preferenceDialogTypesContentProvider;
	}

	@Override
	protected ILabelProvider getLabelProvider() {
		PreferenceDialogTypeLabelProvider provider = new PreferenceDialogTypeLabelProvider(
				factory, super.getLabelProvider());
		ZigguratInject.inject(provider);
		return provider;

	}

	public static boolean isExtensible(Type type, ITypesManager manager) {
		IType javaType = null;
		if (!(type instanceof CustomType)) {
			javaType = type.getIType();
		}
		return javaType != null && javaType.isExtensible();
	}

	@Override
	protected ViewerFilter getFilter() {
		return new ViewerFilter() {

			@Override
			public boolean select(Viewer viewer, Object parentElement,
					Object element) {
				return element instanceof TypeConfigContainer
						|| element instanceof Type;
			}
		};
	}

	@Override
	protected void setInput() {
		if(preferenceDialogTypesContentProvider != null) {
			preferenceDialogTypesContentProvider.setTypeConfigContainer(container);
		}
		treeViewer.setInput(container.eResource());
	}

	@Override
	protected void selectionChanged(SelectionChangedEvent event) {
		if (event.getSelection() instanceof IStructuredSelection) {
			IStructuredSelection struc = (IStructuredSelection) event
					.getSelection();
			if (struc.getFirstElement() instanceof Type) {
				Type type = (Type) struc.getFirstElement();
				if (isExtensible(type, typesManager)) {
					btnAdd.setEnabled(true);
				} else {
					btnAdd.setEnabled(false);
				}
				btnRemove.setEnabled(removeCondition(type));
			}
		}

	}

	@Override
	protected void addProperties(Composite composite) {
		// no properties
	}

	@Override
	protected void removeAction() {
		if (treeViewer.getSelection() instanceof IStructuredSelection) {
			IStructuredSelection new_name = (IStructuredSelection) treeViewer
					.getSelection();
			EObject firstElement = (EObject) new_name.getFirstElement();
			if (removeCondition(firstElement)) {
				EcoreUtil.delete(firstElement, true);
			}
		}
	}

}
