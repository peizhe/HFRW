package com.kol.dbPlugin.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.kol.dbPlugin.DBObjects;
import com.kol.dbPlugin.Util;
import com.kol.dbPlugin.beans.ConnectionData;
import com.kol.dbPlugin.beans.Credentials;
import com.kol.dbPlugin.beans.DBConnectInfo;
import com.kol.dbPlugin.beans.Settings;
import com.kol.dbPlugin.beans.dbObjects.mysql.MySQLProcedure;
import com.kol.dbPlugin.dao.MySQLDao;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;

public class GetDatabaseChanges extends BaseAction {

    public GetDatabaseChanges(){}

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        super.actionPerformed(event);
        final DBConnectInfo connectInfo = Util.FS.getConnectInfo(event);
        if(null == connectInfo) {
            return;
        }

        final Credentials credentials = credentialsManager.get(new Credentials(connectInfo));
        final Settings settings = settingsManager.get(new Settings(connectInfo));
        final ConnectionData data = Util.Database.makeConnectionData(credentials, settings);
        final MySQLDao dao = new MySQLDao(data);

        final Collection<MySQLProcedure> procedures = dao.getStoredProcedures(settings.getDatabase());
        procedures.forEach(p -> createSQLScript(p.getName(), p.makeSQL(), connectInfo));
    }

    private void createSQLScript(@NotNull final String name, @NotNull final String body, @NotNull final DBConnectInfo info) {
        final Path procedure = info.path().resolve(DBObjects.PROCEDURE.getDirName()).resolve(name + ".sql");
        try {
            Util.FS.createFile(procedure);
            Files.write(procedure, body.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
