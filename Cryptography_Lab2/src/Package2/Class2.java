package Package2;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public class Class2 {
    
    /**
     * Вычисляет энтропию Шеннона для массива байтов
     * @param data массив байтов
     * @return энтропия в битах
     */
    public static double calculateEntropy(byte[] data) {
        if (data.length == 0) return 0;
        
        // Считаем частоты каждого байта (0-255)
        long[] frequencies = new long[256];
        for (byte b : data) {
            // Преобразуем байт в беззнаковое целое 0-255
            int index = b & 0xFF;
            frequencies[index]++;
        }
        
        double entropy = 0;
        double total = data.length;
        
        for (long freq : frequencies) {
            if (freq > 0) {
                double p = freq / total;
                entropy -= p * (Math.log(p) / Math.log(2)); // log2(p)
            }
        }
        
        return entropy;
    }
    
    /**
     * Читает файл и возвращает его содержимое как массив байтов
     */
    public static byte[] readFile(String filename) throws IOException {
        File file = new File(filename);
        byte[] data = new byte[(int) file.length()];
        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(data);
        }
        return data;
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("========================================");
        System.out.println("   ВЫЧИСЛЕНИЕ ЭНТРОПИИ ФАЙЛА");
        System.out.println("   по формуле Клода Шеннона");
        System.out.println("========================================");
        System.out.println();
        
        while (true) {
            System.out.print("Введите имя анализируемого файла (или 'exit' для выхода): ");
            String path = scanner.nextLine().trim();
            
            if (path.equalsIgnoreCase("exit")) {
                System.out.println("До свидания!");
                break;
            }
            
            try {
                // Читаем файл
                byte[] data = readFile(path);
                System.out.println("Размер файла: " + data.length + " байт");
                
                // Вычисляем энтропию
                double entropy = calculateEntropy(data);
                System.out.printf("Энтропия: %.4f бит/байт\n", entropy);
                
                // Теоретический максимум для байтовых данных = 8 бит
                System.out.printf("Максимум для случайных байтов: 8.0000 бит/байт\n");
                System.out.printf("Относительная энтропия: %.2f%%\n", (entropy / 8.0) * 100);
                System.out.println();
                
            } catch (IOException e) {
                System.out.println("Ошибка: файл не найден или не удалось прочитать.");
                System.out.println("Проверьте путь и попробуйте снова.\n");
            }
        }
        
        scanner.close();
    }
}