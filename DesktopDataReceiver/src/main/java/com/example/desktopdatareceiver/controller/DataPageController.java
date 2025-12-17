package com.example.desktopdatareceiver.controller;

import database.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import com.example.desktopdatareceiver.model.ReceivedData;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DataPageController {

    @FXML
    private TableView<ReceivedData> tableView;

    @FXML
    private TableColumn<ReceivedData, Integer> idCol;

    @FXML
    private TableColumn<ReceivedData, String> dataCol;

    private ObservableList<ReceivedData> dataList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        dataCol.setCellValueFactory(new PropertyValueFactory<>("data"));

        loadData();
    }

    public void loadData() {
        dataList.clear();

        try (Connection conn = DBConnection.getConnection()) {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM received_data");

            while (rs.next()) {
                dataList.add(
                        new ReceivedData(
                                rs.getInt("id"),
                                rs.getString("data")
                        )
                );
            }

            tableView.setItems(dataList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
