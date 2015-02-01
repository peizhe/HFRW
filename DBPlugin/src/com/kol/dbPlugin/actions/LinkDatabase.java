package com.kol.dbPlugin.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.kol.dbPlugin.C;
import com.kol.dbPlugin.LinkDatabaseError;
import com.kol.dbPlugin.beans.Credentials;
import com.kol.dbPlugin.beans.Settings;
import com.kol.dbPlugin.dialogs.LinkNewDatabaseToProject;
import com.kol.dbPlugin.dialogs.SingleConfigurableEditor;
import org.jetbrains.annotations.NotNull;

public class LinkDatabase extends BaseAction {

    public LinkDatabase(){}

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        super.actionPerformed(event);
        final Project project = event.getProject();
        if(null == project) {
            return;
        }

        final SingleConfigurableEditor editor = new SingleConfigurableEditor(
                project,
                new LinkNewDatabaseToProject(project, credentialsManager.get(Credentials.placeholder(C.DB_DEFAULT_HOST)), Settings.placeholder(C.DB_DEFAULT_HOST, C.DB_DEFAULT_PORT)),
                (credentials, settings) -> {
                    final LinkDatabaseError status = LinkDatabaseError.validate(project, settings, credentials);
                    if(null == status) {
                        credentialsManager.save(credentials);
                        settingsManager.save(settings);
                        return true;
                    } else {
                        if(status.isBlocked()) {
                            Messages.showErrorDialog(project, status.getMessage(), "Error");
                        } else {
                            final int warning = Messages.showOkCancelDialog(project, status.getMessage(), "Warning", Messages.getWarningIcon());
                            if(0 == warning) {
                                settingsManager.save(settings);
                                return true;
                            }
                        }
                    }
                    return false;
                }
        );
        editor.show();
    }
}
