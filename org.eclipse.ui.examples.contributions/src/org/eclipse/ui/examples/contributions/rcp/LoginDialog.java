/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.ui.examples.contributions.rcp;

/**
 * @since 3.4
 *
 */

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.examples.contributions.model.persistence.SessionSourceProvider;
import org.eclipse.ui.services.ISourceProviderService;

public class LoginDialog {

	private static Text txt_Password;
	private static Text txt_Username;
	private Display display;

	public LoginDialog(Display display) {
		this.display = display;
	}

	public void createContents() {
		// Shell must be created with style SWT.NO_TRIM
		final Shell shell = new Shell(display, SWT.NO_TRIM | SWT.ON_TOP);
		final FillLayout fillLayout = new FillLayout();
		fillLayout.marginHeight = 1;
		shell.setLayout(fillLayout);

		// Create a composite with grid layout.
		final Composite composite = new Composite(shell, SWT.NONE);
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		gridLayout.marginHeight = 0;
		gridLayout.verticalSpacing = 0;
		gridLayout.marginWidth = 0;
		gridLayout.horizontalSpacing = 0;
		composite.setLayout(gridLayout);

		// Setting the background of the composite
		// with the image background for login dialog
		final Label img_Label = new Label(composite, SWT.NONE);
		img_Label.setLayoutData(new GridData(195, 181));
		final Image img = new Image(display, getClass().getResourceAsStream("img_login.gif")); //$NON-NLS-1$
		img_Label.setImage(img);

		// Creating the composite which will contain
		// the login related widgets
		final Composite cmp_Login = new Composite(composite, SWT.NONE);
		final RowLayout rowLayout = new RowLayout();
		rowLayout.fill = true;
		cmp_Login.setLayout(rowLayout);
		final GridData gridData = new GridData(GridData.FILL, GridData.FILL, false, false);
		gridData.widthHint = 196;
		cmp_Login.setLayoutData(gridData);

		// Label for the heading
		final CLabel clbl_UserLogin = new CLabel(cmp_Login, SWT.NONE);
		final RowData rowData = new RowData();
		rowData.width = 180;
		clbl_UserLogin.setLayoutData(rowData);
		clbl_UserLogin.setText("User Login"); //$NON-NLS-1$

		// Label for the username
		final CLabel clbl_Username = new CLabel(cmp_Login, SWT.NONE);
		final RowData rowData_1 = new RowData();
		rowData_1.width = 180;
		clbl_Username.setLayoutData(rowData_1);
		clbl_Username.setText("Username"); //$NON-NLS-1$

		// Textfield for the username
		txt_Username = new Text(cmp_Login, SWT.BORDER);
		final RowData rowData_2 = new RowData();
		rowData_2.width = 170;
		txt_Username.setLayoutData(rowData_2);

		// Label for the password
		final CLabel clbl_Password = new CLabel(cmp_Login, SWT.NONE);
		final RowData rowData_3 = new RowData();
		rowData_3.width = 180;
		clbl_Password.setLayoutData(rowData_3);
		clbl_Password.setText("Password"); //$NON-NLS-1$

		// Textfield for the password
		txt_Password = new Text(cmp_Login, SWT.BORDER);
		final RowData rowData_4 = new RowData();
		rowData_4.width = 170;
		txt_Password.setLayoutData(rowData_4);
		txt_Password.setEchoChar('*');

		// Composite to hold button as I want the
		// button to be positioned to my choice.
		final Composite cmp_ButtonBar = new Composite(cmp_Login, SWT.NONE);
		final RowData rowData_5 = new RowData();
		rowData_5.height = 38;
		rowData_5.width = 185;
		cmp_ButtonBar.setLayoutData(rowData_5);
		cmp_ButtonBar.setLayout(new FormLayout());

		// Button for login
		final Button btn_login = new Button(cmp_ButtonBar, SWT.FLAT);
		final FormData formData = new FormData();
		formData.bottom = new FormAttachment(0, 28);
		formData.top = new FormAttachment(0, 5);
		formData.right = new FormAttachment(100, -3);
		formData.left = new FormAttachment(100, -40);
		btn_login.setLayoutData(formData);
		btn_login.setText("Login"); //$NON-NLS-1$

		// Adding CLOSE action to this button.
		btn_login.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event e) {
				ISourceProviderService service = (ISourceProviderService) PlatformUI.getWorkbench().getService(ISourceProviderService.class);
				SessionSourceProvider sessionSourceProvider = (SessionSourceProvider) service.getSourceProvider(SessionSourceProvider.SESSION_STATE);
				sessionSourceProvider.setLoggedIn(true);
				shell.close();
				// In your case, you might wish
				// to call the authentication method.
			}
		});

		// Label for copyright info
		final CLabel clbl_Message = new CLabel(cmp_Login, SWT.NONE);
		clbl_Message.setAlignment(SWT.RIGHT);
		final RowData rowData_6 = new RowData();
		rowData_6.width = 188;
		clbl_Message.setLayoutData(rowData_6);
		clbl_Message.setText("My Custom Login Screen"); //$NON-NLS-1$

		// Drawing a region which will
		// form the base of the login
		Region region = new Region();
		Rectangle pixel = new Rectangle(1, 1, 388, 180);
		region.add(pixel);
		shell.setRegion(region);

		// Adding ability to move shell around
		Listener l = new Listener() {
			Point origin;

			public void handleEvent(Event e) {
				switch (e.type) {
				case SWT.MouseDown:
					origin = new Point(e.x, e.y);
					break;
				case SWT.MouseUp:
					origin = null;
					break;
				case SWT.MouseMove:
					if (origin != null) {
						Point p = display.map(shell, null, e.x, e.y);
						shell.setLocation(p.x - origin.x, p.y - origin.y);
					}
					break;
				}
			}
		};

		// Adding the listeners
		// to all visible components
		composite.addListener(SWT.MouseDown, l);
		composite.addListener(SWT.MouseUp, l);
		composite.addListener(SWT.MouseMove, l);

		img_Label.addListener(SWT.MouseDown, l);
		img_Label.addListener(SWT.MouseUp, l);
		img_Label.addListener(SWT.MouseMove, l);

		// Positioning in the center of the screen.
		// This for the 1024 resolution only. Later,
		// I plan to make generic so, that it takes
		// the resolution and finds the center of
		// the screen.
		shell.setLocation(320, 290);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		region.dispose();
	}
}
