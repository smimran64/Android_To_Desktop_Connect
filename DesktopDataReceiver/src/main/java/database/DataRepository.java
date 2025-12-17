package database;

import com.example.desktopdatareceiver.model.ReceivedData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DataRepository {

    // ✅ 1️⃣ Create table (new design)
    public static void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS received_data (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "message TEXT," +
                "ip_address TEXT," +
                "date TEXT," +
                "time TEXT," +
                "received_at DATETIME DEFAULT CURRENT_TIMESTAMP" +
                ");";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
            System.out.println("Table ready with new columns.");

        } catch (Exception e) {
            System.out.println("Table creation failed: " + e.getMessage());
        }
    }

    // ✅ 2️⃣ Save data from Android
    public static void saveData(String message, String ipAddress) {

        String sql = "INSERT INTO received_data (message, ip_address, date, time) " +
                "VALUES (?, ?, date('now'), time('now'))";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, message);
            ps.setString(2, ipAddress);
            ps.executeUpdate();

            System.out.println("Data saved to database.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ✅ 3️⃣ Fetch all data for TableView
    public static List<ReceivedData> getAllData() {
        List<ReceivedData> list = new ArrayList<>();

        String sql = "SELECT id, message, ip_address, date, time FROM received_data ORDER BY id ASC";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new ReceivedData(
                        rs.getInt("id"),
                        rs.getString("message"),
                        rs.getString("ip_address"),
                        rs.getString("date"),
                        rs.getString("time")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
