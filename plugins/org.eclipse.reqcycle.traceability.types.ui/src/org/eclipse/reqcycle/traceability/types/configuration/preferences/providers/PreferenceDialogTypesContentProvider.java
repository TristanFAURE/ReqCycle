package org.eclipse.reqcycle.traceability.types.configuration.preferences.providers;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.reqcycle.traceability.types.configuration.typeconfiguration.CustomType;
import org.eclipse.reqcycle.traceability.types.configuration.typeconfiguration.Type;
import org.eclipse.reqcycle.traceability.types.configuration.typeconfiguration.TypeConfigContainer;
import org.eclipse.reqcycle.types.IType;
import org.eclipse.reqcycle.types.ITypesManager;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

//FIXME : Don't use a container given in the constructor (Or don't copy the container at each display)
public class PreferenceDialogTypesContentProvider extends
		AdapterFactoryContentProvider {
	@Inject
	private ITypesManager typesManager;
	private TypeConfigContainer container;

	public PreferenceDialogTypesContentProvider(AdapterFactory adapterFactory,
			TypeConfigContainer container) {
		super(adapterFactory);
		this.container = container;
	}

	@Override
	public boolean hasChildren(Object object) {
		return getChildren(object).length > 0;
	}

	@Override
	public Object[] getChildren(Object object) {
		if (object instanceof TypeConfigContainer) {
			return Lists.newArrayList(
					Iterables.filter(Arrays.asList(super.getChildren(object)),
							new Predicate<Object>() {

								public boolean apply(Object o) {
									boolean result = (!(o instanceof CustomType));
									if (result) {
										if (o instanceof Type) {
											Type type = (Type) o;
											return type.getIType() != null
													&& type.getIType()
															.getSuperType() == null;
										}
									}
									return result;
								}
							})).toArray();
		} else if (object instanceof Type) {
			List<Object> list = new LinkedList<Object>();
			for (Type type : container.getTypes()) {
				if (type instanceof CustomType) {
					CustomType cust = (CustomType) type;
					if (cust.getSuperType() == object) {
						list.add(cust);
					}
				} else {
					if (type.getIType() != null) {
						IType superType = type.getIType().getSuperType();
						if (superType != null
								&& superType.getId().equals(
										((Type) object).getTypeId())) {
							list.add(type);
						}
					}
				}

			}
			return list.toArray();
		}
		return super.getChildren(object);
	}

	public void setTypeConfigContainer(TypeConfigContainer container) {
		if(container != null) {
			this.container = container;
		}
	}
}
