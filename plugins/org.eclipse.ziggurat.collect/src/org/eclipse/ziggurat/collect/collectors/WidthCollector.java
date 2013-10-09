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

import java.util.Collections;

import org.eclipse.ziggurat.collect.Picker;
import org.eclipse.ziggurat.collect.ResultHandler;
import org.eclipse.ziggurat.collect.exceptions.CannotHandleException;
import org.eclipse.ziggurat.collect.exceptions.CollectionAbortedException;

import com.google.common.collect.Iterables;

public class WidthCollector<T> extends AbstractCollector<T> {


	public WidthCollector(T startingElement, Picker<T>... pickers) {
		super(startingElement, pickers);
	}


	public void collect(ResultHandler<T> handler) throws CollectionAbortedException {
		collectWidthWise(handler, start);
	}

	/**
	 * Width wise collection.
	 * 
	 * @param handler
	 *        the handler that processes each element.
	 * @param element
	 *        : the element from which the collection is performed.
	 * @throws CollectionAbortedException
	 */
	protected void collectWidthWise(ResultHandler<T> handler, T element) throws CollectionAbortedException {
		Iterable<T> currentLayer = Collections.singletonList(element);
		Iterable<T> nextLayer = Collections.emptyList();
		while(currentLayer != null && !Iterables.isEmpty(currentLayer)) {
			for(T currentElement : currentLayer) {
				try {
					handler.handleResult(currentElement);
					//Building the next layer.
					for(Picker<T> picker : this.getPickers()) {
						Iterable<T> nexts = picker.getNexts(currentElement); //getting children.
						if(nexts != null) {
							nextLayer = Iterables.concat(nextLayer, nexts);
						}
					}
				} catch (CannotHandleException e) {
					//do nothing
				} catch (Exception e) {
					throw new CollectionAbortedException(e);
				}
			}
			//The current layer has been processed. Going through the next one.
			currentLayer = nextLayer;
			nextLayer = Collections.emptyList();
		}
	}

}
