package Package4;

public class TEA {
    
    private int[] key = new int[4];
    private static final int DELTA = 0x9e3779b9;
    
    public void setKey(byte[] keyBytes) {
        if (keyBytes.length != 16) {
            throw new IllegalArgumentException("Ключ должен быть 16 байт, получено " + keyBytes.length);
        }
        for (int i = 0; i < 4; i++) {
            key[i] = ((keyBytes[i*4] & 0xFF)) |
                     ((keyBytes[i*4+1] & 0xFF) << 8) |
                     ((keyBytes[i*4+2] & 0xFF) << 16) |
                     ((keyBytes[i*4+3] & 0xFF) << 24);
        }
    }
    
    public byte[] encryptBlock(byte[] block) {
        if (block.length != 8) {
            throw new IllegalArgumentException("Блок должен быть 8 байт");
        }
        
        int v0 = ((block[0] & 0xFF)) |
                 ((block[1] & 0xFF) << 8) |
                 ((block[2] & 0xFF) << 16) |
                 ((block[3] & 0xFF) << 24);
        int v1 = ((block[4] & 0xFF)) |
                 ((block[5] & 0xFF) << 8) |
                 ((block[6] & 0xFF) << 16) |
                 ((block[7] & 0xFF) << 24);
        
        int sum = 0;
        for (int i = 0; i < 32; i++) {
            sum += DELTA;
            v0 += ((v1 << 4) + key[0]) ^ (v1 + sum) ^ ((v1 >>> 5) + key[1]);
            v1 += ((v0 << 4) + key[2]) ^ (v0 + sum) ^ ((v0 >>> 5) + key[3]);
        }
        
        byte[] result = new byte[8];
        result[0] = (byte)(v0 & 0xFF);
        result[1] = (byte)((v0 >> 8) & 0xFF);
        result[2] = (byte)((v0 >> 16) & 0xFF);
        result[3] = (byte)((v0 >> 24) & 0xFF);
        result[4] = (byte)(v1 & 0xFF);
        result[5] = (byte)((v1 >> 8) & 0xFF);
        result[6] = (byte)((v1 >> 16) & 0xFF);
        result[7] = (byte)((v1 >> 24) & 0xFF);
        
        return result;
    }
    
    public byte[] decryptBlock(byte[] block) {
        if (block.length != 8) {
            throw new IllegalArgumentException("Блок должен быть 8 байт");
        }
        
        int v0 = ((block[0] & 0xFF)) |
                 ((block[1] & 0xFF) << 8) |
                 ((block[2] & 0xFF) << 16) |
                 ((block[3] & 0xFF) << 24);
        int v1 = ((block[4] & 0xFF)) |
                 ((block[5] & 0xFF) << 8) |
                 ((block[6] & 0xFF) << 16) |
                 ((block[7] & 0xFF) << 24);
        
        int sum = DELTA * 32;
        for (int i = 0; i < 32; i++) {
            v1 -= ((v0 << 4) + key[2]) ^ (v0 + sum) ^ ((v0 >>> 5) + key[3]);
            v0 -= ((v1 << 4) + key[0]) ^ (v1 + sum) ^ ((v1 >>> 5) + key[1]);
            sum -= DELTA;
        }
        
        byte[] result = new byte[8];
        result[0] = (byte)(v0 & 0xFF);
        result[1] = (byte)((v0 >> 8) & 0xFF);
        result[2] = (byte)((v0 >> 16) & 0xFF);
        result[3] = (byte)((v0 >> 24) & 0xFF);
        result[4] = (byte)(v1 & 0xFF);
        result[5] = (byte)((v1 >> 8) & 0xFF);
        result[6] = (byte)((v1 >> 16) & 0xFF);
        result[7] = (byte)((v1 >> 24) & 0xFF);
        
        return result;
    }
}