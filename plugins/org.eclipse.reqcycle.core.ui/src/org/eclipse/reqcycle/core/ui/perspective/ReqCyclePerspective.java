package org.eclipse.reqcycle.core.ui.perspective;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;


public class ReqCyclePerspective implements IPerspectiveFactory {
	
	private IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();

	/**
	 * Creates the initial layout for a page.
	 */
	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		addFastViews(layout);
		addViewShortcuts(layout);
		addPerspectiveShortcuts(layout);
		{
			IFolderLayout folderLayout = layout.createFolder("folder", IPageLayout.LEFT, 0.25f, IPageLayout.ID_EDITOR_AREA);
			addView("org.eclipse.ui.navigator.ProjectExplorer", folderLayout);
			addView("org.eclipse.reqcycle.repository.ui.views.requirements", folderLayout);
		}
		{
			IFolderLayout folderLayout = layout.createFolder("folder_2", IPageLayout.RIGHT, 0.60f, IPageLayout.ID_EDITOR_AREA);
			addView("org.eclipse.reqcycle.traceability.ui.views.TraceabilityViewer", folderLayout);
			addView("org.eclipse.reqcycle.traceability.table.partdescriptor.traceability.table", folderLayout);
		}
		{
			IFolderLayout folderLayout = layout.createFolder("folder_1", IPageLayout.BOTTOM, 0.5f, "folder");
			addView("org.eclipse.papyrus.views.modelexplorer.modelexplorer", folderLayout);
			addView("org.eclipse.reqcycle.repository.ui.views.sources", folderLayout);
		}
	}

	private void addView(String viewId, IFolderLayout folderLayout) {
		IViewReference viewRef = activePage.findViewReference(viewId);
		if(viewRef == null) {
			try {
				activePage.showView(viewId, null, IWorkbenchPage.VIEW_ACTIVATE);
				viewRef = activePage.findViewReference(viewId);
			} catch (PartInitException e) {
			}
		}
		if (viewRef != null){
			folderLayout.addView(viewId);
		}
	}
	
	/**
	 * Add fast views to the perspective.
	 */
	private void addFastViews(IPageLayout layout) {
	}

	/**
	 * Add view shortcuts to the perspective.
	 */
	private void addViewShortcuts(IPageLayout layout) {
	}

	/**
	 * Add perspective shortcuts to the perspective.
	 */
	private void addPerspectiveShortcuts(IPageLayout layout) {
	}
	
}
