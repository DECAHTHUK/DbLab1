package Db;

import MyLogger.Logg3r;
import Persistence.WorkingInterfaces;

import java.io.File;
import java.io.IOException;
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
            accessExisting();
        }
    }

    public static void createTables(String name) {
        String path = ".\\Databases\\" + name;
        File file1 = new File(path + "\\students.txt");
        File file2 = new File(path + "\\variants.txt");
        File file3 = new File(path + "\\testingTable.txt");
        try {
            file1.createNewFile();
            file2.createNewFile();
            file3.createNewFile();
        } catch (IOException e) {
            Logg3r.logNew(Level.SEVERE, "Something wrong with creating tables: " + e.getMessage());
        }
    }

    public static void accessExisting() {
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
            accessExisting();
        } else {
            DatabaseController.setDbFile(directories[number - 1].getName());
        }
    }
}
