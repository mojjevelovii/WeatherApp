package ru.shumilova.weatherapp.custom_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import ru.shumilova.weatherapp.R;

public class MyTemperatureView extends View {

    private int textColor;
    private String text;
    private Paint textPaint;

    public MyTemperatureView(Context context) {
        super(context);
        init();
    }

    public MyTemperatureView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttr(context, attrs);
        init();
    }

    public MyTemperatureView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
        init();
    }

    public MyTemperatureView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttr(context, attrs);
        init();
    }

    private void initAttr(Context context, @Nullable AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyTemperatureView, 0, 0);
        textColor = typedArray.getColor(R.styleable.MyTemperatureView_text_color, Color.WHITE);
        text = typedArray.getString(R.styleable.MyTemperatureView_text);
        typedArray.recycle();
    }

    private void init() {
        textPaint = new Paint();
        textPaint.setColor(textColor);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(40);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText(text, 0, 50, textPaint);
    }
}
