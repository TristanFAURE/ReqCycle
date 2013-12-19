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
package org.eclipse.reqcycle.repository.connector.rmf.ui;

import java.util.Collection;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.reqcycle.repository.connector.ui.wizard.MappingComposite;
import org.eclipse.reqcycle.repository.connector.ui.wizard.pages.MappingDialogPage;
import org.eclipse.reqcycle.repository.data.types.IAttribute;
import org.eclipse.reqcycle.repository.data.types.IRequirementType;
import org.eclipse.rmf.reqif10.AttributeDefinition;
import org.eclipse.rmf.reqif10.DatatypeDefinition;
import org.eclipse.rmf.reqif10.SpecType;
import org.eclipse.rmf.reqif10.common.util.ReqIF10Util;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import MappingModel.MappingAttribute;
import MappingModel.MappingElement;
import MappingModel.MappingModelFactory;
import RequirementSourceConf.RequirementSource;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterators;
import com.google.common.collect.Sets;


public abstract class RMFRepositoryMappingPage extends WizardPage implements Listener {

	private MappingComposite mappingComposite;

	private Button autoMappingBtn;

	static ITreeContentProvider contentProvider = new ITreeContentProvider() {

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

		@Override
		public void dispose() {
		}

		@Override
		public boolean hasChildren(Object element) {
			return false;
		}

		@Override
		public Object getParent(Object element) {
			return null;
		}

		@Override
		public Object[] getElements(Object inputElement) {
			return ArrayContentProvider.getInstance().getElements(inputElement);
		}

		@Override
		public Object[] getChildren(Object parentElement) {
			return null;
		}
	};


	/**
	 * @param pageName
	 */
	public RMFRepositoryMappingPage(String title, String description) {
		super(title);
		setTitle(title);
		setDescription(description);
	}

	@Override
	public void createControl(final Composite parent) {
		mappingComposite = new MappingComposite(parent, SWT.NONE, this) {

			@Override
			public MappingElement linkElements(Object sourceSelection, Object targetSelection) {
				return mapElements(parent, sourceSelection, targetSelection);
			}

			@Override
			protected String getResultDetail() {
				return RMFRepositoryMappingPage.this.getResultDetail();
			}

			@Override
			protected Object getTargetInput() {
				return RMFRepositoryMappingPage.this.getTargetInput();
			}

			@Override
			protected Object getSourceInput() {
				return RMFRepositoryMappingPage.this.getSourceInput();
			}

			@Override
			protected IContentProvider getSourceContentProvider() {
				return RMFRepositoryMappingPage.this.getSourceContentProvider();
			}

			@Override
			protected IBaseLabelProvider getSourceLabelProvider() {
				return RMFRepositoryMappingPage.this.getSourceLabelProvider();
			}

			@Override
			protected String getTargetDetail() {
				return RMFRepositoryMappingPage.this.getTargetDetail();
			}

			@Override
			protected String getSourceDetail() {
				return RMFRepositoryMappingPage.this.getSourceDetail();
			}

			@Override
			protected boolean getCanEditLink() {
				return false;
			}

			@Override
			protected void init(Collection<EObject> result) {
				Collection<EObject> mapping = addToMapping();
				if(mapping != null && !mapping.isEmpty()) {
					result.addAll(mapping);
				}
			}

		};


		autoMappingBtn = new Button(mappingComposite, SWT.PUSH);
		autoMappingBtn.setLayoutData(new GridData(1, 1, false, false));
		autoMappingBtn.setText("Map using names");
		autoMappingBtn.setEnabled(true);

		hookListeners();

		setControl(mappingComposite);

	}

	/**
	 * @return
	 */
	protected abstract Collection<EObject> addToMapping();

	protected abstract String getSourceDetail();

	protected abstract String getTargetDetail();

	protected abstract IBaseLabelProvider getSourceLabelProvider();

	protected abstract IContentProvider getSourceContentProvider();

	protected abstract Object getSourceInput();

	protected abstract Object getTargetInput();

	protected abstract String getResultDetail();

