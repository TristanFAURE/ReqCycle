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

import org.eclipse.ziggurat.collect.Picker;
import org.eclipse.ziggurat.collect.ResultHandler;
import org.eclipse.ziggurat.collect.exceptions.CannotHandleException;
import org.eclipse.ziggurat.collect.exceptions.CollectionAbortedException;

public class DepthCollector<T> extends AbstractCollector<T> {


	public DepthCollector(T startingElement, Iterable<? extends Picker<T>> pickers) {
		super(startingElement, pickers);
	}

	/**
	 * By default, the collection is performed depth wise.
	 */
	public void collect(ResultHandler<T> handler) throws CollectionAbortedException {
		this.collectDepthWise(handler, start);
	}

	/**
	 * Depth wise collection.
	 * 
	 * @param handler
	 *        the handler that processes each element.
	 * @param element
	 *        : the element from which the collection is performed.
	 * @throws CollectionAbortedException
	 */
	protected void collectDepthWise(ResultHandler<T> handler, T element) throws CollectionAbortedException {
		try {
			handler.handleResult(element); //send result to the handler.
			for(Picker<T> picker : this.getPickers()) {
				Iterable<T> nexts = picker.getNexts(element); //getting children.
				if(nexts != null) { //some elements do not have any child.
					for(T next : nexts) {
						this.collectDepthWise(handler, next); //recurse
					}
				}
			}
		} catch (CannotHandleException e) {
			// if the element cannot be handled, do nothing.
		} catch (Exception e) {
			throw new CollectionAbortedException(e);
		} 
	}

}
