package com.kol.dbPlugin.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.kol.dbPlugin.C;
import com.kol.dbPlugin.components.DBToolComponent;
import com.kol.dbPlugin.managers.CredentialsManager;
import com.kol.dbPlugin.managers.SettingsManager;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Paths;

public abstract class BaseAction extends AnAction {

    protected DBToolComponent component;
    protected SettingsManager settingsManager;
    protected CredentialsManager credentialsManager;

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        component = ApplicationManager.getApplication().getComponent(DBToolComponent.class);
        credentialsManager = component.getCredentialsManager();
        settingsManager = new SettingsManager(Paths.get(event.getProject().getBasePath()), C.PLUGIN_DIRECTORY_NAME);
    }
}