package org.eclipse.reqcycle.repository.data.ui.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.reqcycle.repository.data.IDataModelManager;
import org.eclipse.ziggurat.inject.ZigguratInject;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class InitCustomDataHandler extends AbstractHandler {

	/**
	 * The constructor.
	 */
	public InitCustomDataHandler() {
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		//FIXME : hide the menu when init is performed
		ZigguratInject.make(IDataModelManager.class);
		return null;
	}
}
