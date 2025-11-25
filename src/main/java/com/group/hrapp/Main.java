package com.group.hrapp;

import com.group.hrapp.model.Role;
import com.group.hrapp.controllers.EmployeeController;
import com.group.hrapp.controllers.PayrollController;
import com.group.hrapp.model.Employee;
import com.group.hrapp.model.User;
import com.group.hrapp.storage.DataStorage;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.List;

public class Main extends Application {
    private final User admin = new User("admin", "1234", Role.ADMIN);
    private final User normalUser = new User("user", "1234", Role.EMPLOYEE);
    private final User manager = new User("manager", "1234", Role.MANAGER);

    private User currentUserLogged;
    private final ObservableList<Employee> sharedEmployees = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Payroll HR Menu");

        Pane root = new Pane();
        root.setPrefSize(653, 350);
        root.setStyle("-fx-background-color: #f4c064;");

        List<Employee> loaded = DataStorage.loadEmployees();
        sharedEmployees.setAll(loaded);

        Font font14 = new Font(14);
        Font font16 = new Font(16);

        Button btnPayroll = new Button("Payroll Management");
        btnPayroll.setLayoutX(120);
        btnPayroll.setLayoutY(120);
        btnPayroll.setPrefSize(160, 60);
        btnPayroll.setFont(font14);
        btnPayroll.setStyle("-fx-background-color: #bca8e4; -fx-text-fill: #000;");
        btnPayroll.setOnAction(e -> {
            PayrollController pc = new PayrollController();
            Stage s = new Stage();
            pc.show(s, sharedEmployees);
        });

        Button btnEmployee = new Button("Employee Management");
        btnEmployee.setLayoutX(360);
        btnEmployee.setLayoutY(120);
        btnEmployee.setPrefSize(170, 60);
        btnEmployee.setFont(font14);
        btnEmployee.setStyle("-fx-background-color: #bca8e4; -fx-text-fill: #000;");
        btnEmployee.setOnAction(e -> {
            if (currentUserLogged != null) {
                EmployeeController c = new EmployeeController(sharedEmployees);
                Stage stage = new Stage();
                c.show(stage, currentUserLogged.getRole());
            }
        });

        Label title = new Label("JavaFX Human Resource Management and Payroll System");
        title.setLayoutX(200);
        title.setLayoutY(60);
        title.setFont(font16);
        title.setPrefWidth(300);
        title.setWrapText(true);

        root.getChildren().addAll(btnPayroll, btnEmployee, title,
                makeLabel("101361487 - Tan Phat Nguyen", 440, 230, font14),
                makeLabel("101492726 - Tien Le", 440, 250, font14),
                makeLabel("101567828 - Duc Thien Doan", 440, 270, font14),
                makeLabel("101570606 - Duc Dou", 440, 290, font14));

        Pane loginRoot = new Pane();
        loginRoot.setPrefSize(400, 250);
        loginRoot.setStyle("-fx-background-color: #f4c064;");

        Label user = new Label("Username:");
        user.setLayoutX(50);
        user.setLayoutY(50);
        user.setFont(font14);

        TextField uname = new TextField();
        uname.setLayoutX(150);
        uname.setLayoutY(45);

        Label password = new Label("Password:");
        password.setLayoutX(50);
        password.setLayoutY(100);
        password.setFont(font14);

        TextField upassword = new TextField();
        upassword.setLayoutX(150);
        upassword.setLayoutY(95);

        Label mess = new Label();
        mess.setLayoutX(50);
        mess.setLayoutY(150);
        mess.setFont(font14);

        Button btnLogin = new Button("Login");
        btnLogin.setLayoutX(150);
        btnLogin.setLayoutY(180);
        btnLogin.setPrefSize(100, 40);
        btnLogin.setFont(font14);
        btnLogin.setStyle("-fx-background-color: #bca8e4; -fx-text-fill: #000;");

        loginRoot.getChildren().addAll(user, uname, password, upassword, btnLogin, mess);

        Scene loginScene = new Scene(loginRoot);
        Scene menuScene = new Scene(root, 653, 350);

        btnLogin.setOnAction(e -> {
            String u = uname.getText().trim();
            String p = upassword.getText().trim();

            if (u.equals(admin.getUsername()) && p.equals(admin.getPassword())) {
                currentUserLogged = admin;
                primaryStage.setScene(menuScene);
            } else if (u.equals(normalUser.getUsername()) && p.equals(normalUser.getPassword())) {
                currentUserLogged = normalUser;
                primaryStage.setScene(menuScene);
            } else if (u.equals(manager.getUsername()) && p.equals(manager.getPassword())) {
                currentUserLogged = manager;
                primaryStage.setScene(menuScene);
            } else {
                mess.setText("Wrong username or password!");
            }
        });

        primaryStage.setScene(loginScene);
        primaryStage.show();
    }

    private Label makeLabel(String text, double x, double y, Font font) {
        Label label = new Label(text);
        label.setLayoutX(x);
        label.setLayoutY(y);
        label.setFont(font);
        return label;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
