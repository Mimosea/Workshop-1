import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TaskManager {

    static final String FILE_NAME = "tasks.csv";
    static final String[] OPTIONS = {"add", "remove", "list", "exit"};
    static String[][] tasks;

    public static void main(String[] args) {
        tasks = loadDataToTab(FILE_NAME);
        printOptions(OPTIONS);

        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();

            switch (input) {
                case "exit":
                    saveTabToFile(FILE_NAME, tasks);
                    System.out.println(ConsoleColors.RED + "Bye, bye.");
                    System.exit(0);
                    break;
                case "add":
                    addTask();
                    break;
                case "remove":
                    removeTask(tasks, getTheNumber());
                    System.out.println("Value was successfully deleted.");
                    break;
                case "list":
                    printTab(tasks);
                    break;
                default:
                    System.out.println("Please select a correct option.");
            }

            printOptions(OPTIONS);
        }
    }

    public static void printTab(String[][] tab) {
        for (int i = 0; i < tab.length; i++) {
            System.out.print(i + " : ");
            for (int j = 0; j < tab[i].length; j++) {
                System.out.print(tab[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void printOptions(String[] options) {
        System.out.println("Select an option:");
        for (String option : options) {
            System.out.println("- " + option);
        }
    }

    private static void addTask() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please add task description");
        String description = scanner.nextLine();

        System.out.println("Please add task due date");
        String dueDate = scanner.nextLine();

        System.out.println("Is your task important: true/false");
        String isImportant = scanner.nextLine();

        tasks = Arrays.copyOf(tasks, tasks.length + 1);
        tasks[tasks.length - 1] = new String[3];
        tasks[tasks.length - 1][0] = description;
        tasks[tasks.length - 1][1] = dueDate;
        tasks[tasks.length - 1][2] = isImportant;
    }

    public static boolean isNumberGreaterEqualZero(String input) {
        if (NumberUtils.isParsable(input)) {
            return Integer.parseInt(input) >= 0;
        }
        return false;
    }

    public static int getTheNumber() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please select number to remove.");
        String n = scanner.nextLine();
        while (!isNumberGreaterEqualZero(n)) {
            System.out.println("Incorrect argument passed. Please give number greater or equal 0");
            n = scanner.nextLine();
        }
        return Integer.parseInt(n);
    }

    private static void removeTask(String[][] tab, int index) {
        try {
            if (index < tab.length) {
                tasks = ArrayUtils.remove(tab, index);
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("Element does not exist in the array.");
        }
    }

    public static String[][] loadDataToTab(String fileName) {
        Path filePath = Paths.get(fileName);
        try {
            List<String> lines = Files.readAllLines(filePath);
            String[][] tab = new String[lines.size()][];
            for (int i = 0; i < lines.size(); i++) {
                String[] line = lines.get(i).split(",");
                tab[i] = line;
            }
            return tab;
        } catch (IOException e) {
            e.printStackTrace();
            return new String[0][];
        }
    }

    public static void saveTabToFile(String fileName, String[][] tab) {
        Path filePath = Paths.get(fileName);
        String[] lines = new String[tab.length];
        for (int i = 0; i < tab.length; i++) {
            lines[i] = String.join(",", tab[i]);
        }
        try {
            Files.write(filePath, Arrays.asList(lines));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    static class ConsoleColors {
        public static final String RESET = "\033[0m";
        public static final String RED = "\033[0;31m";
    }
}
