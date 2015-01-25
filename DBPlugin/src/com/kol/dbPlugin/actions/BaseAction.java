package com.kol.dbPlugin.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.kol.dbPlugin.components.DBToolComponent;
import com.kol.dbPlugin.managers.CredentialsManager;
import com.kol.dbPlugin.managers.SettingsManager;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class BaseAction extends AnAction {

    private static final String PLUGIN_DIRECTORY_NAME = "database";
    private static final String CREDENTIALS_DIRECTORY_NAME = ".DBToolPlugin";
    private static final Path CREDENTIALS_BASE_PATH = Paths.get(System.getProperty("user.home"));

    protected DBToolComponent component;
    protected SettingsManager settingsManager;
    protected CredentialsManager credentialsManager;

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        component = ApplicationManager.getApplication().getComponent(DBToolComponent.class);
        credentialsManager = new CredentialsManager(CREDENTIALS_BASE_PATH, CREDENTIALS_DIRECTORY_NAME);
        settingsManager = new SettingsManager(Paths.get(event.getProject().getBasePath()), PLUGIN_DIRECTORY_NAME);
    }
}