package Package3;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class Class3 {

    // Линейный конгруэнтный генератор (как в лекции)
    public static byte[] generateLCG(int length, int seed) {
        byte[] data = new byte[length];
        int a = 5, b = 12, c = 23;
        int current = seed;

        for (int i = 0; i < length; i++) {
            current = (a * current + b) % c;
            data[i] = (byte) (current % 256);
        }
        return data;
    }

    // Встроенный Random
    public static byte[] generateRandom(int length) {
        byte[] data = new byte[length];
        new Random().nextBytes(data);
        return data;
    }

    // Сохранение в файл
    public static void saveToFile(String filename, byte[] data) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filename)) {
            fos.write(data);
        }
        System.out.println("Создан файл: " + filename + " (" + data.length + " байт)");
    }

    public static void main(String[] args) throws IOException {
        // Генерируем ключевой файл размером 10000 байт
        byte[] key = generateRandom(10000);
        saveToFile("key.bin", key);

        // Также сгенерируем ключ LCG для примера
        byte[] keyLCG = generateLCG(10000, 4);
        saveToFile("key_lcg.bin", keyLCG);

        System.out.println("ГОТОВО!");
    }
}