	private void hookListeners() {
		autoMappingBtn.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				RMFRepositoryMappingPage.this.generateMapping();
			}
		});
	}


	protected void generateMapping() {
		if(getSourceInput() instanceof Collection<?> && getTargetInput() instanceof Collection<?>) {
			for(Object inputElement : (Collection<?>)getSourceInput()) {
				if(inputElement instanceof SpecType) {
					final String inputName = ((SpecType)inputElement).getLongName();

					IRequirementType element = Iterators.find(((Collection)getTargetInput()).iterator(), new Predicate<IRequirementType>() {

						@Override
						public boolean apply(IRequirementType arg0) {
							String ii = inputName;
							return ii.equalsIgnoreCase(arg0.getName());
						}
					});
					if(element != null) {
						MappingElement elementMapping = MappingModelFactory.eINSTANCE.createMappingElement();
						Collection<IAttribute> allAttributes = element.getAttributes();
						Collection<IAttribute> filtered = Collections2.filter(allAttributes, new Predicate<IAttribute>() {

							@Override
							public boolean apply(IAttribute arg0) {
								if(arg0 instanceof IAttribute) {
									return !arg0.isHidden();
								}
								return false;
							}
						});
						elementMapping.getAttributes().addAll(mapAttributes(((SpecType)inputElement).getSpecAttributes(), filtered));
						elementMapping.setSourceQualifier(((SpecType)inputElement).getIdentifier());
						elementMapping.setDescription(((SpecType)inputElement).getLongName());
						elementMapping.setTargetElement((EClass)element);
						if(mappingComposite != null) {
							mappingComposite.addToResult(elementMapping);
						}
					}
				}
			}
		}
	}

	private Collection<MappingAttribute> mapAttributes(Collection<AttributeDefinition> source, Collection<IAttribute> target) {
		Collection<MappingAttribute> result = Sets.newHashSet();

		for(final AttributeDefinition attribute : source) {
			IAttribute element = Iterators.find(target.iterator(), new Predicate<IAttribute>() {

				@Override
				public boolean apply(IAttribute arg0) {
					String name = attribute.getLongName();
					return arg0.getName().equalsIgnoreCase(name);
				}
			});

			EAttribute eAttribute = null;
			if(element instanceof IAdaptable) {
				eAttribute = (EAttribute)((IAdaptable)element).getAdapter(EAttribute.class);
			}
			if(eAttribute == null) {
				continue;
			}

			MappingAttribute attributeMapping = MappingModelFactory.eINSTANCE.createMappingAttribute();
			attributeMapping.setTargetAttribute(eAttribute);
			attributeMapping.setSourceId(attribute.getIdentifier());
			attributeMapping.setDescription(attribute.getLongName());
			result.add(attributeMapping);
		}
		return result;
	}


	/**
	 * @param parent
	 * @param sourceSelection
	 * @param targetSelection
	 * @return
	 */
	private MappingElement mapElements(final Composite parent, Object sourceSelection, final Object targetSelection) {


		if(targetSelection instanceof IRequirementType && sourceSelection instanceof SpecType) {

			final SpecType specType = (SpecType)sourceSelection;


			MappingDialogPage mappingDialog = new MappingDialogPage(parent.getShell()) {

				@Override
				protected MappingAttribute linkElements(Object sourceSelection, Object targetSelection) {
					if(sourceSelection instanceof AttributeDefinition && targetSelection instanceof IAttribute) {
						EAttribute eAttribute = null;
						if(targetSelection instanceof IAdaptable) {
							eAttribute = (EAttribute)((IAdaptable)targetSelection).getAdapter(EAttribute.class);
						}
						if(eAttribute == null) {
							return null;
						}
						MappingAttribute attributeMapping = MappingModelFactory.eINSTANCE.createMappingAttribute();
						attributeMapping.setTargetAttribute(eAttribute);
						attributeMapping.setSourceId(((AttributeDefinition)sourceSelection).getIdentifier());
						attributeMapping.setDescription(((AttributeDefinition)sourceSelection).getLongName());
						return attributeMapping;
					}
					return null;
				}

				@Override
				protected String getResultDetail() {
					return null;
				}

				@Override
				protected Object getTargetInput() {
					Collection<? extends IAttribute> allAttributes = ((IRequirementType)targetSelection).getAttributes();
					Collection<IAttribute> filteredAttribute = (Collection<IAttribute>)Collections2.filter(allAttributes, new Predicate<IAttribute>() {

						@Override
						public boolean apply(IAttribute arg0) {
							if(arg0 instanceof IAttribute) {
								return !arg0.isHidden();
							}
							return false;
						}
					});
					return filteredAttribute;
				}

				@Override
				protected Object getSourceInput() {
					return specType.getSpecAttributes();
				}

				@Override
				protected boolean getCanEditLink() {
					return false;
				}

				@Override
				protected String getSourceDetail() {
					return null;
				}

				@Override
				protected String getTargetDetail() {
					return null;
				}

				@Override
				protected IContentProvider getSourceContentProvider() {
					return contentProvider;
				}

				@Override
				public ILabelProvider getSourceLabelProvider() {
					return new LabelProvider() {

						@Override
						public String getText(Object element) {
							if(element instanceof AttributeDefinition) {
								DatatypeDefinition dataTypeDef = ReqIF10Util.getDatatypeDefinition((AttributeDefinition)element);
								return ((AttributeDefinition)element).getLongName() + " : " + dataTypeDef.getClass().getSimpleName();
							}
							return super.getText(element);
						}
					};
				}
			};

			if(mappingDialog.open() == Window.OK) {
				if(mappingDialog.getResult() != null) {

					EClass eClass = null;
					if(targetSelection instanceof IAdaptable) {
						eClass = (EClass)((IAdaptable)targetSelection).getAdapter(EClass.class);
					}
					if(eClass == null) {
						return null;
					}

					MappingElement element = MappingModelFactory.eINSTANCE.createMappingElement();
					element.getAttributes().addAll((Collection<? extends MappingAttribute>)mappingDialog.getResult());
					element.setSourceQualifier(specType.getIdentifier());
					element.setDescription(specType.getLongName());
					element.setTargetElement(eClass);
					return element;
				}
			}

		}

		return null;
	}

	public boolean preFinish(RequirementSource repository) {
		return true;
	}

	@Override
	public boolean isPageComplete() {
		return mappingComposite != null && mappingComposite.getResult() != null && !mappingComposite.getResult().isEmpty();
	}

	public Collection<EObject> getResult() {
		if(mappingComposite != null) {
			return mappingComposite.getResult();
		}
		return null;
	}

	public static class RMFMappingBean {

		private Collection<MappingElement> mapping;

		public void setMapping(Collection<MappingElement> mapping) {
			this.mapping = mapping;
		}

		public Collection<MappingElement> getMapping() {
			return mapping;
		}
	}

	@Override
	public void handleEvent(Event event) {

	}

}
