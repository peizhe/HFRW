package com.kol.dbPlugin.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.kol.dbPlugin.Credentials;
import com.kol.dbPlugin.Settings;
import com.kol.dbPlugin.managers.LinkNewDatabaseToProject;
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

        final SingleConfigurableEditor ed = new SingleConfigurableEditor(project, new LinkNewDatabaseToProject());
        ed.show();
//        final LinkDatabaseWizard w = new LinkDatabaseWizard("Configuring DBTool Plugin", project, FSSettingsManager.instance());
//        w.show();

        /*Credentials credentials = safeGetCredentials("general");
        if(null == credentials) {
            final CredentialsDialog auth = new CredentialsDialog(project, "Database Credentials", null, null);
            auth.show();
            credentials = new Credentials(auth.getUsername(), auth.getPassword());
            safeSaveCredentials(credentials);
        }*/
        /*final SQLWM test = new SQLWM("Test", project);
        final WizardDialog<WizardModel> dialog = new WizardDialog<>(project, true, test);
        dialog.show();

        final String url = Messages.showInputDialog(project, "Input URL to your database", "Database URL", Messages.getQuestionIcon());

        final ConnectionData data = new ConnectionData("jdbc:mysql://" + url, credentials.getUsername(), credentials.getPassword(), "com.mysql.jdbc.Driver");
        final boolean isOk = DatabaseConnector.isCorrectDBProperties(data);
        final DatabaseOperations jdbc = new DatabaseTemplate(data);

        final Integer num = jdbc.queryForObject("select count(*) from information_schema.TRIGGERS", Integer.class);
        final List<String> strings = jdbc.queryForList("SELECT class_code FROM recognition_data_class", String.class);

        System.out.println("OK");*/
    }
}
