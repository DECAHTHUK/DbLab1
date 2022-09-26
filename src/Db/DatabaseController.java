package Db;

import Business.Student;
import Business.Variant;
import MyLogger.Logg3r;
import Persistence.WorkingInterfaces;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;

public class DatabaseController {
    private static File db = null;
    private static File studentsTable = null;
    private static File variantsTable = null;
    private static File testingTable = null;

    public static void setDbFile(String name) {
        db = new File(".\\DataBases\\" + name);
        studentsTable = new File(".\\DataBases\\" + name + "students.txt");
        variantsTable = new File(".\\DataBases\\" + name + "variants.txt");
        testingTable = new File(".\\DataBases\\" + name + "testingTable.txt");
    }

    public static void generateTestingTable() {

    }

    public static void addVariantsTableThroughFile() {

    }

    public static void addStudentsTableThroughFile() {

    }

    public static void addNewStudent() {

    }

    public static void addNewVariant() {

    }

    public static void deleteVariant() {
        System.out.println("|Which variant would you like to change?(write id)|");
        ArrayList<Variant> array = getAllVariants();
        printAllVariants(array);
        int id = new Scanner(System.in).nextInt();

        boolean changed = false;
        try (FileWriter writer = new FileWriter(variantsTable)) {
            for (Variant var : array) {
                if (var.getId() == id) {
                    changed = true;
                } else {
                    writer.write(var.getId() + " " + var.getPathToFile() + "\n");
                }
            }
        } catch (IOException e) {
            Logg3r.logNew(Level.SEVERE, "Error deleting data from variants table: " + e.getMessage());
        }
        if (changed) {
            System.out.println("Done!");
        } else {
            System.out.println("Variant with specified id not found.");
        }
        WorkingInterfaces.variantsChoiceMenu();
    }

    public static void deleteStudent() {
        System.out.println("|Which student would you like to delete?(write id)|");
        ArrayList<Student> array = getAllStudents();
        printAllStudents(array);
        int id = new Scanner(System.in).nextInt();

        boolean changed = false;
        try (FileWriter writer = new FileWriter(studentsTable)) {
            for (Student st : array) {
                if (st.getId() == id) {
                    changed = true;
                } else {
                    writer.write(String.format("%d %s %s %s\n", st.getId(), st.getName(), st.getSurname(), st.getPatronymic()));
                }
            }
        } catch (IOException e) {
            Logg3r.logNew(Level.SEVERE, "Error deleting data from student table" + e.getMessage());
        }
        if (changed) {
            System.out.println("Done!");
        } else {
            System.out.println("Student with specified id not found.");
        }
        WorkingInterfaces.studentsChoiceMenu();
    }

    public static void alterVariant() {
        System.out.println("|Which variant would you like to change?(write id)|");
        ArrayList<Variant> array = getAllVariants();
        printAllVariants(array);
        int id = new Scanner(System.in).nextInt();

        System.out.println("Write the new variant name.");
        String newVariant = new Scanner(System.in).nextLine();

        boolean changed = false;
        if (!checkDuplicatedVariant(array, newVariant)) {
            WorkingInterfaces.variantsChoiceMenu();
        } else {
            try (FileWriter writer = new FileWriter(variantsTable)) {
                for (Variant var : array) {
                    if (var.getId() == id) {
                        changed = true;
                        var = new Variant(id, newVariant);
                    }
                    writer.write(var.getId() + " " + var.getPathToFile() + "\n");
                }
            } catch (IOException e) {
                Logg3r.logNew(Level.SEVERE, "Error writing data to variants table: " + e.getMessage());
            }
        }
        if (changed) {
            System.out.println("Done!");
        } else {
            System.out.println("Variant with specified id not found.");
        }
        WorkingInterfaces.variantsChoiceMenu();
    }

