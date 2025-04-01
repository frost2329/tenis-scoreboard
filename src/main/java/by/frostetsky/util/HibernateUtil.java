package by.frostetsky.util;

import by.frostetsky.entity.FinishedMatch;
import by.frostetsky.entity.Player;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

@Slf4j
public class HibernateUtil {
    private static final SessionFactory SESSION_FACTORY = buildSessionFactory();

    private HibernateUtil() {
    }

    private static SessionFactory buildSessionFactory(){
        log.info("SessionFactory is configured");
        return new Configuration()
                .addAnnotatedClass(Player.class)
                .addAnnotatedClass(FinishedMatch.class)
                .configure("hibernate.cfg.xml").buildSessionFactory();
    }

    public static SessionFactory getSessionFactory() {
        log.info("SessionFactory has been requested");
        return SESSION_FACTORY;
    }
}
