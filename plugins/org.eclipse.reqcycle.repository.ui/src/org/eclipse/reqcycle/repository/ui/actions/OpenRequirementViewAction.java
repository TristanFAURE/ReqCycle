package org.eclipse.reqcycle.repository.ui.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.inject.Inject;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.reqcycle.core.ILogger;
import org.eclipse.reqcycle.repository.data.util.DataUtil;
import org.eclipse.reqcycle.repository.ui.views.RequirementView;
import org.eclipse.ziggurat.inject.ZigguratInject;

import RequirementSourceConf.RequirementSource;

public class OpenRequirementViewAction extends Action {

	@Inject
	ILogger logger = ZigguratInject.make(ILogger.class);

	private TreeViewer viewer;

	public OpenRequirementViewAction(TreeViewer viewer) {
		this.viewer = viewer;
	}

	@Override
	public void run() {
		ISelection selection = viewer.getSelection();
		if(selection instanceof IStructuredSelection) {

			Collection<Object> selectedObj = new ArrayList<Object>();
			for(Iterator<?> iterator = ((IStructuredSelection)selection).iterator(); iterator.hasNext();) {
				selectedObj.add(iterator.next());
			}

			Collection<RequirementSource> input = new ArrayList<RequirementSource>();
			for(Iterator<Object> iterator = selectedObj.iterator(); iterator.hasNext();) {
				Object obj = iterator.next();
				input.addAll(DataUtil.getRepositories(obj));
			}

			if(!input.isEmpty()) {
				try {
					//					Collection<IPredicate> selectedPredicates = PredicatesUIHelper.openPredicatesChooser(null, "Requirement filtering", "Select a predicate to apply or press OK to continue without filtering.", true);
					//					if(selectedPredicates != null) {
					RequirementView.openNewRequirementView(input, null);
					//					}
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("Unable to open the View of filtered requirements : " + e.getMessage());
					logger.error(e.toString());
				}
			}
		}
	}

}
