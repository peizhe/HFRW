package com.kol.dbPlugin.dialogs;

import com.intellij.openapi.options.BaseConfigurableWithChangeSupport;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.FieldPanel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.UIUtil;
import com.intellij.util.ui.UIUtil.FontSize;
import com.kol.dbPlugin.Util;
import com.kol.dbPlugin.beans.Credentials;
import com.kol.dbPlugin.beans.Settings;
import com.kol.dbPlugin.jdbc.DatabaseConnector;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class LinkNewDatabaseToProject extends BaseConfigurableWithChangeSupport {

    private JPanel panel;
    private Project project;
    private FieldPanel host;
    private FieldPanel port;
    private FieldPanel database;
    private FieldPanel username;
    private FieldPanel password;
    private FieldPanel databaseUrl;

    public LinkNewDatabaseToProject(@NotNull Project project, @NotNull Credentials defaultCredentials, @NotNull Settings defaultSettings) {
        this.project = project;

        createCenterPanel();
        host.setText(defaultSettings.getHost());
        port.setText(defaultSettings.getPort());
        username.setText(defaultCredentials.getUsername());
        password.setText(defaultCredentials.getPassword());

        updateURLFromParameters();

        final KeyAdapter adapter = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                updateURLFromParameters();
            }
        };
        host.getTextField().addKeyListener(adapter);
        port.getTextField().addKeyListener(adapter);
        database.getTextField().addKeyListener(adapter);
    }

    public Settings getSettings() {
        return new Settings(host.getText(), port.getText(), database.getText());
    }

    public Credentials getCredentials() {
        return new Credentials(host.getText(), username.getText(), password.getText());
    }

    private void updateURLFromParameters() {
        databaseUrl.setText(Util.Database.makeDBUrl(host.getText(), port.getText(), database.getText()));
    }

    private void createCenterPanel() {
        panel = new JPanel(new BorderLayout(0, 5));
        final JPanel basePanel = new JPanel(new GridBagLayout());

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
                final String message = DatabaseConnector.testConnect(getSettings(), getCredentials());
                if(Util.Str.isEmpty(message)) {
                    Messages.showMessageDialog(project, "Connection Successful", "Successful", Messages.getInformationIcon());
                } else {
                    Messages.showErrorDialog(project, message, "Connection Failed");
                }
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
        return host;
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