package com.group.hrapp.controllers;

import com.group.hrapp.model.Attendance;
import com.group.hrapp.model.Employee;
import com.group.hrapp.model.Role;
import com.group.hrapp.storage.DataStorage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.chart.PieChart;

import java.time.LocalDate;
import java.util.List;

public class AttendanceController {

    private TableView<Attendance> table;
    private ObservableList<Attendance> attendanceList;

    private DatePicker inputDate;
    private CheckBox presentCheck, leaveCheck;
    private Button btnAdd, btnUpdate, btnDelete;

    private final Employee employee;
    private final ObservableList<Employee> allEmployees;
    private Role currentUserRole;

    public AttendanceController(Employee employee, ObservableList<Employee> allEmployees, Role role) {
        this.employee = employee;
        this.allEmployees = allEmployees;
        this.currentUserRole = role;
        this.attendanceList = FXCollections.observableArrayList(employee.getAttendanceList());
    }

    public void show(Stage stage) {

        if (currentUserRole != Role.ADMIN && currentUserRole != Role.MANAGER) {
            Alert a = new Alert(Alert.AlertType.WARNING, "Access Denied!");
            a.show();
            return;
        }

        stage.setTitle("Attendance Manager: " + employee.getName());

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));
        root.setStyle("-fx-background-color: #f4c064;");

        table = new TableView<>(attendanceList);

        TableColumn<Attendance, String> colDate = new TableColumn<>("Date");
        colDate.setPrefWidth(120);
        colDate.setCellValueFactory(
                c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getDate().toString()));

        TableColumn<Attendance, String> colPresent = new TableColumn<>("Present");
        colPresent.setPrefWidth(80);
        colPresent.setCellValueFactory(
                c -> new javafx.beans.property.SimpleStringProperty(c.getValue().isPresent() ? "Yes" : "No"));

        TableColumn<Attendance, String> colLeave = new TableColumn<>("On Leave");
        colLeave.setPrefWidth(80);
        colLeave.setCellValueFactory(
                c -> new javafx.beans.property.SimpleStringProperty(c.getValue().isOnLeave() ? "Yes" : "No"));

        table.getColumns().addAll(colDate, colPresent, colLeave);
        table.setOnMouseClicked(e -> fillInputs());

        inputDate = new DatePicker(LocalDate.now());
        presentCheck = new CheckBox("Present");
        leaveCheck = new CheckBox("On Leave");

        HBox inputBox = new HBox(10, inputDate, presentCheck, leaveCheck);
        inputBox.setPadding(new Insets(10));

        btnAdd = new Button("Add");
        btnUpdate = new Button("Update");
        btnDelete = new Button("Delete");

        String normal = "-fx-background-color: #bca8e4; -fx-text-fill: black; "
                + "-fx-border-color: #3d364a; -fx-border-radius: 4px; -fx-background-radius: 4px;";
        String pressed = "-fx-background-color: #7c6f97; -fx-text-fill: black; "
                + "-fx-border-color: #3d364a; -fx-border-radius: 4px; -fx-background-radius: 4px;";

        for (Button btn : new Button[] { btnAdd, btnUpdate, btnDelete }) {
            btn.setStyle(normal);
            btn.setOnMousePressed(e -> btn.setStyle(pressed));
            btn.setOnMouseReleased(e -> btn.setStyle(normal));
        }

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

        Scene scene = new Scene(root, 400, 400);
        stage.setScene(scene);
        stage.show();
    }

    private void fillInputs() {
        Attendance a = table.getSelectionModel().getSelectedItem();
        if (a == null)
            return;
        inputDate.setValue(a.getDate());
        presentCheck.setSelected(a.isPresent());
        leaveCheck.setSelected(a.isOnLeave());
    }

    private void handleAdd() {
        try {
            Attendance a = new Attendance(inputDate.getValue(), presentCheck.isSelected(), leaveCheck.isSelected());
            attendanceList.add(a);
            employee.addAttendance(a);
            table.refresh();
            clear();
        } catch (Exception e) {
            showAlert("Error", "Invalid input!");
        }
    }

    private void handleUpdate() {
        Attendance a = table.getSelectionModel().getSelectedItem();
        if (a == null) {
            showAlert("Error", "Select a record.");
            return;
        }
        a.setDate(inputDate.getValue());
        a.setPresent(presentCheck.isSelected());
        a.setOnLeave(leaveCheck.isSelected());
        table.refresh();
        clear();
    }

    private void handleDelete() {
        Attendance a = table.getSelectionModel().getSelectedItem();
        if (a == null) {
            showAlert("Error", "Select a record.");
            return;
        }
        attendanceList.remove(a);
        employee.getAttendanceList().remove(a);
        table.refresh();
        clear();
    }

    private void autoSave() {
        // Save all employees to file
        DataStorage.saveEmployees(allEmployees);
    }

    private void clear() {
        inputDate.setValue(LocalDate.now());
        presentCheck.setSelected(false);
        leaveCheck.setSelected(false);
    }

    private void showAlert(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.show();
    }
}
