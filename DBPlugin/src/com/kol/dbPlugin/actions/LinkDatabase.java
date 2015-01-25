package com.kol.dbPlugin.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.kol.dbPlugin.LinkDatabaseError;
import com.kol.dbPlugin.beans.Credentials;
import com.kol.dbPlugin.beans.Settings;
import com.kol.dbPlugin.dialogs.LinkNewDatabaseToProject;
import com.kol.dbPlugin.dialogs.SingleConfigurableEditor;
import org.jetbrains.annotations.NotNull;

public class LinkDatabase extends BaseAction {

    private static final String DB_DEFAULT_PORT = "3306";
    private static final String DB_DEFAULT_HOST = "localhost";

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
                new LinkNewDatabaseToProject(project, credentialsManager.get(Credentials.placeholder(DB_DEFAULT_HOST)), Settings.placeholder(DB_DEFAULT_HOST, DB_DEFAULT_PORT)),
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
//        final LinkDatabaseWizard w = new LinkDatabaseWizard("Configuring DBTool Plugin", project, FSSettingsManager.instance());
//        w.show();

        /*Credentials credentials = safeGetCredentials("general");
        if(null == credentials) {
            final CredentialsDialog auth = new CredentialsDialog(project, "Database Credentials", null, null);
            auth.show();
            credentials = new Credentials(auth.getUsername(), auth.getPassword());
            safeSaveCredentials(credentials);
        }
        final String url = Messages.showInputDialog(project, "Input URL to your database", "Database URL", Messages.getQuestionIcon());

        final ConnectionData data = new ConnectionData("jdbc:mysql://" + url, credentials.getUsername(), credentials.getPassword(), "com.mysql.jdbc.Driver");
        final boolean isOk = DatabaseConnector.isCorrectDBProperties(data);
        final DatabaseOperations jdbc = new DatabaseTemplate(data);

        final Integer num = jdbc.queryForObject("select count(*) from information_schema.TRIGGERS", Integer.class);
        final List<String> strings = jdbc.queryForList("SELECT class_code FROM recognition_data_class", String.class);

        System.out.println("OK");*/
    }
}
