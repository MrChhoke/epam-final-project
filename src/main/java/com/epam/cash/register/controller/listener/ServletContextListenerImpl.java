package com.epam.cash.register.controller.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ServletContextListenerImpl implements ServletContextListener {

    private final static Logger log = LogManager.getLogger(ServletContextListenerImpl.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.info("The server started");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        log.info("The server destroyed");
    }
}
