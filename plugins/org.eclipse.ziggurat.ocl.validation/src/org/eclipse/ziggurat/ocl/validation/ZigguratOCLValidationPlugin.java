package org.eclipse.ziggurat.ocl.validation;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class ZigguratOCLValidationPlugin implements BundleActivator {

	public static final String PLUGIN_ID = "org.eclipse.ziggurat.ocl.validation"; //$NON-NLS-1$
	
	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		ZigguratOCLValidationPlugin.context = bundleContext;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		ZigguratOCLValidationPlugin.context = null;
	}

	
}
