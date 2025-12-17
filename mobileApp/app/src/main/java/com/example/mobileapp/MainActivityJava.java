package com.example.mobileapp;

import android.app.Activity;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.util.Enumeration;

public class MainActivityJava extends Activity {

    EditText ipField, portField, messageField;
    Button sendButton;
    TextView statusText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ipField = findViewById(R.id.ipField);
        portField = findViewById(R.id.portField);
        messageField = findViewById(R.id.messageField);
        sendButton = findViewById(R.id.sendButton);
        statusText = findViewById(R.id.statusText);

        if (isEmulator()) {
            ipField.setText("10.0.2.2"); // Emulator → PC
        } else {
            ipField.setText("");
        }

        portField.setText("3005");

        // 🔥 HERE is the answer
        sendButton.setOnClickListener(v -> sendMessage());
    }



    private void sendMessage() {

        final String ip = ipField.getText().toString().trim();
        final int port = Integer.parseInt(portField.getText().toString().trim());
        final String message = messageField.getText().toString().trim();

        if (message.isEmpty()) {
            statusText.setText("❌ Message empty");
            return;
        }

        statusText.setText("Sending...");

        new Thread(() -> {
            try {
                Socket socket = new Socket(ip, port);

                OutputStream os = socket.getOutputStream();
                os.write((message + "\n").getBytes());
                os.flush();

                socket.close();

                runOnUiThread(() ->
                        statusText.setText("✅ Sent: " + message)
                );

            } catch (Exception e) {
                runOnUiThread(() ->
                        statusText.setText("❌ Failed: " + e.getMessage())
                );
            }
        }).start();
    }


    private boolean isEmulator() {
        return (android.os.Build.FINGERPRINT.startsWith("generic")
                || android.os.Build.MODEL.contains("Emulator")
                || android.os.Build.MODEL.contains("Android SDK built for x86")
                || android.os.Build.MANUFACTURER.contains("Genymotion")
                || android.os.Build.BRAND.startsWith("generic"));
    }


//    private String getLocalIpAddress() {
//        try {
//            Enumeration<NetworkInterface> interfaces =
//                    NetworkInterface.getNetworkInterfaces();
//
//            while (interfaces.hasMoreElements()) {
//                NetworkInterface ni = interfaces.nextElement();
//
//                Enumeration<InetAddress> addresses = ni.getInetAddresses();
//                while (addresses.hasMoreElements()) {
//                    InetAddress addr = addresses.nextElement();
//
//                    if (!addr.isLoopbackAddress()
//                            && addr instanceof Inet4Address) {
//                        return addr.getHostAddress(); // 🔥 real LAN IP
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return ""; // ❌ 127.0.0.1 না
//    }




}
