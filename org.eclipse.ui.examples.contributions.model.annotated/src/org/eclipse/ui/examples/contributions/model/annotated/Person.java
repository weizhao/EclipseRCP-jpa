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

package org.eclipse.ui.examples.contributions.model.annotated;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;



/**
 * A simple model object that is mutable.
 * 
 * @since 3.3
 */
@Entity
@Table(name="person")
public class Person implements Serializable {
	
	private static final long serialVersionUID = -3322749056120599468L;

	@Id
    @GeneratedValue
	private int id;
	private String surname;
	private String givenname;
	private boolean admin = false;
	
	@Transient
    private boolean loggedIn;



	public Person(int id, String sn, String gn) {
		surname = sn;
		givenname = gn;
		this.id = id;
	}
	public Person() {
		this(0,"","");
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public int getId() {
		return id;
	}

	public String getGivenname() {
		return givenname;
	}

	public void setGivenname(String givenname) {
		this.givenname = givenname;
	}
	
	public boolean hasAdminRights() {
		return admin;
	}
	
	public void setAdminRights(boolean admin) {
		this.admin = admin;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}
	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer(surname);
		buf.append(", "); //$NON-NLS-1$
		buf.append(givenname);
		buf.append(" ("); //$NON-NLS-1$
		buf.append(id);
		if (admin) {
			buf.append("-adm"); //$NON-NLS-1$
		}
		buf.append(")"); //$NON-NLS-1$
		return buf.toString();
	}

	public Person copy() {
		return new Person(id, surname, givenname);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		if (o instanceof Person) {
			Person p = (Person) o;
			return p.givenname == givenname && p.id == id
					&& p.surname == surname;
		}
		return false;
	}
}
