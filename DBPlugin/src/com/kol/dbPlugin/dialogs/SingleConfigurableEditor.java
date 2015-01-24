package com.kol.dbPlugin.dialogs;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import javax.swing.JComponent;

import com.kol.dbPlugin.Credentials;
import com.kol.dbPlugin.Settings;
import com.kol.dbPlugin.managers.LinkNewDatabaseToProject;
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
//                if(this.project != null) {
//                    Messages.showMessageDialog(this.project, var2.getMessage(), var2.getTitle(), Messages.getErrorIcon());
//                } else {
//                    Messages.showMessageDialog(this.getRootPane(), var2.getMessage(), var2.getTitle(), Messages.getErrorIcon());
//                }
        super.doOKAction();
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