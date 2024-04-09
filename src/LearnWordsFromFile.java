
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class LearnWordsFromFile {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Select unit number (1, 2, 3, etc.): ");
        int unitNumber = scanner.nextInt();
        String filePath = "C:\\Users\\Oleksii\\AppData\\Roaming\\JetBrains\\IntelliJIdea2023.3\\scratches\\unit" + unitNumber + ".txt";

        if (!new File(filePath).exists()) {
            System.out.println("The file for the selected unit was not found.");
            return;
        }

        LearnWordsFromFile learnWords = new LearnWordsFromFile();
        learnWords.startLearning(filePath);

        scanner.close();
    }

    private void startLearning(String filePath) {
        Map<String, String> wordsMap = new HashMap<>();
        List<Map.Entry<String, String>> incorrectWords = new ArrayList<>(1);

        try {
            Scanner fileScanner = new Scanner(new File(filePath));

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split("\\s{2}");
                if (parts.length >= 2) {
                    String word = parts[0];
                    String translation = parts[1];
                    wordsMap.put(word, translation);
                }
            }
            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
            return;
        }

        // Создаем сканер для ввода с клавиатуры
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        int choice;
        do {
            System.out.println("Select translation direction:");
            System.out.println("1. Learning words from Norwegian to Russian");
            System.out.println("2. Learning words from Russian to Norwegian");
            System.out.println("3. End the program");
            System.out.print("Your choice:");
            choice = scanner.nextInt();
            scanner.nextLine(); // Clear the buffer

            if (choice == 1 || choice == 2) {
                // Get an array of keys from the dictionary and shuffle it
                Object[] keys = wordsMap.keySet().toArray();
                Collections.shuffle(Arrays.asList(keys), random);

                for (Object key : keys) {
                    String word = (String) key;
                    String translation = wordsMap.get(word);

                    System.out.print("Enter the translation of the word " + (choice == 1 ? word : translation) + "': ");
                    String userTranslation = scanner.nextLine();

                    if (!userTranslation.equalsIgnoreCase(choice == 1 ? translation : word)) {
                        System.out.println("Wrong. Correct answer:" + (choice == 1 ? translation : word));
                        incorrectWords.add(new AbstractMap.SimpleEntry<>(choice == 1 ? word : translation, choice == 1 ? translation : word)); // Add a word-translation pair
                    } else {
                        System.out.println("Right!");
                    }
                }

                // Check if there are any incorrectly answered words
                if (!incorrectWords.isEmpty()) {
                    System.out.println("Do you want to go over the incorrectly answered words again? (Not/Yes): ");
                    String answer = scanner.nextLine();
                    if (answer.equalsIgnoreCase("Yes")) {
                        System.out.println("Let's go through the incorrectly answered words again: ");
                        for (Map.Entry<String, String> entry : incorrectWords) {
                            String wordToTranslate = entry.getKey();
                            String correctTranslation = entry.getValue();
                            System.out.print("Enter word translation" + wordToTranslate + "': ");
                            String userTranslation = scanner.nextLine();
                            if (!userTranslation.equalsIgnoreCase(correctTranslation)) {
                                System.out.println("Wrong. Correct answer: " + correctTranslation);
                            } else {
                                System.out.println("Right!");
                            }
                        }
                        // Clear the list of incorrectly answered words for the next iteration
                        incorrectWords.clear();
                    }
                }
            } else if (choice != 3) {
                System.out.println("Incorrect choice.");
            }

        } while (choice != 3);
        // Close the scanner
        scanner.close();
    }
}