package org.eclipse.reqcycle.repository.data.impl;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.URIHandlerImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.ziggurat.configuration.IConfigurationManager;

@Singleton
public class ConfResourceSet extends ResourceSetImpl {

	static class ResourceFactory implements Resource.Factory {

		@Override
		public Resource createResource(URI uri) {
			XMIResourceImpl result = new XMIResourceImpl(uri) {

				@Override
				protected boolean useUUIDs() {
					return true;
				}

			};
			result.setEncoding("UTF-8");

			result.getDefaultSaveOptions().put(XMLResource.OPTION_USE_ENCODED_ATTRIBUTE_STYLE, Boolean.TRUE);
			result.getDefaultSaveOptions().put(XMLResource.OPTION_URI_HANDLER, new URIHandlerImpl.PlatformSchemeAware());
			return result;
		}
	}

	IConfigurationManager confManager;

	@Inject
	public ConfResourceSet(IConfigurationManager confManager) {

		this.confManager = confManager;
		getResourceFactoryRegistry().getExtensionToFactoryMap().put(confManager.getConfigurationResourceExtension(), new ResourceFactory());
		getResourceFactoryRegistry().getExtensionToFactoryMap().put("reqcycle", new ResourceFactory());
	}



}
