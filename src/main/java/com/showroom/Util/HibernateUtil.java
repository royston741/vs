package com.showroom.Util;

import com.showroom.Entity.Customer;
import com.showroom.Entity.Order;
import com.showroom.Entity.Vehicle;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class HibernateUtil {
    private static SessionFactory sessionFactory;
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            Properties properties = new Properties();

            try {
                properties.load(new FileInputStream("src/main/resources/application.properties"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Configuration cfg = new Configuration();
            cfg.setProperties(properties);
            cfg.addAnnotatedClass(Vehicle.class).addAnnotatedClass(Customer.class).addAnnotatedClass(Order.class);

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(cfg.getProperties()).build();
            sessionFactory = cfg.buildSessionFactory(serviceRegistry);
        }

        return sessionFactory;
    }

}
