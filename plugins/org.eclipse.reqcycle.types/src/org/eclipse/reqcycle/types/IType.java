package org.eclipse.reqcycle.types;

import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.reqcycle.uri.model.Reachable;

public interface IType {

	public String getId();

	public String getLabel();

	public ImageDescriptor getIcon();

	public IType getSuperType();

	public boolean is(Reachable r);

	public boolean isExtensible();

	public Class<? extends ITypeChecker> getCheckerClass();
	
	/**
	 * Available only if isExtensible = true
	 * 
	 * @return
	 */
	public List<IType.FieldDescriptor> getDescriptors();

	public class FieldDescriptor {
		public String name;
		public Class<?> type;

		public FieldDescriptor(String name, Class<?> type) {
			this.name = name;
			this.type = type;
		}
	}
	
	public class FieldURIDescriptor extends FieldDescriptor {
		public String name;
		public Class<?> type;
		public Class<?> realType;

		public FieldURIDescriptor(String name, Class<?> type, Class<?> realType) {
			super(name, type);
			this.realType = realType;
		}
	}

}
