
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class LearnWordsFromFile {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Выберите номер юнита (1, 2, 3 и т.д.): ");
        int unitNumber = scanner.nextInt();
        String filePath = "C:\\Users\\Oleksii\\AppData\\Roaming\\JetBrains\\IntelliJIdea2023.3\\scratches\\unit" + unitNumber + ".txt";

        if (!new File(filePath).exists()) {
            System.out.println("Файл для выбранного юнита не найден.");
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
            System.out.println("Файл не найден: " + e.getMessage());
            return;
        }

        // Создаем сканер для ввода с клавиатуры
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        int choice;
        do {
            System.out.println("Выберите направление перевода:");
            System.out.println("1. Изучение слов с норвежского на русский");
            System.out.println("2. Изучение слов с русского на норвежский");
            System.out.println("3. Завершить программу");
            System.out.print("Ваш выбор: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Очищаем буфер

            if (choice == 1 || choice == 2) {
                // Получаем массив ключей из словаря и перемешиваем его
                Object[] keys = wordsMap.keySet().toArray();
                Collections.shuffle(Arrays.asList(keys), random);

                for (Object key : keys) {
                    String word = (String) key;
                    String translation = wordsMap.get(word);

                    System.out.print("Введите перевод слова '" + (choice == 1 ? word : translation) + "': ");
                    String userTranslation = scanner.nextLine();

                    if (!userTranslation.equalsIgnoreCase(choice == 1 ? translation : word)) {
                        System.out.println("Неправильно. Правильный ответ: " + (choice == 1 ? translation : word));
                        incorrectWords.add(new AbstractMap.SimpleEntry<>(choice == 1 ? word : translation, choice == 1 ? translation : word)); // Добавляем пару слово-перевод
                    } else {
                        System.out.println("Правильно!");
                    }
                }

                // Проверяем, есть ли неправильно отвеченные слова
                if (!incorrectWords.isEmpty()) {
                    System.out.println("Хотите пройти еще раз неправильно отвеченные слова? (да/нет): ");
                    String answer = scanner.nextLine();
                    if (answer.equalsIgnoreCase("да")) {
                        System.out.println("Пройдем еще раз неправильно отвеченные слова:");
                        for (Map.Entry<String, String> entry : incorrectWords) {
                            String wordToTranslate = entry.getKey();
                            String correctTranslation = entry.getValue();
                            System.out.print("Введите перевод слова '" + wordToTranslate + "': ");
                            String userTranslation = scanner.nextLine();
                            if (!userTranslation.equalsIgnoreCase(correctTranslation)) {
                                System.out.println("Неправильно. Правильный ответ: " + correctTranslation);
                            } else {
                                System.out.println("Правильно!");
                            }
                        }
                        // Очищаем список неправильно отвеченных слов для следующей итерации
                        incorrectWords.clear();
                    }
                }
            } else if (choice != 3) {
                System.out.println("Некорректный выбор.");
            }

        } while (choice != 3);

        // Закрываем сканер
        scanner.close();
    }
}