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
package org.eclipse.reqcycle.core.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;


/**
 * The Class ExportConfigurationHandler.
 */
public class ExportConfigurationHandler extends AbstractHandler {

	/** The ReqCycle conf prefix. */
	protected String reqCyclePrefix = "org.eclipse.reqcycle";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		exportConf();
		return null;
	}

	/**
	 * Export configuration
	 */
	protected void exportConf() {
		FileDialog dialog = new FileDialog(Display.getDefault().getActiveShell(), SWT.SAVE);
		String uri = dialog.open();
		if(uri != null) {
			IPath confFilePath = org.eclipse.ziggurat.configuration.Activator.getDefault().getStateLocation();
			URI folderURI = URI.createURI(confFilePath.toOSString());
			createZipFromFiles(folderURI, uri);
			MessageDialog.openInformation(Display.getDefault().getActiveShell(), "Export ReqCycle Configuration", "Export finished.");
		}
	}

	/**
	 * Creates the zip from files.
	 * 
	 * @param directoryURI
	 *        the directory uri to zip
	 * @param outputURI
	 *        the output uri
	 */
	private void createZipFromFiles(URI directoryURI, String outputURI) {

		try {

			File folder = new File(directoryURI.toString());
			File[] listOfFiles = folder.listFiles();

			File f = new File(outputURI);
			FileOutputStream fileOutputStream = new FileOutputStream(f);
			ZipOutputStream outputStream = new ZipOutputStream(fileOutputStream);
			for(File file : listOfFiles) {
				if(file.isFile() && file.getName().startsWith(reqCyclePrefix)) {

					FileInputStream inStream = new FileInputStream(file);

					outputStream.putNextEntry(new ZipEntry(file.getName()));

					byte[] buffer = new byte[1024];
					int bytesRead;

					while((bytesRead = inStream.read(buffer)) > 0) {
						outputStream.write(buffer, 0, bytesRead);
					}
					outputStream.closeEntry();

					inStream.close();
				}
			}
			outputStream.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
