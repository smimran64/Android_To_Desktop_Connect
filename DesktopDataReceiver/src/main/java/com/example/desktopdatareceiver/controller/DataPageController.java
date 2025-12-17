package com.example.desktopdatareceiver.controller;

import database.DataRepository;
import com.example.desktopdatareceiver.model.ReceivedData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class DataPageController {

    @FXML private TableView<ReceivedData> tableView;
    @FXML private TableColumn<ReceivedData, Integer> idCol;
    @FXML private TableColumn<ReceivedData, String> dataCol;

    @FXML
    public void initialize() {

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        dataCol.setCellValueFactory(new PropertyValueFactory<>("data"));

        loadData();
    }

    private void loadData() {
        ObservableList<ReceivedData> data =
                FXCollections.observableArrayList(
                        DataRepository.getAllData()
                );

        tableView.setItems(data);
    }
}
