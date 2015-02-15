package com.kol.dbPlugin.dao;

import com.kol.dbPlugin.beans.dbObjects.Change;
import com.kol.dbPlugin.beans.dbObjects.Function;
import com.kol.dbPlugin.beans.dbObjects.Procedure;
import com.kol.dbPlugin.beans.dbObjects.View;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface DBPluginDAO<SP extends Procedure, F extends Function, V extends View, TC extends Change> {

    void createPluginTableIfNotExists();

    Collection<SP> getStoredProcedures(@NotNull String dbName);

    Collection<F> getFunctions(@NotNull String dbName);

    Collection<V> getViews(@NotNull String dbName);

    Collection<TC> getTableChanges(@NotNull String dbName);
}
