package com.kol.dbPlugin.dialogs;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.kol.dbPlugin.interfaces.LinkDatabaseOkCallback;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class SingleConfigurableEditor extends DialogWrapper {
    private LinkDatabaseOkCallback okCallback;
    private LinkNewDatabaseToProject configurable;

    public SingleConfigurableEditor(@Nullable Project project, LinkNewDatabaseToProject configurable, @NotNull IdeModalityType type, @Nullable LinkDatabaseOkCallback okCallback) {
        super(project, true, type);
        this.configurable = configurable;
        this.setTitle(configurable.getDisplayName());
        this.init();
        this.configurable.reset();
        this.okCallback = okCallback;
    }

    public SingleConfigurableEditor(@Nullable Project project, LinkNewDatabaseToProject configurable, @Nullable LinkDatabaseOkCallback okCallback) {
        this(project, configurable, IdeModalityType.IDE, okCallback);
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
        if(null != okCallback) {
            if(okCallback.apply(configurable.getCredentials(), configurable.getSettings())) {
                super.doOKAction();
            }
        } else {
            super.doOKAction();
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