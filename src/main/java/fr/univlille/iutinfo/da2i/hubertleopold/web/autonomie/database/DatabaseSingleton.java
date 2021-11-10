package fr.univlille.iutinfo.da2i.hubertleopold.web.autonomie.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseSingleton {

    public static Connection connection;

    public DatabaseSingleton(String driverClass, String host, String username, String password) throws ClassNotFoundException, SQLException {
        if (connection != null) return;
        Class.forName(driverClass);
        connection = DriverManager.getConnection(host, username, password);
    }

}
