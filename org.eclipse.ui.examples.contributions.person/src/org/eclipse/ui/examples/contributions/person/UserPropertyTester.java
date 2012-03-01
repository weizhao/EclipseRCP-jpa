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

package org.eclipse.ui.examples.contributions.person;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.examples.contributions.model.annotated.Person;
import org.eclipse.ui.examples.contributions.model.persistence.SessionSourceProvider;
import org.eclipse.ui.services.ISourceProviderService;

/**
 * Test properties of a Person object provided as a variable.
 * 
 * @since 3.4
 */
public class UserPropertyTester extends PropertyTester {
	private final static String IS_ADMIN = "isAdmin"; //$NON-NLS-1$
	private final static String ID = "id"; //$NON-NLS-1$
	private final static String LOGGED_IN = "loggedIn"; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.expressions.IPropertyTester#test(java.lang.Object,
	 * java.lang.String, java.lang.Object[], java.lang.Object)
	 */
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		if (!(receiver instanceof Person) ) {
			return false;
		}
		Person person = (Person) receiver;
		if (property.equals(IS_ADMIN) && expectedValue instanceof Boolean) {
//			ISourceProviderService service = (ISourceProviderService) PlatformUI.getWorkbench().getService(ISourceProviderService.class);
//			SessionSourceProvider sessionSourceProvider = (SessionSourceProvider) service.getSourceProvider(SessionSourceProvider.SESSION_STATE);
//			if (!sessionSourceProvider.isLoggedIn()) {
//				return false;
//			}
			boolean value = ((Boolean) expectedValue).booleanValue();
			return person.hasAdminRights() == value;
		}
		if (property.equals(ID) && expectedValue instanceof Integer) {
			int value = ((Integer) expectedValue).intValue();
			return person.getId() == value;
		}
		return false;
	}
}
