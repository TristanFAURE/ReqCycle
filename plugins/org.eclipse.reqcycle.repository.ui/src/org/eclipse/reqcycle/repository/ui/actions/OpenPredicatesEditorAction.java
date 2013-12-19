package org.eclipse.reqcycle.repository.ui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.reqcycle.predicates.ui.util.PredicatesUIHelper;

public class OpenPredicatesEditorAction extends Action {

	public OpenPredicatesEditorAction() {
	}

	@Override
	public void run() {
		PredicatesUIHelper.openNewPredicateEditor();
	}


}
