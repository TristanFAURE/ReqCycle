package org.eclipse.reqcycle.repository.connector.local;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.reqcycle.repository.data.IDataManager;
import org.eclipse.reqcycle.repository.data.IDataModelManager;
import org.eclipse.reqcycle.repository.data.IDataTopics;
import org.eclipse.reqcycle.repository.data.types.IRequirementType;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.CompoundContributionItem;
import org.eclipse.ziggurat.inject.ZigguratInject;

import RequirementSourceConf.RequirementSource;
import RequirementSourceData.AbstractElement;
import RequirementSourceData.RequirementSourceDataPackage;
import RequirementSourceData.Section;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;

public class ReqCycleContributionItem extends CompoundContributionItem {

	@Inject
	IDataModelManager dataModelManager;

	@Inject
	IDataManager dataManager;

	protected EObject selectedElement;


	public ReqCycleContributionItem() {
		super();
		ZigguratInject.inject(this);
	}

	@Override
	protected IContributionItem[] getContributionItems() {

		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		Set<EClass> classes = new HashSet<EClass>();
		if(window != null) {
			ISelection selection = window.getSelectionService().getSelection();
			if(selection instanceof IStructuredSelection) {
				Object firstElement = ((IStructuredSelection)selection).getFirstElement();
				selectedElement = null;
				if(firstElement instanceof EObject) {
					selectedElement = (EObject)firstElement;
					classes = getDataEClasses(selectedElement);
				}
			}
		}

		Collection<IContributionItem> menuContributionList = Collections2.transform(classes, new Function<EClass, IContributionItem>() {

			@Override
			public IContributionItem apply(final EClass arg0) {
				ContributionItem contributionItem = new ContributionItem() {

					@Override
					public void fill(Menu menu, int index) {
						MenuItem menuItem = new MenuItem(menu, SWT.None);
						menuItem.setText(arg0.getName());
						menuItem.addSelectionListener(new SelectionAdapter() {

							@Override
							public void widgetSelected(SelectionEvent e) {

								try {

									EObject object = arg0.getEPackage().getEFactoryInstance().create(arg0);
									AbstractElement element;

									if(object instanceof AbstractElement) {
										element = (AbstractElement)object;
									} else {
										throw new Exception("Error while creating a " + arg0.getName() + " element.");
									}


									if(selectedElement instanceof RequirementSource) {
										dataManager.addElementsToSource((RequirementSource)selectedElement, element);
									}

									if(selectedElement instanceof Section) {
										dataManager.addElementsToSection((Section)selectedElement, element);
									}

									dataManager.notifyChange(IDataTopics.NEW_ELEMENT, element);

									// FIXME : set element scope

								} catch (Exception e1) {
									e1.printStackTrace();
								}

							}
						});
					}
				};
				return contributionItem;
			}
		});

		IContributionItem[] array = new IContributionItem[menuContributionList.size()];
		return menuContributionList.toArray(array);
	}

	/**
	 * Gets the data e classes.
	 * 
	 * @param selectedElement
	 *        the selected element
	 * @return the data e classes
	 */
	protected Set<EClass> getDataEClasses(Object selectedElement) {
		if(!(selectedElement instanceof Section || selectedElement instanceof RequirementSource)) {
			return Sets.newHashSet();
		}

		Set<EClass> classes = new HashSet<EClass>();

		//Gets Requirement Types EClasses
		Collection<IRequirementType> dataTypes = dataModelManager.getAllRequirementTypes();
		classes.addAll(Collections2.transform(dataTypes, new Function<IRequirementType, EClass>() {

			@Override
			public EClass apply(IRequirementType arg0) {
				if(arg0 instanceof IAdaptable) {
					return (EClass)((IAdaptable)arg0).getAdapter(EClass.class);
				}
				return null;
			}
		}));

		//Add Section EClass
		classes.add(RequirementSourceDataPackage.Literals.SECTION);
		return classes;
	}

}
