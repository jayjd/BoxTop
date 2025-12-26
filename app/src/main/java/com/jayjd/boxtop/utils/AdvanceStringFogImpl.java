package com.jayjd.boxtop.utils;

import com.github.megatronking.stringfog.IStringFog;

import java.nio.charset.StandardCharsets;

public final class AdvanceStringFogImpl implements IStringFog {

    @Override
    public byte[] encrypt(String data, byte[] key) {
        byte[] src = data.getBytes(StandardCharsets.UTF_8);
        byte[] out = new byte[src.length];

        int len = src.length;
        for (int i = 0; i < len; i++) {
            out[i] = (byte) (
                    src[i]
                    ^ key[i % key.length]
                    ^ (i * 31)
                    ^ len
            );
        }
        return out;
    }

    @Override
    public String decrypt(byte[] data, byte[] key) {
        byte[] out = new byte[data.length];
        int len = data.length;

        for (int i = 0; i < len; i++) {
            out[i] = (byte) (
                    data[i]
                    ^ key[i % key.length]
                    ^ (i * 31)
                    ^ len
            );
        }
        return new String(out, StandardCharsets.UTF_8);
    }

    @Override
    public boolean shouldFog(String data) {
        // 过滤不值得加密的字符串
        if (data == null) return false;
        return !data.isEmpty();
    }
}