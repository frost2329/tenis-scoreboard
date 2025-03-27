package by.frostetsky.util;

import by.frostetsky.entity.FinishedMatch;
import by.frostetsky.entity.Player;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static final SessionFactory SESSION_FACTORY = buildSessionFactory();

    private HibernateUtil() {
    }

    private static SessionFactory buildSessionFactory(){
        return new Configuration()
                .addAnnotatedClass(Player.class)
                .addAnnotatedClass(FinishedMatch.class)
                .configure("hibernate.cfg.xml").buildSessionFactory();
    }

    public static SessionFactory getSessionFactory() {
        return SESSION_FACTORY;
    }
}
