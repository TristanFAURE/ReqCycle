package org.eclipse.reqcycle.uri.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.reqcycle.uri.Activator;
import org.eclipse.reqcycle.uri.IReachableManager;
import org.eclipse.reqcycle.uri.exceptions.IReachableHandlerException;
import org.eclipse.reqcycle.uri.model.IHandler;
import org.eclipse.reqcycle.uri.model.IObjectHandler;
import org.eclipse.reqcycle.uri.model.IReachableHandler;
import org.eclipse.reqcycle.uri.model.Reachable;
import org.eclipse.ziggurat.inject.ZigguratInject;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.collect.Iterables;

@Singleton
public class ReachableManager implements IReachableManager {
	private static final CacheBuilder<Object, Object> BUILDER = CacheBuilder
			.newBuilder().weakKeys().expireAfterAccess(15, TimeUnit.MINUTES);
	private static String EXT_POINT_HANDLER = "reachableHandler";
	private static String OBJECT_HANDLER = "objectHandler";
	private static String REACH_HANDLER = EXT_POINT_HANDLER;
	private Map<IHandler, Integer> priorities = new HashMap<IHandler, Integer>();

	private static final Pattern numberPattern = Pattern.compile("\\d*");

	private List<IHandler> handlers = new ArrayList<IHandler>();
	IExtensionRegistry registry = Platform.getExtensionRegistry();
	Cache<Object, IObjectHandler> cacheObjects = BUILDER
			.build();
	Cache<Reachable, IReachableHandler> cacheReachables = BUILDER
			.build();

	@PostConstruct
	public void postConstruct() {
		for (IConfigurationElement i : registry.getConfigurationElementsFor(
				Activator.PLUGIN_ID, EXT_POINT_HANDLER)) {
			try {
				IHandler toAdd = null;
				if (OBJECT_HANDLER.equals(i.getName())) {
					toAdd = (IObjectHandler) i
							.createExecutableExtension("handler");
				} else if (REACH_HANDLER.equals(i.getName())) {
					toAdd = (IReachableHandler) i
							.createExecutableExtension("handler");

				}
				if (toAdd != null) {
					ZigguratInject.inject(toAdd);
					String prio = i.getAttribute("priority");
					Integer val = 0;
					if (prio != null && numberPattern.matcher(prio).matches()) {
						val = Integer.valueOf(prio);
					}
					priorities.put(toAdd, val);
					handlers.add(toAdd);
				}
			} catch (CoreException e) {
				// TODO error management
				e.printStackTrace();
			}
		}
		Collections.sort(handlers, new Comparator<IHandler>() {

			@Override
			public int compare(IHandler o1, IHandler o2) {
				Integer pval1 = priorities.get(o1);
				Integer pval2 = priorities.get(o2);
				Integer p1 = pval1 != null ? pval1 : 0;
				Integer p2 = pval2 != null ? pval2 : 0;
				return -(p1.compareTo(p2));
			}
		});
	}

	@Override
	public IReachableHandler getHandlerFromReachable(final Reachable t)
			throws IReachableHandlerException {
		IReachableHandler get;
		try {
			get = cacheReachables.get(t,new Callable<IReachableHandler>() {

				@Override
				public IReachableHandler call() throws Exception {
					
					for (IReachableHandler h : Iterables.filter(handlers,
							IReachableHandler.class)) {
						if (h.handlesReachable(t)) {
							return h;
						}
					}
					throw new Exception();
					
					
				}
			});
		} catch (ExecutionException e) {
			e.printStackTrace();
			throw new IReachableHandlerException();
		}
		if (get == null) {
			throw new IReachableHandlerException();
		}
		return get;
	}

	@Override
	public IObjectHandler getHandlerFromObject(final Object o)
			throws IReachableHandlerException {
		if (o == null) {
			throw new IReachableHandlerException();
		}
		IObjectHandler get;
		try {
			get = cacheObjects.get(o, new Callable<IObjectHandler>() {

				@Override
				public IObjectHandler call() throws Exception {
					for (IObjectHandler h : Iterables.filter(handlers,
						IObjectHandler.class)) {
					if (h.handlesObject(o)) {
						return h;
					}
				}
				throw new Exception();
				}
			});
		} catch (ExecutionException e) {
			throw new IReachableHandlerException();
		}
		if (get == null) {
			throw new IReachableHandlerException();
		}
		return get;
	}
}
