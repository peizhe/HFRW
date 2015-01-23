package com.kol.dbPlugin.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.kol.dbPlugin.Credentials;
import com.kol.dbPlugin.dialogs.CredentialsDialog;
import org.jetbrains.annotations.NotNull;

public class LinkDatabase extends BaseAction {

    public LinkDatabase(){}

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        final Project project = event.getProject();

        final Credentials credentialsOld = safeGetCredentials("general");
        System.out.println(credentialsOld);
        final CredentialsDialog auth = new CredentialsDialog(
                project,
                "Database Credentials",
                credentialsOld.getUsername(),
                credentialsOld.getPassword()
        );
        auth.show();

        safeSaveCredentials(new Credentials(auth.getUsername(), auth.getPassword()));
        System.out.println("OK");
    }
}
