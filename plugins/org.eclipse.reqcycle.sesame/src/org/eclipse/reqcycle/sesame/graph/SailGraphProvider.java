package org.eclipse.reqcycle.sesame.graph;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import org.eclipse.reqcycle.traceability.storage.blueprints.graph.IBusinessOperationProvider;
import org.eclipse.reqcycle.traceability.storage.blueprints.graph.IOneFileGraphProvider;
import org.eclipse.ziggurat.inject.ZigguratInject;
import org.openrdf.model.Resource;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;
import org.openrdf.rio.RDFWriter;
import org.openrdf.rio.Rio;
import org.openrdf.sail.Sail;
import org.openrdf.sail.SailException;
import org.openrdf.sail.memory.MemoryStore;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.impls.sail.SailGraph;

public class SailGraphProvider implements IOneFileGraphProvider, IBusinessOperationProvider {

	@Override
	public Graph getGraph(String path) {
		// NativeStoreSailGraph nativeStoreSailGraph = new NativeStoreSailGraph(
		// path + "/sail/");
		// Sail sail = nativeStoreSailGraph.getRawGraph();
		// return nativeStoreSailGraph;
		return getGraph(path, RDFFormat.RDFXML);
	}
	

	@Override
	public Graph getGraph(InputStream inputStream) {
		MemoryStore store = new MemoryStore();
		store.setPersist(false);
		store.setSyncDelay(1000L);
		CustomSailGraph customSailGraph = new CustomSailGraph(inputStream, RDFFormat.RDFXML,
				store);
		return customSailGraph;
	}

	public Graph getGraph(String path, RDFFormat format) {
		if (!path.endsWith(format.getDefaultFileExtension())) {
			path = path.concat("." + format.getDefaultFileExtension());
		}
		MemoryStore store = new MemoryStore();
		store.setPersist(false);
		store.setSyncDelay(1000L);
		CustomSailGraph customSailGraph = new CustomSailGraph(path, format,
				store);
		return customSailGraph;
	}

	private static class CustomSailGraph extends SailGraph {

		private File file;
		private RDFFormat format;

		public CustomSailGraph(String path, RDFFormat format, Sail sail) {
			super(new MemoryStore());
			this.format = format;
			file = new File(path);
			if (file.exists()) {
				SailRepository repo = new SailRepository(getRawGraph());
				try {
					repo.getConnection().add(file, null, format,
							new Resource[] {});
				} catch (RDFParseException e) {
				} catch (RepositoryException e) {
				} catch (IOException e) {
				}
			}
		}

		public CustomSailGraph(InputStream inputStream, RDFFormat format, MemoryStore store) {
			super(new MemoryStore());
			this.format = format;
			SailRepository repo = new SailRepository(getRawGraph());
			try {
				repo.getConnection().add(inputStream, "", format, new Resource[]{});
			} catch (RDFParseException e) {
			} catch (RepositoryException e) {
			} catch (IOException e) {
			}
		}

		@Override
		public synchronized void shutdown() {
			// TODO very ugly
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						getRawGraph().getConnection().close();
					} catch (SailException e) {
						e.printStackTrace();
					}
					CustomSailGraph.super.shutdown();
				}
			}).start();
		}

		@Override
		public void commit() {
			super.commit();
			SailRepository repo = new SailRepository(getRawGraph());
			try {
				if (getEdges().iterator().hasNext()) {
					RDFWriter writer = Rio.createWriter(
							format,
							new OutputStreamWriter(new BufferedOutputStream(
									new FileOutputStream(file)), Charset
									.forName("UTF-8")));
					repo.getConnection().export(writer, new Resource[] {});
				}
			} catch (RepositoryException e) {
				e.printStackTrace();
			} catch (RDFHandlerException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	// public static void main(String[] args) {
	// CustomSailGraph graph = new CustomSailGraph("d:/test.ttl");
	// Vertex g1 = graph.addVertex("http://test1");
	// Vertex g2 = graph.addVertex("http://test2");
	// Vertex g3 = graph.addVertex("http://test2");
	// // g1.setProperty("test", "value");
	// g1.addEdge("http://test", g2);
	// graph.commit();
	// graph.shutdown();
	// CustomSailGraph graph2 = new CustomSailGraph("d:/test.ttl");
	// System.out.println(Lists.newArrayList(graph2.getEdges()));
	// }

	@Override
	public IBusinessOperations getBusinessOperation() {
		SailBusinessOperations sailBusinessOperations = new SailBusinessOperations();
		ZigguratInject.inject(sailBusinessOperations);
		return sailBusinessOperations;
	}

}
