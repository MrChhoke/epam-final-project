package com.epam.cash.register.util;


import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DBUtil {

    private final static String RESOURCE = "java:/comp/env/jdbc/CASH_REGISTER";

    public static DataSource getDataSource() throws NamingException {
        InitialContext ctx = new InitialContext();
        DataSource ds = (DataSource) ctx.lookup(RESOURCE);
        return ds;
    }

    public static Connection getConnection() throws NamingException, SQLException {
        return getDataSource().getConnection();
    }


}
