package com.kol.dbPlugin.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.kol.dbPlugin.managers.FSSettingsManager;
import org.jetbrains.annotations.NotNull;

public abstract class BaseAction extends AnAction {

    protected FSSettingsManager manager;

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        manager = FSSettingsManager.instance();
    }
}