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
package org.eclipse.ziggurat.collect.yieldadapters;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.ziggurat.collect.Collector;
import org.eclipse.ziggurat.collect.ResultHandler;
import org.eclipse.ziggurat.collect.YieldAdapter;
import org.eclipse.ziggurat.collect.YieldAdapterIterable;
import org.eclipse.ziggurat.collect.YieldAdapterIterator;
import org.eclipse.ziggurat.collect.exceptions.CannotHandleException;
import org.eclipse.ziggurat.collect.exceptions.CollectionAbortedException;

/**
 * Same as ReferenceYieldAdapter, excepts it gathers the results in a set.
 */
public class SetYieldAdapter<T> implements YieldAdapter<T> {

	/**
	 * Convert a method that implements the Collector<> class with a standard Iterable<>, by
	 * collecting the results in a list, and returning an iterator to that list.
	 */
	public YieldAdapterIterable<T> adapt(Collector<T> client) {

		final Set<T> results = new LinkedHashSet<T>();

		try {
			client.collect(new ResultHandler<T>() {

				public void handleResult(T value) throws CannotHandleException {
					if(!results.contains(value)) {
						results.add(value);
					}
				}
			});
		} catch (CollectionAbortedException e) {
			// The process was aborted by calling code.
		}

		// Wrap container's iterator with yield adapter interface for compatibility
		return new YieldAdapterIterable<T>() {

			public YieldAdapterIterator<T> iterator() {
				final Iterator<T> iterator = results.iterator();
				return new YieldAdapterIterator<T>() {

					public boolean hasNext() {
						return iterator.hasNext();
					}

					public T next() {
						return iterator.next();
					}

					public void remove() {
						iterator.remove();
					}

					public void dispose() {
						// Does nothing in this implementation
					}
				};
			}
		};

	}
}
