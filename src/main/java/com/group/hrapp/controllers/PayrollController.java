package com.group.hrapp.controllers;

import com.group.hrapp.model.Employee;
import com.group.hrapp.model.Payroll;
import com.group.hrapp.model.Attendance;
import com.group.hrapp.storage.DataStorage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;

public class PayrollController {

    private TableView<Payroll> payrollTable;
    private ObservableList<Payroll> payrollList;

    private ComboBox<Employee> employeeCombo;
    private TextField inputHours, inputOvertime, inputBonus, inputTaxRate;
    private DatePicker datePicker;

    public void show(Stage stage, ObservableList<Employee> employeeData) {

        stage.setTitle("Payroll Manager");

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));
        root.setStyle("-fx-background-color: #f4c064;");

        List<Payroll> loaded = DataStorage.loadPayrolls();
        payrollList = FXCollections.observableArrayList(loaded);

        payrollTable = new TableView<>(payrollList);
        payrollTable.setStyle("-fx-background-color: #ffffff; -fx-border-color: #3d364a;");

        TableColumn<Payroll, String> colEmployee = new TableColumn<>("Employee");
        colEmployee.setPrefWidth(180);
        colEmployee.setCellValueFactory(
                c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getEmployee().getName()));

        TableColumn<Payroll, Double> colGross = new TableColumn<>("Gross Pay");
        colGross.setPrefWidth(120);
        colGross.setCellValueFactory(
                c -> new javafx.beans.property.SimpleDoubleProperty(c.getValue().calculateGrossPay()).asObject());

        TableColumn<Payroll, Double> colTax = new TableColumn<>("Tax");
        colTax.setPrefWidth(120);
        colTax.setCellValueFactory(
                c -> new javafx.beans.property.SimpleDoubleProperty(c.getValue().calculateTax()).asObject());

        TableColumn<Payroll, Double> colNet = new TableColumn<>("Net Pay");
        colNet.setPrefWidth(120);
        colNet.setCellValueFactory(
                c -> new javafx.beans.property.SimpleDoubleProperty(c.getValue().calculateNetPay()).asObject());

        TableColumn<Payroll, String> colDate = new TableColumn<>("Date");
        colDate.setPrefWidth(120);
        colDate.setCellValueFactory(
                c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getDate().toString()));

        payrollTable.getColumns().addAll(colEmployee, colGross, colTax, colNet, colDate);

        employeeCombo = new ComboBox<>(employeeData);
        employeeCombo.setPromptText("Select Employee");

        inputHours = new TextField();
        inputHours.setPromptText("Hours Worked");

        inputOvertime = new TextField();
        inputOvertime.setPromptText("Overtime Hours");

        inputBonus = new TextField();
        inputBonus.setPromptText("Bonus");

        inputTaxRate = new TextField();
        inputTaxRate.setPromptText("Tax Rate (0.2)");

        datePicker = new DatePicker(LocalDate.now());

        HBox inputBox = new HBox(10, employeeCombo, inputHours, inputOvertime, inputBonus, inputTaxRate, datePicker);
        inputBox.setPadding(new Insets(10));

        // Buttons
        String normal = "-fx-background-color: #bca8e4; -fx-text-fill: black; "
                + "-fx-border-color: #3d364a; -fx-border-radius: 4px; -fx-background-radius: 4px;";
        String pressed = "-fx-background-color: #7c6f97; -fx-text-fill: black; "
                + "-fx-border-color: #3d364a; -fx-border-radius: 4px; -fx-background-radius: 4px;";

        Button btnCalculate = new Button("Calculate Payroll");
        Button btnSaveOne = new Button("Save Selected");
        Button btnSaveAll = new Button("Save All");
        Button btnReport = new Button("Filter Report");
        Button btnAnalytics = new Button("Analytics");

        for (Button btn : new Button[] { btnCalculate, btnSaveOne, btnSaveAll, btnReport, btnAnalytics }) {
            btn.setStyle(normal);
            btn.setOnMousePressed(e -> btn.setStyle(pressed));
            btn.setOnMouseReleased(e -> btn.setStyle(normal));
        }

        btnCalculate.setOnAction(e -> handleCalculate());
        btnSaveOne.setOnAction(e -> handleSaveSingle());
        btnSaveAll.setOnAction(e -> {
            DataStorage.savePayrolls(payrollList);
            showAlert("Saved", "All payrolls saved.");
        });
        btnReport.setOnAction(e -> filterReport());

        btnAnalytics.setOnAction(e -> showAnalytics());

        HBox buttons = new HBox(10, btnCalculate, btnSaveOne, btnSaveAll, btnReport, btnAnalytics);
        buttons.setPadding(new Insets(10));

        root.getChildren().addAll(payrollTable, inputBox, buttons);

        Scene scene = new Scene(root, 980, 480);
        stage.setScene(scene);
        stage.show();
    }

    private void handleCalculate() {
        try {
            Employee emp = employeeCombo.getValue();
            if (emp == null) {
                showAlert("Error", "Select an employee.");
                return;
            }

            double hours = Double.parseDouble(inputHours.getText());
            double overtime = Double.parseDouble(inputOvertime.getText());
            double bonus = Double.parseDouble(inputBonus.getText());
            double tax = Double.parseDouble(inputTaxRate.getText());
            LocalDate date = datePicker.getValue();

            Payroll p = new Payroll(emp, emp.getHourlyRate(), hours, overtime, bonus, tax, date);
            payrollList.add(p);
            showAlert("Added", "Payroll calculated.");

        } catch (Exception ex) {
            showAlert("Error", "Invalid numbers.");
        }
    }

    private void handleSaveSingle() {
        Payroll selected = payrollTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Error", "No payroll selected.");
            return;
        }

        DataStorage.savePayrolls(payrollList);
        showAlert("Saved", "Selected payroll saved.");
    }

    private void filterReport() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Filter");
        dialog.setHeaderText("Enter employee name (partial match):");
        dialog.setContentText("Name:");
        dialog.showAndWait().ifPresent(filter -> {
            if (filter.isEmpty()) {
                payrollTable.setItems(payrollList);
                return;
            }

            ObservableList<Payroll> filtered = FXCollections.observableArrayList();
            for (Payroll p : payrollList) {
                if (p.getEmployee().getName().toLowerCase().contains(filter.toLowerCase()))
                    filtered.add(p);
            }
            payrollTable.setItems(filtered);
        });
    }

    private void showAlert(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.show();
    }

    private void showAnalytics() {
        Stage s = new Stage();
        VBox root = new VBox(15);
        root.setPadding(new Insets(10));

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Employee");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Gross Pay");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Payroll Overview");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Gross Pay");

        for (Payroll p : payrollList) {
            series.getData().add(new XYChart.Data<>(p.getEmployee().getName(), p.calculateGrossPay()));
        }
        barChart.getData().add(series);

        Employee selected = employeeCombo.getValue();
        PieChart pieChart = null;
        if (selected != null) {
            int present = 0, onLeave = 0;
            for (Attendance a : selected.getAttendanceList()) {
                if (a.isPresent())
                    present++;
                if (a.isOnLeave())
                    onLeave++;
            }
            ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList(
                    new PieChart.Data("Present", present),
                    new PieChart.Data("On Leave", onLeave));
            pieChart = new PieChart(pieData);
            pieChart.setTitle(selected.getName() + " Attendance");
        }

        if (pieChart != null)
            root.getChildren().addAll(barChart, pieChart);
        else
            root.getChildren().add(barChart);

        Scene scene = new Scene(root, 700, 500);
        s.setScene(scene);
        s.show();
    }
}
