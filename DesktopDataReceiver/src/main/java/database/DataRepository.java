package database;

import com.example.desktopdatareceiver.model.ReceivedData;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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


    public static List<ReceivedData> getAllData() {
        List<ReceivedData> list = new ArrayList<>();

        String sql = "SELECT id, data, received_at FROM received_data ORDER BY id DESC";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new ReceivedData(
                        rs.getInt("id"),
                        rs.getString("data"),
                        rs.getString("received_at")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
