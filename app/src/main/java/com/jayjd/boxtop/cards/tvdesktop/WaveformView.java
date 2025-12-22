package com.jayjd.boxtop.cards.tvdesktop;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class WaveformView extends View {

    private static final int DEFAULT_LINES = 2; // 默认 2 条线
    private static final int MAX_POINTS = 60;

    private float[][] values;        // [line][points]
    private Paint[] linePaints;
    private Paint gridPaint;

    private int lineCount;

    public WaveformView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(DEFAULT_LINES);
    }

    /** 初始化多少条线 */
    public void init(int lines) {
        this.lineCount = lines;

        values = new float[lines][MAX_POINTS];
        linePaints = new Paint[lines];

        for (int i = 0; i < lines; i++) {
            Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
            p.setStyle(Paint.Style.STROKE);
            p.setStrokeWidth(dp(2));
            p.setColor(getDefaultColor(i));
            linePaints[i] = p;
        }

        gridPaint = new Paint();
        gridPaint.setColor(0x22FFFFFF);
        gridPaint.setStrokeWidth(1);
    }

    /** 设置某条线的颜色 */
    public void setLineColor(int index, int color) {
        if (index < 0 || index >= lineCount) return;
        linePaints[index].setColor(color);
    }

    /** 推入数据：lineIndex + value(0~1) */
    public void pushValue(int lineIndex, float value) {
        if (lineIndex < 0 || lineIndex >= lineCount) return;

        if (value < 0) value = 0;
        if (value > 1) value = 1;

        float[] line = values[lineIndex];

        // 左移
        System.arraycopy(line, 1, line, 0, line.length - 1);

        // 平滑一点（TV 很舒服）
        line[line.length - 1] =
                line[line.length - 2] * 0.7f + value * 0.3f;

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int w = getWidth();
        int h = getHeight();

        drawGrid(canvas, w, h);
        drawAllWaves(canvas, w, h);
    }

    private void drawGrid(Canvas canvas, int w, int h) {
        canvas.drawLine(0, h / 2f, w, h / 2f, gridPaint);
        canvas.drawLine(0, h / 4f, w, h / 4f, gridPaint);
        canvas.drawLine(0, h * 3 / 4f, w, h * 3 / 4f, gridPaint);
    }

    private void drawAllWaves(Canvas canvas, int w, int h) {
        float stepX = (float) w / (MAX_POINTS - 1);

        for (int l = 0; l < lineCount; l++) {
            Path path = new Path();
            float[] line = values[l];

            for (int i = 0; i < MAX_POINTS; i++) {
                float x = i * stepX;
                float y = h - (line[i] * h);

                if (i == 0) {
                    path.moveTo(x, y);
                } else {
                    path.lineTo(x, y);
                }
            }
            canvas.drawPath(path, linePaints[l]);
        }
    }

    private int getDefaultColor(int index) {
        switch (index) {
            case 0:
                return 0xFF4CAF50; // 绿 CPU
            case 1:
                return 0xFF03A9F4; // 蓝 内存
            case 2:
                return 0xFFFFC107; // 黄 FPS
            default:
                return Color.WHITE;
        }
    }

    private float dp(float v) {
        return v * getResources().getDisplayMetrics().density;
    }
}


