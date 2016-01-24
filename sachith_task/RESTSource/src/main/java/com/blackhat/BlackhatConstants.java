package com.blackhat;

import javax.servlet.ServletContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 *
 * @author Sachith Dickwella
 */
public abstract class BlackhatConstants {

    public static String SESSION_FACTORY = "SessionFactory";

    public static final Session getSession(ServletContext sc) {        
        return ((SessionFactory) sc.getAttribute(SESSION_FACTORY)).openSession();
    }
}
