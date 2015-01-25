package com.kol.dbPlugin.components;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.project.ProjectManager;
import com.kol.dbPlugin.managers.CredentialsManager;
import com.kol.dbPlugin.managers.SettingsManager;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.nio.file.Paths;

public class DBToolComponent implements ApplicationComponent {

    private static final String PLUGIN_DIRECTORY_NAME = "database";
    private static final String CREDENTIALS_DIRECTORY_NAME = ".DBToolPlugin";
    private static final Path CREDENTIALS_BASE_PATH = Paths.get(System.getProperty("user.home"));

    private SettingsManager settingsManager;
    private CredentialsManager credentialsManager;

    @Override
    public void initComponent() {
        final String basePath = ProjectManager.getInstance().getDefaultProject().getBasePath();
        settingsManager = new SettingsManager(Paths.get(basePath), PLUGIN_DIRECTORY_NAME);
        credentialsManager = new CredentialsManager(CREDENTIALS_BASE_PATH, CREDENTIALS_DIRECTORY_NAME);
    }

    @Override
    public void disposeComponent() {

    }

    @NotNull
    @Override
    public String getComponentName() {
        return "DBToolComponent";
    }

    public SettingsManager getSettingsManager() {
        return settingsManager;
    }

    public CredentialsManager getCredentialsManager() {
        return credentialsManager;
    }
}