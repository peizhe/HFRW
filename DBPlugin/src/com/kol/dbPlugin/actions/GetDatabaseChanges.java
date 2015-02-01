package com.kol.dbPlugin.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

public class GetDatabaseChanges extends BaseAction {

    public GetDatabaseChanges(){}

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        super.actionPerformed(event);
        final VirtualFile actionFile = event.getData(LangDataKeys.VIRTUAL_FILE);
        int i = 0;
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
