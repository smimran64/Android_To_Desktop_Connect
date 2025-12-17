package com.example.desktopdatareceiver.controller;

import com.example.desktopdatareceiver.network.NetworkController;
import database.DataRepository;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class ConnectionController {

    @FXML private TextField ipField;
    @FXML private TextField portField;

    @FXML private Button connectDbBtn;
    @FXML private Button startServerBtn;
    @FXML private Button openDbBtn;


    @FXML private TextArea logArea;

    @FXML private Circle dbStatusDot;
    @FXML private Label dbStatusText;

    @FXML private Circle serverStatusDot;
    @FXML private Label serverStatusText;

    @FXML private Label counterLabel;

    private NetworkController networkController;
    private boolean dbConnected = false;
    private boolean serverRunning = false;
    private int receivedCount = 0;

    @FXML
    public void initialize() {

        ipField.setText(getLocalIpAddress());
        portField.setText("3005");

        updateDbStatus(false);
        updateServerStatus(false);

        counterLabel.setText("0");
        connectDbBtn.setText("Connect Database");
        startServerBtn.setText("Start Server");

        connectDbBtn.setOnAction(e -> toggleDatabase());
        startServerBtn.setOnAction(e -> toggleServer());
    }

                         // DATABASE

    private void toggleDatabase() {
        try {
            if (!dbConnected) {
                database.DataRepository.createTable();

                dbConnected = true;
                updateDbStatus(true);
                connectDbBtn.setText("Disconnect Database");
                openDbBtn.setDisable(false);        // ENABLE BUTTON

                log("Database connected");
                showAlert("Database Connected",
                        "Database connection successful ",
                        Alert.AlertType.INFORMATION);

            } else {
                dbConnected = false;
                updateDbStatus(false);
                connectDbBtn.setText("Connect Database");
                openDbBtn.setDisable(true); // DISABLE BUTTON

                log("⚠ Database disconnected");
                showAlert("Database Disconnected",
                        "Database connection closed ⚠",
                        Alert.AlertType.WARNING);
            }
        } catch (Exception e) {
            showAlert("DB Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }


                       //  SERVER

    private void toggleServer() {
        int port;

        try {
            port = Integer.parseInt(portField.getText());
        } catch (NumberFormatException e) {
            showAlert("Invalid Port", "Please enter a valid port number", Alert.AlertType.ERROR);
            return;
        }

        if (!serverRunning) {
            networkController = new NetworkController(logArea, this);
            networkController.startServer(port);

            serverRunning = true;
            updateServerStatus(true);
            startServerBtn.setText("Stop Server");

            showAlert("Server Started",
                    "Server running on port " + port + " ",
                    Alert.AlertType.INFORMATION);

        } else {
            networkController.stopServer();

            serverRunning = false;
            updateServerStatus(false);
            startServerBtn.setText("Start Server");

            showAlert("Server Stopped",
                    "Server has been stopped ",
                    Alert.AlertType.WARNING);
        }
    }


                   // RECEIVE MASSAGE

    public void onMessageReceived(String message, String ipAddress) {

        DataRepository.saveData(message, ipAddress);

        Platform.runLater(() -> {
            receivedCount++;
            counterLabel.setText(String.valueOf(receivedCount));
        });
    }


    private void updateDbStatus(boolean connected) {
        Platform.runLater(() -> {
            dbStatusDot.setFill(connected ? Color.LIMEGREEN : Color.RED);
            dbStatusText.setText(connected ? "DB Connected" : "DB Disconnected");
        });
    }

    private void updateServerStatus(boolean running) {
        Platform.runLater(() -> {
            serverStatusDot.setFill(running ? Color.LIMEGREEN : Color.RED);
            serverStatusText.setText(running ? "Server Running" : "Server Stopped");
        });
    }

                         // ALERT MASSAGE

    private void showAlert(String title, String msg, Alert.AlertType type) {
        Platform.runLater(() -> {
            Alert alert = new Alert(type);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(msg);
            alert.show();
        });
    }

    private void log(String msg) {
        Platform.runLater(() ->
                logArea.appendText(msg + "\n")
        );
    }
                    // GET IP ADDRESS

    private String getLocalIpAddress() {
        try {
            Enumeration<NetworkInterface> interfaces =
                    NetworkInterface.getNetworkInterfaces();

            while (interfaces.hasMoreElements()) {
                NetworkInterface ni = interfaces.nextElement();

                Enumeration<InetAddress> addresses = ni.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();

                    if (!addr.isLoopbackAddress()
                            && addr instanceof Inet4Address) {
                        return addr.getHostAddress();
                    }
                }
            }
        } catch (Exception ignored) {}
        return "";
    }

                // DATABASE PAGE

    @FXML
    private void openDatabasePage() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/view/data_page.fxml")
            );

            Scene scene = new Scene(loader.load());

            Stage stage = new Stage();
            stage.setTitle("Database Records");
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(
                    "UI Error",
                    "Failed to open database page",
                    Alert.AlertType.ERROR
            );
        }
    }

}
