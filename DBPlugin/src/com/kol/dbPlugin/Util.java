package com.kol.dbPlugin;

import com.kol.dbPlugin.interfaces.Constants;
import org.jetbrains.annotations.Nullable;

public class Util implements Constants {

    private Util() {}
    
    public static final class Str {
        private Str() {}
        
        public static boolean isEmpty(@Nullable final String str) {
            return null == str || str.trim().isEmpty();
        }

        public static boolean isNonEmpty(@Nullable final String str) {
            return !isEmpty(str);
        }
    }
    
    public static final class Database {
        private Database() {}

        public static String makeDBUrl(@Nullable final String host, @Nullable final String port, @Nullable final String database) {
            final StringBuilder sb = new StringBuilder(DB_URL_PREFIX);
            if(null != host) {
                sb.append(host);
            }
            if(Str.isNonEmpty(port)) {
                sb.append(DB_URL_PORT_PREFIX).append(port);
            }
            if(Str.isNonEmpty(database)) {
                sb.append(DB_URL_DATABASE_SEPARATOR).append(database);
            }
            return sb.toString();
        }

        public static String makeDBFolderName(@Nullable final String host, @Nullable final String database) {
            final StringBuilder sb = new StringBuilder();
            if(Str.isNonEmpty(database)) {
                sb.append(database);
            }
            if(null != host) {
                sb.append(DB_NAME_HOST_SEPARATOR).append(host);
            }
            return sb.toString();
        }
    }
}