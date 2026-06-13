package Package5;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Class5 {
    
    private static final String HEXES = "0123456789ABCDEF";
    
    // Преобразование байтов в шестнадцатеричную строку
    public static String bytesToHex(byte[] bytes) {
        StringBuilder hex = new StringBuilder();
        for (byte b : bytes) {
            hex.append(HEXES.charAt((b & 0xF0) >> 4));
            hex.append(HEXES.charAt((b & 0x0F)));
        }
        return hex.toString();
    }
    
    // Преобразование шестнадцатеричной строки в битовую строку
    public static String hexToBits(String hex) {
        StringBuilder bits = new StringBuilder();
        for (int i = 0; i < hex.length(); i++) {
            char c = hex.charAt(i);
            int val;
            if (c >= '0' && c <= '9') val = c - '0';
            else val = 10 + (c - 'A');
            
            // Добавляем 4 бита
            bits.append((val >> 3) & 1);
            bits.append((val >> 2) & 1);
            bits.append((val >> 1) & 1);
            bits.append(val & 1);
        }
        return bits.toString();
    }
    
    // Вычисление SHA-256 хеша
    public static byte[] sha256(byte[] input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return md.digest(input);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 не поддерживается", e);
        }
    }
    
    // Получение первых N бит хеша в виде строки
    public static String getFirstNBits(byte[] hash, int n) {
        String hex = bytesToHex(hash);
        String bits = hexToBits(hex);
        return bits.substring(0, Math.min(n, bits.length()));
    }
    
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("   ПОИСК ЧАСТИЧНЫХ КОЛЛИЗИЙ SHA-256");
        System.out.println("   (Парадокс дней рождения)");
        System.out.println("========================================\n");
        
        // Запрашиваем количество бит для коллизии
        System.out.print("Сколько бит должны совпадать? (рекомендуется 16-24): ");
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        int bits = scanner.nextInt();
        scanner.close();
        
        System.out.println("\nИщем два разных сообщения, у которых первые " + bits + " бит хеша совпадают...");
        System.out.println("Теоретически потребуется около " + (int)(1.25 * Math.sqrt(Math.pow(2, bits))) + " попыток\n");
        
        Random random = new Random();
        Map<String, String> seen = new HashMap<>();
        int attempts = 0;
        
        long startTime = System.currentTimeMillis();
        
        while (true) {
            attempts++;
            
            // Генерируем случайное сообщение
            int length = 10 + random.nextInt(20); // 10-30 байт
            byte[] message = new byte[length];
            random.nextBytes(message);
            String messageStr = bytesToHex(message);
            
            // Вычисляем хеш
            byte[] hash = sha256(message);
            String hashPrefix = getFirstNBits(hash, bits);
            
            // Проверяем, есть ли уже такой префикс
            if (seen.containsKey(hashPrefix)) {
                String existingMessage = seen.get(hashPrefix);
                if (!existingMessage.equals(messageStr)) {
                    // Нашли коллизию!
                    long endTime = System.currentTimeMillis();
                    System.out.println("\n✅ КОЛЛИЗИЯ НАЙДЕНА!");
                    System.out.println("Попыток: " + attempts);
                    System.out.println("Время: " + (endTime - startTime) + " мс");
                    System.out.println("\n--- СООБЩЕНИЕ 1 ---");
                    System.out.println("Hex: " + existingMessage);
                    byte[] hash1 = sha256(hexToBytes(existingMessage));
                    System.out.println("SHA-256: " + bytesToHex(hash1));
                    System.out.println("Первые " + bits + " бит: " + getFirstNBits(hash1, bits));
                    
                    System.out.println("\n--- СООБЩЕНИЕ 2 ---");
                    System.out.println("Hex: " + messageStr);
                    System.out.println("SHA-256: " + bytesToHex(hash));
                    System.out.println("Первые " + bits + " бит: " + hashPrefix);
                    
                    System.out.println("\n✓ Первые " + bits + " бит совпадают!");
                    break;
                }
            } else {
                seen.put(hashPrefix, messageStr);
            }
            
            // Прогресс каждые 10000 попыток
            if (attempts % 10000 == 0) {
                System.out.println("Попыток: " + attempts + ", уникальных префиксов: " + seen.size());
            }
        }
    }
    
    // Преобразование шестнадцатеричной строки в байты
    public static byte[] hexToBytes(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                                 + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }
}