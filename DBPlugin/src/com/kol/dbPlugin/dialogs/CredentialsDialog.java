package com.kol.dbPlugin.dialogs;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.util.net.AuthenticationPanel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class CredentialsDialog extends DialogWrapper {

    private final AuthenticationPanel authPanel;

    public CredentialsDialog(@NotNull Project project, @NotNull String title, @Nullable String login, @Nullable String password) {
        super(project, false);
        setTitle(title);
        authPanel = new AuthenticationPanel(null, login, password, null);
        init();
    }

    protected JComponent createCenterPanel() {
        return authPanel;
    }

    public JComponent getPreferredFocusedComponent() {
        return authPanel.getPreferredFocusedComponent();
    }

    public String getUsername() {
        return authPanel.getLogin();
    }

    public String getPassword() {
        return String.valueOf(authPanel.getPassword());
    }
}