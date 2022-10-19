package com.epam.cash.register.controller.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

/**
 * Session attribute listener. Implementation of {@link HttpSessionAttributeListener}.
 */
@WebListener
public class SessionListenerImpl implements HttpSessionAttributeListener {

    private final static Logger log = LogManager.getLogger(ServletContextListenerImpl.class);

    @Override
    public void attributeAdded(HttpSessionBindingEvent event) {
        log.trace("Session attribute added: {} : {}", event.getName(), event.getValue().toString());
    }
}
