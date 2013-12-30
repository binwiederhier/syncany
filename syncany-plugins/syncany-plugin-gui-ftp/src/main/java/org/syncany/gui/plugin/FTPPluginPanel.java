/*
 * Syncany, www.syncany.org
 * Copyright (C) 2011-2013 Philipp C. Heckel <philipp.heckel@gmail.com> 
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.syncany.gui.plugin;

import java.io.IOException;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.net.ftp.FTPClient;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.syncany.gui.panel.ApplicationResources;
import org.syncany.gui.panel.PluginPanel;
import org.syncany.util.I18n;

/**
 * @author vincent
 *
 */
public class FTPPluginPanel extends PluginPanel {
	private static final Logger log = Logger.getLogger(FTPPluginPanel.class.getSimpleName());
	private static final int TIMEOUT_CONNECT = 5000;
	
	private Text hostText;
	private Text usernameText;
	private Text passwordText;
	private Text pathText;
	private Spinner spinner;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public FTPPluginPanel(Composite parent, int style) {
		super(parent, style);
		
		initComposite();
	}
	
	public void initComposite(){
		Font fontNormal = ApplicationResources.FONT_NORMAL;
		Font fontBold = ApplicationResources.FONT_BOLD;
		
		setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_composite = new GridLayout(4, false);
		gl_composite.verticalSpacing = 10;
		setLayout(gl_composite);
		
		Label lblNewLabel = new Label(this, SWT.WRAP);
		lblNewLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));
		lblNewLabel.setText("New Label");
		lblNewLabel.setFont(fontBold);
		
		Label hostLabel = new Label(this, SWT.NONE);
		hostLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		hostLabel.setText(I18n.getString("plugin.ftp.host"));
		hostLabel.setFont(fontNormal);
		
		hostText = new Text(this, SWT.BORDER);
		hostText.setFont(fontNormal);
		GridData gd_hostText = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_hostText.minimumWidth = 200;
		hostText.setLayoutData(gd_hostText);
		
		Label portLabel = new Label(this, SWT.NONE);
		portLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		portLabel.setText(I18n.getString("plugin.ftp.port"));
		portLabel.setFont(fontNormal);
		
		spinner = new Spinner(this, SWT.BORDER);
		spinner.setPageIncrement(1);
		spinner.setMaximum(100000);
		spinner.setFont(fontNormal);
		spinner.setSelection(21);
		GridData gd_spinner = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_spinner.widthHint = 50;
		gd_spinner.heightHint = 15;
		spinner.setLayoutData(gd_spinner);
		
		Label usernameLabel = new Label(this, SWT.NONE);
		usernameLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		usernameLabel.setText(I18n.getString("plugin.ftp.username"));
		usernameLabel.setFont(fontNormal);
		
		passwordText = new Text(this, SWT.BORDER);
		passwordText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));
		passwordText.setFont(fontNormal);
		
		Label passwordLabel = new Label(this, SWT.NONE);
		passwordLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		passwordLabel.setText(I18n.getString("plugin.ftp.password"));
		passwordLabel.setFont(fontNormal);
		
		usernameText = new Text(this, SWT.BORDER);
		usernameText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));
		usernameText.setFont(fontNormal);
		
		Label pathLabel = new Label(this, SWT.NONE);
		pathLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		pathLabel.setText(I18n.getString("plugin.ftp.path"));
		pathLabel.setFont(fontNormal);
		
		pathText = new Text(this, SWT.BORDER);
		pathText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));
		pathText.setFont(fontNormal);
		
		Composite buttonComposite = new Composite(this, SWT.NONE);
		GridLayout gl_buttonComposite = new GridLayout(2, false);
		gl_buttonComposite.horizontalSpacing = 0;
		gl_buttonComposite.verticalSpacing = 0;
		gl_buttonComposite.marginWidth = 0;
		gl_buttonComposite.marginHeight = 0;
		buttonComposite.setLayout(gl_buttonComposite);
		GridData gd_buttonComposite = new GridData(SWT.RIGHT, SWT.BOTTOM, false, true, 4, 1);
		gd_buttonComposite.minimumHeight = 30;
		buttonComposite.setLayoutData(gd_buttonComposite);
		
		final Label testResultLabel = new Label(buttonComposite, SWT.NONE);
		testResultLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		testResultLabel.setAlignment(SWT.CENTER);
		
		final Button testFtpButton = new Button(buttonComposite, SWT.NONE);

		GridData gd_testFtpButton = new GridData(SWT.CENTER, SWT.FILL, false, false, 1, 1);
		gd_testFtpButton.heightHint = 30;
		gd_testFtpButton.widthHint = 100;
		testFtpButton.setLayoutData(gd_testFtpButton);
		testFtpButton.setFont(fontNormal);
		testFtpButton.setText(I18n.getString("plugin.ftp.test"));
		testFtpButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				testFtpButton.setEnabled(false);
				final boolean test = testFtpConnection();
				testFtpButton.setEnabled(true);
				
				Display.getCurrent().syncExec(new Runnable() {
				    public void run() {
				    	if (test){
				    		testResultLabel.setText(I18n.getString("plugin.ftp.testSucceed"));
				    	}
				    	else{
				    		testResultLabel.setText(I18n.getString("plugin.ftp.testFails"));
				    	}
				    }
				});
			}
		});
	}

	protected boolean testFtpConnection() {
		FTPClient ftp = new FTPClient();

		ftp.setConnectTimeout(TIMEOUT_CONNECT);

		try{
			ftp.connect(hostText.getText(), Integer.parseInt(spinner.getText()));
			boolean success = ftp.login(usernameText.getText(), passwordText.getText());
			ftp.disconnect();
			return success;
		}
		catch (NumberFormatException e){
			log.warning("NumberFormatException "+e.toString());
		}
		catch (SocketException e) {
			log.warning("SocketException "+e.toString());
		}
		catch (IOException e) {
			log.warning("IOException "+e.toString());
		}
		return false;
	}

	@Override
	public Map<String, String> getParameters() {
		Map<String, String> parameters = new HashMap<>();
		parameters.put("host", hostText.getText());
		parameters.put("username", usernameText.getText());
		parameters.put("password", passwordText.getText());
		parameters.put("path", pathText.getText());
		parameters.put("port", spinner.getText());
		return parameters;
	}
	
	@Override
	public boolean isValid() {
		// TODO Auto-generated method stub
		return true;
	}
}
