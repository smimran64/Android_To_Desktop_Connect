package com.example.desktopdatareceiver.network;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class NetworkController {

    private TextArea logArea;
    private ServerSocket serverSocket;

    public NetworkController(TextArea logArea) {
        this.logArea = logArea;
    }

    public void startServer(int port) {

        //confirm method entered

        log("➡ startServer() called");

        new Thread(() -> {
            try {
                log("➡ Trying to bind port " + port);

                serverSocket = new ServerSocket(port);

                log("✅ Server listening on port " + port);

                while (true) {
                    Socket client = serverSocket.accept();
                    log("✅ Client connected");

                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(client.getInputStream())
                    );

                    String data;
                    while ((data = in.readLine()) != null) {
                        log("✅ Received: " + data);
                        saveToDB(data);
                    }

                    client.close();
                }

            } catch (Exception e) {
                e.printStackTrace(); //VERY IMPORTANT
                log("❌ Server error: " + e.toString());
            }
        }).start();
    }


    private void log(String msg) {
        Platform.runLater(() ->
                logArea.appendText(msg + "\n")
        );
    }

    private void saveToDB(String data) {
        try (var conn = database.DBConnection.getConnection()) {

            var sql = "INSERT INTO received_data (data) VALUES (?)";
            var ps = conn.prepareStatement(sql);
            ps.setString(1, data);
            ps.executeUpdate();

            log("💾 Saved to DB");

        } catch (Exception e) {
            log("❌ DB error: " + e.getMessage());
        }
    }

}
