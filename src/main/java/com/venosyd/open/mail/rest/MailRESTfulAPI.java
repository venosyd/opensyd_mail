package com.venosyd.open.mail.rest;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * @author sergio lisan <sels@venosyd.com>
 */
@ApplicationPath(MailRS.MAIL_BASE_URI)
public class MailRESTfulAPI extends Application {

    public Set<Class<?>> getClasses() {
        var classes = new HashSet<Class<?>>();

        classes.add(MailRSImpl.class);

        return classes;
    }
}
