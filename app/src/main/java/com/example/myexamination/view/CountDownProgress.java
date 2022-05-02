package com.example.myexamination.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import android.view.animation.LinearInterpolator;

import com.example.myexamination.R;

/**
 * 自定义圆形倒计时
 */
public class CountDownProgress extends View {

    //定义一些常量(大小写字母切换快捷键 Ctrl + Shift + U)
    private static final int DEFAULT_CIRCLE_SOLIDE_COLOR = Color.parseColor("#FFFFFF");
    private static final int DEFAULT_CIRCLE_STROKE_COLOR = Color.parseColor("#D1D1D1");
    private static final int DEFAULT_CIRCLE_STROKE_WIDTH = 5;
    private static final int DEFAULT_CIRCLE_RADIUS = 120;

    private static final int PROGRESS_COLOR = Color.parseColor("#F76E6B");
    private static final int PROGRESS_WIDTH = 5;

    private static final int SMALL_CIRCLE_SOLIDE_COLOR = Color.parseColor("#FFFFFF");
    private static final int SMALL_CIRCLE_STROKE_COLOR = Color.parseColor("#F76E6B");
    private static final int SMALL_CIRCLE_STROKE_WIDTH = 2;
    private static final int SMALL_CIRCLE_RADIUS = 6;

    //默认圆
    private int defaultCircleSolideColor = DEFAULT_CIRCLE_SOLIDE_COLOR;
    private int defaultCircleStrokeColor = DEFAULT_CIRCLE_STROKE_COLOR;
    private int defaultCircleStrokeWidth = dp2px(DEFAULT_CIRCLE_STROKE_WIDTH);
    private int defaultCircleRadius = dp2px(DEFAULT_CIRCLE_RADIUS);//半径
    //进度条
    private int progressColor = PROGRESS_COLOR;
    private int progressWidth = dp2px(PROGRESS_WIDTH);
    //默认圆边框上面的小圆
    private int smallCircleSolideColor = SMALL_CIRCLE_SOLIDE_COLOR;
    private int smallCircleStrokeColor = SMALL_CIRCLE_STROKE_COLOR;
    private int smallCircleStrokeWidth = dp2px(SMALL_CIRCLE_STROKE_WIDTH);
    private int smallCircleRadius = dp2px(SMALL_CIRCLE_RADIUS);

    //画笔
    private Paint defaultCirclePaint;
    private Paint progressPaint;
    private Paint smallCirclePaint;//画小圆边框的画笔
    private Paint smallCircleSolidePaint;//画小圆的实心的画笔

    //圆弧开始的角度
    private float mStartSweepValue = -90;
    //当前的角度
    private float currentAngle;
    //提供一个外界可以设置的倒计时数值，毫秒值
    private long countdownTime;
    private Path mPath;

    //额外距离
    private float extraDistance = 0.7F;


    public CountDownProgress(Context context) {
        this(context, null);
    }

