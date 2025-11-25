package com.group.hrapp.storage;

import com.group.hrapp.model.Employee;
import com.group.hrapp.model.Payroll;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataStorage {

    private static final String BASE_DIR = "data";
    private static final String EMP_FILE = BASE_DIR + File.separator + "employees.dat";
    private static final String PAY_FILE = BASE_DIR + File.separator + "payrolls.dat";

    static {
        File dir = new File(BASE_DIR);
        if (!dir.exists()) {
            if (dir.mkdirs()) {
                System.out.println("Created data directory: " + dir.getAbsolutePath());
            } else {
                System.err.println("Failed to create data directory: " + dir.getAbsolutePath());
            }
        }
    }

    public static void saveEmployees(List<Employee> employees) {
        // the logic here is change the ObservableList to ArrayList before save the data
        List<Employee> plainList = new ArrayList<>(employees);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(EMP_FILE))) {
            oos.writeObject(plainList);
            System.out.println("Employees saved successfully! Count: " + plainList.size());
        } catch (IOException e) {
            System.err.println("Failed to save employees!");
            e.printStackTrace();
        }
    }

    public static List<Employee> loadEmployees() {
        File f = new File(EMP_FILE);
        if (!f.exists()) {
            System.out.println("No employee data file found: " + f.getAbsolutePath());
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            @SuppressWarnings("unchecked")
            List<Employee> list = (List<Employee>) ois.readObject();
            System.out.println("Successfully loaded " + list.size() + " employees from: " + f.getAbsolutePath());
            return list;
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            System.err.println("CRITICAL: Failed to load employees! Data may be corrupted or outdated.");
            System.err.println("Reason: " + e.getMessage());
            e.printStackTrace();

            if (f.delete()) {
                System.out.println("Deleted corrupted file: " + f.getAbsolutePath());
            }

            return new ArrayList<>();
        }
    }

    public static void savePayrolls(List<Payroll> payrolls) {
        List<Payroll> plainList = new ArrayList<>(payrolls);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(PAY_FILE))) {
            oos.writeObject(plainList);
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
            @SuppressWarnings("unchecked")
            List<Payroll> list = (List<Payroll>) ois.readObject();
            System.out.println("Successfully loaded " + list.size() + " payroll from: " + f.getAbsolutePath());
            return list;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Not able to load the payrol!Not save ");
            System.err.println("because of: " + e.getMessage());
            e.printStackTrace();

            if (f.delete()) {
                System.out.println("Deleted corrupted file: " + f.getAbsolutePath());
            }

            return new ArrayList<>();
        }
    }

    static {
        System.out.println("Data directory: " + new File(BASE_DIR).getAbsolutePath());
    }
}
