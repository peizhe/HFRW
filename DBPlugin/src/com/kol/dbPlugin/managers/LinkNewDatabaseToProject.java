package com.kol.dbPlugin.managers;

import com.intellij.openapi.options.BaseConfigurableWithChangeSupport;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.ui.FieldPanel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.UIUtil;
import com.intellij.util.ui.UIUtil.FontSize;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;

import com.kol.dbPlugin.Credentials;
import com.kol.dbPlugin.Settings;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LinkNewDatabaseToProject extends BaseConfigurableWithChangeSupport {

    private JPanel panel;
    private FieldPanel name;
    private FieldPanel host;
    private FieldPanel port;
    private FieldPanel database;
    private FieldPanel username;
    private FieldPanel password;
    private FieldPanel databaseUrl;

    private static final String DB_DEFAULT_PORT = "3306";
    private static final String DB_URL_PORT_PREFIX = ":";
    private static final String DB_DEFAULT_HOST = "localhost";
    private static final String DB_URL_PREFIX = "jdbc:mysql://";
    private static final String DB_URL_DATABASE_SEPARATOR = "/";

    private static final String DB_DRIVER_NAME = "MySQL";
    private static final String DB_NAME_HOST_SEPARATOR = "@";
    private static final String DB_DRIVER_NAME_SEPARATOR = "_";

    public LinkNewDatabaseToProject() {
        createCenterPanel();
        host.setText(DB_DEFAULT_HOST);
        port.setText(DB_DEFAULT_PORT);
        updateURLFromParameters();

        final KeyAdapter hostDBName = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                updateURLFromParameters();
                generateNameFromParameters();
            }
        };

        final KeyAdapter portAdapter = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                updateURLFromParameters();
            }
        };

        host.getTextField().addKeyListener(hostDBName);
        port.getTextField().addKeyListener(portAdapter);
        database.getTextField().addKeyListener(hostDBName);
    }

    public Settings getSettings() {
        return new Settings(name.getText(), host.getText(), port.getText(), database.getText());
    }

    public Credentials getCredentials() {
        return new Credentials(name.getText(), username.getText(), password.getText());
    }

    private void updateURLFromParameters() {
        final String host = this.host.getText();
        final String port = this.port.getText();
        final String database = this.database.getText();

        final StringBuilder sb = new StringBuilder(DB_URL_PREFIX);
        if(null != host) {
            sb.append(host);
        }
        if(null != port && !port.isEmpty()) {
            sb.append(DB_URL_PORT_PREFIX).append(port);
        }
        if(null != database && !database.isEmpty()) {
            sb.append(DB_URL_DATABASE_SEPARATOR).append(database);
        }
        databaseUrl.setText(sb.toString());
    }

    private void generateNameFromParameters() {
        final StringBuilder sb = new StringBuilder(DB_DRIVER_NAME);
        sb.append(DB_DRIVER_NAME_SEPARATOR);

        final String host = this.host.getText();
        final String database = this.database.getText();

        if(null != database && !database.isEmpty()) {
            sb.append(database);
        }
        if(null != host && !host.isEmpty()) {
            sb.append(DB_NAME_HOST_SEPARATOR).append(host);
        }
        name.setText(sb.toString());
    }

    private void createCenterPanel() {
        panel = new JPanel(new BorderLayout(0, 5));
        final JPanel basePanel = new JPanel(new GridBagLayout());

        addNamePanel(basePanel);
        final JPanel hostPortPanel = createHostPortPanel(basePanel);
        addHostPanel(hostPortPanel);
        addPortPanel(hostPortPanel);
        addDatabasePanel(basePanel);
        addDatabaseUrlPanel(basePanel);
        addUsernamePanel(basePanel);
        addPasswordPanel(basePanel);
        addTestButton(basePanel);

        panel.add(basePanel, "North");
    }

    private GridBagConstraints getGBC() {
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.BASELINE;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1.0D;
        return gbc;
    }

    private GridBagConstraints getGBCi() {
        final GridBagConstraints gbc = getGBC();
        gbc.insets = new Insets(5, 5, 5, 5);
        return gbc;
    }

    private void addNamePanel(final JPanel basePanel) {
        name = new FieldPanel(new JBTextField(), "Name:", null, null, null);
        ((JBTextField) name.getComponent()).getEmptyText().setText("<name>");
        basePanel.add(name, getGBCi());
    }

    private void addHostPanel(final JPanel hostPortPanel) {
        host = new FieldPanel(new JBTextField(), "Host:", null, null, null);
        ((JBTextField) host.getComponent()).getEmptyText().setText("<database host>");

        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.BASELINE;
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        gbc.weightx = 4.0D;
        gbc.insets = new Insets(0, 0, 0, 5);

        hostPortPanel.add(host, gbc);
    }

    private void addPortPanel(final JPanel hostPortPanel) {
        port = new FieldPanel(new JBTextField(), "Port:", null, null, null);
        ((JBTextField) port.getComponent()).getEmptyText().setText("<port>");

        final GridBagConstraints gbc = getGBC();
        gbc.insets = new Insets(0, 5, 0, 0);

        hostPortPanel.add(port, gbc);
    }

    private void addDatabasePanel(final JPanel basePanel) {
        database = new FieldPanel(new JBTextField(), "Database:", null, null, null);
        ((JBTextField) database.getComponent()).getEmptyText().setText("<database name>");

        final GridBagConstraints gbc = getGBC();
        gbc.insets = new Insets(5, 5, 5, 5);

        basePanel.add(database, gbc);
    }

    private void addDatabaseUrlPanel(final JPanel basePanel) {
        databaseUrl = new FieldPanel("The following URL will be used to connect:", null, null, null);
        databaseUrl.getFieldLabel().setFont(UIUtil.getLabelFont(FontSize.MINI));
        databaseUrl.getFieldLabel().setIcon(UIUtil.getBalloonInformationIcon());
        databaseUrl.setEditable(false);

        basePanel.add(databaseUrl, getGBCi());
    }

    private void addUsernamePanel(final JPanel basePanel) {
        username = new FieldPanel(new JBTextField(30), "Username", null, null, null);

        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.BASELINE;
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        gbc.weightx = 1.0D;
        gbc.insets = new Insets(5, 5, 0, 5);

        basePanel.add(username, gbc);
    }

    private void addPasswordPanel(final JPanel basePanel) {
        password = new FieldPanel(new JPasswordField(30), "Password", null, null, null);

        final GridBagConstraints gbc = getGBC();
        gbc.insets = new Insets(5, 5, 0, 5);

        basePanel.add(password, gbc);
    }

    private void addTestButton(final JPanel basePanel) {
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.BASELINE;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1.0D;
        gbc.insets = new Insets(5, 5, 5, 5);

        final JPanel test = new JPanel(new BorderLayout());
        test.add(new JButton(new AbstractAction("Test Connection") {
            public void actionPerformed(@NotNull ActionEvent e) {
//                DataSourceConfigurable.this.testConnection();
                System.out.println("test");
            }
        }), "East");
        basePanel.add(test, gbc);
    }

    private JPanel createHostPortPanel(final JPanel basePanel) {
        final JPanel hostPortPanel = new JPanel(new GridBagLayout());
        basePanel.add(hostPortPanel, getGBCi());
        return hostPortPanel;
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
        return name;
    }

    @Nls @Override
    public String getDisplayName() {
        return "Link New Database to Project";
    }

    @Nullable @Override
    public String getHelpTopic() {
        return null;
    }

    @Nullable @Override
    public JComponent createComponent() {
        return panel;
    }

    @Override
    public void apply() throws ConfigurationException {}

    @Override
    public void reset() {}

    @Override
    public void disposeUIResources() {}
}