package org.eclipse.reqcycle.traceability.ui.providers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import javax.inject.Inject;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.deferred.DeferredContentProvider;
import org.eclipse.reqcycle.traceability.builder.IBuildingTraceabilityEngine;
import org.eclipse.reqcycle.traceability.engine.ITraceabilityEngine;
import org.eclipse.reqcycle.traceability.engine.Request;
import org.eclipse.reqcycle.traceability.engine.Request.Couple;
import org.eclipse.reqcycle.traceability.engine.Request.DEPTH;
import org.eclipse.reqcycle.traceability.exceptions.EngineException;
import org.eclipse.reqcycle.traceability.model.Link;
import org.eclipse.reqcycle.traceability.model.Pair;
import org.eclipse.reqcycle.traceability.types.ITypesConfigurationProvider;
import org.eclipse.reqcycle.traceability.types.configuration.typeconfiguration.Configuration;
import org.eclipse.reqcycle.traceability.types.engine.ITypedTraceabilityEngine;
import org.eclipse.reqcycle.traceability.utils.EngineUtils;
import org.eclipse.reqcycle.uri.IReachableListener;
import org.eclipse.reqcycle.uri.IReachableListenerManager;
import org.eclipse.reqcycle.uri.model.Reachable;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.internal.progress.ProgressMessages;
import org.eclipse.ui.progress.DeferredTreeContentManager;
import org.eclipse.ui.progress.WorkbenchJob;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

