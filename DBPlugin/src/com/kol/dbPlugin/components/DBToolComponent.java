package com.kol.dbPlugin.components;

import com.intellij.openapi.components.ApplicationComponent;
import com.kol.dbPlugin.C;
import com.kol.dbPlugin.managers.CredentialsManager;
import org.jetbrains.annotations.NotNull;

public class DBToolComponent implements ApplicationComponent {

    protected CredentialsManager credentialsManager;

    @Override
    public void initComponent() {
        credentialsManager = new CredentialsManager(C.CREDENTIALS_BASE_PATH, C.CREDENTIALS_DIRECTORY_NAME);
    }

    @Override
    public void disposeComponent() {

    }

    @NotNull
    @Override
    public String getComponentName() {
        return "DBToolComponent";
    }

    public CredentialsManager getCredentialsManager() {
        return credentialsManager;
    }
}