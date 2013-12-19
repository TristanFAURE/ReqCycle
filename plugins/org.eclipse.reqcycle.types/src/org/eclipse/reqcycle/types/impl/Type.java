package org.eclipse.reqcycle.types.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.reqcycle.types.ICachedTypedChecker;
import org.eclipse.reqcycle.types.IInjectedTypeChecker;
import org.eclipse.reqcycle.types.IInjectedTypeChecker.InjectValue;
import org.eclipse.reqcycle.types.IInjectedTypeChecker.InjectValueName;
import org.eclipse.reqcycle.types.IType;
import org.eclipse.reqcycle.types.ITypeChecker;
import org.eclipse.reqcycle.uri.model.Reachable;
import org.eclipse.ziggurat.inject.ZigguratInject;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

public class Type implements IType {

	private String label;
	private String id;
	private ImageDescriptor desc;
	private String subTypeOf;
	private Class<? extends ITypeChecker> checker;
	protected ITypeChecker instance = null;
	public IType subType = null;
	@Inject
	ICachedTypedChecker cache;

	public Type() {
	}

	public Type(String id, IType parent) {
		this.setChecker(parent.getCheckerClass());
		this.setIcon(parent.getIcon());
		this.setId(id);
		this.setLabel(parent.getLabel());
		this.setSubTypeOf(parent.getId());
	}

	protected ITypeChecker getChecker() {
		if (instance == null) {
			try {
				instance = checker.newInstance();
				ZigguratInject.inject(instance);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return instance;
	}

	@Override
	public boolean is(Reachable reachable) {
		if (cache != null) {
			return cache.is(reachable, getChecker());
		}
		return getChecker().apply(reachable);
	}

	protected void setLabel(String label) {
		this.label = label;
	}

	protected void setIcon(ImageDescriptor desc) {
		this.desc = desc;
	}

	protected void setSubTypeOf(String type) {
		this.subTypeOf = type;
	}

	protected void setChecker(Class<? extends ITypeChecker> aClass) {
		this.checker = aClass;
	}

	protected void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public String getLabel() {
		return label;
	}

	public ImageDescriptor getIcon() {
		return desc;
	}

	@Override
	public IType getSuperType() {
		if (subType == null) {
			Iterator<IType> iterator = Iterables.filter(
					TypesManager.allTypes.values(), new Predicate<IType>() {
						public boolean apply(IType t) {
							return subTypeOf != null
									&& subTypeOf.equals(t.getId());
						}
					}).iterator();
			if (iterator.hasNext()) {
				subType = iterator.next();
			}
		}
		return subType;
	}

	@Override
	public List<FieldDescriptor> getDescriptors() {
		List<FieldDescriptor> descriptors = new LinkedList<FieldDescriptor>();
		for (Field f : getFieldsToInject()) {
			if(f.isAnnotationPresent(InjectValueName.class)) {
				InjectValueName annotation = f.getAnnotation(InjectValueName.class);
				descriptors.add(new FieldURIDescriptor(f.getName(), f.getType(), annotation.type()));
			} else {
				descriptors.add(new FieldDescriptor(f.getName(), f.getType()));
			}
		}
		return descriptors;
	}

	protected List<Field> getFieldsToInject() {
		List<Field> fields = new ArrayList<Field>();
		Class<?> current = getCheckerClass();
		if (IInjectedTypeChecker.class.isAssignableFrom(current)) {
			while (current != null) {
				for (Field f : current.getDeclaredFields()) {
					if (f.isAnnotationPresent(InjectValue.class) || f.isAnnotationPresent(InjectValueName.class)) {
						fields.add(f);
					}
				}
				current = current.getSuperclass();
			}
		}
		return fields;
	}

	@Override
	public boolean isExtensible() {
		return IInjectedTypeChecker.class.isAssignableFrom(getCheckerClass());
	}

	@Override
	public Class<? extends ITypeChecker> getCheckerClass() {
		return checker;
	}
	
	// public static class InjectedType extends Type implements IInjectedType {
	// @Override
	// public boolean is(Reachable reachable, IValueInjecter injecter) {
	// ITypeChecker aChecker = getChecker();
	// if (injecter != null) {
	// if (aChecker instanceof IInjectedTypeChecker) {
	// IInjectedTypeChecker injected = (IInjectedTypeChecker) aChecker;
	// List<Field> fields = getFieldsToInject();
	// for (Field f : fields) {
	// boolean oldAccess = f.isAccessible();
	// if (!oldAccess) {
	// f.setAccessible(true);
	// }
	// try {
	// f.set(injected,
	// injecter.getValue(getId(), f.getName(),
	// f.getType()));
	// } catch (IllegalArgumentException e) {
	// e.printStackTrace();
	// } catch (IllegalAccessException e) {
	// e.printStackTrace();
	// }
	// if (!oldAccess) {
	// f.setAccessible(false);
	// }
	// }
	// }
	// }
	// return aChecker.apply(reachable);
	// }
	//
	// }

}
