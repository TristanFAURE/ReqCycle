package org.eclipse.reqcycle.traceability.storage.blueprints.storage;

import static com.google.common.collect.Iterables.transform;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.reqcycle.traceability.engine.ITraceabilityEngine.DIRECTION;
import org.eclipse.reqcycle.traceability.model.Link;
import org.eclipse.reqcycle.traceability.model.Pair;
import org.eclipse.reqcycle.traceability.model.TType;
import org.eclipse.reqcycle.traceability.storage.IStorageProvider;
import org.eclipse.reqcycle.traceability.storage.ITraceabilityStorage;
import org.eclipse.reqcycle.traceability.storage.ITraceabilityStorageTopics;
import org.eclipse.reqcycle.traceability.storage.blueprints.graph.IBusinessOperationProvider;
import org.eclipse.reqcycle.uri.IReachableCreator;
import org.eclipse.reqcycle.uri.model.Reachable;
import org.eclipse.ziggurat.inject.ZigguratInject;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.TransactionalGraph;
import com.tinkerpop.blueprints.Vertex;

public class GraphStorage implements ITraceabilityStorage {

	@Inject
	IReachableCreator creator;
	@Inject
	IStorageProvider provider;

	private Graph graph;

	private IBusinessOperationProvider.IBusinessOperations graphUtils;
	private String path;

	public GraphStorage(Graph graph, String path) {
		this(graph, path, null);
	}

	public GraphStorage(Graph graph, String path,
			IBusinessOperationProvider.IBusinessOperations operations) {
		this.graph = graph;
		this.path = path;
		if (operations == null) {
			this.graphUtils = new GraphUtils();
			ZigguratInject.inject(this.graphUtils);
		} else {
			this.graphUtils = operations;
		}
	}

	@Override
	public void save() {
		if (graph instanceof TransactionalGraph) {
			provider.notifyChanged(ITraceabilityStorageTopics.SAVE, this);
			TransactionalGraph tGraph = (TransactionalGraph) graph;
			tGraph.commit();
		}
	}

	@Override
	public void addOrUpdateUpwardRelationShip(TType kind, Reachable tracea,
			Reachable container, Reachable source, Reachable... targets) {
		// FIXME improve the multi target
		// in general cases only one target is provided.
		// currently several traceability links are created and the first one is
		// returned
		// in the future, create one traceability link for several targets
		for (Reachable target : targets) {
			Vertex traceabilityVertex = graph.getVertex(tracea);
			if (traceabilityVertex != null) {
				Reachable sourceT = getReachable(graphUtils
						.getSourceFromTraceabilityVertex(traceabilityVertex));
				Reachable targetT = getReachable(graphUtils
						.getTargetFromTraceabilityVertex(traceabilityVertex));
				Reachable containerT = container;
				TType kindT = graphUtils.getKind(traceabilityVertex);
				if (sourceT != null && targetT != null && containerT != null
						&& kindT != null) {
					// in this case it is an update of the traceability link the
					// kind properties are kept
					graphUtils.removeUpwardRelationShip(graph, kindT,
							containerT, sourceT, targetT);
				}
			}
			Vertex vertex = graphUtils.addTraceabilityRelation(graph, tracea,
					source, target, kind);
			graphUtils.addChildrenRelation(graph,
					graphUtils.getVertex(graph, container), vertex);
		}
		provider.notifyChanged(ITraceabilityStorageTopics.NEW, tracea);
	}

	private Reachable getReachable(Vertex vertex) {
		if (vertex == null) {
			return null;
		}
		return getReachable((String) vertex.getId());
	}

	@Override
	public Reachable getReachable(String uri) {
		Vertex v = graphUtils.getVertex(graph, uri);
		if (v != null) {
			Reachable r;
			try {
				r = creator.getReachable(new URI(uri));
				Map<String, String> properties = graphUtils.getProperties(v);
				for (String s : properties.keySet()) {
					r.put(s, (String) properties.get(s));
				}
				return r;
			} catch (URISyntaxException e) {
			}
		}
		return null;
	}

