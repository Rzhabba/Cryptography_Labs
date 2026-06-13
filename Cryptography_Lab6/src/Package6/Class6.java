package Package6;

import java.util.Scanner;

public class Class6 {
    
    private static int multiplicationCount = 0;
    
    /**
     * Быстрое возведение в степень по модулю (алгоритм справа-налево)
     */
    public static long powModFast(long a, long x, long p) {
        multiplicationCount = 0;
        
        System.out.println("\n========================================");
        System.out.println("БЫСТРОЕ ВОЗВЕДЕНИЕ В СТЕПЕНЬ ПО МОДУЛЮ");
        System.out.println("========================================");
        System.out.println("Вычисляем: " + a + "^" + x + " mod " + p);
        System.out.println();
        
        // Двоичное представление показателя
        String binaryX = Long.toBinaryString(x);
        System.out.println("Показатель x = " + x + " в двоичном виде: " + binaryX);
        System.out.println();
        
        // Таблица трассировки (как в лекции)
        System.out.println("i | Текущий бит | s (текущее) | y (результат)");
        System.out.println("--|-------------|-------------|--------------");
        
        long y = 1;
        long s = a;
        
        for (int i = 0; i < binaryX.length(); i++) {
            // Берём биты справа налево (младшие разряды первыми)
            char bit = binaryX.charAt(binaryX.length() - 1 - i);
            
            System.out.printf("%d |      %c      | %11d | %12d\n", i, bit, s, y);
            
            // Если бит = 1, умножаем y на s
            if (bit == '1') {
                y = (y * s) % p;
                multiplicationCount++;
                System.out.printf("   |             |             | после *: %d\n", y);
            }
            
            // Возводим s в квадрат (для следующего бита)
            if (i < binaryX.length() - 1) {
                s = (s * s) % p;
                multiplicationCount++;
                System.out.printf("   |             | после ^2: %d | \n", s);
            }
        }
        
        System.out.println("--|-------------|-------------|--------------");
        System.out.println();
        
        return y;
    }
    
    /**
     * Медленное возведение в степень (только для проверки)
     */
    public static long powModSlow(long a, long x, long p) {
        long result = 1;
        for (long i = 0; i < x; i++) {
            result = (result * a) % p;
        }
        return result;
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Введите основание a: ");
        long a = scanner.nextLong();
        
        System.out.print("Введите показатель x: ");
        long x = scanner.nextLong();
        
        System.out.print("Введите модуль p: ");
        long p = scanner.nextLong();
        
        scanner.close();
        
        // Быстрое возведение (с трассировкой)
        long resultFast = powModFast(a, x, p);
        
        // Медленное возведение (для проверки)
        long resultSlow = powModSlow(a, x, p);
        
        System.out.println("=== РЕЗУЛЬТАТЫ ===");
        System.out.println("Быстрый алгоритм: " + a + "^" + x + " mod " + p + " = " + resultFast);
        System.out.println("Медленный алгоритм (проверка): " + resultSlow);
        System.out.println("Количество умножений: " + multiplicationCount);
        
        int bits = Long.toBinaryString(x).length();
        System.out.println("Теоретический максимум: 2 * log2(x) ≈ " + (2 * bits));
        
        // Проверка с разным весом Хэмминга
        System.out.println("\n=== ПРОВЕРКА С РАЗНЫМ ВЕСОМ ХЭММИНГА ===");
        System.out.println("Показатель | Вес Хэмминга | Умножений | Теор. максимум");
        System.out.println("-----------|---------------|-----------|---------------");
        
        long[] exponents = {1, 2, 3, 7, 8, 15, 16, 31, 32, 63, 64, 127, 128, 255, 256, 511, 512};
        
        for (long exp : exponents) {
            if (exp > x) continue;
            
            // Быстрое вычисление без вывода трассировки
            long y = 1;
            long s = a;
            int mulCount = 0;
            String bin = Long.toBinaryString(exp);
            
            for (int i = 0; i < bin.length(); i++) {
                char bit = bin.charAt(bin.length() - 1 - i);
                if (bit == '1') {
                    y = (y * s) % p;
                    mulCount++;
                }
                if (i < bin.length() - 1) {
                    s = (s * s) % p;
                    mulCount++;
                }
            }
            
            int weight = Long.bitCount(exp);
            int bitLen = bin.length();
            System.out.printf("%10d | %13d | %9d | %15d\n", exp, weight, mulCount, 2 * bitLen);
        }
        
        System.out.println("\nГотово!");
    }
}