    public static void alterStudent() {
        System.out.println("|Which student would you like to change?(write id)|");
        ArrayList<Student> array = getAllStudents();
        printAllStudents(array);
        int id = new Scanner(System.in).nextInt();

        System.out.println("|Write the new Student full name.(Without id)|");
        String newStudent = new Scanner(System.in).nextLine();

        String[] a = newStudent.split(" ");
        boolean changed = false;
        try (FileWriter writer = new FileWriter(studentsTable)) {
            for (Student st : array) {
                if (st.getId() == id) {
                    changed = true;
                    st = new Student(id, a[0], a[1], a[2]);
                }
                writer.write(String.format("%d %s %s %s\n", st.getId(), st.getName(), st.getSurname(), st.getPatronymic()));
            }
        } catch (IOException e) {
            Logg3r.logNew(Level.SEVERE, "Error writing data to student table" + e.getMessage());
        }
        if (changed) {
            System.out.println("Done!");
        } else {
            System.out.println("Student with specified id not found.");
        }
        WorkingInterfaces.studentsChoiceMenu();
    }

    public static ArrayList<Student> getAllStudents() {
        ArrayList<Student> array = new ArrayList<>();
        try (Scanner scanner = new Scanner(studentsTable)) {
            while (scanner.hasNext()) {
                String[] a = scanner.nextLine().split(" ");
                array.add(new Student(Integer.parseInt(a[0]), a[1], a[2], a[3]));
            }
        } catch (FileNotFoundException e) {
            Logg3r.logNew(Level.SEVERE, "Error accessing students table: " + e.getMessage());
        }
        return array;
    }

    public static void getStudentById() {
        System.out.println("|Enter the student's id:|");
        int id = new Scanner(System.in).nextInt();
        ArrayList<Student> array = getAllStudents();
        for (Student st : array) {
            if (st.getId() == id) {
                System.out.println("id\t\tname\t\tsurname\t\tpatronymic");
                System.out.printf("%d\t\t%s\t\t%s\t\t%s", st.getId(), st.getName(), st.getSurname(), st.getPatronymic());
                WorkingInterfaces.studentsChoiceMenu();
            }
        }
        System.out.println("Student with id " + id + " not found.");
        WorkingInterfaces.studentsChoiceMenu();
    }

    public static void getVariantById() {
        System.out.println("|Enter the variant's id:|");
        int id = new Scanner(System.in).nextInt();
        ArrayList<Variant> array = getAllVariants();
        for (Variant var : array) {
            if (var.getId() == id) {
                System.out.println("id\t\tpath_to_file");
                System.out.printf("%d\t\t%s", var.getId(), var.getPathToFile());
                WorkingInterfaces.variantsChoiceMenu();
            }
        }
        System.out.println("Variant with id " + id + "not found.");
        WorkingInterfaces.variantsChoiceMenu();
    }

    public static ArrayList<Variant> getAllVariants() {
        ArrayList<Variant> array = new ArrayList<>();
        try (Scanner scanner = new Scanner(studentsTable)) {
            while (scanner.hasNext()) {
                String[] a = scanner.nextLine().split(" ");
                array.add(new Variant(Integer.parseInt(a[0]), a[1]));
            }
        } catch (FileNotFoundException e) {
            Logg3r.logNew(Level.SEVERE, "Error accessing variants table: " + e.getMessage());
        }
        return array;
    }

    public static boolean checkDuplicatedVariant(ArrayList<Variant> array, String newVariant) {
        for (Variant a : array) {
            if (a.getPathToFile().equals(newVariant)) {
                System.out.println("Variant like this already exists!");
                return false;
            }
        }
        return true;
    }

    public static void printAllStudents(ArrayList<Student> array) {
        System.out.println("id\t\tname\t\tsurname\t\tpatronymic");
        for (Student st : array) {
            System.out.printf("%d\t\t%s\t\t%s\t\t%s", st.getId(), st.getName(), st.getSurname(), st.getPatronymic());
        }
    }

    public static void printAllVariants(ArrayList<Variant> array) {
        System.out.println("id\t\tpath_to_file");
        for (Variant st : array) {
            System.out.printf("%d\t\t%s", st.getId(), st.getPathToFile());
        }
    }

}
