package Package3;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class VernamCipher {
    
    // XOR двух массивов байтов
    public static byte[] xor(byte[] a, byte[] b) {
        int len = Math.min(a.length, b.length);
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++) {
            result[i] = (byte) (a[i] ^ b[i]);
        }
        return result;
    }
    
    // Чтение файла
    public static byte[] readFile(String filename) throws IOException {
        try (FileInputStream fis = new FileInputStream(filename)) {
            byte[] data = new byte[fis.available()];
            fis.read(data);
            return data;
        }
    }
    
    // Запись файла
    public static void writeFile(String filename, byte[] data) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filename)) {
            fos.write(data);
        }
    }
    
    public static void main(String[] args) {
        try {
            // Сначала создадим тестовый файл для шифрования
            String plaintext = "Hello, World! This is a test message for Vernam cipher.\n"
                              + "Vernam cipher is absolutely secure if the key is truly random "
                              + "and used only once.\n";
            writeFile("plain.txt", plaintext.getBytes());
            System.out.println("Создан файл plain.txt");
            
            // Читаем plain.txt и ключ
            byte[] plain = readFile("plain.txt");
            byte[] key = readFile("key.bin");
            
            if (plain.length > key.length) {
                System.out.println("Ошибка: ключ короче сообщения!");
                return;
            }
            
            // Шифруем
            byte[] cipher = xor(plain, key);
            writeFile("cipher_vernam.bin", cipher);
            System.out.println("Зашифровано в cipher_vernam.bin");
            
            // Расшифровываем
            byte[] decrypted = xor(cipher, key);
            writeFile("decrypted_vernam.txt", decrypted);
            System.out.println("Расшифровано в decrypted_vernam.txt");
            
            // Проверяем, совпадает ли расшифрованный с исходным
            String original = new String(plain);
            String recovered = new String(decrypted);
            if (original.equals(recovered)) {
                System.out.println("✓ Расшифрование прошло успешно!");
            } else {
                System.out.println("✗ Ошибка расшифрования!");
            }
            
        } catch (IOException e) {
            System.err.println("Ошибка: " + e.getMessage());
        }
    }
}