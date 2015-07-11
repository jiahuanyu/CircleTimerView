package com.jiahuan.circletimerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

public class CircleTimerView extends View
{
    private static final String TAG = "CircleTimerView";

    // Status
    private static final String INSTANCE_STATUS = "instance_status";
    private static final String STATUS_RADIAN = "status_radian";

    // Default dimension in dp/pt
    private static final float DEFAULT_GAP_BETWEEN_CIRCLE_AND_LINE = 5;
    private static final float DEFAULT_GAP_BETWEEN_NUMBER_AND_LINE = 5;
    private static final float DEFAULT_NUMBER_SIZE = 10;
    private static final float DEFAULT_LINE_LENGTH = 14;
    private static final float DEFAULT_LONGER_LINE_LENGTH = 23;
    private static final float DEFAULT_LINE_WIDTH = 0.5f;
    private static final float DEFAULT_CIRCLE_BUTTON_RADIUS = 15;
    private static final float DEFAULT_CIRCLE_STROKE_WIDTH = 1;
    private static final float DEFAULT_CIRCLE_BUTTON_SHADOW_RADIUS = 2;
    private static final float DEFAULT_CIRCLE_BUTTON_SHADOW_X_OFFSET = 0;
    private static final float DEFAULT_CIRCLE_BUTTON_SHADOW_Y_OFFSET = 0;
    private static final float DEFAULT_TIMER_NUMBER_SIZE = 50;
    private static final float DEFAULT_TIMER_TEXT_SIZE = 14;
    private static final float DEFAULT_GAP_BETWEEN_TIMER_NUMBER_AND_TEXT = 30;

    // Default color
    private static final int DEFAULT_CIRCLE_COLOR = 0xFFE9E2D9;
    private static final int DEFAULT_CIRCLE_BUTTON_COLOR = 0xFFFFFFFF;
    private static final int DEFAULT_LINE_COLOR = 0xFFE9E2D9;
    private static final int DEFAULT_HIGHLIGHT_LINE_COLOR = 0xFF68C5D7;
    private static final int DEFAULT_NUMBER_COLOR = 0x99866A60;
    private static final int DEFAULT_CIRCLE_BUTTON_SHADOW_COLOR = 0xFF909090;
    private static final int DEFAULT_TIMER_NUMBER_COLOR = 0xFFFA7777;
    private static final int DEFAULT_TIMER_COLON_COLOR = 0x80FA7777;
    private static final int DEFAULT_TIMER_TEXT_COLOR = 0x99000000;

    // Paint
    private Paint circlePaint;
    private Paint highlightLinePaint;
    private Paint linePaint;
    private Paint circleButtonPaint;
    private Paint numberPaint;
    private Paint timerNumberPaint;
    private Paint timerTextPaint;
    private Paint timerColonPaint;

    // Dimension
    private float gapBetweenCircleAndLine;
    private float gapBetweenNumberAndLine;
    private float numberSize;
    private float lineLength;
    private float longerLineLength;
    private float lineWidth;
    private float circleButtonRadius;
    private float circleStorkeWidth;
    private float shadowRadius;
    private float shadowXOffset;
    private float shadowYOffset;
    private float timerNumberSize;
    private float timerTextSize;
    private float gapBetweenTimerNumberAndText;

    // Color
    private int circleColor;
    private int circleButtonColor;
    private int lineColor;
    private int highlightLineColor;
    private int numberColor;
    private int shadowColor;
    private int timerNumberColor;
    private int timerTextColor;

    // Parameters
    private float cx;
    private float cy;
    private float radius;
    private float currentRadian = 0;
    private float preRadian;
    private boolean isInCircleButton;
    private int currentTime; // seconds
    private boolean isStartTimer;

    // TimerTask
    private Timer timer = new Timer();

