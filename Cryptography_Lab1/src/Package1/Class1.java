package Package1;

import java.util.Scanner;

public class Class1 {
    
    public static String encrypt(String text, int key) {
        StringBuilder result = new StringBuilder();
        text = text.toUpperCase();
        
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c >= 'A' && c <= 'Z') {
                int newChar = c + key;
                if (newChar > 'Z') {
                    newChar = newChar - 26;
                }
                result.append((char) newChar);
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }
    
    public static String decrypt(String text, int key) {
        return encrypt(text, 26 - key);
    }
    
    public static int findKey(String plaintext, String ciphertext) {
        plaintext = plaintext.toUpperCase();
        ciphertext = ciphertext.toUpperCase();
        
        char plainChar = plaintext.charAt(0);
        char cipherChar = ciphertext.charAt(0);
        
        int key = cipherChar - plainChar;
        if (key < 0) {
            key = key + 26;
        }
        
        for (int i = 0; i < plaintext.length(); i++) {
            char p = plaintext.charAt(i);
            char c = ciphertext.charAt(i);
            int expected = c - p;
            if (expected < 0) expected += 26;
            if (expected != key) {
                return -1;
            }
        }
        return key;
    }
    
    public static void tryAllKeys(String ciphertext) {
        System.out.println("\n=== ПЕРЕБОР ВСЕХ КЛЮЧЕЙ ===");
        for (int key = 0; key < 26; key++) {
            String decrypted = decrypt(ciphertext, key);
            System.out.printf("Ключ %2d: %s\n", key, decrypted);
        }
    }
    
    public static int findKeyWithDictionary(String ciphertext, String[] dictionary) {
        for (int key = 0; key < 26; key++) {
            String decrypted = decrypt(ciphertext, key);
            decrypted = decrypted.toLowerCase();
            
            for (String word : dictionary) {
                if (decrypted.contains(word.toLowerCase())) {
                    return key;
                }
            }
        }
        return -1;
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("========================================");
        System.out.println("       ШИФР ЦЕЗАРЯ - ЛАБОРАТОРНАЯ 1");
        System.out.println("========================================");
        
        while (true) {
            System.out.println("\nВыберите действие:");
            System.out.println("1. Зашифровать текст");
            System.out.println("2. Расшифровать текст");
            System.out.println("3. Атака по известному открытому тексту (найти ключ)");
            System.out.println("4. Атака по шифртексту (перебор всех ключей)");
            System.out.println("5. Атака по шифртексту со словарем");
            System.out.println("0. Выход");
            System.out.print("Ваш выбор: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            if (choice == 0) {
                System.out.println("До свидания!");
                break;
            }
            
            switch (choice) {
                case 1:
                    System.out.print("Введите текст для шифрования: ");
                    String plain = scanner.nextLine();
                    System.out.print("Введите ключ (0-25): ");
                    int keyEnc = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("Результат: " + encrypt(plain, keyEnc));
                    break;
                    
                case 2:
                    System.out.print("Введите текст для расшифрования: ");
                    String cipher = scanner.nextLine();
                    System.out.print("Введите ключ (0-25): ");
                    int keyDec = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("Результат: " + decrypt(cipher, keyDec));
                    break;
                    
                case 3:
                    System.out.print("Введите открытый текст: ");
                    String knownPlain = scanner.nextLine();
                    System.out.print("Введите зашифрованный текст: ");
                    String knownCipher = scanner.nextLine();
                    int foundKey = findKey(knownPlain, knownCipher);
                    if (foundKey != -1) {
                        System.out.println("Найден ключ: " + foundKey);
                    } else {
                        System.out.println("Ошибка: тексты не соответствуют друг другу!");
                    }
                    break;
                    
                case 4:
                    System.out.print("Введите зашифрованный текст: ");
                    String cipherForBrute = scanner.nextLine();
                    tryAllKeys(cipherForBrute);
                    break;
                    
                case 5:
                    System.out.print("Введите зашифрованный текст: ");
                    String cipherForDict = scanner.nextLine();
                    
                    String[] dictionary = {
                        "hello", "world", "test", "good", "bad", "message", 
                        "secret", "computer", "program", "java", "cipher", 
                        "key", "text", "word", "the", "and", "for", "are"
                    };
                    
                    int dictKey = findKeyWithDictionary(cipherForDict, dictionary);
                    if (dictKey != -1) {
                        System.out.println("Найден ключ: " + dictKey);
                        System.out.println("Расшифрованный текст: " + decrypt(cipherForDict, dictKey));
                    } else {
                        System.out.println("Не удалось найти ключ со словарем.");
                        System.out.println("Пробуем вывести все варианты:");
                        tryAllKeys(cipherForDict);
                    }
                    break;
                    
                default:
                    System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }
        
        scanner.close();
    }
}