package org.eclipse.reqcycle.traceability.storage.blueprints.storage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.reqcycle.traceability.model.TType;
import org.eclipse.reqcycle.traceability.storage.blueprints.graph.IBusinessOperationProvider;
import org.eclipse.reqcycle.uri.model.Reachable;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.transform;

public class GraphUtils implements IBusinessOperationProvider.IBusinessOperations {
	private static final String KIND = "kind";
	// EDGES
	public static final String CHILDREN_EDGE = "children";
	public static final String TRACE_TARGET = "target";
	public static final String VERTEX_OUTGOING = "outgoing";

	public Vertex getVertex(Graph graph, Reachable reachable) {
		String id = reachable.toString();
		Vertex v = graph.getVertex(id);
		if (v == null) {
			v = graph.addVertex(id);
			for (String s : reachable.getProperties().keySet()) {
				String value = reachable.get(s);
				if (value != null) {
					v.setProperty(s, value);
				}
			}
		}
		return v;
	}

	public Vertex getVertex(Graph graph, String reachableUri) {
		Vertex v = graph.getVertex(reachableUri);
		return v;
	}

	public void addRelation(Graph graph, Vertex source, Vertex target,
			String relation) {
		graph.addEdge(null, source, target, relation);
	}

	public void addRelation(Graph graph, Reachable source, Reachable target,
			String relation) {
		addRelation(graph, getVertex(graph, source), getVertex(graph, target),
				relation);
	}

	public Vertex addTraceabilityRelation(Graph graph, Reachable tracea,
			Reachable source, Reachable target, TType relation) {
		Vertex vSource = getVertex(graph, source);
		Vertex vTarget = getVertex(graph, target);
		Vertex vTracea = null;
		if (tracea != null) {
			vTracea = getVertex(graph, tracea);
		} else {
			vTracea = graph.addVertex(null);
		}
		return addTraceabilityRelation(graph, relation, vTracea, vSource,
				vTarget);
	}

	private Vertex addTraceabilityRelation(Graph graph, TType relation,
			Vertex traceability, Vertex vSource, Vertex vTarget) {
		setKind(graph, relation, traceability);
		graph.addEdge(null, vSource, traceability, VERTEX_OUTGOING);
		graph.addEdge(null, traceability, vTarget, TRACE_TARGET);
		return traceability;
	}

	private String getLitteral(String relation) {
		return "\"" + relation + "\"";
	}

	public void addChildrenRelation(Graph graph, Vertex container,
			Vertex children) {
		addRelation(graph, container, children, CHILDREN_EDGE);
	}

	public Iterable<Vertex> getTraceability(Vertex v,
			final Direction graphDirection) {
		String label = graphDirection == Direction.IN ? TRACE_TARGET
				: VERTEX_OUTGOING;
		return Iterables.transform(v.getEdges(graphDirection, label),
				new Function<Edge, Vertex>() {
					public Vertex apply(Edge e) {
						return e.getVertex(invert(graphDirection));
					}

				});
	}

	private Direction invert(final Direction graphDirection) {
		return graphDirection == Direction.IN ? Direction.OUT : Direction.IN;
	}

	public Vertex getTraceabilityTarget(Vertex trac, Direction direction) {
		String label = direction == Direction.IN ? VERTEX_OUTGOING
				: TRACE_TARGET;
		return trac.getEdges(direction, label).iterator().next()
				.getVertex(invert(direction));
	}

	public TType getKind(Vertex trac) {
		return (TType) trac.getProperty(KIND);
	}

	public Iterable<Vertex> getTraceabilityIn(Graph graph, Reachable reachable) {
		List<Vertex> result = new LinkedList<Vertex>();
		Vertex vContainer = graph.getVertex(reachable.toString());
		if (vContainer != null) {
			for (Edge e : vContainer.getEdges(Direction.OUT, CHILDREN_EDGE)) {
				result.add(e.getVertex(Direction.IN));
			}
		}
		return result;
	}

	public void delete(Vertex v) {
		v.remove();
	}

	@Override
	public TType getTType(Vertex traceabilityvertex) {
		// TODO Auto-generated method stub
		return traceabilityvertex.getProperty(KIND);
	}

	@Override
	public Map<String, String> getProperties(Vertex v) {
		Map<String, String> map = new HashMap<String, String>();
		for (String s : v.getPropertyKeys()) {
			Object val = v.getProperty(s);
			if (val instanceof String) {
				String new_name = (String) val;
				map.put(s, new_name);
			}
		}
		return map;
	}