    private TimerTask timerTask;

    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            Log.d(TAG, "handleMessage");
            super.handleMessage(msg);
            if (currentRadian > 0 && currentTime > 0)
            {
                currentRadian -= (2 * Math.PI) / 3600;
            }
            else
            {
                currentRadian = 0;
                timer.cancel();
                isStartTimer = false;
                if (circleTimerListener != null)
                {
                    circleTimerListener.onTimerStop();
                }
            }
            invalidate();
        }
    };

    // Runt
    private CircleTimerListener circleTimerListener;

    public CircleTimerView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    public CircleTimerView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public CircleTimerView(Context context)
    {
        this(context, null);
    }

    private void initialize()
    {
        Log.d(TAG, "initialize");
        // Set default dimension or read xml attributes
        gapBetweenCircleAndLine = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_GAP_BETWEEN_CIRCLE_AND_LINE, getContext().getResources().getDisplayMetrics());
        gapBetweenNumberAndLine = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_GAP_BETWEEN_NUMBER_AND_LINE, getContext().getResources().getDisplayMetrics());
        numberSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_NUMBER_SIZE, getContext().getResources().getDisplayMetrics());
        lineLength = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_LINE_LENGTH, getContext().getResources().getDisplayMetrics());
        longerLineLength = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_LONGER_LINE_LENGTH, getContext().getResources().getDisplayMetrics());
        lineWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_LINE_WIDTH, getContext().getResources().getDisplayMetrics());
        circleButtonRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_CIRCLE_BUTTON_RADIUS, getContext().getResources().getDisplayMetrics());
        circleStorkeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_CIRCLE_STROKE_WIDTH, getContext().getResources().getDisplayMetrics());
        shadowRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_CIRCLE_BUTTON_SHADOW_RADIUS, getContext().getResources().getDisplayMetrics());
        shadowXOffset = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_CIRCLE_BUTTON_SHADOW_X_OFFSET, getContext().getResources().getDisplayMetrics());
        shadowYOffset = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_CIRCLE_BUTTON_SHADOW_Y_OFFSET, getContext().getResources().getDisplayMetrics());
        timerNumberSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_TIMER_NUMBER_SIZE, getContext().getResources().getDisplayMetrics());
        timerTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_TIMER_TEXT_SIZE, getContext().getResources().getDisplayMetrics());
        gapBetweenTimerNumberAndText = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_GAP_BETWEEN_TIMER_NUMBER_AND_TEXT, getContext().getResources().getDisplayMetrics());

        // Set default color or read xml attributes
        circleColor = DEFAULT_CIRCLE_COLOR;
        circleButtonColor = DEFAULT_CIRCLE_BUTTON_COLOR;
        lineColor = DEFAULT_LINE_COLOR;
        highlightLineColor = DEFAULT_HIGHLIGHT_LINE_COLOR;
        numberColor = DEFAULT_NUMBER_COLOR;
        shadowColor = DEFAULT_CIRCLE_BUTTON_SHADOW_COLOR;
        timerNumberColor = DEFAULT_TIMER_NUMBER_COLOR;
        timerTextColor = DEFAULT_TIMER_TEXT_COLOR;

        // Init all paints
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circleButtonPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        highlightLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        numberPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        timerNumberPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        timerTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        timerColonPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        // CirclePaint
        circlePaint.setColor(circleColor);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(circleStorkeWidth);

        // CircleButtonPaint
        circleButtonPaint.setColor(circleButtonColor);
        circleButtonPaint.setAntiAlias(true);
        circleButtonPaint.setStyle(Paint.Style.FILL);
        circleButtonPaint.setShadowLayer(shadowRadius, shadowXOffset, shadowYOffset, shadowColor);

        // LinePaint
        linePaint.setColor(lineColor);
        linePaint.setStrokeWidth(lineWidth);

        // HighlightLinePaint
        highlightLinePaint.setColor(highlightLineColor);
        highlightLinePaint.setStrokeWidth(lineWidth);

        // NumberPaint
        numberPaint.setColor(numberColor);
        numberPaint.setTextSize(numberSize);
        numberPaint.setTextAlign(Paint.Align.CENTER);

        // TimerNumberPaint
        timerNumberPaint.setColor(timerNumberColor);
        timerNumberPaint.setTextSize(timerNumberSize);
        timerNumberPaint.setTextAlign(Paint.Align.CENTER);

        // TimerTextPaint
        timerTextPaint.setColor(timerTextColor);
        timerTextPaint.setTextSize(timerTextSize);
        timerTextPaint.setTextAlign(Paint.Align.CENTER);

        // TimerColonPaint
        timerColonPaint.setColor(DEFAULT_TIMER_COLON_COLOR);
        timerColonPaint.setTextAlign(Paint.Align.CENTER);
        timerColonPaint.setTextSize(timerNumberSize);

        // Solve the target version related to shadow
        // setLayerType(View.LAYER_TYPE_SOFTWARE, null); // use this, when targetSdkVersion is greater than or equal to api 14
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        currentTime = (int) (60 / (2 * Math.PI) * currentRadian * 60);
        // Assist lines
