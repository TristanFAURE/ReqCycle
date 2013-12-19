package org.eclipse.reqcycle.types;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface IInjectedTypeChecker extends ITypeChecker {

	public interface IValueInjecter {
		public <T> T getValue(String typeId, String attributeName,
				Class<T> typeOfTheAttribute);

	}

	/**
	 * Annotation to put on fields where value shall be injected the current
	 * authorized types are : String, int, boolean,
	 * 
	 * @author tfaure
	 * 
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface InjectValue {
	}
	
	/**
	 * Annotation to put on fields where element name shall be injected
	 * 
	 * @author aradouan
	 * 
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface InjectValueName {
		//Element type
		Class<?> type();
	}

}
