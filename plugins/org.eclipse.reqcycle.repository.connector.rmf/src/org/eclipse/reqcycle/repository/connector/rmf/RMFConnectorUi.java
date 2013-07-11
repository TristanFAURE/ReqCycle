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

package org.eclipse.reqcycle.repository.connector.rmf;



public class RMFConnectorUi { // implements IConnectorWizard {

//	/** ReqIf file uri */
//	private String reqIfFile;
//
//	private RMFSettingPage selectDocumentsPage;
//
//	private RMFRepositoryMappingPage rmfMappingPage;
//	
//	@Inject ILogger logger = ZigguratInject.make(ILogger.class);
//	
//	private Scope scope;
//
//	private String repositoryLabel;
//
//	private SettingWizard settingWizard;
//
//	private boolean skipMapping;
//	
//	public static ITreeContentProvider contentProvider =  new ITreeContentProvider() {
//		
//		@Override
//		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
//		}
//		
//		@Override
//		public void dispose() {
//		}
//		
//		@Override
//		public boolean hasChildren(Object element) {
//			return false;
//		}
//		
//		@Override
//		public Object getParent(Object element) {
//			return null;
//		}
//		
//		@Override
//		public Object[] getElements(Object inputElement) {
//			return ArrayContentProvider.getInstance().getElements(inputElement);
//		}
//		
//		@Override
//		public Object[] getChildren(Object parentElement) {
//			return null;
//		}
//	};
//	
//	final static LabelProvider targetLabelProvider = new LabelProvider() {
//		@Override
//		public String getText(Object element) {
//			if(element instanceof EClass) {
//				return ((EClass)element).getName();
//			}
//			return super.getText(element);
//		}
//	};
//
//	@Override
//	public IRequirementSourceSettingPage getNextPage(IRequirementSourceSettingPage page) {
//
//		if(page == null) {
//			rmfMappingPage = null;
//			selectDocumentsPage = new RMFSettingPage("ReqIF Connector", "");
//			return selectDocumentsPage;
//		}
//
//		if(page instanceof RMFSettingPage) {
//
//			ResourceSet resourceSet = new ResourceSetImpl();
//
//			repositoryLabel = ((RMFSettingPage) page).getRequirementSourceName();
//			
//			reqIfFile = ((RMFSettingPage)page).getSourceUrl();
//			EList<SpecType> specTypes = getReqIFTypes(resourceSet, reqIfFile);
//			
//			scope = ((RMFSettingPage)page).getScope();
//
//			Collection<EClassifier> targetEClassifiers = DataUtil.getTargetEPackage(resourceSet, "org.eclipse.reqcycle.repository.data/model/CustomDataModel.ecore");
//
//			rmfMappingPage = getMappingPage(specTypes, targetEClassifiers, null);
//
//			return rmfMappingPage;
//		}
//		return null;
//	}
//
//
//	/**
//	 * @param specTypes
//	 * @param targetEClassifiers
//	 * @param targetLabelProvider
//	 * @return
//	 */
//	protected static RMFRepositoryMappingPage getMappingPage(final EList<SpecType> specTypes, final Collection<EClassifier> targetEClassifiers, final Collection<EObject> mapping) {
//		RMFRepositoryMappingPage mappingPage = new RMFRepositoryMappingPage("ReqIF Mapping Page", ""){
//
//			@Override
//			protected String getSourceDetail() {
//				return "ReqIF";
//			}
//
//			@Override
//			protected String getTargetDetail() {
//				return "Data Type";
//			}
//
//			@Override
//			protected IBaseLabelProvider getSourceLabelProvider() {
//				return new LabelProvider() {
//					@Override
//					public String getText(Object element) {
//						if(element instanceof SpecType) {
//							return ((SpecType)element).getLongName();
//						}
//						return super.getText(element);
//					}
//				};
//			}
//
//			@Override
//			protected IContentProvider getSourceContentProvider() {
//				return RMFConnectorUi.contentProvider;
//			}
//
//			@Override
//			protected Object getSourceInput() {
//				return specTypes;
//			}
//
//			@Override
//			protected Object getTargetInput() {
//				// TODO Auto-generated method stub
//				return targetEClassifiers;
//			}
//
//			@Override
//			protected String getResultDetail() {
//				// TODO Auto-generated method stub
//				return null;
//			}
//
//			@Override
//			protected Collection<EObject> addToMapping() {
//				return mapping;
//			}
//			
//		};
//		
//		return mappingPage;
//	}
//
//	//TODO : move to an rmf util class
//	public static EList<SpecType> getReqIFTypes(ResourceSet resourceSet, String fileLocation) {
//		URI uriReqIf = URI.createURI(fileLocation, false);
//		Resource reqIfResource = resourceSet.getResource(uriReqIf, true);
//		EList<EObject> contents = reqIfResource.getContents();
//		if(contents.size() > 0) {
//			EObject content = contents.get(0);
//			if(content instanceof ReqIF) {
//				ReqIFContent coreContent = ((ReqIF)content).getCoreContent();
//				return coreContent.getSpecTypes();
//			}
//		}
//
//		return null;
//	}
//
//	@Override
//	public void performFinish(RequirementSource repository) {
//		try {
//			repository.setProperty(RepositoryConstants.PROPERTY_URL, reqIfFile);
//			repository.setProperty(RepositoryConstants.PROPERTY_LABEL, repositoryLabel);
//			repository.getMapping().addAll((Collection<? extends ElementMapping>)rmfMappingPage.getResult());
//		} catch (Exception e) {
//			boolean debug = logger.isDebug(Activator.OPTIONS_DEBUG, Activator.getDefault());
//			if(debug) {
//				logger.trace("Properties " + RepositoryConstants.PROPERTY_URL + " and " + RepositoryConstants.PROPERTY_LABEL + " can't be set on " + repository.getRepositoryLabel() + "\n Trace : " + e.getMessage());
//			}
//		}
//	}
//
//	@Override
//	public boolean preFinish() {
//		boolean result = true;
//
//		
//		
//		//TODO :
//		// if there isn't a mapping -> do nothing
//		// if there is a mapping -> check if the mapping is valid
//		// check needed information
//		
//		if(settingWizard != null) {
//			reqIfFile = settingWizard.getUri();
//			repositoryLabel = settingWizard.getLabel();
//			skipMapping = settingWizard.isSkipMapping();
//		} else {
//			return false;
//		}
//		
////		Collection<EObject> mapping = rmfMappingPage.getResult();
////		for(EObject eObject : mapping) {
////			if(eObject instanceof ElementMapping) {
////				EList<EClass> superTypes = ((ElementMapping)eObject).getTargetElement().getESuperTypes();
////				if (!isSectionSuperType(superTypes))
////				{
////					Shell shell = new Shell();
////					boolean dialog = MessageDialog.openQuestion(shell, "Type WARNING", "One or more mapping element doesn't inherit from Reachable Section Type. Its children will be ignored. Would you continue?");
////					shell.dispose();
////					if (!dialog) {
////						result = false;
////					}
////					break;
////				}
////			}
////		}
//		
//		
//		
//		return result;
//	}
//
//	public boolean isSectionSuperType(Collection<EClass> superTypes){
//		for(EClass eClass : superTypes) {
//			if("ReachableSection".equals(eClass.getName()) || "RequirementSection".equals(eClass.getName())) {
//				return true;
//			}
//		}
//		return false;
//	}
//	
//	@Override
//	public boolean canFinish() {
//		
//		if(selectDocumentsPage == null || !selectDocumentsPage.isPageComplete()) {
//			return false;
//		}
//		
//		if(rmfMappingPage == null || !rmfMappingPage.isPageComplete()) {
//			return false;
//		}
//		
//		return true;
//	}
//
//	
//	public Scope getScope() {
//		return scope;
//	}
//
//	@Override
//	public LabelProvider getSourceLabelProvider() {
//		return null;
//	}
//
//	@Override
//	public IWizard getSettingWizard() {
//		settingWizard = new SettingWizard();
//		return settingWizard;
//	}
//
//	@Override
//	public boolean skipMapping() {
//		return skipMapping;
//	}
//	
}
