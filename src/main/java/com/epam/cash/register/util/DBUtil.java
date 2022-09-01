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

    public static DataSource getDataSource() throws NamingException {
        InitialContext ctx = new InitialContext();
        DataSource ds = (DataSource) ctx.lookup(RESOURCE);
        log.trace("Getting datasource from Tomcat");
        return ds;
    }

    public static Connection getConnection() throws NamingException, SQLException {
        return getDataSource().getConnection();
    }


}
