package com.t20simulator.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Singleton JDBC connection factory that reads credentials from config.properties.
 * All DAO classes call this class when they need to execute SQL.
 */
public final class DBConnection {
    // volatile keeps singleton creation safe when multiple threads access it.
    private static volatile DBConnection instance;

    private final String url;
    private final String user;
    private final String password;

    private DBConnection() {
        Properties properties = new Properties();
        // config.properties is loaded from src/main/resources at runtime.
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (inputStream == null) {
                throw new IllegalStateException("config.properties not found in resources.");
            }
            properties.load(inputStream);
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to load database configuration.", exception);
        }

        this.url = properties.getProperty("db.url");
        this.user = properties.getProperty("db.user");
        this.password = properties.getProperty("db.password");
    }

    public static DBConnection getInstance() {
        // Double-check locking creates the DBConnection object only once.
        if (instance == null) {
            synchronized (DBConnection.class) {
                if (instance == null) {
                    instance = new DBConnection();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        // DriverManager opens a new JDBC connection using MySQL URL, username, and password.
        return DriverManager.getConnection(url, user, password);
    }
}
