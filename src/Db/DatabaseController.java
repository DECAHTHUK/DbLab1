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
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class DatabaseController {
    private static File db = null;
    private static File studentsTable = null;
    private static File variantsTable = null;
    private static File testingTable = null;

    public static void setDbFile(String name) {
        db = new File(".\\DataBases\\" + name);
        studentsTable = new File(".\\DataBases\\" + name + "\\students.txt");
        variantsTable = new File(".\\DataBases\\" + name + "\\variants.txt");
        testingTable = new File(".\\DataBases\\" + name + "\\testingTable.txt");
    }

    public static void generateTestingTable() {
        ArrayList<Student> students = getAllStudents();
        ArrayList<Variant> variants = getAllVariants();
        Random random = new Random();
        if (students.size() == 0 || variants.size() == 0) {
            System.out.println("Some of the necessary tables is empty...");
            WorkingInterfaces.tableChoice();
        }
        try (FileWriter writer = new FileWriter(testingTable)) {
            for (Student st : students) {
                int varId = random.nextInt(variants.size());
                writer.write(String.format("%d %d\n", st.getId(), variants.get(varId).getId()));
            }
        } catch (IOException e) {
            Logg3r.logNew(Level.SEVERE, "Error generating testing table: " + e.getMessage());
            WorkingInterfaces.testingTableChooseMenu();
        }
        System.out.println("Success!");
    }

    public static void getTestingTable() {
        ArrayList<Student> students = getAllStudents();
        ArrayList<Variant> variants = getAllVariants();

        if (students.size() == 0 || variants.size() == 0) {
            System.out.println("Some of the necessary tables is empty...");
            WorkingInterfaces.tableChoice();
        }

        try (Scanner scanner = new Scanner(testingTable)) {
            System.out.println("--------------------------------------------------------------------------------------------");
            System.out.printf("%50s %20s\n", "Full name", "Path_to_file");
            while (scanner.hasNext()) {
                String[] ids = scanner.nextLine().split("\\s+");
                System.out.printf("%50s %20s\n",
                        students.stream().filter(t -> t.getId() == Integer.parseInt(ids[0]))
                                .collect(Collectors.toList()).get(0).getFullName(),
                                    variants.stream().filter(t -> t.getId() == Integer.parseInt(ids[1]))
                                            .collect(Collectors.toList()).get(0).getPathToFile());
            }
            System.out.println("--------------------------------------------------------------------------------------------");
        } catch (IOException e) {
            Logg3r.logNew(Level.SEVERE, "Error accessing testing table: " + e.getMessage());
            WorkingInterfaces.testingTableChooseMenu();
        }
    }

    public static void addVariantsTableThroughFile() {
        ArrayList<Variant> array = getAllVariants();
        int id = array.size() > 0 ? array.get(array.size() - 1).getId() + 1 : 1;

        System.out.println("|Please, enter the path to the file you'd like to add:(relative or full)|");
        String filePath = new Scanner(System.in).nextLine();

        File file = new File(filePath);
        ArrayList<Variant> newVariantsArray = getVariantsDataFromFile(file, id);

        try (FileWriter writer = new FileWriter(variantsTable, true)) {
            for (Variant var : newVariantsArray) {
                writer.write(var.getId() + " " + var.getPathToFile() + "\n");
            }
        } catch (IOException e) {
            Logg3r.logNew(Level.SEVERE, "Error adding data to variants table: " + e.getMessage());
            WorkingInterfaces.variantsChoiceMenu();
        }
        System.out.println("Success!");
    }

    public static void addStudentsTableThroughFile() {
        ArrayList<Student> array = getAllStudents();
        int id = array.size() > 0? array.get(array.size() - 1).getId() + 1 : 1;

        System.out.println("|Please, enter the path to the file you'd like to add:(relative or full)|");
        String filePath = new Scanner(System.in).nextLine();

        File file = new File(filePath);
        ArrayList<Student> newStudentsArray = getStudentDataFromFile(file, id);

        try (FileWriter writer = new FileWriter(studentsTable, true)) {
            for (Student st : newStudentsArray) {
                writer.write(String.format("%d %s %s %s\n", st.getId(), st.getName(), st.getSurname(), st.getPatronymic()));
            }
        } catch (IOException e) {
            Logg3r.logNew(Level.SEVERE, "Error adding data to students table: " + e.getMessage());
            WorkingInterfaces.studentsChoiceMenu();
        }
        System.out.println("Success!");
    }

    public static void addNewStudent() {
        ArrayList<Student> array = getAllStudents();
        int id = array.size() > 0? array.get(array.size() - 1).getId() + 1 : 1;

        System.out.println("|Enter student's full name.(name surname patronymic)|");
        String newStudent = new Scanner(System.in).nextLine();

        try (FileWriter writer = new FileWriter(studentsTable, true)) {
            writer.write(id + " " + newStudent + "\n");
        } catch (IOException e) {
            Logg3r.logNew(Level.SEVERE, "Error adding data to students table: " + e.getMessage());
            WorkingInterfaces.studentsChoiceMenu();
        }
        System.out.println("Success!");
    }

    public static void addNewVariant() {
        ArrayList<Variant> array = getAllVariants();
        int id = array.size() > 0 ? array.get(array.size() - 1).getId() + 1 : 1;

        System.out.println("|Enter the variant path.|");
        String newVar = new Scanner(System.in).nextLine();

        if (!checkDuplicatedVariant(array, newVar)) {
            WorkingInterfaces.variantsChoiceMenu();
        }

        try (FileWriter writer = new FileWriter(variantsTable, true)) {
            writer.write(id + " " + newVar + "\n");
        } catch (IOException e) {
            Logg3r.logNew(Level.SEVERE, "Error adding data to variants table: " + e.getMessage());
            WorkingInterfaces.variantsChoiceMenu();
        }
        System.out.println("Success!");
    }

    public static void deleteVariant() {
        ArrayList<Variant> array = getAllVariants();
        printAllVariants(array);
        System.out.println("|Which variant would you like to change?(write id)|");
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
            WorkingInterfaces.variantsChoiceMenu();
        }
        if (changed) {
            System.out.println("Done!");
        } else {
            System.out.println("Variant with specified id not found.");
        }
    }

    public static void deleteStudent() {
        ArrayList<Student> array = getAllStudents();
        printAllStudents(array);
        System.out.println("|Which student would you like to delete?(write id)|");
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
            WorkingInterfaces.studentsChoiceMenu();
        }
        if (changed) {
            System.out.println("Done!");
        } else {
            System.out.println("Student with specified id not found.");
        }
    }

    public static void alterVariant() {
        ArrayList<Variant> array = getAllVariants();
        printAllVariants(array);
        System.out.println("|Which variant would you like to change?(write id)|");
        int id = new Scanner(System.in).nextInt();

        System.out.println("Write the new variant path.");
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
                WorkingInterfaces.variantsChoiceMenu();
            }
        }
        if (changed) {
            System.out.println("Done!");
        } else {
            System.out.println("Variant with specified id not found.");
        }
    }

    public static void alterStudent() {
        ArrayList<Student> array = getAllStudents();
        printAllStudents(array);
        System.out.println("|Which student would you like to change?(write id)|");
        int id = new Scanner(System.in).nextInt();

        System.out.println("|Write the new Student full name.(Without id)|");
        String newStudent = new Scanner(System.in).nextLine();

        String[] a = newStudent.split("\\s+");
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
            WorkingInterfaces.studentsChoiceMenu();
        }
        if (changed) {
            System.out.println("Done!");
        } else {
            System.out.println("Student with specified id not found.");
        }
    }

    public static ArrayList<Student> getAllStudents() {
        ArrayList<Student> array = new ArrayList<>();
        try (Scanner scanner = new Scanner(studentsTable)) {
            while (scanner.hasNext()) {
                String[] a = scanner.nextLine().split("\\s+");
                array.add(new Student(Integer.parseInt(a[0]), a[1], a[2], a[3]));
            }
        } catch (FileNotFoundException e) {
            Logg3r.logNew(Level.SEVERE, "Error accessing students table: " + e.getMessage());
            WorkingInterfaces.studentsChoiceMenu();
        }
        return array;
    }

    public static void getStudentById() {
        System.out.println("|Enter the student's id:|");
        int id = new Scanner(System.in).nextInt();
        ArrayList<Student> array = getAllStudents();
        for (Student st : array) {
            if (st.getId() == id) {
                System.out.println("--------------------------------------------------------------------------------------------");
                System.out.printf("%3s %20s %20s %20s\n", "id", "name", "surname", "patronymic");
                System.out.printf("%3s %20s %20s %20s\n", st.getId(), st.getName(), st.getSurname(), st.getPatronymic());
                System.out.println("--------------------------------------------------------------------------------------------");
                WorkingInterfaces.studentsChoiceMenu();
            }
        }
        System.out.println("Student with id " + id + " not found.");
    }

    public static void getVariantById() {
        System.out.println("|Enter the variant's id:|");
        int id = new Scanner(System.in).nextInt();
        ArrayList<Variant> array = getAllVariants();
        for (Variant var : array) {
            if (var.getId() == id) {
                System.out.println("--------------------------------------------------------------------------------------------");
                System.out.printf("%3s %20s\n", "id", "path_to_file");
                System.out.printf("%3s %20s\n", var.getId(), var.getPathToFile());
                System.out.println("--------------------------------------------------------------------------------------------");
                WorkingInterfaces.variantsChoiceMenu();
            }
        }
        System.out.println("Variant with id " + id + "not found.");
    }

    public static ArrayList<Variant> getAllVariants() {
        ArrayList<Variant> array = new ArrayList<>();
        try (Scanner scanner = new Scanner(variantsTable)) {
            while (scanner.hasNext()) {
                String[] a = scanner.nextLine().split("\\s+");
                array.add(new Variant(Integer.parseInt(a[0]), a[1]));
            }
        } catch (FileNotFoundException e) {
            Logg3r.logNew(Level.SEVERE, "Error accessing variants table: " + e.getMessage());
            WorkingInterfaces.variantsChoiceMenu();
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
        System.out.println("--------------------------------------------------------------------------------------------");
        System.out.printf("%3s %20s %20s %20s\n", "id", "name", "surname", "patronymic");
        for (Student st : array) {
            System.out.printf("%3d %20s %20s %20s\n", st.getId(), st.getName(), st.getSurname(), st.getPatronymic());
        }
        System.out.println("--------------------------------------------------------------------------------------------");
    }

    public static void printAllVariants(ArrayList<Variant> array) {
        System.out.println("--------------------------------------------------------------------------------------------");
        System.out.printf("%3s %20s\n", "id", "path_to_file");
        for (Variant st : array) {
            System.out.printf("%3s %20s\n", st.getId(), st.getPathToFile());
        }
        System.out.println("--------------------------------------------------------------------------------------------");
    }

    public static ArrayList<Student> getStudentDataFromFile(File file, int id) {
        ArrayList<Student> array = new ArrayList<>();
        try(Scanner scanner = new Scanner(file)) {
            while (scanner.hasNext()) {
                String[] a = scanner.nextLine().split("\\s+");
                array.add(new Student(id++, a[0], a[1], a[2]));
            }
        } catch (FileNotFoundException e) {
            Logg3r.logNew(Level.SEVERE, "Error accessing file " + file.getName() + " :" + e.getMessage());
        }
        return array;
    }

    public static ArrayList<Variant> getVariantsDataFromFile(File file, int id) {
        ArrayList<Variant> array = new ArrayList<>();
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNext()) {
                array.add(new Variant(id++, scanner.nextLine()));
            }
        } catch (FileNotFoundException e) {
            Logg3r.logNew(Level.SEVERE, "Error accessing file " + file.getName() + " :" + e.getMessage());
        }
        return array;
    }

}
