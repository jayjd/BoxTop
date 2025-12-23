package com.jayjd.boxtop.nanohttpd;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.HashMap;
import java.util.Map;

public class QRCodeGen {
    public static Bitmap generateBitmap(String content, int width, int height, int padding) {
        return initBitmap(content, width, height, padding, Color.WHITE,  0x003549);
    }

    public static Bitmap generateBitmap(String content, int width, int height, int padding, int color,int colorOther) {
        return initBitmap(content, width, height, padding, color,colorOther);
    }


    private static Bitmap initBitmap(String content, int width, int height, int padding, int color, int colorOther) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, String> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.MARGIN, padding + "");
        try {
            BitMatrix encode = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            int[] pixels = new int[width * height];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (encode.get(j, i)) {
                        pixels[i * width + j] = color;
                    } else {
                        pixels[i * width + j] = colorOther;
                    }
                }
            }
            return Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.RGB_565);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap generateBitmap(String content, int width, int height) {
        return generateBitmap(content, width, height, 0);
    }
}