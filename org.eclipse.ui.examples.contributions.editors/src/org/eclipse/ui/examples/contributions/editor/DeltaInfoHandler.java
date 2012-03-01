/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.ui.examples.contributions.editor;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.examples.contributions.common.ContributionMessages;
import org.eclipse.ui.examples.contributions.model.annotated.Person;
import org.eclipse.ui.examples.contributions.model.persistence.IPersonService;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * Show if there is any delta from the model for the active editor.
 * 
 * @since 3.3
 */
public class DeltaInfoHandler extends AbstractHandler {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		InfoEditor editor = (InfoEditor) HandlerUtil
				.getActiveEditorChecked(event);
		Person local = editor.getCurrentPerson();

		IPersonService service = (IPersonService) editor.getSite().getService(
				IPersonService.class);
		Person model = service.getPerson(local.getId());

		boolean delta = false;
		StringBuffer buf = new StringBuffer();
		buf.append(ContributionMessages.InfoEditor_surname);
		if (!model.getSurname().equals(local.getSurname())) {
			delta = true;
			buf.append(' ');
			buf.append(model.getSurname());
			buf.append(", "); //$NON-NLS-1$
			buf.append(local.getSurname());
		}
		buf.append(" - "); //$NON-NLS-1$
		buf.append(ContributionMessages.InfoEditor_givenname);
		if (!model.getGivenname().equals(local.getGivenname())) {
			delta = true;
			buf.append(' ');
			buf.append(model.getGivenname());
			buf.append(", "); //$NON-NLS-1$
			buf.append(local.getGivenname());
		}
		buf.append(" - "); //$NON-NLS-1$
		if (delta) {
			buf.append(ContributionMessages.DeltaInfoHandler_found);
		} else {
			buf.append(ContributionMessages.DeltaInfoHandler_notFound);
		}
		MessageDialog.openInformation(editor.getSite().getShell(),
				ContributionMessages.DeltaInfoHandler_shellTitle, buf
						.toString());
		return null;
	}
}
