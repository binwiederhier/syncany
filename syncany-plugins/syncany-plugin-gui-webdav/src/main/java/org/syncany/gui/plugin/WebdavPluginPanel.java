package org.syncany.gui.plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.syncany.gui.panel.ApplicationResources;
import org.syncany.gui.panel.PluginPanel;
import org.syncany.util.I18n;


/**
 * @author vincent
 * 
 * "url", "username", "password"
 */
public class WebdavPluginPanel extends PluginPanel {
	private static final Logger log = Logger.getLogger(WebdavPluginPanel.class.getSimpleName());
	
	private Text url;
	private Text username;
	private Text password;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public WebdavPluginPanel(Composite parent, int style) {
		super(parent, style);
		
		initComposite();
	}
	
	public void initComposite(){
		Font fontNormal = ApplicationResources.FONT_NORMAL;
		Font fontBold = ApplicationResources.FONT_BOLD;
		
		setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_composite = new GridLayout(2, false);
		gl_composite.verticalSpacing = 10;
		setLayout(gl_composite);
		
		Label webdavTextTitleLabel = new Label(this, SWT.WRAP);
		webdavTextTitleLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		webdavTextTitleLabel.setText(I18n.getString("plugin.webdav.introduction.title"));
		webdavTextTitleLabel.setFont(fontBold);
		
		Label webdavTextLabel = new Label(this, SWT.WRAP);
		webdavTextLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		webdavTextLabel.setText(I18n.getString("plugin.webdav.introduction"));
		webdavTextLabel.setFont(fontNormal);
		
		Label urlLabel = new Label(this, SWT.NONE);
		urlLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		urlLabel.setText(I18n.getString("plugin.webdav.url", true));
		urlLabel.setFont(fontNormal);
		
		url = new Text(this, SWT.BORDER);
		url.setFont(fontNormal);
		GridData gd_url = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_url.minimumWidth = 200;
		url.setLayoutData(gd_url);
		
		Label usernameLabel = new Label(this, SWT.NONE);
		usernameLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		usernameLabel.setText(I18n.getString("plugin.webdav.username", true));
		usernameLabel.setFont(fontNormal);
		
		username = new Text(this, SWT.BORDER);
		username.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		username.setFont(fontNormal);
		
		Label passwordLabel = new Label(this, SWT.NONE);
		passwordLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		passwordLabel.setText(I18n.getString("plugin.webdav.password", true));
		passwordLabel.setFont(fontNormal);
		
		password = new Text(this, SWT.BORDER | SWT.PASSWORD);
		password.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		password.setFont(fontNormal);
		
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
		
		final Button testButton = new Button(buttonComposite, SWT.NONE);

		GridData gd_testButton = new GridData(SWT.CENTER, SWT.FILL, false, false, 1, 1);
		gd_testButton.heightHint = 30;
		gd_testButton.widthHint = 100;
		testButton.setLayoutData(gd_testButton);
		testButton.setFont(fontNormal);
		testButton.setText(I18n.getString("plugin.amazon.testConnection"));
	}

	@Override
	public Map<String, String> getParameters() {
		Map<String, String> parameters = new HashMap<>();
		parameters.put("url", url.getText());
		parameters.put("username", username.getText());
		parameters.put("password", password.getText());
		return parameters;
	}
	
	@Override
	public boolean isValid() {
		// TODO Auto-generated method stub
		return true;
	}
}
