package cn.modificator.pieface;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Modificator
 * time: 16-9-27.上午9:52
 * des:create file and achieve model
 */

public class PieFaceView extends View {

    Paint mSecondPaint;
    boolean isSecondFull = false;
    float width;
    float height;
    float mFaceSize;

    public PieFaceView(Context context) {
        super(context);
    }

    public PieFaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PieFaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Path path = new Path();
        path.moveTo(mFaceSize / 2, mFaceSize / 2);
        path.lineTo(mFaceSize / 2, 0);
        path.addArc(mSecondPaint.getStrokeWidth() / 2,
                mSecondPaint.getStrokeWidth() / 2,
                mFaceSize - mSecondPaint.getStrokeWidth() / 2,
                mFaceSize - mSecondPaint.getStrokeWidth() / 2,
                -90,
                30);
        path.lineTo(mFaceSize / 2, mFaceSize / 2);

        canvas.clipPath(path, Region.Op.REPLACE);
        canvas.drawOval(mSecondPaint.getStrokeWidth() / 2, mSecondPaint.getStrokeWidth() / 2, mFaceSize - mSecondPaint.getStrokeWidth() / 2, mFaceSize - mSecondPaint.getStrokeWidth() / 2, mSecondPaint);
//        canvas.drawPath(path, mSecondPaint);
    }

    private void createPaints() {
        isSecondFull = true;
        mSecondPaint = new Paint();
        mSecondPaint.setStyle(isSecondFull ? Paint.Style.FILL : Paint.Style.STROKE);
        mSecondPaint.setStrokeWidth(mFaceSize / 40f);
        mSecondPaint.setColor(0xff00f0f0);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = right - left;
        height = bottom - top;
        mFaceSize = Math.min(width, height);
        createPaints();
    }
}
