package org.eclipse.reqcycle.traceability.ui;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.eclipse.reqcycle.traceability.model.Link;
import org.eclipse.reqcycle.traceability.types.ITraceabilityAttributesManager;
import org.eclipse.reqcycle.traceability.types.ITraceabilityAttributesManager.EditableAttribute;
import org.eclipse.reqcycle.traceability.ui.views.LinkPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource2;
import org.eclipse.ziggurat.inject.ZigguratInject;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class LinkPropertySource implements IPropertySource2 {

	private ITraceabilityAttributesManager attributesManager = ZigguratInject.make(ITraceabilityAttributesManager.class);

	private Callable<?> callback;

	Map<String, EditableAttribute> attributes = null;

	public LinkPropertySource(Link link, Callable<?> callback) {
		attributes = Maps.uniqueIndex(attributesManager.getAttributes(link.getId()), new Function<EditableAttribute, String>() {

			@Override
			public String apply(EditableAttribute arg0) {
				return arg0.getName();
			}
		});
		this.callback = callback;
	}

	@Override
	public Object getEditableValue() {
		return this;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		List<EditableAttribute> editableAttributes = Lists.newArrayList(attributes.values());
		IPropertyDescriptor[] descriptors = new IPropertyDescriptor[editableAttributes.size()];
		for(int i = 0; i < editableAttributes.size(); i++) {
			descriptors[i] = new LinkPropertyDescriptor(editableAttributes.get(i));
		}
		return descriptors;
	}

	@Override
	public Object getPropertyValue(Object id) {
		EditableAttribute att = attributes.get(id);
		return att.getValue();
	}

	@Override
	public void resetPropertyValue(Object id) {

	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		EditableAttribute att = attributes.get(id);
		att.setValue(value);
		if(callback != null) {
			try {
				callback.call();
			} catch (Exception e) {
				//Do nothing.
			}
		}
	}

	@Override
	public boolean isPropertyResettable(Object id) {
		return false;
	}

	@Override
	public boolean isPropertySet(Object id) {
		return false;
	}

}
