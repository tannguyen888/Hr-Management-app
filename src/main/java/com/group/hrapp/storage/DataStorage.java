package com.group.hrapp.storage;

import com.group.hrapp.model.Employee;
import com.group.hrapp.model.Payroll;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataStorage {

    private static final String BASE_DIR = System.getProperty("user.home") + File.separator + "hrappdata";
    private static final String EMP_FILE = BASE_DIR + File.separator + "employees.ser";
    private static final String PAY_FILE = BASE_DIR + File.separator + "payrolls.ser";

    static {
        File dir = new File(BASE_DIR);
        if (!dir.exists() && !dir.mkdirs()) {
            System.err.println("Cannot create data directory: " + BASE_DIR);
        }
    }

    public static void saveEmployees(List<Employee> employees) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(EMP_FILE))) {
            oos.writeObject(employees);
            System.out.println("Employees saved to: " + EMP_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Employee> loadEmployees() {
        File f = new File(EMP_FILE);
        if (!f.exists())
            return new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            return (List<Employee>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static void savePayrolls(List<Payroll> payrolls) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(PAY_FILE))) {
            oos.writeObject(payrolls);
            System.out.println("Payrolls saved to: " + PAY_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Payroll> loadPayrolls() {
        File f = new File(PAY_FILE);
        if (!f.exists())
            return new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            return (List<Payroll>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
