package database;

import java.sql.Connection;
import java.sql.Statement;

public class DataRepository {

    public static void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS received_data (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "data TEXT," +
                "received_at DATETIME DEFAULT CURRENT_TIMESTAMP" +
                ");";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
            System.out.println("Table ready.");

        } catch (Exception e) {
            System.out.println("Table creation failed: " + e.getMessage());
        }
    }
}
