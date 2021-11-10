package fr.univlille.iutinfo.da2i.hubertleopold.web.autonomie.listeners;

import fr.univlille.iutinfo.da2i.hubertleopold.web.autonomie.database.DatabaseSingleton;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.sql.SQLException;

public class ContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            new DatabaseSingleton(
                    sce.getServletContext().getInitParameter("db.driverClassName"),
                    sce.getServletContext().getInitParameter("db.url"),
                    sce.getServletContext().getInitParameter("db.username"),
                    sce.getServletContext().getInitParameter("db.password")
                    );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            DatabaseSingleton.connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
