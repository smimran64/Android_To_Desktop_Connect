package com.example.desktopdatareceiver.model;

public class ReceivedData {

    private int id;
    private String data;

    public ReceivedData(int id, String data) {
        this.id = id;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public String getData() {
        return data;
    }
}
