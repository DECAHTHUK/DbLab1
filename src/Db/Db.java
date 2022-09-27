package Db;

import MyLogger.Logg3r;
import Persistence.WorkingInterfaces;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;
import java.util.logging.Level;

public class Db {
    public static void createNewDb() {
        System.out.println("Enter the name you want:");
        String name = new Scanner(System.in).nextLine();
        String path = ".\\DataBases\\" + name;
        File file = new File(path);
        boolean createdNewDir = file.mkdir();
        if (!createdNewDir) {
            System.out.println("This db already exists!");
        }
        else {
            createTables(name);
            if (name.length() > 15) {
                System.out.println("Wow, what a name...");
            }
            System.out.println("Success");
            accessExisting(Action.ACCESS);
        }
    }

    public static void createTables(String name) {
        String path = ".\\Databases\\" + name;
        String backupPath = ".\\Backups\\" + name;
        File file1 = new File(path + "\\students.txt");
        File file2 = new File(path + "\\variants.txt");
        File file3 = new File(path + "\\testingTable.txt");
        File backup = new File(backupPath);
        try {
            file1.createNewFile();
            file2.createNewFile();
            file3.createNewFile();
            backup.mkdir();
        } catch (IOException e) {
            Logg3r.logNew(Level.SEVERE, "Something wrong with creating tables: " + e.getMessage());
        }
    }

    public static void accessExisting(Action action) {
        File[] directories = new File(".\\Databases\\").listFiles(File::isDirectory);
        if (directories.length == 0) {
            System.out.println("You don't have any dbs yet.");
            WorkingInterfaces.mainChooseMenu();
        }
        System.out.println("Choose the database:");
        int cnt = 1;
        for (File a : directories) {
            System.out.println(cnt++ + ". " + a.getName());
        }
        int number = new Scanner(System.in).nextInt();
        if (number < 1 || number > directories.length) {
            System.out.println("Wrong number, cringe...");
            accessExisting(action);
        } else {
            DatabaseController.setDbFile(directories[number - 1].getName());
            switch (action) {
                case LOAD -> loadABackUp(directories[number - 1].getName());
                case ACCESS -> WorkingInterfaces.tableChoice();
                case SAVE -> createABackUp(directories[number - 1].getName());
            }
        }
    }

    public static void createABackUp(String dbName) {
        try {
            System.out.println("|Type in the version of the backup you wanna make:|");
            String version = new Scanner(System.in).nextLine();
            File backupFolder = new File(".\\Backups\\" + dbName + "\\" + version);
            boolean isMade = backupFolder.mkdir();
            if (!isMade) {
                System.out.println("Such version already exists!");
                WorkingInterfaces.mainChooseMenu();
            } else {
                String[] paths = new String[]{"\\students.txt", "\\variants.txt", "\\testingTable.txt"};
                for (String file : paths) {
                    Path original = Paths.get(".\\DataBases\\" + dbName + file);
                    Path copied = Paths.get(".\\Backups\\" + dbName + "\\" + version + file);
                    Files.copy(original, copied, StandardCopyOption.REPLACE_EXISTING);
                }
                System.out.println("Success!");
            }

        } catch (IOException e) {
            Logg3r.logNew(Level.SEVERE, "Error creating a backup: " + e.getMessage());
        }
        WorkingInterfaces.mainChooseMenu();
    }

    public static void loadABackUp(String dbName) {
        try {
            File[] directories = new File(".\\Backups\\" + dbName).listFiles(File::isDirectory);
            if (directories.length == 0) {
                System.out.println("You don't have any backups yet.");
                WorkingInterfaces.mainChooseMenu();
            }
            int cnt = 1;
            for (File a : directories) {
                System.out.println(cnt++ + ". " + a.getName());
            }
            System.out.println("Choose the backup:");

            int number = new Scanner(System.in).nextInt();
            if (number < 1 || number > directories.length) {
                System.out.println("Wrong number, cringe...");
                WorkingInterfaces.mainChooseMenu();
            } else {
                System.out.println("""
                        WARNING: THE EXISTING DATA IN YOUR DATABASE WILL BE OVERWRITTEN!
                        DO YOU WANT TO CONTINUE?
                        1. Yes.
                        2. No""");
                int choice = new Scanner(System.in).nextInt();
                switch (choice) {
                    case 1:
                        break;
                    case 2:
                        WorkingInterfaces.mainChooseMenu();
                        break;
                    default:
                        System.out.println("Wrong choice.");
                        break;
                }
                String[] paths = new String[]{"\\students.txt", "\\variants.txt", "\\testingTable.txt"};
                for (String file : paths) {
                    Path original = Paths.get(".\\Backups\\" + dbName + "\\"
                            + directories[number - 1].getName() + file);
                    Path copied = Paths.get(".\\DataBases\\" + dbName + file);
                    Files.copy(original, copied, StandardCopyOption.REPLACE_EXISTING);
                }
                System.out.println("Success!");

            }
        } catch (IOException e) {
            Logg3r.logNew(Level.SEVERE, "Error loading a backup: " + e.getMessage());
        }
        WorkingInterfaces.mainChooseMenu();
    }
}
