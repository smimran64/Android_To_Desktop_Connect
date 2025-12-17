




package com.example.desktopdatareceiver.model;

public class ReceivedData {

    private int id;
    private String message;
    private String ipAddress;
    private String date;
    private String time;

    public ReceivedData(int id, String message, String ipAddress, String date, String time) {
        this.id = id;
        this.message = message;
        this.ipAddress = ipAddress;
        this.date = date;
        this.time = time;
    }

    public int getId() { return id; }
    public String getMessage() { return message; }
    public String getIpAddress() { return ipAddress; }
    public String getDate() { return date; }
    public String getTime() { return time; }
}

