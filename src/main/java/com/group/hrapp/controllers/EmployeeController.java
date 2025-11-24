package com.group.hrapp.controllers;

import com.group.hrapp.model.Role;
import com.group.hrapp.model.Department;
import com.group.hrapp.model.Employee;
import com.group.hrapp.storage.DataStorage;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class EmployeeController {

    private Role currentUserRole;
    private final ObservableList<Employee> employeeList;

    private TableView<Employee> table;
    private TextField inputId, inputName, inputDepartment, inputRate, inputEmail, inputPhone;
    private Button btnAdd, btnUpdate, btnDelete;

    public EmployeeController(ObservableList<Employee> sharedList) {
        this.employeeList = sharedList;
    }

    public void show(Stage stage, Role role) {
        this.currentUserRole = role;

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));
        root.setStyle("-fx-background-color: #f4c064;");
        stage.setTitle("Employee Manager");

        table = new TableView<>(employeeList);

        TableColumn<Employee, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colId.setPrefWidth(60);

        TableColumn<Employee, String> colName = new TableColumn<>("Name");
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colName.setPrefWidth(150);

        TableColumn<Employee, String> colDept = new TableColumn<>("Department");
        colDept.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getDepartment() != null ? c.getValue().getDepartment().getName() : "N/A"));
        colDept.setPrefWidth(120);

        TableColumn<Employee, Double> colRate = new TableColumn<>("Hourly Rate");
        colRate.setCellValueFactory(new PropertyValueFactory<>("hourlyRate"));

        TableColumn<Employee, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<Employee, String> colPhone = new TableColumn<>("Phone");
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));

        table.getColumns().addAll(colId, colName, colDept, colRate, colEmail, colPhone);
        table.setOnMouseClicked(e -> fillInputs());

        inputId = new TextField();
        inputId.setPromptText("ID");
        inputName = new TextField();
        inputName.setPromptText("Name");
        inputDepartment = new TextField();
        inputDepartment.setPromptText("Department");
        inputRate = new TextField();
        inputRate.setPromptText("Hourly Rate");
        inputEmail = new TextField();
        inputEmail.setPromptText("Email");
        inputPhone = new TextField();
        inputPhone.setPromptText("Phone");

        HBox inputBox = new HBox(10, inputId, inputName, inputDepartment,
                inputRate, inputEmail, inputPhone);
        inputBox.setPadding(new Insets(10));

        btnAdd = new Button("Add");
        btnUpdate = new Button("Update");
        btnDelete = new Button("Delete");
        Button btnSave = new Button("Save All");

        Button btnLeave = new Button("Manage Leave");
        Button btnAttendance = new Button("Attendance");

        btnLeave.setPrefWidth(120);
        btnAttendance.setPrefWidth(120);

        if (role == Role.ADMIN || role == Role.MANAGER) {
            HBox extraButtons = new HBox(10, btnLeave, btnAttendance);
            extraButtons.setPadding(new Insets(10));
            root.getChildren().add(extraButtons);
        }

        btnLeave.setOnAction(e -> {
            Employee selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                LeaveController lc = new LeaveController(selected);
                Stage s = new Stage();
                lc.show(s);
            }
        });

        btnAttendance.setOnAction(e -> {
            Employee selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                AttendanceController ac = new AttendanceController(selected, employeeList, role);

                Stage s = new Stage();
                ac.show(s);
            }
        });

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
        btnSave.setOnAction(e -> DataStorage.saveEmployees(employeeList));

        applyRoleRestriction(role);
        HBox box = new HBox(10, btnAdd, btnUpdate, btnDelete, btnSave);

        root.getChildren().addAll(table, inputBox, box);

        Scene scene = new Scene(root, 950, 450);
        stage.setScene(scene);
        stage.show();
    }

    private void fillInputs() {
        Employee e = table.getSelectionModel().getSelectedItem();
        if (e == null)
            return;
        inputId.setText(String.valueOf(e.getId()));
        inputName.setText(e.getName());
        inputDepartment.setText(e.getDepartment() != null ? e.getDepartment().getName() : "");
        inputRate.setText(String.valueOf(e.getHourlyRate()));
        inputEmail.setText(e.getEmail());
        inputPhone.setText(e.getPhone());
    }

    private void handleAdd() {
        try {
            int id = Integer.parseInt(inputId.getText());
            String name = inputName.getText();
            String dept = inputDepartment.getText();
            double rate = Double.parseDouble(inputRate.getText());
            String email = inputEmail.getText();
            String phone = inputPhone.getText();

            Department department = new Department("D-" + id, dept);
            Employee emp = new Employee(id, name, department, rate, email, phone);
            employeeList.add(emp);
            table.refresh();
            clear();
        } catch (Exception e) {
            alert("Error", "Invalid input!");
        }
    }

    private void handleUpdate() {
        Employee s = table.getSelectionModel().getSelectedItem();
        if (s == null) {
            alert("Error", "Select an employee.");
            return;
        }
        try {
            s.setName(inputName.getText());
            s.setHourlyRate(Double.parseDouble(inputRate.getText()));
            s.setEmail(inputEmail.getText());
            s.setPhone(inputPhone.getText());

            if (s.getDepartment() == null)
                s.setDepartment(new Department("D-" + s.getId(), inputDepartment.getText()));
            else
                s.getDepartment().setName(inputDepartment.getText());

            table.refresh();
            clear();
        } catch (Exception e) {
            alert("Error", "Invalid update input!");
        }
    }

    private void handleDelete() {
        Employee s = table.getSelectionModel().getSelectedItem();
        if (s == null) {
            alert("Error", "Select someone to delete.");
            return;
        }
        employeeList.remove(s);
        table.refresh();
        clear();
    }

    private void autoSave() {
        DataStorage.saveEmployees(employeeList);
    }

    private void applyRoleRestriction(Role r) {
        if (r == Role.EMPLOYEE) {
            btnAdd.setDisable(true);
            btnDelete.setDisable(true);
        }
        if (r == Role.MANAGER)
            btnDelete.setDisable(true);
    }

    private void clear() {
        inputId.clear();
        inputName.clear();
        inputDepartment.clear();
        inputRate.clear();
        inputEmail.clear();
        inputPhone.clear();
    }

    private void alert(String t, String m) {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setTitle(t);
        a.setHeaderText(null);
        a.setContentText(m);
        a.show();
    }
}
