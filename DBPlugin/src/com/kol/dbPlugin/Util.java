package com.kol.dbPlugin;

import org.jetbrains.annotations.Nullable;

public class Util {

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
            final StringBuilder sb = new StringBuilder(C.DB_URL_PREFIX);
            if(null != host) {
                sb.append(host);
            }
            if(Str.isNonEmpty(port)) {
                sb.append(C.DB_URL_PORT_PREFIX).append(port);
            }
            if(Str.isNonEmpty(database)) {
                sb.append(C.DB_URL_DATABASE_SEPARATOR).append(database);
            }
            return sb.toString();
        }
    }
}