/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.ui.examples.contributions.view;

import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.IElementUpdater;
import org.eclipse.ui.examples.contributions.model.annotated.Person;
import org.eclipse.ui.examples.contributions.model.persistence.IPersonService;
import org.eclipse.ui.examples.contributions.model.persistence.SessionSourceProvider;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.menus.UIElement;
import org.eclipse.ui.services.ISourceProviderService;

/**
 * Allow login as the selected user. If you are doing this for real, I would
 * suggest checking their credentials!
 * 
 * @since 3.4
 */
public class LoginHandler extends AbstractHandler implements IElementUpdater {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchPart part = HandlerUtil.getActivePartChecked(event);
		ISelection sel = HandlerUtil.getActiveMenuSelection(event);
		if (sel == null) {
			sel = HandlerUtil.getCurrentSelection(event);
		}
		if (sel instanceof IStructuredSelection && !sel.isEmpty()) {
			IStructuredSelection selection = (IStructuredSelection) sel;
			Person person = (Person) selection.getFirstElement();
			IPersonService service = (IPersonService) part.getSite()
					.getService(IPersonService.class);
			service.login(person);
		}
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event); 
        ISourceProviderService service = (ISourceProviderService) window.getService(ISourceProviderService.class); 
        SessionSourceProvider sessionSourceProvider = (SessionSourceProvider) service.getSourceProvider(SessionSourceProvider.SESSION_STATE); 

        // update the source provider 
        sessionSourceProvider.setLoggedIn(!sessionSourceProvider.isLoggedIn());

		return null;
	}
	 @Override 
	 public void updateElement(UIElement element, Map parameters) { 
		 ISourceProviderService service = (ISourceProviderService) PlatformUI.getWorkbench()
			.getService(ISourceProviderService.class);
	     SessionSourceProvider sessionSourceProvider = (SessionSourceProvider) service.getSourceProvider(SessionSourceProvider.SESSION_STATE); 

		 element.setText(sessionSourceProvider.isLoggedIn()?"Log out":"Log in"); 
	} 
}
