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
package org.eclipse.ziggurat.collect;

import java.util.Arrays;
import java.util.Collections;

import org.eclipse.ziggurat.collect.collectors.DepthCollector;
import org.eclipse.ziggurat.collect.collectors.WidthCollector;
import org.eclipse.ziggurat.collect.yieldadapters.ThreadedYieldAdapter;

import com.google.common.collect.Iterables;

/**
 * Class that generates iterables, provided a bunch of pickers and a starting element. 
 * The iterable created can then be filtered using Guava. These iterables are strictly 
 * not collections, and should not be considered as such. Guava provides utilities that
 * should be used if you need to test the uniqueness of an element, for instance...
 */
public class IterableFactory {
	
	/**
	 * Creates a depth wise iterable : in the order given by the caller, the first element returned
	 * by {@link Picker#getNexts(Object)} call serves as the parameter for the next call before 
	 * the second element is processed.
	 */
	public static <T> Iterable <T> createIterable(T startingElement, Iterable<? extends Picker<T>> pickers){
		Collector<T> collector = new DepthCollector<T>(startingElement, pickers);
		YieldAdapter<T> yieldAdapter = new ThreadedYieldAdapter<T>();
		return yieldAdapter.adapt(collector);
	}
	
	/**
	 * Creates a depth wise iterable : in the order given by the caller, the first element returned
	 * by {@link Picker#getNexts(Object)} call serves as the parameter for the next call before 
	 * the second element is processed.
	 */
	public static <T> Iterable <T> createIterable(T startingElement, Picker<T>... pickers){
		Collector<T> collector = new DepthCollector<T>(startingElement, Arrays.asList(pickers));
		YieldAdapter<T> yieldAdapter = new ThreadedYieldAdapter<T>();
		return yieldAdapter.adapt(collector);
	}
	
	/**
	 * Creates a depth wise iterable that concats all the iterables created by the call of 
	 * createIterable on each of the starting elements.
	 */
	public static <T> Iterable <T> createIterable(Iterable<T> startingElements, Iterable<? extends Picker<T>> pickers){
		Iterable<T> result = Collections.emptyList();
		for(T t : startingElements){
			Iterable<T> tIterable = createIterable(t, pickers);
			result = Iterables.concat(result, tIterable);
		}
		return result;
	}
	
	/**
	 * Creates a depth wise iterable : in the order given by the caller, the first element returned
	 * by {@link Picker#getNexts(Object)} call serves as the parameter for the next call before 
	 * the second element is processed.
	 */
	public static <T> Iterable <T> createWidthWiseIterable(T startingElement, Iterable<? extends Picker<T>> pickers){
		Collector<T> collector = new WidthCollector<T>(startingElement, pickers);
		YieldAdapter<T> yieldAdapter = new ThreadedYieldAdapter<T>();
		return yieldAdapter.adapt(collector);
	}
	
	/**
	 * Creates a depth wise iterable : in the order given by the caller, the first element returned
	 * by {@link Picker#getNexts(Object)} call serves as the parameter for the next call before 
	 * the second element is processed.
	 */
	public static <T> Iterable <T> createWidthWiseIterable(T startingElement, Picker<T>... pickers){
		Collector<T> collector = new WidthCollector<T>(startingElement, Arrays.asList(pickers));
		YieldAdapter<T> yieldAdapter = new ThreadedYieldAdapter<T>();
		return yieldAdapter.adapt(collector);
	}
	
	
	/**
	 * Creates a width wise iterable that iterates over starting elements and then on the concatenation
	 * on the call of createWidthWiseIterable on each of these elements.
	 */
	public static <T> Iterable <T> createWidthWiseIterable(Iterable<T> startingElements, Iterable<? extends Picker<T>> pickers){
		Collector<T> collector = new WidthCollector<T>(startingElements, pickers);
		YieldAdapter<T> yieldAdapter = new ThreadedYieldAdapter<T>();
		return yieldAdapter.adapt(collector);
	}
	
}
