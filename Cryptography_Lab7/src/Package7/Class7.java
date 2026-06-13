package Package7;

import java.util.*;

public class Class7 {
    
    private static int mulCount = 0;  // счётчик умножений
    
    /**
     * Быстрое возведение в степень по модулю (с подсчётом умножений)
     */
    public static long powMod(long a, long x, long p) {
        long result = 1;
        long base = a % p;
        
        while (x > 0) {
            if ((x & 1) == 1) {
                result = (result * base) % p;
                mulCount++;
            }
            base = (base * base) % p;
            mulCount++;
            x >>= 1;
        }
        return result;
    }
    
    /**
     * Сброс счётчика умножений
     */
    private static void resetCount() {
        mulCount = 0;
    }
    
    /**
     * Метод 1: Полный перебор
     */
    public static long bruteForce(long a, long y, long p) {
        resetCount();
        System.out.println("\n=== МЕТОД ПОЛНОГО ПЕРЕБОРА ===");
        System.out.println("Ищем x: " + a + "^x ≡ " + y + " (mod " + p + ")");
        System.out.println("Перебираем x от 0 до " + (p - 1));
        
        for (long x = 0; x < p; x++) {
            long val = powMod(a, x, p);
            if (val == y) {
                System.out.println("Нашли! x = " + x);
                System.out.println("Количество умножений: " + mulCount);
                return x;
            }
        }
        System.out.println("Решение не найдено!");
        return -1;
    }
    
    /**
     * Метод 2: Шаг младенца – шаг великана (метод Шэнкса)
     */
    public static long babyStepGiantStep(long a, long y, long p) {
        resetCount();
        System.out.println("\n=== МЕТОД ШЭНКСА (Baby Step - Giant Step) ===");
        System.out.println("Решаем: " + a + "^x ≡ " + y + " (mod " + p + ")");
        
        // Выбираем m = ceil(sqrt(p))
        long m = (long) Math.ceil(Math.sqrt(p));
        System.out.println("Выбрано m = " + m);
        
        // Шаг 1: Baby steps (вычисляем a^j для j = 0..m-1)
        System.out.println("\n--- BABY STEPS (младшие шаги) ---");
        Map<Long, Long> babySteps = new HashMap<>();
        
        long current = 1;  // a^0 = 1
        for (long j = 0; j < m; j++) {
            // Запоминаем (значение, j)
            if (!babySteps.containsKey(current)) {
                babySteps.put(current, j);
            }
            System.out.println("  a^" + j + " = " + current);
            
            // Вычисляем a^(j+1) = a^j * a
            if (j < m - 1) {
                current = (current * a) % p;
                mulCount++;
            }
        }
        
        // Шаг 2: Giant steps (вычисляем y * a^(-m*i))
        System.out.println("\n--- GIANT STEPS (великаньи шаги) ---");
        
        // Вычисляем a^(-m) = a^(p-1-m) mod p (по малой теореме Ферма)
        long aInv = powMod(a, p - 1 - m, p);
        resetCount();  // сбросим счётчик для чистоты эксперимента
        
        long giant = y;
        for (long i = 0; i <= m; i++) {
            System.out.println("  Шаг " + i + ": y * a^(-" + (i*m) + ") = " + giant);
            
            if (babySteps.containsKey(giant)) {
                long j = babySteps.get(giant);
                long x = i * m + j;
                System.out.println("\n  Совпадение! babySteps[" + giant + "] = " + j);
                System.out.println("  x = i*m + j = " + i + "*" + m + " + " + j + " = " + x);
                System.out.println("Количество умножений: " + mulCount);
                return x;
            }
            
            // Переход к следующему гигантскому шагу: giant = giant * a^(-m) mod p
            if (i < m) {
                giant = (giant * aInv) % p;
                mulCount++;
            }
        }
        
        System.out.println("Решение не найдено!");
        return -1;
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("========================================");
        System.out.println("   ДИСКРЕТНОЕ ЛОГАРИФМИРОВАНИЕ");
        System.out.println("   Решение уравнения a^x ≡ y (mod p)");
        System.out.println("========================================");
        
        System.out.print("\nВведите a: ");
        long a = scanner.nextLong();
        
        System.out.print("Введите y: ");
        long y = scanner.nextLong();
        
        System.out.print("Введите p (простое): ");
        long p = scanner.nextLong();
        
        scanner.close();
        
        System.out.println("\n========================================");
        System.out.println("Начальные параметры:");
        System.out.println("  a = " + a);
        System.out.println("  y = " + y);
        System.out.println("  p = " + p);
        System.out.println("========================================");
        
        // Метод полного перебора
        long x1 = bruteForce(a, y, p);
        
        // Метод Шэнкса
        long x2 = babyStepGiantStep(a, y, p);
        
        // Сравнение результатов
        System.out.println("\n=== СРАВНЕНИЕ РЕЗУЛЬТАТОВ ===");
        System.out.println("Метод полного перебора:   x = " + x1);
        System.out.println("Метод Шэнкса:             x = " + x2);
        
        if (x1 == x2 && x1 != -1) {
            System.out.println("✓ Результаты совпадают!");
            // Проверка: a^x mod p должно равняться y
            resetCount();
            long check = powMod(a, x1, p);
            System.out.println("Проверка: " + a + "^" + x1 + " mod " + p + " = " + check);
            System.out.println("Ожидалось: " + y);
            if (check == y) {
                System.out.println("✓ Проверка пройдена!");
            }
        } else {
            System.out.println("✗ Результаты не совпадают!");
        }
        
        System.out.println("\nГотово!");
    }
}