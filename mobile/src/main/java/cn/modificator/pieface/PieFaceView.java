package cn.modificator.pieface;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.graphics.Typeface;
import android.text.format.Time;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Created by Modificator
 * time: 16-9-27.上午9:52
 * des:create file and achieve model
 */

public class PieFaceView extends View {

    Paint mHourPaint, mMinutePaint, mSecondPaint, mBackgroundPaint;
    FaceConfig mFaceConfig;

    boolean isSecondFull = false;
    float width;
    float height;
    float mFaceSize;
    Calendar mCalendar;
    int minute = 0;

    public PieFaceView(Context context) {
        this(context, null);
    }

    public PieFaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PieFaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mCalendar = Calendar.getInstance();
    }

    long lastTime = 0;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        long thisTime = System.currentTimeMillis();
        Log.e("----", "fps:" + (1000f / (thisTime - lastTime)));
        lastTime = thisTime;

        mCalendar.setTimeInMillis(System.currentTimeMillis());
        mCalendar.setTimeZone(TimeZone.getDefault());
        minute = getMinutes();
//        mBackgroundPaint.setColor(minute % 2 == 0 ? 0xff000000 : 0xffffffff);
//        mHourPaint.setColor(minute % 2 == 0 ? 0xffffffff : 0xff000000);
//        mMinutePaint.setColor(minute % 2 == 0 ? 0xffffffff : 0xff000000);
        mBackgroundPaint.setColor(0xff000000);
        mHourPaint.setColor(0xffffffff);
        mMinutePaint.setColor(0xffffffff);


        canvas.drawOval(mBackgroundPaint.getStrokeWidth() / 2, mBackgroundPaint.getStrokeWidth() / 2, mFaceSize - mBackgroundPaint.getStrokeWidth() / 2, mFaceSize - mBackgroundPaint.getStrokeWidth() / 2, mBackgroundPaint);
        if (minute % 2 == 1) {
            mCalendar.add(Calendar.MINUTE, 1);
        }
        canvas.drawText(String.format("%02d", getHour()), mFaceSize / 2f, mFaceSize / 2f - mFaceSize / 15f, mHourPaint);
//        canvas.drawText(String.format("%02d", minute % 2 == 0?minute:minute+1), mFaceSize / 2f , mFaceSize-mFaceSize/10f, mMinutePaint);
        canvas.drawText(String.format("%02d", getMinutes()), mFaceSize / 2f, mFaceSize - mFaceSize / 10f, mMinutePaint);
        if (minute % 2 == 1) {
            mCalendar.add(Calendar.MINUTE, -1);
        }


//        Log.e("------", getSmoothSeconds() + "  --  " + getSmoothMinutes());
//        Log.e("------", mCalendar.get(Calendar.MINUTE) + ":" + mCalendar.get(Calendar.SECOND) + ":" + mCalendar.get(Calendar.MILLISECOND));
        float startAngle = minute % 2 == 0 ? -90 : getSmoothSeconds() * 6f - 90f;
        float endAngle = minute % 2 == 1 ? 270 - startAngle : getSmoothSeconds() * 6f;
//        Log.e("-----", "startAngle:" + startAngle + "\nendAngle:" + endAngle);
        Path path = new Path();
        path.moveTo(mFaceSize / 2, mFaceSize / 2);
//        path.lineTo(mFaceSize / 2, 0);
        path.addArc(0,
                0,
                mFaceSize,
                mFaceSize,
                startAngle,
                endAngle);
        path.lineTo(mFaceSize / 2, mFaceSize / 2);
        path.close();
        canvas.clipPath(path, Region.Op.REPLACE);


