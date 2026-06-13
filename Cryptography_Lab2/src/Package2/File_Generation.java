package Package2;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class File_Generation {
    
    public static void generateRandomBytes(String filename, int size, Random random) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filename)) {
            byte[] data = new byte[size];
            random.nextBytes(data);
            fos.write(data);
        }
        System.out.println("Создан файл: " + filename + " (" + size + " байт)");
    }
    
    public static void generateSameBytes(String filename, int size, byte value) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filename)) {
            byte[] data = new byte[size];
            for (int i = 0; i < size; i++) {
                data[i] = value;
            }
            fos.write(data);
        }
        System.out.println("Создан файл: " + filename + " (" + size + " байт)");
    }
    
    public static void main(String[] args) {
        Random random = new Random();
        
        try {
            // 1. Файл из одинаковых символов (буква 'A' = 65)
            generateSameBytes("test_same_A.bin", 10000, (byte) 65);
            
            // 2. Файл из случайных байтов
            generateRandomBytes("test_random.bin", 10000, random);
            
            // 3. Файл из битов 0 и 1 (чередование)
            try (FileOutputStream fos = new FileOutputStream("test_bits.bin")) {
                byte[] data = new byte[10000];
                for (int i = 0; i < data.length; i++) {
                    data[i] = (byte) (i % 2); // 0,1,0,1...
                }
                fos.write(data);
            }
            System.out.println("Создан файл: test_bits.bin (10000 байт)");
            
            // 4. Файл из нулей
            generateSameBytes("test_zeros.bin", 10000, (byte) 0);
            
        } catch (IOException e) {
            System.err.println("Ошибка при создании файлов: " + e.getMessage());
        }
    }
}