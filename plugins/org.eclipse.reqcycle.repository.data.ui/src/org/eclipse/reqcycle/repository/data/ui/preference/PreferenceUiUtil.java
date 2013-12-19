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
package org.eclipse.reqcycle.repository.data.ui.preference;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.TableColumn;

public class PreferenceUiUtil {


	/**
	 * Creates a Group
	 * 
	 * @param parent
	 *        Parent composite
	 * @param name
	 *        Group Name
	 * @return Group with the given name
	 */
	public static Group createGroup(Composite parent, String name) {
		Group grpElements = new Group(parent, SWT.NONE);
		grpElements.setLayout(new GridLayout(2, false));
		grpElements.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpElements.setText(name);
		return grpElements;
	}

	/**
	 * Creates a Table Viewer Column
	 * 
	 * @param viewer
	 *        The table Viewer Container
	 * @param title
	 *        The Column Title
	 * @param style
	 *        The Column Style
	 * @return Table Column Viewer
	 */
	public static TableViewerColumn createTableViewerColumn(TableViewer viewer, String title, int style) {
		TableViewerColumn viewerColumn = new TableViewerColumn(viewer, style);
		TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setResizable(true);
		column.setMoveable(true);
		return viewerColumn;
	}

	/**
	 * Creates a Button
	 * 
	 * @param parent
	 *        Parent Composite
	 * @param toolTip
	 *        The button toolTip
	 * @param image
	 *        The button image
	 * @return
	 */
	public static Button createButton(Composite parent, String toolTip, Image image) {
		Button btn = createButton(parent, toolTip);
		btn.setImage(image);
		return btn;
	}

	/**
	 * Creates a push Button
	 * 
	 * @param parent
	 *        Parent Composite
	 * @param toolTip
	 *        the Button toolTip
	 * @return Push button
	 */
	private static Button createButton(Composite parent, String toolTip) {
		Button btn = new Button(parent, SWT.PUSH);
		btn.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));
		btn.setToolTipText(toolTip);
		return btn;
	}

}