	@Override
	public Iterable<Pair<Link, Reachable>> getTraceability(Reachable r,
			DIRECTION direction) {
		Direction graphDirection = null;
		if (direction == DIRECTION.DOWNWARD) {
			graphDirection = Direction.IN;
		} else {
			graphDirection = Direction.OUT;
		}
		Vertex v = graph.getVertex(r.toString());
		if (v == null) {
			return Lists.newArrayList();
		}
		Iterable<Vertex> tracVertexes = graphUtils.getTraceability(v,
				graphDirection);
		List<Pair<Link, Reachable>> result = new LinkedList<Pair<Link, Reachable>>();
		for (Vertex trac : tracVertexes) {
			Vertex target = graphUtils.getTraceabilityTarget(trac,
					graphDirection);
			if (target != null) {
				Reachable rTarget = getReachable((String) target.getId());
				Link link = new Link(getReachable((String) trac.getId()),
						graphUtils.getKind(trac), r, rTarget);
				result.add(new Pair<Link, Reachable>(link, rTarget));
			}
		}
		return result;
	}

	@Override
	public void dispose() {
		provider.notifyChanged(ITraceabilityStorageTopics.DISPOSE, this);
		graph.shutdown();
	}

	@Override
	public void startTransaction() {
	}

	@Override
	public void commit() {
		if (graph instanceof TransactionalGraph) {
			provider.notifyChanged(ITraceabilityStorageTopics.COMMIT, this);
			TransactionalGraph tGraph = (TransactionalGraph) graph;
			tGraph.commit();
		}
	}

	@Override
	public void rollback() {
		if (graph instanceof TransactionalGraph) {
			TransactionalGraph tGraph = (TransactionalGraph) graph;
			tGraph.rollback();
		}

	}

	@Override
	public Iterable<Reachable> getTraceabilityLinksContainedIn(
			Reachable reachable) {
		Iterable<Vertex> traceabilities = graphUtils.getTraceabilityIn(graph,
				reachable);
		return transform(traceabilities, new Function<Vertex, Reachable>() {

			@Override
			public Reachable apply(Vertex arg0) {
				return getReachable((String) arg0.getId());
			}
		});
	}

	public void removeTraceabilityLinksContainedIn(Reachable reachable) {
		Iterable<Vertex> traceabilities = graphUtils.getTraceabilityIn(graph,
				reachable);
		for (Vertex v : traceabilities) {
			deleteTraceability(reachable, v);
		}

	}

	private void deleteTraceability(Reachable reachable, Vertex v) {
		graphUtils.delete(v);
		provider.notifyChanged(ITraceabilityStorageTopics.REMOVE, reachable);
	}

	@Override
	public Iterable<Pair<Link, Reachable>> getAllTraceability(
			final DIRECTION direction) {
		Iterable<Vertex> allVertices = graphUtils
				.getAllTraceabilityVertices(graph);
		return Lists.newArrayList(transform(allVertices,
				new Function<Vertex, Pair<Link, Reachable>>() {

					@Override
					public Pair<Link, Reachable> apply(Vertex arg0) {
						Vertex source = graphUtils
								.getSourceFromTraceabilityVertex(arg0);
						Vertex target = graphUtils
								.getTargetFromTraceabilityVertex(arg0);
						if (direction == DIRECTION.DOWNWARD) {
							Vertex sourceTmp = source;
							source = target;
							target = sourceTmp;
						}
						Link link = new Link(
								getReachable((String) arg0.getId()), graphUtils
										.getKind(arg0),
								getReachable((String) source.getId()),
								getReachable((String) target.getId()));
						return new Pair<Link, Reachable>(link, link
								.getTargets().iterator().next());
					}
				}));

	}

	@Override
	public void removeUpwardRelationShip(TType kind, Reachable container,
			Reachable source, Reachable... targets) {
		for (Reachable t : targets) {
			graphUtils.removeUpwardRelationShip(graph, kind, container, source,
					t);
		}
	}