//        mBackgroundPaint.setColor(minute % 2 == 1 ? 0xff000000 : 0xffffffff);
//        mHourPaint.setColor(minute % 2 == 1 ? 0xffffffff : 0xff000000);
//        mMinutePaint.setColor(minute % 2 == 1 ? 0xffffffff : 0xff000000);
        mBackgroundPaint.setColor(0xffffffff);
        mHourPaint.setColor(0xff000000);
        mMinutePaint.setColor(0xff000000);
        if (minute % 2 == 0) {
            mCalendar.add(Calendar.MINUTE, 1);
        }
        canvas.drawOval(mBackgroundPaint.getStrokeWidth() / 2, mBackgroundPaint.getStrokeWidth() / 2, mFaceSize - mBackgroundPaint.getStrokeWidth() / 2, mFaceSize - mBackgroundPaint.getStrokeWidth() / 2, mBackgroundPaint);
        canvas.drawText(String.format("%02d", getHour()), mFaceSize / 2f, mFaceSize / 2f - mFaceSize / 15f, mHourPaint);
//        canvas.drawText(String.format("%02d", minute % 2 == 0 ? minute + 1 : minute), mFaceSize / 2f, mFaceSize - mFaceSize / 10f, mMinutePaint);
        canvas.drawText(String.format("%02d", getMinutes()), mFaceSize / 2f, mFaceSize - mFaceSize / 10f, mMinutePaint);
        if (minute % 2 == 0) {
            mCalendar.add(Calendar.MINUTE, -1);
        }


//        canvas.drawOval(mSecondPaint.getStrokeWidth() / 2, mSecondPaint.getStrokeWidth() / 2, mFaceSize - mSecondPaint.getStrokeWidth() / 2, mFaceSize - mSecondPaint.getStrokeWidth() / 2, mSecondPaint);
//        canvas.drawPath(path, mSecondPaint);
        invalidate();
    }

    private float getSmoothSeconds() {
        return mCalendar.get(Calendar.SECOND) + mCalendar.get(Calendar.MILLISECOND) / 1000f;
    }

    private float getSmoothMinutes() {
        return mCalendar.get(Calendar.MINUTE) + getSmoothSeconds() / 60f;
    }

    private int getMinutes() {
        return mCalendar.get(Calendar.MINUTE);
    }

    private int getHour() {
        return mCalendar.get(Calendar.HOUR_OF_DAY);
    }

    private void createPaints() {
        isSecondFull = false;

        mHourPaint = new Paint();
        mHourPaint.setStyle(Paint.Style.STROKE);
//        mHourPaint.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fzltcxhjt.TTF"));
        mHourPaint.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "Roboto-Thin.ttf"));
        mHourPaint.setColor(getFaceConfig().getHourColor());
        mHourPaint.setTextSize(mFaceSize / 2.2f);
        mHourPaint.setTextAlign(Paint.Align.CENTER);

        mMinutePaint = new Paint();
        mMinutePaint.setStyle(Paint.Style.STROKE);
        mMinutePaint.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "Roboto-Thin.ttf"));
        mMinutePaint.setTextSize(mFaceSize / 2.2f);
        mMinutePaint.setColor(0xffffffff);
        mMinutePaint.setTextAlign(Paint.Align.CENTER);

        mSecondPaint = new Paint();
        mSecondPaint.setStyle(isSecondFull ? Paint.Style.FILL : Paint.Style.STROKE);
        mSecondPaint.setStrokeWidth(mFaceSize / 40f);
//        mSecondPaint.setColor(0xff00f0f0);
        mSecondPaint.setColor(0xffffffff);

        mBackgroundPaint = new Paint();
        mBackgroundPaint.setStyle(Paint.Style.FILL);
        mBackgroundPaint.setColor(0xff000000);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = right - left;
        height = bottom - top;
        mFaceSize = Math.min(width, height);
        createPaints();
    }

    public void setFaceConfig(FaceConfig faceConfig) {
        this.mFaceConfig = faceConfig;
    }

    public FaceConfig getFaceConfig() {
        if (mFaceConfig == null) {
            mFaceConfig = new FaceConfig();
            mFaceConfig.setBackgroundColor(0xffffffff);
            mFaceConfig.setBackgroundClipColor(0xff000000);
            mFaceConfig.setHourColor(0xff000000);
            mFaceConfig.setHourClipColor(0xffffffff);
            mFaceConfig.setMinuteColor(0xff000000);
            mFaceConfig.setMinuteClipColor(0xffffffff);
            mFaceConfig.setFullSeconds(true);
        }
        return mFaceConfig;
    }
}
