package com.example.desktopdatareceiver.network;

import com.example.desktopdatareceiver.controller.ConnectionController;
import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class NetworkController {

    private final TextArea logArea;
    private final ConnectionController ui;

    private ServerSocket serverSocket;
    private boolean running = false;

    public NetworkController(TextArea logArea, ConnectionController ui) {
        this.logArea = logArea;
        this.ui = ui;
    }

    public void startServer(int port) {
        running = true;

        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(port);
                log("Server listening on port " + port);

                while (running) {
                    Socket client = serverSocket.accept();
                    String clientIp = client.getInetAddress().getHostAddress();
                    log("Client connected: " + clientIp);

                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(client.getInputStream())
                    );

                    String message = in.readLine();
                    if (message != null && !message.isBlank()) {
                        log("Received: " + message);


                        ui.onMessageReceived(message, clientIp);
                    }

                    client.close();
                }

            } catch (Exception e) {
                log("Server error: " + e.getMessage());
            }
        }).start();
    }

    public void stopServer() {
        try {
            running = false;
            if (serverSocket != null) serverSocket.close();
            log("Server stopped");
        } catch (Exception e) {
            log("Stop error: " + e.getMessage());
        }
    }

    private void log(String msg) {
        Platform.runLater(() ->
                logArea.appendText(msg + "\n")
        );
    }
}
