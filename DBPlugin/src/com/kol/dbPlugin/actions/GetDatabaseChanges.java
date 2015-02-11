package com.kol.dbPlugin.actions;

import com.google.common.base.Splitter;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.kol.dbPlugin.C;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Paths;

public class GetDatabaseChanges extends BaseAction {

    public GetDatabaseChanges(){}

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        super.actionPerformed(event);
        final Project project = event.getProject();
        final VirtualFile actionFile = event.getData(LangDataKeys.VIRTUAL_FILE);
        if(null == actionFile || null == project || null == actionFile.getCanonicalPath()) {
            return;
        }
        final String canonicalPath = Paths.get(actionFile.getCanonicalPath()).toString();
        final String basePath = Paths.get(project.getBasePath()).toString();
        final String replace = canonicalPath.replace(basePath, "").replaceAll("^\\\\", "");
        final String dataFolders;
        if(replace.startsWith(C.PLUGIN_DIRECTORY_NAME)) {
            dataFolders = replace.replaceAll("^" + C.PLUGIN_DIRECTORY_NAME, "");
        } else {
            throw new RuntimeException("");
        }

        final Iterable<String> split = Splitter.on("\\").omitEmptyStrings().limit(2).split(dataFolders);
        System.out.println(split);

//        event.getRequiredData()

        /*final ConnectionData data = new ConnectionData(
                "jdbc:mysql://" + url,
                credentials.getUsername(),
                credentials.getPassword(),
                "com.mysql.jdbc.Driver"
        );
        final DatabaseOperations jdbc = new DatabaseTemplate(data);
        final Integer num = jdbc.queryForObject("select count(*) from information_schema.TRIGGERS", Integer.class);
*/
        int i = 0;
        /*Credentials credentials = safeGetCredentials("general");
        if(null == credentials) {
            final CredentialsDialog auth = new CredentialsDialog(project, "Database Credentials", null, null);
            auth.show();
            credentials = new Credentials(auth.getUsername(), auth.getPassword());
            safeSaveCredentials(credentials);
        }
        final ConnectionData data = new ConnectionData("jdbc:mysql://" + url, credentials.getUsername(), credentials.getPassword(), "com.mysql.jdbc.Driver");
        final boolean isOk = DatabaseConnector.isCorrectDBProperties(data);
        final DatabaseOperations jdbc = new DatabaseTemplate(data);

        final Integer num = jdbc.queryForObject("select count(*) from information_schema.TRIGGERS", Integer.class);
        final List<String> strings = jdbc.queryForList("SELECT class_code FROM recognition_data_class", String.class);

        System.out.println("OK");*/
    }
}
