package com.example.desktopdatareceiver.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.example.desktopdatareceiver.network.NetworkController;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class ConnectionController {

    @FXML
    private TextField ipField;

    @FXML
    private TextField portField;

    @FXML
    private Button connectDbBtn;

    @FXML
    private Button startServerBtn;

    @FXML
    private TextArea logArea; // Add to connection.fxml

    private NetworkController networkController;



    @FXML
    public void initialize() {
        ipField.setText(getLocalIpAddress());

        System.out.println("Detected IP = " + getLocalIpAddress());

        ipField.setText(getLocalIpAddress());

        portField.setText("3005");


        connectDbBtn.setOnAction(e -> {
            database.DataRepository.createTable();
            logArea.appendText("Database connected\n");
        });

        startServerBtn.setOnAction(this::handle);
    }



    @FXML
    private void openDataPage() throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/view/data_page.fxml")
        );
        Scene scene = new Scene(loader.load());

        Stage stage = new Stage();
        stage.setTitle("Data Preview");
        stage.setScene(scene);
        stage.show();
    }

    private void handle(ActionEvent e) {
        logArea.appendText("START SERVER button clicked\n");

        int port = Integer.parseInt(portField.getText());
        networkController = new NetworkController(logArea);
        networkController.startServer(port);
    }


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
                        return addr.getHostAddress(); // 🔥 real LAN IP
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ""; // ❌ 127.0.0.1 না
    }


}
