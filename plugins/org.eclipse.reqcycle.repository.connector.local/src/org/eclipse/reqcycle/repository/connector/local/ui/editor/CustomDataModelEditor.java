/*****************************************************************************
 * Copyright (c) 2013 AtoS.
 *
 *    
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Anass RADOUANI (AtoS) anass.radouani@atos.net - Initial API and implementation
 *
 *****************************************************************************/
package org.eclipse.reqcycle.repository.connector.local.ui.editor;

import java.util.EventObject;
import java.util.Iterator;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.common.ui.URIEditorInput;
import org.eclipse.emf.common.ui.ViewerPane;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain.EditingDomainProvider;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.ui.celleditor.AdapterFactoryTreeEditor;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.reqcycle.core.ILogger;
import org.eclipse.reqcycle.repository.connector.local.ui.editor.provider.CustomDataModelItemProviderAdapterFactory;
import org.eclipse.reqcycle.repository.connector.local.ui.editor.provider.RequirementSourceItemProviderAdapterFactory;
import org.eclipse.reqcycle.repository.data.IDataManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.views.properties.PropertySheetPage;
import org.eclipse.ziggurat.inject.ZigguratInject;

import RequirementSourceConf.RequirementSource;
import RequirementSourceData.presentation.RequirementSourceDataEditor;
import RequirementSourceData.presentation.RequirementSourceDataEditorPlugin;


/**
 * The Class RequirementSourceEditor.
 */
public class CustomDataModelEditor extends RequirementSourceDataEditor {

	//FIXME : Use manager or local connector to retrieve this ID
	public final static String LOCAL_CONNECTOR_ID = "org.eclipse.reqcycle.repository.connector.local.connectorCore";

	/** The Constant EDITOR ID. */
	public static final String EDITOR_ID = "RequirementSourceData.presentation.RequirementSourceDataEditorID";

	/** The input Object uri. */
	protected URI inputURI;

	@Inject
	@Named("confResourceSet")
	ResourceSet rs;

	/** The logger. */
	@Inject
	ILogger logger;

	@Inject
	IDataManager dataManager;

	EditingDomainProvider editingDomainAdapter;

