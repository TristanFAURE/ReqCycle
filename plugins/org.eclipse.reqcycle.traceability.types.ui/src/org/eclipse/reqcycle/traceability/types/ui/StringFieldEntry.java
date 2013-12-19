package org.eclipse.reqcycle.traceability.types.ui;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.reqcycle.traceability.types.configuration.preferences.validators.RegexPredicate;
import org.eclipse.reqcycle.traceability.types.configuration.typeconfiguration.Entry;
import org.eclipse.reqcycle.traceability.types.configuration.typeconfiguration.TypeconfigurationFactory;
import org.eclipse.reqcycle.types.IType.FieldDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;


public class StringFieldEntry implements IEntryCompositeProvider {

	private static final String AN_ENTRY = "entry";
	private static final String A_PREDICATE = "predicate";
	private static LabelProvider labelProvider = new LabelProvider();
	
	private Text text_1;

	@Override
	public Entry createEntryComposite(Composite parent, int style, FieldDescriptor fd) {
		Entry entry = createContent(parent, fd);
		return entry;
	}
	
	protected Entry createContent(Composite composite, FieldDescriptor fieldDescriptor) {
		
		text_1 = new Text(composite, SWT.BORDER);
		text_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1,
				1));
		final Predicate<String> predicate = getPredicate(fieldDescriptor.type);
		
		Entry entry = TypeconfigurationFactory.eINSTANCE.createEntry();

		entry.setName(fieldDescriptor.name);
		
		text_1.setData(AN_ENTRY, entry);
		text_1.setData(A_PREDICATE, predicate);
		text_1.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				Widget w = e.widget;
				if (w instanceof Text) {
					Text text = (Text) w;
					text.setBackground(Display.getDefault().getSystemColor(
							SWT.COLOR_WHITE));
					Predicate<String> p = (Predicate<String>) text.getData(A_PREDICATE);
					Entry currentEntry = (Entry) text.getData(AN_ENTRY);
					String value = text.getText();
					if (value.length() != 0) {
						if (!p.apply(value)) {
							text.setBackground(Display.getDefault()
									.getSystemColor(SWT.COLOR_RED));
						} else {
							currentEntry.setValue(value);
						}
					} else {
						currentEntry.setValue(null);
					}
				}
			}
		});
		return entry;
	}

	private Predicate<String> getPredicate(Class<?> type) {
		if (String.class.isAssignableFrom(type)) {
			return Predicates.alwaysTrue();
		} else if ((Boolean.class.isAssignableFrom(type))
				|| (boolean.class.isAssignableFrom(type))) {
			return new RegexPredicate("true|false");
		} else if ((Integer.class.isAssignableFrom(type))
				|| int.class.isAssignableFrom(type)) {
			return new RegexPredicate("\\d*");
		} else {
			return Predicates.alwaysFalse();
		}
	}
	
}
