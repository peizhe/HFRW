package com.kol.dbPlugin.components;

import com.intellij.openapi.components.ApplicationComponent;
import org.jetbrains.annotations.NotNull;

public class DBToolComponent implements ApplicationComponent {

    @Override
    public void initComponent() {

    }

    @Override
    public void disposeComponent() {

    }

    @NotNull
    @Override
    public String getComponentName() {
        return "DBToolComponent";
    }
}
