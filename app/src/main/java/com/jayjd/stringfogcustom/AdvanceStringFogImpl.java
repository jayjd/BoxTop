package com.jayjd.stringfogcustom;

import com.github.megatronking.stringfog.IStringFog;

import java.nio.charset.StandardCharsets;

public final class AdvanceStringFogImpl implements IStringFog {

    private static final int MAGIC = 0x5A;

    @Override
    public byte[] encrypt(String data, byte[] key) {
        byte[] src = data.getBytes(StandardCharsets.UTF_8);
        byte[] out = new byte[src.length];

        int len = src.length;
        int acc = MAGIC ^ len;

        for (int i = 0; i < len; i++) {
            int k = key[(i + acc) % key.length] ^ (acc >> 1);
            int v = src[i] ^ k ^ acc;

            // 位旋转（左）
            v = ((v << 3) | (v >>> 5)) & 0xFF;

            acc = (acc + v + i) & 0xFF;
            out[i] = (byte) v;
        }
        return out;
    }

    @Override
    public String decrypt(byte[] data, byte[] key) {
        byte[] out = new byte[data.length];

        int len = data.length;
        int acc = MAGIC ^ len;

        for (int i = 0; i < len; i++) {
            int v = data[i] & 0xFF;

            // 位旋转（右，还原）
            v = ((v >>> 3) | (v << 5)) & 0xFF;

            int k = key[(i + acc) % key.length] ^ (acc >> 1);
            out[i] = (byte) (v ^ k ^ acc);

            acc = (acc + (data[i] & 0xFF) + i) & 0xFF;
        }
        return new String(out, StandardCharsets.UTF_8);
    }

    @Override
    public boolean shouldFog(String data) {
        if (data == null || data.isEmpty()) return false;
        // 1️⃣ Date / Time pattern
        if (data.contains("yyyy") || data.contains("MM") || data.contains("dd")) {
            return false;
        }

        // 2️⃣ SimpleDateFormat / Formatter 特殊字符
        if (data.matches(".*[yMdHhmsSEDFwWaZzX].*")) {
            return false;
        }

        // 3️⃣ URL / Scheme
        if (data.startsWith("http://") || data.startsWith("https://")) {
            return false;
        }

        // 4️⃣ 正则
        if (data.contains("\\") || data.contains("[") || data.contains("]")) {
            return false;
        }

        // 5️⃣ Android 系统关键字
        if (data.startsWith("android.") || data.startsWith("com.android.")) {
            return false;
        }

        // 6️⃣ 太短没意义
        if (data.length() < 3) {
            return false;
        }

        return true;
    }
}
