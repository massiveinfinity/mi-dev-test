package com.blackhat.listener;

import static com.blackhat.BlackhatConstants.SESSION_FACTORY;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.apache.log4j.PropertyConfigurator;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

/**
 *
 * @author Sachith Dickwella
 */
@WebListener
public class ContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        /**
         * Log4j property configuration.
         */
        PropertyConfigurator.configure(getClass().getResource("/log/log4j.properties"));
        /**
         * Hibernate property configuration.
         */
        Configuration config = new Configuration().configure("/hbm/hibernate.cfg.xml");
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(config.getProperties()).build();
        SessionFactory sessionFactory = config.buildSessionFactory(serviceRegistry);

        sce.getServletContext().setAttribute(SESSION_FACTORY, sessionFactory);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        SessionFactory sessionFactory = (SessionFactory) sce.getServletContext().getAttribute(SESSION_FACTORY);
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }
}