	@Override
	public Iterable<Vertex> getAllTraceabilityVertices(Graph graph) {
		Iterable<Edge> edges = graph.getEdges();
		Iterable<Edge> edgesFilterd = filter(edges, new Predicate<Edge>() {

			@Override
			public boolean apply(Edge arg0) {
				return TRACE_TARGET.equals(arg0.getLabel());
			}
		});
		return transform(edgesFilterd, new Function<Edge, Vertex>() {

			@Override
			public Vertex apply(Edge arg0) {
				return arg0.getVertex(Direction.IN);
			}
		});
	}

	@Override
	public Vertex getSourceFromTraceabilityVertex(Vertex arg0) {
		return arg0.getEdges(Direction.IN, VERTEX_OUTGOING).iterator().next()
				.getVertex(Direction.IN);
	}

	@Override
	public Vertex getTargetFromTraceabilityVertex(Vertex arg0) {
		return arg0.getEdges(Direction.OUT, TRACE_TARGET).iterator().next()
				.getVertex(Direction.OUT);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.reqcycle.traceability.storage.blueprints.graph.
	 * ISpecificGraphProvider
	 * .IBusinessOperations#removeUpwardRelationShip(com.tinkerpop
	 * .blueprints.Graph, org.eclipse.reqcycle.traceability.model.TType,
	 * org.eclipse.reqcycle.uri.model.Reachable,
	 * org.eclipse.reqcycle.uri.model.Reachable,
	 * org.eclipse.reqcycle.uri.model.Reachable)
	 */
	@Override
	public void removeUpwardRelationShip(Graph graph, TType kind,
			Reachable container, Reachable source, Reachable target) {
		removeUpwardRelationShip(graph, kind,
				container != null ? graph.getVertex(container.toString())
						: null, graph.getVertex(source.toString()),
				graph.getVertex(target.toString()));
	}

	public void removeUpwardRelationShip(Graph graph, TType kind,
			Vertex container, Vertex sourceVertex, Vertex target) {
		Set<Edge> toDelete = new HashSet<Edge>();
		if (sourceVertex != null) {
			for (Edge e : sourceVertex.getEdges(Direction.OUT, VERTEX_OUTGOING)) {
				Vertex traceVertex = e.getVertex(Direction.IN);
				for (Edge e2 : traceVertex
						.getEdges(Direction.OUT, TRACE_TARGET)) {
					if (e2.getVertex(Direction.OUT).equals(target)) {
						toDelete.add(e);
						toDelete.add(e2);
						if (container != null) {
							for (Edge e3 : traceVertex.getEdges(Direction.IN,
									CHILDREN_EDGE)) {
								toDelete.add(e3);
							}
						}
					}
				}
			}
		}
		for (Edge e : toDelete) {
			graph.removeEdge(e);
		}
	}

	@Override
	public void setTarget(Graph graph, Reachable newTarget,
			Vertex traceabilityVertex) {
		TType kind = getKind(traceabilityVertex);
		Vertex vSource = getSourceFromTraceabilityVertex(traceabilityVertex);
		Vertex vTarget = getTargetFromTraceabilityVertex(traceabilityVertex);
		removeUpwardRelationShip(graph, kind, null, vSource, vTarget);
		addTraceabilityRelation(graph, kind, traceabilityVertex, vSource,
				getVertex(graph, newTarget));
	}

	@Override
	public void setSource(Graph graph, Reachable newSource, Vertex vTrac) {
		TType kind = getKind(vTrac);
		Vertex vSource = getSourceFromTraceabilityVertex(vTrac);
		Vertex vTarget = getTargetFromTraceabilityVertex(vTrac);
		removeUpwardRelationShip(graph, kind, null, vSource, vTarget);
		addTraceabilityRelation(graph, kind, vTrac,
				getVertex(graph, newSource), vTarget);

	}

	@Override
	public void setKind(Graph graph, TType newType, Vertex vTrac) {
		vTrac.setProperty(KIND, newType);
	}

	@Override
	public void setProperty(Graph graph, Vertex vertex, String propertyName,
			String propertyValue) {
		vertex.setProperty(propertyName, propertyValue);
	}

	@Override
	public void removeProperty(Graph graph, Vertex vertex, String propertyName) {
		vertex.removeProperty(propertyName);
	}

}