	@Override
	public void updateRelationShip(Link oldLink, Link newLink,
			DIRECTION direction) {
		Reachable source = oldLink.getSources().iterator().next();
		Reachable target = oldLink.getTargets().iterator().next();
		Vertex vSource = graphUtils.getVertex(graph, source);
		if (vSource == null) {
			return;
		}
		Iterable<Vertex> traceability = graphUtils.getTraceability(vSource,
				Direction.OUT);
		for (Vertex vTrac : traceability) {
			Vertex vTarget = graphUtils.getTraceabilityTarget(vTrac,
					Direction.OUT);
			if (vTarget != null) {
				if (getReachable((String) vTarget.getId()).equals(target)) {
					// target is the same check for kind
					TType aKind = graphUtils.getTType(vTrac);
					if (aKind != null && aKind.equals(oldLink.getKind())) {
						handleTraceability(oldLink, source, target, newLink,
								vSource, vTarget, vTrac, direction);
						provider.notifyChanged(ITraceabilityStorageTopics.UPDATE,
								newLink);
						break;
					}
				}
			}
		}
	}

	protected void handleTraceability(Link oldLink, Reachable oldSource,
			Reachable oldTarget, Link newLink, Vertex vSource, Vertex vTarget,
			Vertex vTrac, DIRECTION direction) {
		Reachable newSource = newLink.getSources().iterator().next();
		Reachable newTarget = newLink.getTargets().iterator().next();
		if (!newSource.equals(oldSource)) {
			if (direction == DIRECTION.DOWNWARD) {
				handleTargetChanged(oldTarget, newTarget, vTrac);
			} else {
				handleSourceChanged(oldSource, newSource, vTrac);
			}
		}
		if (!newTarget.equals(oldTarget)) {
			if (direction == DIRECTION.DOWNWARD) {
				handleTargetChanged(oldSource, newSource, vTrac);
			} else {
				handleSourceChanged(oldTarget, newTarget, vTrac);
			}
		}
		TType newType = newLink.getKind();
		if (!newType.equals(oldLink.getKind())) {
			handleKindChanged(oldLink.getKind(), newType, vTrac);
		}

	}

	protected void handleKindChanged(TType kind, TType newType, Vertex vTrac) {
		graphUtils.setKind(graph, newType, vTrac);
	}

	protected void handleTargetChanged(Reachable oldTarget,
			Reachable newTarget, Vertex vTrac) {
		graphUtils.setTarget(graph, newTarget, vTrac);
	}

	private void handleSourceChanged(Reachable oldSource, Reachable newSource,
			Vertex vTrac) {
		graphUtils.setSource(graph, newSource, vTrac);
	}

	@Override
	public String getProperty(Reachable reachable, String propertyName) {
		String id = reachable.toString();
		Vertex vertex = graphUtils.getVertex(graph, id);
		if (vertex != null) {
			Map<String, String> p = graphUtils.getProperties(vertex);
			if (p != null) {
				return p.get(propertyName);
			}
		}
		return null;
	}

	@Override
	public void removeTraceabilityLink(Reachable r) {
		Vertex vertex = graphUtils.getVertex(graph, r);
		deleteTraceability(r, vertex);
	}

	@Override
	public String getPath() {
		return path;
	}

	@Override
	public void removeProperty(Reachable reachable, String propertyName) {
		String id = reachable.toString();
		Vertex vertex = graphUtils.getVertex(graph, id);
		if (vertex != null) {
			graphUtils.removeProperty(graph, vertex, propertyName);
		}
	}

	@Override
	public void addUpdateProperty(Reachable reachable, String propertyName,
			String propertyValue) {
		String id = reachable.toString();
		Vertex vertex = graphUtils.getVertex(graph, id);
		if (vertex != null) {
			graphUtils.setProperty(graph, vertex, propertyName, propertyValue);
		}
	}

}
