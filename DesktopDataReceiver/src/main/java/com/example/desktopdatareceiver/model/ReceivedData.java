




package com.example.desktopdatareceiver.model;

public class ReceivedData {

    private final int id;
    private final String data;
    private final String receivedAt;

    public ReceivedData(int id, String data, String receivedAt) {
        this.id = id;
        this.data = data;
        this.receivedAt = receivedAt;
    }

    public int getId() {
        return id;
    }

    public String getData() {
        return data;
    }

    public String getReceivedAt() {
        return receivedAt;
    }
}
