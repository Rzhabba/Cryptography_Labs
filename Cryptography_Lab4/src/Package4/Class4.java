package Package4;

import java.io.*;

public class Class4 {
    
    private TEA tea = new TEA();
    private byte[] iv = new byte[8];
    
    public void setKey(byte[] key) {
        System.out.println("Установка ключа, длина: " + key.length + " байт");
        tea.setKey(key);
    }
    
    public void setIV(byte[] iv) {
        if (iv.length != 8) {
            throw new IllegalArgumentException("IV должен быть 8 байт");
        }
        System.arraycopy(iv, 0, this.iv, 0, 8);
        System.out.println("IV установлен");
    }
    
    private byte[] pad(byte[] data) {
        int padding = 8 - (data.length % 8);
        if (padding == 0) padding = 8;
        
        byte[] padded = new byte[data.length + padding];
        System.arraycopy(data, 0, padded, 0, data.length);
        for (int i = data.length; i < padded.length; i++) {
            padded[i] = (byte) padding;
        }
        System.out.println("Дополнение: добавлено " + padding + " байт");
        return padded;
    }
    
    private byte[] unpad(byte[] data) {
        int padding = data[data.length - 1] & 0xFF;
        if (padding < 1 || padding > 8) {
            throw new IllegalArgumentException("Неверное дополнение");
        }
        byte[] result = new byte[data.length - padding];
        System.arraycopy(data, 0, result, 0, result.length);
        System.out.println("Удалено дополнение: " + padding + " байт");
        return result;
    }
    
    private byte[] readFile(String filename) throws IOException {
        try (FileInputStream fis = new FileInputStream(filename)) {
            byte[] data = new byte[fis.available()];
            fis.read(data);
            System.out.println("Прочитан файл: " + filename + ", размер: " + data.length + " байт");
            return data;
        }
    }
    
    private void writeFile(String filename, byte[] data) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filename)) {
            fos.write(data);
        }
        System.out.println("Записан файл: " + filename + ", размер: " + data.length + " байт");
    }
    
    public void encryptFile(String inputFile, String outputFile) throws IOException {
        byte[] plainData = readFile(inputFile);
        byte[] paddedData = pad(plainData);
        
        byte[] cipherData = new byte[paddedData.length];
        byte[] prevBlock = iv.clone();
        
        for (int i = 0; i < paddedData.length; i += 8) {
            byte[] block = new byte[8];
            System.arraycopy(paddedData, i, block, 0, 8);
            
            for (int j = 0; j < 8; j++) {
                block[j] ^= prevBlock[j];
            }
            
            byte[] encrypted = tea.encryptBlock(block);
            System.arraycopy(encrypted, 0, cipherData, i, 8);
            prevBlock = encrypted;
        }
        
        writeFile(outputFile, cipherData);
    }
    
    public void decryptFile(String inputFile, String outputFile) throws IOException {
        byte[] cipherData = readFile(inputFile);
        
        if (cipherData.length % 8 != 0) {
            throw new IllegalArgumentException("Размер зашифрованного файла не кратен 8");
        }
        
        byte[] plainData = new byte[cipherData.length];
        byte[] prevBlock = iv.clone();
        
        for (int i = 0; i < cipherData.length; i += 8) {
            byte[] block = new byte[8];
            System.arraycopy(cipherData, i, block, 0, 8);
            
            byte[] decrypted = tea.decryptBlock(block);
            
            for (int j = 0; j < 8; j++) {
                decrypted[j] ^= prevBlock[j];
            }
            
            System.arraycopy(decrypted, 0, plainData, i, 8);
            prevBlock = block;
        }
        
        byte[] unpaddedData = unpad(plainData);
        writeFile(outputFile, unpaddedData);
    }
    
    public static void main(String[] args) {
        try {
            System.out.println("=== TEA ШИФРОВАНИЕ ФАЙЛА ===\n");
            
            String testText = "Hello, World! This is a test message for TEA cipher.\n"
                             + "TEA is a simple block cipher with 64-bit block and 128-bit key.\n"
                             + "It uses 32 rounds of Feistel network.\n";
            byte[] testData = testText.getBytes();
            
            try (FileOutputStream fos = new FileOutputStream("plain.txt")) {
                fos.write(testData);
            }
            System.out.println("Создан файл plain.txt (" + testData.length + " байт)");
            
            // КЛЮЧ 16 БАЙТ (ИСПРАВЛЕНО!)
            byte[] key = "1234567890abcdef".getBytes();  // ← 16 символов!
            System.out.println("Ключ: \"" + new String(key) + "\", длина: " + key.length + " байт");
            
            byte[] iv = {0x12, 0x34, 0x56, 0x78, 0x7A, 0x7B, 0x7C, 0x7D};
            
            Class4 cipher = new Class4();
            cipher.setKey(key);
            cipher.setIV(iv);
            System.out.println();
            
            cipher.encryptFile("plain.txt", "cipher_tea.bin");
            System.out.println();
            
            cipher.decryptFile("cipher_tea.bin", "decrypted_tea.txt");
            System.out.println();
            
            try (FileInputStream fis = new FileInputStream("decrypted_tea.txt")) {
                byte[] recovered = new byte[fis.available()];
                fis.read(recovered);
                String recoveredText = new String(recovered);
                if (testText.equals(recoveredText)) {
                    System.out.println("✓ УСПЕХ! Шифрование и расшифрование прошли успешно!");
                } else {
                    System.out.println("✗ ОШИБКА: текст не совпадает!");
                }
            }
            
        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }
}