public class RequestContentProvider extends DeferredContentProvider implements
		ITreeContentProvider, IReachableListener {

	public static final String CONF_KEY = "conf";
	public static final String EXPAND_ALL = "expandAll";

	@Inject
	ITraceabilityEngine defaultEngine;
	@Inject
	ITypedTraceabilityEngine typedEngine;
	@Inject
	IReachableListenerManager listenerManger;
	@Inject
	ITypesConfigurationProvider typeProvider;

	Multimap<Reachable, Link> links = ArrayListMultimap.create();
	private Collection<Request> requests = new LinkedList<Request>();
	private TreeViewer treeViewer;
	private Object newInput;
	private Request baseRequest = null;
	private DeferredTreeContentManager contentManager;
	// map saving all the parents already asked
	private Set<Object> allParents = new HashSet<Object>();

	public RequestContentProvider() {
		super(new Comparator<Object>() {
			@Override
			public int compare(Object arg0, Object arg1) {
				return arg0.toString().compareTo(arg1.toString());
			}
		});
	}

	@Override
	public void dispose() {
		listenerManger.removeReachableListener(this);
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		this.treeViewer = (TreeViewer) viewer;
		this.newInput = newInput;
		listenerManger.removeReachableListener(this);
		allParents.clear();
		links.clear();
		requests.clear();
		contentManager = new DeferredTreeContentManager(
				(AbstractTreeViewer) viewer) {
			@Override
			protected void addChildren(final Object parent,
					final Object[] children, IProgressMonitor monitor) {
				WorkbenchJob updateJob = new WorkbenchJob(
						ProgressMessages.DeferredTreeContentManager_AddingChildren) {
					/*
					 * (non-Javadoc)
					 * 
					 * @see
					 * org.eclipse.ui.progress.UIJob#runInUIThread(org.eclipse
					 * .core.runtime.IProgressMonitor)
					 */
					public IStatus runInUIThread(IProgressMonitor updateMonitor) {
						// Cancel the job if the tree viewer got closed
						if (treeViewer.getControl().isDisposed()
								|| updateMonitor.isCanceled()) {
							return Status.CANCEL_STATUS;
						}
						((AbstractTreeViewer) treeViewer).add(parent, children);
						if (isSync()) {
							for (Object o : children) {
								if (o instanceof BusinessDeffered) {
									BusinessDeffered bd = (BusinessDeffered) o;
									treeViewer.expandToLevel(bd,
											bd.getLevel() + 1);
								}
							}
						}
						return Status.OK_STATUS;
					}

				};
				updateJob.setSystem(true);
				updateJob.schedule();

			}

		};
	}

	@Override
	public Object[] getElements(Object inputElement) {
		if (newInput instanceof Request) {
			links.clear();
			requests.clear();
			baseRequest = (Request) newInput;
			requests.add(baseRequest);
		} else if (newInput instanceof Collection) {
			links.clear();
			requests.clear();
			Collection<? extends Request> newInput2 = (Collection<? extends Request>) newInput;
			if (!newInput2.isEmpty()) {
				requests.addAll(newInput2);
				baseRequest = requests.iterator().next();
			}
		}
		Collection<Object> result = new LinkedList<Object>();
		for (Request r : requests) {
			for (Couple c : r.getCouples()) {
				Reachable source = c.getSource();
				if (source != null) {
					listenerManger.addReachableListener(source, this);
					listenerManger.addReachableListener(source.trimFragment(),
							this);
					result.add(new BusinessDeffered(source, this));
				}
			}
		}
		return result.toArray();
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		return contentManager.getChildren(parentElement);
	}

	private Iterator<Pair<Link, Reachable>> getTraceability(Request r)
			throws EngineException {
		Object conf = r.getProperty(CONF_KEY);
		Configuration defaultConfiguration = typeProvider
				.getDefaultConfiguration();
		if (defaultConfiguration != null && Boolean.TRUE.equals(conf)) {
			return typedEngine.getTraceability(defaultConfiguration, r);
		} else {
			return defaultEngine.getTraceability(r);
		}
	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		return contentManager.mayHaveChildren(element);
	}

	@Override
	public void hasChanged(final Reachable reachable) {
		Display.getDefault().syncExec(new Runnable() {

			@Override
			public void run() {
				// to avoid infinite reflexive call
				if (treeViewer != null) {
					Object[] objects = treeViewer.getExpandedElements();
					treeViewer.refresh();
					treeViewer.setExpandedElements(objects);
				}
			}
		});
	}

	public Collection<Object> doGetChildren(Object parentElement) {
		if (!allParents.contains(parentElement)) {
			allParents.add(parentElement);
		} else {
			if (isSync()) {
				return Collections.emptyList();
			}
		}
		Collection<Object> result = new LinkedList<Object>();
		if (parentElement instanceof Reachable) {
			Reachable reachable = (Reachable) parentElement;
			Collection<Link> c = links.get(reachable);
			if (c.isEmpty()) {
				if (baseRequest.getDepth() == DEPTH.INFINITE) {
					// an infinite request is computed one time
					if (!Boolean.TRUE.equals(baseRequest
							.getProperty("COMPUTED"))) {
						try {
							Iterator<Pair<Link, Reachable>> traceIterator = getTraceability(baseRequest);
							links.putAll(EngineUtils
									.toFollowingMap(traceIterator));
							c = links.get(reachable);
							baseRequest.addProperty("COMPUTED", true);
						} catch (EngineException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				} else {
					// for DEPTH ONE request, a request is created each time
					Request r = new Request()
							.addProperty(
									IBuildingTraceabilityEngine.OPTION_CHECK_CACHE,
									true)

							.setDepth(DEPTH.ONE)
							.setDirection(baseRequest.getDirection())
							.setScope(baseRequest.getScope())
							.addProperty(CONF_KEY,
									baseRequest.getProperty(CONF_KEY));
					ArrayList<Couple> listOfCouples = Lists
							.newArrayList(baseRequest.getCouples());
					for (Couple cTmp : listOfCouples) {
						r.addSourceAndCondition(reachable,
								cTmp.getStopCondition());
						if (cTmp.getStopCondition() != null) {
							r.setDepth(DEPTH.INFINITE);
						}
					}
					try {
						Iterator<Pair<Link, Reachable>> traceIterator = getTraceability(r);
						links.putAll(EngineUtils.toFollowingMap(traceIterator));
						c = links.get(reachable);
					} catch (EngineException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			result.addAll(c);
		} else if (parentElement instanceof Link) {
			// if (request.getDirection() == DIRECTION.UPWARD) {
			Set<Reachable> targets = ((Link) parentElement).getTargets();
			for (Reachable r : targets) {
				listenerManger.addReachableListener(r, this);
				listenerManger.addReachableListener(r.trimFragment(), this);
				result.add(r);
			}
			// } else {
			// result.addAll(((Link) parentElement).getSources());
			// }
		}
		return result;
	}

	private boolean isSync() {
		return String.valueOf(true).equals(treeViewer.getData(EXPAND_ALL));
	}
}