//        canvas.drawColor(Color.RED);
        // canvas.drawLine(cx, 0, cx, getHeight(), new Paint());
        // canvas.drawLine(0, cy, getWidth(), cy, new Paint());

        // Content
        canvas.drawCircle(cx, cy, radius, circlePaint);
        canvas.save();
        for (int i = 0; i < 120; i++)
        {
            canvas.save();
            canvas.rotate(360 / 120 * i, cx, cy);
            if (i % 30 == 0)
            {
                if (360 / 120 * i <= Math.toDegrees(currentRadian))
                {
                    canvas.drawLine(cx, getMeasuredHeight() / 2 - radius + circleStorkeWidth / 2 + gapBetweenCircleAndLine, cx, getMeasuredHeight() / 2 - radius + circleStorkeWidth / 2 + gapBetweenCircleAndLine + longerLineLength, highlightLinePaint);
                }
                else
                {
                    canvas.drawLine(cx, getMeasuredHeight() / 2 - radius + circleStorkeWidth / 2 + gapBetweenCircleAndLine, cx, getMeasuredHeight() / 2 - radius + circleStorkeWidth / 2 + gapBetweenCircleAndLine + longerLineLength, linePaint);
                }
            }
            else
            {
                if (360 / 120 * i <= Math.toDegrees(currentRadian))
                {
                    canvas.drawLine(cx, getMeasuredHeight() / 2 - radius + circleStorkeWidth / 2 + gapBetweenCircleAndLine, cx, getMeasuredHeight() / 2 - radius + circleStorkeWidth / 2 + gapBetweenCircleAndLine + lineLength, highlightLinePaint);
                }
                else
                {
                    canvas.drawLine(cx, getMeasuredHeight() / 2 - radius + circleStorkeWidth / 2 + gapBetweenCircleAndLine, cx, getMeasuredHeight() / 2 - radius + circleStorkeWidth / 2 + gapBetweenCircleAndLine + lineLength, linePaint);
                }
            }
            canvas.restore();
        }
        canvas.restore();
        // Number it is rubbish code
        float textLength = numberPaint.measureText("15");
        canvas.drawText("60", cx, getMeasuredHeight() / 2 - radius + circleStorkeWidth / 2 + gapBetweenCircleAndLine + longerLineLength + gapBetweenNumberAndLine + getFontHeight(numberPaint), numberPaint);
        canvas.drawText("15", cx + radius - circleStorkeWidth / 2 - gapBetweenCircleAndLine - longerLineLength - textLength / 2 - gapBetweenNumberAndLine, cy + getFontHeight(numberPaint) / 2, numberPaint);
        canvas.drawText("30", cx, getMeasuredHeight() / 2 + radius - circleStorkeWidth / 2 - gapBetweenCircleAndLine - longerLineLength - gapBetweenNumberAndLine, numberPaint);
        canvas.drawText("45", getMeasuredHeight() / 2 - radius + circleStorkeWidth / 2 + gapBetweenCircleAndLine + longerLineLength + gapBetweenNumberAndLine + textLength / 2, cy + getFontHeight(numberPaint) / 2, numberPaint);
        // Circle button
        canvas.save();
        canvas.rotate((float) Math.toDegrees(currentRadian), cx, cy);
        canvas.drawCircle(cx, getMeasuredHeight() / 2 - radius + circleStorkeWidth / 2 + gapBetweenCircleAndLine + lineLength / 2, circleButtonRadius, circleButtonPaint);
        canvas.restore();
        // TimerNumber
        canvas.save();
        canvas.drawText((currentTime / 60 < 10 ? "0" + currentTime / 60 : currentTime / 60) + " " + (currentTime % 60 < 10 ? "0" + currentTime % 60 : currentTime % 60), cx, cy + getFontHeight(timerNumberPaint) / 2, timerNumberPaint);
        canvas.drawText(":", cx, cy + getFontHeight(timerNumberPaint) / 2, timerColonPaint);
        canvas.restore();
        // Timer Text
        canvas.save();
        canvas.drawText("时间设置", cx, cy + getFontHeight(timerNumberPaint) / 2 + gapBetweenTimerNumberAndText + getFontHeight(timerTextPaint) / 2, timerTextPaint);
        canvas.restore();
        super.onDraw(canvas);
    }

    private float getFontHeight(Paint paint)
    {
        // FontMetrics sF = paint.getFontMetrics();
        // return sF.descent - sF.ascent;
        Rect rect = new Rect();
        paint.getTextBounds("1", 0, 1, rect);
        return rect.height();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch (event.getAction() & event.getActionMasked())
        {
            case MotionEvent.ACTION_DOWN:
                // If the point in the circle button
                if (isInCircleButton(event.getX(), event.getY()))
                {
                    isInCircleButton = true;
                    preRadian = getRadian(event.getX(), event.getY());
                    Log.d(TAG, "In circle button");
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (isInCircleButton)
                {
                    float temp = getRadian(event.getX(), event.getY());
                    if (preRadian > Math.toRadians(270) && temp < Math.toRadians(90))
                    {
                        preRadian -= 2 * Math.PI;
                    }
                    else if (preRadian < Math.toRadians(90) && temp > Math.toRadians(270))
                    {
                        preRadian = (float) (temp + (temp - 2 * Math.PI) - preRadian);
                    }
                    currentRadian += (temp - preRadian);
                    preRadian = temp;
                    if (currentRadian > 2 * Math.PI)
                    {
                        currentRadian = (float) (2 * Math.PI);
                    }
                    else if (currentRadian < 0)
                    {
                        currentRadian = 0;
                    }

                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isInCircleButton)
                {
                    isInCircleButton = false;
                }
                break;
        }
        return true;
    }

    // Whether the down event inside circle button
    private boolean isInCircleButton(float x, float y)
    {
        float r = radius - circleStorkeWidth / 2 - gapBetweenCircleAndLine - lineLength / 2;
        float x2 = (float) (cx + r * Math.sin(currentRadian));
        float y2 = (float) (cy - r * Math.cos(currentRadian));
        if (Math.sqrt((x - x2) * (x - x2) + (y - y2) * (y - y2)) < circleButtonRadius)
        {
            return true;
        }
        return false;
    }

    // Use tri to cal radian
    private float getRadian(float x, float y)
    {
        float alpha = (float) Math.atan((x - cx) / (cy - y));
        // Quadrant
        if (x > cx && y > cy)
        {
            // 2
            alpha += Math.PI;
        }
        else if (x < cx && y > cy)
        {
            // 3
            alpha += Math.PI;
        }
        else if (x < cx && y < cy)
        {
            // 4
            alpha = (float) (2 * Math.PI + alpha);
        }
        return alpha;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        Log.d(TAG, "onMeasure");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // Ensure width = height
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        this.cx = width / 2;
        this.cy = height / 2;
        // Radius
        if (lineLength / 2 + gapBetweenCircleAndLine + circleStorkeWidth >= (circleButtonRadius + shadowRadius))
        {
            this.radius = width / 2 - circleStorkeWidth / 2;
            Log.d(TAG, "No exceed");
        }
        else
        {
            this.radius = width / 2 - (circleButtonRadius + shadowRadius - gapBetweenCircleAndLine - lineLength / 2 - circleStorkeWidth / 2);
            Log.d(TAG, "Exceed");
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected Parcelable onSaveInstanceState()
    {
        Log.d(TAG, "onSaveInstanceState");
        Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE_STATUS, super.onSaveInstanceState());
        bundle.putFloat(STATUS_RADIAN, currentRadian);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state)
    {
        Log.d(TAG, "onRestoreInstanceState");
        if (state instanceof Bundle)
        {
            Bundle bundle = (Bundle) state;
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATUS));
            currentRadian = bundle.getFloat(STATUS_RADIAN);
            currentTime = (int) (60 / (2 * Math.PI) * currentRadian * 60);
            return;
        }
        super.onRestoreInstanceState(state);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    // Star timer
    public void startTimer()
    {
        Log.d(TAG, "startTimer");
        if (currentRadian > 0 && !isStartTimer)
        {

            timerTask = new TimerTask()
            {
                @Override
                public void run()
                {
                    Log.d(TAG, "TimerTask");
                    handler.obtainMessage().sendToTarget();
                }
            };
            timer.schedule(timerTask, 1000, 1000);
            isStartTimer = true;
            if (this.circleTimerListener != null)
            {
                this.circleTimerListener.onTimerStart(currentTime);
            }
        }
    }

    // Pause timer
    public void pauseTimer()
    {
        if (isStartTimer)
        {
            timerTask.cancel();
            isStartTimer = false;
            if (this.circleTimerListener != null)
            {
                this.circleTimerListener.onTimerPause(currentTime);
            }
        }
    }

    // Set timer listener
    public void setCircleTimerListener(CircleTimerListener circleTimerListener)
    {
        this.circleTimerListener = circleTimerListener;
    }

    public interface CircleTimerListener
    {
        void onTimerStop();

        void onTimerStart(int time);

        void onTimerPause(int time);
    }

    public int getCurrentTime()
    {
        return currentTime;
    }
}
