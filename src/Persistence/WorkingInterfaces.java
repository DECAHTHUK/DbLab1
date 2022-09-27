package Persistence;

import Db.Db;
import Db.DatabaseController;

import java.util.Scanner;

import static Db.Action.*;

public class WorkingInterfaces {
    public static void mainChooseMenu() {
        System.out.println("""
                
                Hello, friend, what would you like to do today?
                1. Create a new DataBase.
                2. Access an existing one.
                3. Create a backup of your database.
                4. Load a backUp of your database.
                0. Exit.""");
        int choice = new Scanner(System.in).nextInt();
        switch (choice) {
            case 1 -> Db.createNewDb();
            case 2 -> Db.accessExisting(ACCESS);
            case 3 -> Db.accessExisting(SAVE);
            case 4 -> Db.accessExisting(LOAD);
            case 0 -> {
                System.out.println("Ok...");
                System.exit(0);
            }
            default -> {
                System.out.println("Wrong choice(CHOOSE 1 OR 2 OR 3 IM NOT ASKING MUCH)");
                mainChooseMenu();
            }
        }
    }

    public static void tableChoice() {
        System.out.println("""
                
                Choose table you'd like to do sth with:
                1. Students.
                2. Variants.
                3. Testing table.
                0. Back.""");
        int choice = new Scanner(System.in).nextInt();
        switch (choice) {
            case 1 -> studentsChoiceMenu();
            case 2 -> variantsChoiceMenu();
            case 3 -> testingTableChooseMenu();
            case 0 -> mainChooseMenu();
            default -> System.out.println("Wrong choice(CHOOSE 1 OR 2 OR 3 OR 4 IM NOT ASKING MUCH)");
        }
        tableChoice();
    }

    public static void studentsChoiceMenu() {
        System.out.println("""
                
                Choose action:
                1. Add new student.
                2. Alter existing student by id.
                3. Delete student by id.
                4. Get student by id.
                5. Get students table.
                6. Add new students through file.
                0. Back.""");
        int choice = new Scanner(System.in).nextInt();
        switch (choice) {
            case 1 -> DatabaseController.addNewStudent();
            case 2 -> DatabaseController.alterStudent();
            case 3 -> DatabaseController.deleteStudent();
            case 4 -> DatabaseController.getStudentById();
            case 5 -> DatabaseController.printAllStudents(DatabaseController.getAllStudents());
            case 6 -> DatabaseController.addStudentsTableThroughFile();
            case 0 -> tableChoice();
            default -> System.out.println("Wrong number, you dumbass...");
        }
        studentsChoiceMenu();
    }

    public static void variantsChoiceMenu() {
        System.out.println("""
                
                Choose action:
                1. Add new variant.
                2. Alter existing variant by id.
                3. Delete variant by id.
                4. Get variant by id.
                5. Get variants table.
                6. Add new variants through file.
                0. Back.""");
        int choice = new Scanner(System.in).nextInt();
        switch (choice) {
            case 1 -> DatabaseController.addNewVariant();
            case 2 -> DatabaseController.alterVariant();
            case 3 -> DatabaseController.deleteVariant();
            case 4 -> DatabaseController.getVariantById();
            case 5 -> DatabaseController.printAllVariants(DatabaseController.getAllVariants());
            case 6 -> DatabaseController.addVariantsTableThroughFile();
            case 0 -> tableChoice();
            default -> System.out.println("Wrong number, you dumbass...");
        }
        variantsChoiceMenu();
    }

    public static void testingTableChooseMenu() {
        System.out.println("""
                
                Choose action:
                1. Generate testing Table.
                2. Print testing table.
                3. Back.""");
        int choice = new Scanner(System.in).nextInt();
        switch (choice) {
            case 1 -> DatabaseController.generateTestingTable();
            case 2 -> DatabaseController.getTestingTable();
            case 3 -> tableChoice();
            default -> {
                System.out.println("Wrong number, you dumbass...");
                variantsChoiceMenu();
            }
        }
        testingTableChooseMenu();
    }
}
