package com.epam.cash.register.util;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DBUtil {

    private final static String RESOURCE = "java:/comp/env/jdbc/CASH_REGISTER";

    private static final Logger log = LogManager.getLogger(DBUtil.class);

    private static DataSource dataSource;

    public static DataSource getDataSource() {
        if (dataSource == null) {
            try {
                InitialContext ctx = new InitialContext();
                dataSource = (DataSource) ctx.lookup(RESOURCE);
            } catch (NamingException exp) {
                log.error("A wrong with datasourse {0}", exp);
            }
        }
        log.trace("Getting datasource from Tomcat");
        return dataSource;
    }

    public static Connection getConnection() throws SQLException {
        return getDataSource().getConnection();
    }


}