	/**
	 * Open Requirement editor.
	 * 
	 * @param uri
	 *        the input uri
	 */
	public static void openEditor(URI uri) {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		try {
			URIEditorInput editorInput = new URIEditorInput(uri);
			IDE.openEditor(page, editorInput, EDITOR_ID);
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}


	public CustomDataModelEditor() {
		super();
	}

	/**
	 * Initialize editor input.
	 */
	protected void initializeEditorInput() {
		inputURI = null;
		IEditorInput editorInput = getEditorInput();
		if(editorInput instanceof URIEditorInput) {
			inputURI = ((URIEditorInput)editorInput).getURI();
		}
	}

	@Override
	protected void initializeEditingDomain() {
		ZigguratInject.inject(this);
		adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		adapterFactory.addAdapterFactory(new CustomDataModelItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new RequirementSourceItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());

		// Create the command stack that will notify this editor as commands are executed.
		//
		BasicCommandStack commandStack = new BasicCommandStack();

		// Add a listener to set the most recent command's affected objects to be the selection of the viewer with focus.
		//
		commandStack.addCommandStackListener(new CommandStackListener() {

			@Override
			public void commandStackChanged(final EventObject event) {
				getContainer().getDisplay().asyncExec(new Runnable() {

					@Override
					public void run() {
						firePropertyChange(IEditorPart.PROP_DIRTY);

						// Try to select the affected objects.
						//
						Command mostRecentCommand = ((CommandStack)event.getSource()).getMostRecentCommand();
						if(mostRecentCommand != null) {
							setSelectionToViewer(mostRecentCommand.getAffectedObjects());
						}
						for(Iterator<PropertySheetPage> i = propertySheetPages.iterator(); i.hasNext();) {
							PropertySheetPage propertySheetPage = i.next();
							if(propertySheetPage.getControl().isDisposed()) {
								i.remove();
							} else {
								propertySheetPage.refresh();
							}
						}
					}
				});
			}
		});

		// Create the editing domain with a special command stack initialized with ReqCycle ResourceSet.
		//

		editingDomain = new AdapterFactoryEditingDomain(adapterFactory, commandStack, rs);
		editingDomainAdapter = new AdapterFactoryEditingDomain.EditingDomainProvider(editingDomain);
		rs.eAdapters().add(editingDomainAdapter);
	}

	@Override
	public void createPages() {
		//Initialize the inputURI : the input object uri
		//
		initializeEditorInput();

		// Creates the model from the editor input
		//
		createModel();

		// Only creates the selection page
		//
		if(!getEditingDomain().getResourceSet().getResources().isEmpty()) {
			// Create a page for the selection tree view.
			//
			{
				ViewerPane viewerPane = new ViewerPane(getSite().getPage(), this) {

					@Override
					public Viewer createViewer(Composite composite) {
						Tree tree = new Tree(composite, SWT.None);
						TreeViewer newTreeViewer = new TreeViewer(tree);
						return newTreeViewer;
					}

					@Override
					public void requestActivation() {
						super.requestActivation();
						setCurrentViewerPane(this);
					}

					@Override
					protected void createTitleBar() {
						//initialize the title label and call the super method to let this title bar empty and not visible
						titleLabel = new CLabel(control, SWT.SHADOW_NONE);
						super.createTitleBar();
					}
				};
				viewerPane.createControl(getContainer());

				selectionViewer = (TreeViewer)viewerPane.getViewer();
				selectionViewer.setContentProvider(new AdapterFactoryContentProvider(adapterFactory));

				selectionViewer.setLabelProvider(new AdapterFactoryLabelProvider(adapterFactory));
				selectionViewer.setInput(getInput());
				//Filter other requirement sources if there is any
				//				selectionViewer.setFilters(createViewerFilters());

				new AdapterFactoryTreeEditor(selectionViewer.getTree(), adapterFactory);

				createContextMenuFor(selectionViewer);
				int pageIndex = addPage(viewerPane.getControl());
				setPageText(pageIndex, getString("_UI_SelectionPage_label"));
			}

			getSite().getShell().getDisplay().asyncExec(new Runnable() {

				@Override
				public void run() {
					setActivePage(0);
				}
			});
		}

		// Ensures that this editor will only display the page's tab
		// area if there are more than one page
		//
		getContainer().addControlListener(new ControlAdapter() {

			boolean guard = false;

			@Override
			public void controlResized(ControlEvent event) {
				if(!guard) {
					guard = true;
					hideTabs();
					guard = false;
				}
			}
		});

		getSite().getShell().getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				updateProblemIndication();
			}
		});
	}


	/**
	 * Creates the viewer filters.
	 * 
	 * @return the viewer filter[]
	 */
	protected ViewerFilter[] createViewerFilters() {

		ViewerFilter viewerFilter = new ViewerFilter() {

			@Override
			public boolean select(Viewer viewer, Object parentElement, Object element) {
				if(element instanceof RequirementSource) {
					if(LOCAL_CONNECTOR_ID.equals(((RequirementSource)element).getConnectorId())) {
						if(inputURI != null && inputURI.hasFragment()) {
							//show the corresponding requirement source
							return inputURI.fragment().equals(((RequirementSource)element).eResource().getURIFragment((RequirementSource)element));
						}
						//if there isn't a fragment in the uri -> show all local requirement sources
						return true;
					}
					//If the requirement source is not a local one -> filter
					return false;
				}
				// FIXME : use supported element list to filter unsupported ones 
				// element is not a requirement source -> Show
				return true;
			}
		};

		return new ViewerFilter[]{ viewerFilter };
	}

	/**
	 * Gets the editor input.
	 * 
	 * @return the editor input
	 */
	protected Object getInput() {
		//FIXME : return the Object corresponding to the fragment
		if(inputURI != null) {
			return editingDomain.getResourceSet().getResource(inputURI.trimFragment(), true);
		}
		return editingDomain.getResourceSet().getResources();
	}

	/**
	 * Looks up a string in the plugin's plugin.properties file..
	 * 
	 * @param key
	 *        the String key
	 * @return the corresponding string
	 */
	protected String getString(String key) {
		return RequirementSourceDataEditorPlugin.INSTANCE.getString(key);
	}

	@Override
	protected void setPartName(String partName) {
		super.setPartName("Requirements Editor");
	}

	@Override
	public void dispose() {
		rs.eAdapters().remove(editingDomainAdapter);
		super.dispose();
	}

	@Override
	public Diagnostic analyzeResourceProblems(Resource resource, Exception exception) {
		if(inputURI.trimFragment().equals(resource.getURI())) {
			return super.analyzeResourceProblems(resource, exception);
		}
		return Diagnostic.OK_INSTANCE;
	}

}
