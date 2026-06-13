package Package3;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class RC {
    
    private byte[] S = new byte[256];
    private int i, j;
    
    // Инициализация RC4 с ключом
    public void init(byte[] key) {
        // Заполняем S
        for (int i = 0; i < 256; i++) {
            S[i] = (byte) i;
        }
        
        // Перемешиваем с ключом
        int j = 0;
        for (int i = 0; i < 256; i++) {
            j = (j + (S[i] & 0xFF) + (key[i % key.length] & 0xFF)) % 256;
            byte temp = S[i];
            S[i] = S[j];
            S[j] = temp;
        }
        
        this.i = 0;
        this.j = 0;
    }
    
    // Генерация одного псевдослучайного байта
    public byte nextByte() {
        i = (i + 1) % 256;
        j = (j + (S[i] & 0xFF)) % 256;
        
        byte temp = S[i];
        S[i] = S[j];
        S[j] = temp;
        
        int t = ((S[i] & 0xFF) + (S[j] & 0xFF)) % 256;
        return S[t];
    }
    
    // Генерация потока нужной длины
    public byte[] generateStream(int length) {
        byte[] stream = new byte[length];
        for (int k = 0; k < length; k++) {
            stream[k] = nextByte();
        }
        return stream;
    }
    
    // Шифрование/расшифрование (одно и то же)
    public byte[] encrypt(byte[] data, byte[] key) {
        init(key);
        byte[] result = new byte[data.length];
        for (int i = 0; i < data.length; i++) {
            result[i] = (byte) (data[i] ^ nextByte());
        }
        return result;
    }
    
    public static void main(String[] args) {
        try {
            // Читаем исходный текст
            byte[] plain = VernamCipher.readFile("plain.txt");
            
            // Ключ RC4 (можно любой)
            byte[] rc4Key = "mysecretkey123".getBytes();
            
            RC rc4 = new RC();
            
            // Шифруем
            byte[] cipher = rc4.encrypt(plain, rc4Key);
            VernamCipher.writeFile("cipher_rc4.bin", cipher);
            System.out.println("Зашифровано RC4 в cipher_rc4.bin");
            
            // Расшифровываем
            byte[] decrypted = rc4.encrypt(cipher, rc4Key);
            VernamCipher.writeFile("decrypted_rc4.txt", decrypted);
            System.out.println("Расшифровано RC4 в decrypted_rc4.txt");
            
            // Проверяем
            String original = new String(plain);
            String recovered = new String(decrypted);
            if (original.equals(recovered)) {
                System.out.println("✓ RC4 работает корректно!");
            } else {
                System.out.println("✗ Ошибка RC4!");
            }
            
        } catch (IOException e) {
            System.err.println("Ошибка: " + e.getMessage());
        }
    }
}