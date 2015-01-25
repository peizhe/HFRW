package com.kol.dbPlugin.dialogs;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import javax.swing.JComponent;

import com.intellij.openapi.ui.Messages;
import com.kol.dbPlugin.beans.Credentials;
import com.kol.dbPlugin.beans.LinkDatabaseError;
import com.kol.dbPlugin.beans.Settings;
import com.kol.dbPlugin.managers.FSSettingsManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SingleConfigurableEditor extends DialogWrapper {
    private Project project;
    private LinkNewDatabaseToProject configurable;

    public SingleConfigurableEditor(@Nullable Project project, LinkNewDatabaseToProject configurable, @NotNull IdeModalityType type) {
        super(project, true, type);
        this.project = project;
        this.configurable = configurable;
        this.setTitle(configurable.getDisplayName());
        this.init();
        this.configurable.reset();
    }

    public SingleConfigurableEditor(@Nullable Project project, LinkNewDatabaseToProject configurable) {
        this(project, configurable, IdeModalityType.IDE);
    }

    @NotNull
    protected Action[] createActions() {
        List<Action> actions = new ArrayList<>();
        actions.add(this.getOKAction());
        actions.add(this.getCancelAction());
        return actions.toArray(new Action[actions.size()]);
    }

    public void doCancelAction() {
        super.doCancelAction();
    }

    protected void doOKAction() {
        final Settings settings = configurable.getSettings();
        final Credentials credentials = configurable.getCredentials();
        final LinkDatabaseError status = LinkDatabaseError.validate(project, settings, credentials);
        final FSSettingsManager manager = FSSettingsManager.instance();
        if(null == status) {
            manager.saveCredentials(credentials);
            manager.saveSettings(project, settings);
            manager.createDatabaseFolder(project, settings);
            super.doOKAction();
        } else {
            if(status.isBlocked()) {
                Messages.showErrorDialog(project, status.getMessage(), "Error");
            } else {
                final int warning = Messages.showOkCancelDialog(project, status.getMessage(), "Warning", Messages.getWarningIcon());
                if(0 == warning) {
                    manager.createDatabaseFolder(project, settings);
                    super.doOKAction();
                }
            }
        }
    }

    protected JComponent createCenterPanel() {
        return configurable.createComponent();
    }

    public JComponent getPreferredFocusedComponent() {
        return configurable.getPreferredFocusedComponent();
    }

    public void dispose() {
        super.dispose();
        this.configurable.disposeUIResources();
        this.configurable = null;
    }
}