package com.group.hrapp.controllers;

import com.group.hrapp.model.Employee;
import com.group.hrapp.model.Leave;
import com.group.hrapp.storage.DataStorage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.time.LocalDate;

public class LeaveController {

    private TableView<Leave> table;
    private ObservableList<Leave> leaveList;

    private DatePicker inputStartDate, inputEndDate;
    private TextField inputType;
    private CheckBox approvedCheck;
    private Button btnAdd, btnUpdate, btnDelete;

    private final Employee employee;

    public LeaveController(Employee employee) {
        this.employee = employee;
        this.leaveList = FXCollections.observableArrayList(employee.getLeaveList());
    }

    public void show(Stage stage) {
        stage.setTitle("Leave Manager: " + employee.getName());

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));
        root.setStyle("-fx-background-color: #f4c064;");

        table = new TableView<>(leaveList);

        TableColumn<Leave, String> colStart = new TableColumn<>("Start Date");
        colStart.setPrefWidth(100);
        colStart.setCellValueFactory(
                c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getStart().toString()));

        TableColumn<Leave, String> colEnd = new TableColumn<>("End Date");
        colEnd.setPrefWidth(100);
        colEnd.setCellValueFactory(
                c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getEnd().toString()));

        TableColumn<Leave, String> colType = new TableColumn<>("Type");
        colType.setPrefWidth(120);
        colType.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getType()));

        TableColumn<Leave, String> colApproved = new TableColumn<>("Approved");
        colApproved.setPrefWidth(80);
        colApproved.setCellValueFactory(
                c -> new javafx.beans.property.SimpleStringProperty(c.getValue().isApproved() ? "Yes" : "No"));

        table.getColumns().addAll(colStart, colEnd, colType, colApproved);
        table.setOnMouseClicked(e -> fillInputs());

        inputStartDate = new DatePicker(LocalDate.now());
        inputEndDate = new DatePicker(LocalDate.now());
        inputType = new TextField();
        inputType.setPromptText("Type (Sick/Vacation)");
        approvedCheck = new CheckBox("Approved");

        HBox inputBox = new HBox(10, inputStartDate, inputEndDate, inputType, approvedCheck);
        inputBox.setPadding(new Insets(10));

        btnAdd = new Button("Add");
        btnUpdate = new Button("Update");
        btnDelete = new Button("Delete");

        btnAdd.setOnAction(e -> {
            handleAdd();
            autoSave();
        });
        btnUpdate.setOnAction(e -> {
            handleUpdate();
            autoSave();
        });
        btnDelete.setOnAction(e -> {
            handleDelete();
            autoSave();
        });

        HBox btnBox = new HBox(10, btnAdd, btnUpdate, btnDelete);
        btnBox.setPadding(new Insets(10));

        root.getChildren().addAll(table, inputBox, btnBox);

        Scene scene = new Scene(root, 500, 400);
        stage.setScene(scene);
        stage.show();
    }

    private void fillInputs() {
        Leave l = table.getSelectionModel().getSelectedItem();
        if (l == null)
            return;

        inputStartDate.setValue(l.getStart());
        inputEndDate.setValue(l.getEnd());
        inputType.setText(l.getType());
        approvedCheck.setSelected(l.isApproved());
    }

    private void handleAdd() {
        try {
            Leave l = new Leave(inputStartDate.getValue(), inputEndDate.getValue(), inputType.getText(),
                    approvedCheck.isSelected());
            leaveList.add(l);
            employee.addLeave(l);
            table.refresh();
            clear();
        } catch (Exception e) {
            showAlert("Error", "Invalid input!");
        }
    }

    private void handleUpdate() {
        Leave l = table.getSelectionModel().getSelectedItem();
        if (l == null) {
            showAlert("Error", "Select a record.");
            return;
        }
        l.setStart(inputStartDate.getValue());
        l.setEnd(inputEndDate.getValue());
        l.setType(inputType.getText());
        l.setApproved(approvedCheck.isSelected());
        table.refresh();
        clear();
    }

    private void handleDelete() {
        Leave l = table.getSelectionModel().getSelectedItem();
        if (l == null) {
            showAlert("Error", "Select a record.");
            return;
        }
        leaveList.remove(l);
        employee.getLeaveList().remove(l);
        table.refresh();
        clear();
    }

    private void autoSave() {
        DataStorage.saveEmployees(FXCollections.observableArrayList(employee));
    }

    private void clear() {
        inputStartDate.setValue(LocalDate.now());
        inputEndDate.setValue(LocalDate.now());
        inputType.clear();
        approvedCheck.setSelected(false);
    }

    private void showAlert(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.show();
    }
}
