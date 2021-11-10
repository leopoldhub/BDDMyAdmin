package fr.univlille.iutinfo.da2i.hubertleopold.web.autonomie.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseSingleton {

    private static DatabaseSingleton singleton;
    private Connection connection;
    private Thread shutdownHook;

    public DatabaseSingleton(String driverClass, String host, String username, String password) throws ClassNotFoundException, SQLException {
        Class.forName(driverClass);
        if (singleton != null && connection != null && !connection.isClosed()) return;

        if (shutdownHook != null) Runtime.getRuntime().removeShutdownHook(shutdownHook);

        singleton = this;
        connection = DriverManager.getConnection(host, username, password);
        shutdownHook = new Thread(() -> {
            try {
                connection.close();
            } catch (SQLException e) {
            }
        });

        Runtime.getRuntime().addShutdownHook(shutdownHook);
    }

    public static DatabaseSingleton getSingleton() {
        return singleton;
    }

    public Connection getConnection() {
        return connection;
    }

}
