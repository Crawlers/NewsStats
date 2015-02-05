package com.cse10.database;

/**
 * Created by TharinduWijewardane on 02.07.2014.
 */


import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.util.Properties;

public class HibernateUtil {

    private static Logger logger = Logger.getLogger(HibernateUtil.class);

    private static SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {

            Properties dbConnectionProperties = new Properties();
            try {
                dbConnectionProperties.load(ClassLoader.getSystemClassLoader().getResourceAsStream("dbConnection.properties"));
            } catch (Exception e) {
                logger.info("Error: dbConnection.properties file read error: ", e);
            }

            Configuration configuration = new Configuration().mergeProperties(dbConnectionProperties).configure();
            StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
            SessionFactory sessionFactory = configuration.buildSessionFactory(builder.build());

            return sessionFactory;

        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            logger.info("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory.isClosed()) {
            sessionFactory = buildSessionFactory();
        }
        return sessionFactory;
    }

    public static void shutdown() {
        // Close caches and connection pools
        getSessionFactory().close();
    }

}

//try{
//        Configuration configuration = new Configuration().configure();
//        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
//        SessionFactory sessionFactory = configuration.buildSessionFactory(builder.build());
//        return sessionFactory;
//        } catch (Throwable ex) {
//        // Make sure you log the exception, as it might be swallowed
//        System.err.println("Initial SessionFactory creation failed." + ex);
//        throw new ExceptionInInitializerError(ex);
//        }

//        try {
//            // Create the SessionFactory from hibernate.cfg.xml
//            return new Configuration().configure().buildSessionFactory();
//          } catch (Throwable ex) {
//        // Make sure you log the exception, as it might be swallowed
//        System.err.println("Initial SessionFactory creation failed." + ex);
//        throw new ExceptionInInitializerError(ex);
//        }