    public CountDownProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("NonConstantResourceId")
    public CountDownProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取自定义属性
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CountDownProgress);
        int indexCount = typedArray.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.CountDownProgress_default_circle_solide_color:
                    defaultCircleSolideColor = typedArray.getColor(attr, defaultCircleSolideColor);
                    break;
                case R.styleable.CountDownProgress_default_circle_stroke_color:
                    defaultCircleStrokeColor = typedArray.getColor(attr, defaultCircleStrokeColor);
                    break;
                case R.styleable.CountDownProgress_default_circle_stroke_width:
                    defaultCircleStrokeWidth = (int) typedArray.getDimension(attr, defaultCircleStrokeWidth);
                    break;
                case R.styleable.CountDownProgress_default_circle_radius:
                    defaultCircleRadius = (int) typedArray.getDimension(attr, defaultCircleRadius);
                    break;
                case R.styleable.CountDownProgress_progress_color:
                    progressColor = typedArray.getColor(attr, progressColor);
                    break;
                case R.styleable.CountDownProgress_progress_width:
                    progressWidth = (int) typedArray.getDimension(attr, progressWidth);
                    break;
                case R.styleable.CountDownProgress_small_circle_solide_color:
                    smallCircleSolideColor = typedArray.getColor(attr, smallCircleSolideColor);
                    break;
                case R.styleable.CountDownProgress_small_circle_stroke_color:
                    smallCircleStrokeColor = typedArray.getColor(attr, smallCircleStrokeColor);
                    break;
                case R.styleable.CountDownProgress_small_circle_stroke_width:
                    smallCircleStrokeWidth = (int) typedArray.getDimension(attr, smallCircleStrokeWidth);
                    break;
                case R.styleable.CountDownProgress_small_circle_radius:
                    smallCircleRadius = (int) typedArray.getDimension(attr, smallCircleRadius);
                    break;
            }
        }
        //回收typedArray对象
        typedArray.recycle();
        //设置画笔
        setPaint();
    }

    private void setPaint() {
        //默认圆
        defaultCirclePaint = new Paint();
        defaultCirclePaint.setAntiAlias(true);//抗锯齿
        defaultCirclePaint.setDither(true);//防抖动
        defaultCirclePaint.setStyle(Paint.Style.STROKE);
        defaultCirclePaint.setStrokeWidth(defaultCircleStrokeWidth);
        defaultCirclePaint.setColor(defaultCircleStrokeColor);//这里先画边框的颜色，后续再添加画笔画实心的颜色
        //默认圆上面的进度弧度
        progressPaint = new Paint();
        progressPaint.setAntiAlias(true);
        progressPaint.setDither(true);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeWidth(progressWidth);
        progressPaint.setColor(progressColor);
        progressPaint.setStrokeCap(Paint.Cap.ROUND);//设置画笔笔刷样式
        //进度上面的小圆
        smallCirclePaint = new Paint();
        smallCirclePaint.setAntiAlias(true);
        smallCirclePaint.setDither(true);
        smallCirclePaint.setStyle(Paint.Style.STROKE);
        smallCirclePaint.setStrokeWidth(smallCircleStrokeWidth);
        smallCirclePaint.setColor(smallCircleStrokeColor);
        //画进度上面的小圆的实心画笔（主要是将小圆的实心颜色设置成白色）
        smallCircleSolidePaint = new Paint();
        smallCircleSolidePaint.setAntiAlias(true);
        smallCircleSolidePaint.setDither(true);
        smallCircleSolidePaint.setStyle(Paint.Style.FILL);
        smallCircleSolidePaint.setColor(smallCircleSolideColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize;
        int heightSize;
        int strokeWidth = Math.max(defaultCircleStrokeWidth, progressWidth);
        if (widthMode != MeasureSpec.EXACTLY) {
            widthSize = getPaddingLeft() + defaultCircleRadius * 2 + strokeWidth + getPaddingRight();
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY);
        }
        if (heightMode != MeasureSpec.EXACTLY) {
            heightSize = getPaddingTop() + defaultCircleRadius * 2 + strokeWidth + getPaddingBottom();
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        canvas.translate(getPaddingLeft(), getPaddingTop());
        //画默认圆
        canvas.drawCircle(defaultCircleRadius, defaultCircleRadius, defaultCircleRadius, defaultCirclePaint);

        canvas.drawArc(new RectF(0, 0, defaultCircleRadius * 2, defaultCircleRadius * 2), mStartSweepValue, 360 * currentAngle, false, progressPaint);

        //画小圆
        float currentDegreeFlag = 360 * currentAngle + extraDistance;
        float smallCircleX, smallCircleY;
        float hudu = (float) Math.abs(Math.PI * currentDegreeFlag / 180);//Math.abs：绝对值 ，Math.PI：表示π ， 弧度 = 度*π / 180
        smallCircleX = (float) Math.abs(Math.sin(hudu) * defaultCircleRadius + defaultCircleRadius);
        smallCircleY = (float) Math.abs(defaultCircleRadius - Math.cos(hudu) * defaultCircleRadius);
        canvas.drawCircle(smallCircleX, smallCircleY, smallCircleRadius, smallCirclePaint);
        canvas.drawCircle(smallCircleX, smallCircleY, smallCircleRadius - smallCircleStrokeWidth, smallCircleSolidePaint);//画小圆的实心

        canvas.restore();
    }

    protected int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }

    //提供一个外界可以设置的倒计时数值
    public void setCountdownTime(long countdownTime) {
        this.countdownTime = countdownTime;
    }

    public void setCurrentAngle() {
        currentAngle = 0;
    }

    //属性动画
    public void startCountDownTime(final OnCountdownFinishListener countdownFinishListener) {
        setClickable(false);
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1.0f);
        //动画时长
        animator.setDuration(countdownTime);
        animator.setInterpolator(new LinearInterpolator());//匀速
        animator.setRepeatCount(0);//表示不循环，-1表示无限循环
        //添加监听器,监听动画过程中值的实时变化
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                currentAngle = (float) animation.getAnimatedValue();

                invalidate();//实时刷新view，这样我们的进度条弧度就动起来了
            }
        });
        //开启动画
        animator.start();
        //还需要另一个监听，监听动画状态的监听器
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //倒计时结束的时候，需要通过自定义接口通知UI去处理其他业务逻辑
                if (countdownFinishListener != null) {
                    countdownFinishListener.countdownFinished();
                }
                setClickable(countdownTime > 0);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        //调用倒计时操作
        countdownMethod();
    }

    //倒计时的方法
    private void countdownMethod() {
        new CountDownTimer(countdownTime + 500, 1000 * 5 * 60) {
            @Override
            public void onTick(long millisUntilFinished) {
                countdownTime = countdownTime - 1000;
                Log.e("time", countdownTime + "");
                //刷新view
                invalidate();
            }

            @Override
            public void onFinish() {
                //刷新view
                invalidate();
            }
        }.start();
    }

    //通过自定义接口通知UI去处理其他业务逻辑
    public interface OnCountdownFinishListener {
        void countdownFinished();
    }
}
