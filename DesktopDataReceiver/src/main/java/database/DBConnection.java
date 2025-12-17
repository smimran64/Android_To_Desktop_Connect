package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL = "jdbc:sqlite:database.db";

    public static Connection getConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(URL);

            System.out.println("Database connected successfully.");
            return conn;

        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Database connection failed: " + e.getMessage());
            return null;
        }
    }
}
