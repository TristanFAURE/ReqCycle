/*******************************************************************************
 * Copyright (c) 2013 Atos.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Atos - initial API and implementation
 ******************************************************************************/
package org.eclipse.ziggurat.collect.collectors;

import java.util.Collection;

import org.eclipse.ziggurat.collect.Collector;
import org.eclipse.ziggurat.collect.Picker;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;


/**
 * Basic abstract realization of a collector, that should delegate the collecting job to the pickers.
 */
public abstract class AbstractCollector<T> implements Collector<T> {

	/**
	 * The element from which the collector should start collecting elements.
	 */
	protected T start;

	/**
	 * Pickers, referenced in an attribute.
	 */
	protected Collection<Picker<T>> pickers;

	public AbstractCollector(T startingElement, Iterable<? extends Picker<T>> pickers) {
		this.start = startingElement;
		this.pickers = Lists.newArrayList();
		Iterables.addAll(this.pickers, pickers);
	}

	public Object getStartingElement() {
		return this.start;
	}

	public Collection<Picker<T>> getPickers() {
		return pickers;
	}

}
