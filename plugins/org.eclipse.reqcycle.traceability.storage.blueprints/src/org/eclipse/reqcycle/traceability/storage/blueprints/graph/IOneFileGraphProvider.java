package org.eclipse.reqcycle.traceability.storage.blueprints.graph;

import java.io.InputStream;

import com.tinkerpop.blueprints.Graph;


public interface IOneFileGraphProvider extends IGraphProvider {

	Graph getGraph(InputStream inputStream);
}
