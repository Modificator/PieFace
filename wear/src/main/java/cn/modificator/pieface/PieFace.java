/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.modificator.pieface;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.wearable.watchface.CanvasWatchFaceService;
import android.support.wearable.watchface.WatchFaceService;
import android.support.wearable.watchface.WatchFaceStyle;
import android.text.TextPaint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.logging.LogRecord;

/**
 * Analog watch face with a ticking second hand. In ambient mode, the second hand isn't
 * shown. On devices with low-bit ambient mode, the hands are drawn without anti-aliasing in ambient
 * mode. The watch face is drawn with less contrast in mute mode.
 */
public class PieFace extends CanvasWatchFaceService {

    static Paint mHourPaint;
    static Paint mMinutePaint;
    static Paint mSecondPaint;


    public static String KEY_TEXT_HOUR_COLOR = "#04cd99";
    public static String KEY_TEXT_MINUTE_COLOR = "#818181";
    public static String KEY_TEXT_HOUR_COLOR_CLIP = "#04cd99";
    public static String KEY_TEXT_MINUTE_COLOR_CLIP = "#818181";
    public static String KEY_TEXT_SECOND_COLOR = "#ff9b50";

    /*
     * Update rate in milliseconds for interactive mode. We update once a second to advance the
     * second hand.
     */
    private static final long INTERACTIVE_UPDATE_RATE_MS = 30;

    /**
     * Handler message id for updating the time periodically in interactive mode.
     */
    private static final int MSG_UPDATE_TIME = 0;

    @Override
    public Engine onCreateEngine() {
        return new Engine();
    }

    private static class EngineHandler extends Handler {
        private final WeakReference<PieFace.Engine> mWeakReference;

        public EngineHandler(PieFace.Engine reference) {
            mWeakReference = new WeakReference<>(reference);
        }

        @Override
        public void handleMessage(Message msg) {
            PieFace.Engine engine = mWeakReference.get();
            if (engine != null) {
                switch (msg.what) {
                    case MSG_UPDATE_TIME:
                        engine.handleUpdateTimeMessage();
                        break;
                }
            }
        }
    }

    private class Engine extends CanvasWatchFaceService.Engine {


        /* Handler to update the time once a second in interactive mode. */
        private final Handler mUpdateTimeHandler = new EngineHandler(this);
        private Calendar mCalendar;
        private final BroadcastReceiver mTimeZoneReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mCalendar.setTimeZone(TimeZone.getDefault());
                invalidate();
            }
        };
        private boolean mRegisteredTimeZoneReceiver = false;
        private boolean mAmbient;
        private final Typeface ROBOTO_THIN =
                Typeface.createFromAsset(getAssets(), "fzltcxhjt.TTF");/*
        private final Typeface ROBOTO_THIN =
                Typeface.createFromAsset(getAssets(), "Roboto-Thin.ttf");*/

        float width;
        float height;
        float startAngle = 0;
        float endAngle = 360;
        boolean addAngle = true;

        @Override
        public void onCreate(SurfaceHolder holder) {
            super.onCreate(holder);

            setWatchFaceStyle(new WatchFaceStyle.Builder(PieFace.this)
                    .setCardPeekMode(WatchFaceStyle.PEEK_MODE_SHORT)
                    .setBackgroundVisibility(WatchFaceStyle.BACKGROUND_VISIBILITY_INTERRUPTIVE)
                    .setShowSystemUiTime(false)
                    .setAcceptsTapEvents(true)
                    .build());
            mCalendar = Calendar.getInstance();
            startAngle = 0;
            endAngle = getSecondAngle();
            createPaints();
        }

        private void createPaints() {
            mHourPaint = new TextPaint();
            mMinutePaint = new TextPaint();
            mSecondPaint = new Paint();

            mHourPaint.setAntiAlias(true);
            mSecondPaint.setAntiAlias(true);

            mHourPaint.setColor(Color.parseColor(KEY_TEXT_HOUR_COLOR));
            mMinutePaint.setColor(Color.parseColor(KEY_TEXT_MINUTE_COLOR));
            mSecondPaint.setColor(Color.parseColor(KEY_TEXT_SECOND_COLOR));
            mHourPaint.setTypeface(ROBOTO_THIN);
            mMinutePaint.setTypeface(ROBOTO_THIN);

            mHourPaint.setTextAlign(Paint.Align.CENTER);
            mMinutePaint.setTextAlign(Paint.Align.CENTER);

//            mSecondPaint.setStyle(Paint.Style.STROKE);
            mSecondPaint.setStyle(Paint.Style.FILL);
            mSecondPaint.setStrokeWidth(4f);
        }

        @Override
        public void onDestroy() {
            mUpdateTimeHandler.removeMessages(MSG_UPDATE_TIME);
            super.onDestroy();
        }

        @Override
        public void onPropertiesChanged(Bundle properties) {
            super.onPropertiesChanged(properties);
        }

        @Override
        public void onTimeTick() {
            super.onTimeTick();
            invalidate();
        }

        @Override
        public void onAmbientModeChanged(boolean inAmbientMode) {
            super.onAmbientModeChanged(inAmbientMode);
            mAmbient = inAmbientMode;


            /* Check and trigger whether or not timer should be running (only in active mode). */
            updateTimer();
        }


        @Override
        public void onInterruptionFilterChanged(int interruptionFilter) {
            super.onInterruptionFilterChanged(interruptionFilter);
            boolean inMuteMode = (interruptionFilter == WatchFaceService.INTERRUPTION_FILTER_NONE);

        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);


        }


        @Override
        public void onTapCommand(int tapType, int x, int y, long eventTime) {
            switch (tapType) {
                case TAP_TYPE_TOUCH:
                    // The user has started touching the screen.
                    break;
                case TAP_TYPE_TOUCH_CANCEL:
                    // The user has started a different gesture or otherwise cancelled the tap.
                    break;
                case TAP_TYPE_TAP:
                    // The user has completed the tap gesture.
                    // TODO: Add code to handle the tap gesture.
                    Toast.makeText(getApplicationContext(), R.string.message, Toast.LENGTH_SHORT)
                            .show();
                    break;
            }
            invalidate();
        }

        @Override
        public void onDraw(Canvas canvas, Rect bounds) {
            mCalendar.setTimeInMillis(System.currentTimeMillis());
            width = bounds.exactCenterX();
            height = bounds.exactCenterY();

            Log.e("------", bounds.toString());

            canvas.drawARGB(255, 255, 255, 255);

            mHourPaint.setTextSize(bounds.width() * 0.45f);
            mMinutePaint.setTextSize(bounds.width() * 0.45f);

            canvas.drawArc(width * 0.05f, width * 0.05f, bounds.bottom - width * 0.05f, bounds.right - width * 0.05f, startAngle - 90, endAngle, true, mSecondPaint);

            Rect hourRect = new Rect();
            mHourPaint.getTextBounds(getHour(), 0, 2, hourRect);
            canvas.drawText(getHour(), width, height * 0.9f, mHourPaint);
            canvas.drawText(getMinute(), width, height * 1.1f + hourRect.height(), mMinutePaint);

            if (!addAngle) {
                startAngle = getSecondAngle();
                endAngle = 360 - startAngle;
                if (startAngle > 359.8) addAngle = true;
            } else {
                startAngle = 0;
                endAngle = getSecondAngle();
                if (endAngle > 359.8) addAngle = false;
            }
//            canvas.drawArc(width*0.05f, width*0.05f, bounds.bottom - width*0.05f, bounds.right - width*0.05f, startAngle - 90, endAngle, true, mSecondPaint);

        }

        private String getHour() {
            return String.format("%02d", mCalendar.get(Calendar.HOUR_OF_DAY));
//            return String.format("%02d",mCalendar.get(Calendar.HOUR));
        }

        private String getMinute() {
            return String.format("%02d", mCalendar.get(Calendar.MINUTE));
        }

        private float getSecondAngle() {
            return (mCalendar.get(Calendar.SECOND) + mCalendar.get(Calendar.MILLISECOND) / 1000f) / 60f * 360f;
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);

            if (visible) {
                registerReceiver();
                /* Update time zone in case it changed while we weren't visible. */
                mCalendar.setTimeZone(TimeZone.getDefault());
                invalidate();
            } else {
                unregisterReceiver();
            }

            /* Check and trigger whether or not timer should be running (only in active mode). */
            updateTimer();
        }

        @Override
        public void onPeekCardPositionUpdate(Rect rect) {
            super.onPeekCardPositionUpdate(rect);
        }

        private void registerReceiver() {
            if (mRegisteredTimeZoneReceiver) {
                return;
            }
            mRegisteredTimeZoneReceiver = true;
            IntentFilter filter = new IntentFilter(Intent.ACTION_TIMEZONE_CHANGED);
            PieFace.this.registerReceiver(mTimeZoneReceiver, filter);
        }

        private void unregisterReceiver() {
            if (!mRegisteredTimeZoneReceiver) {
                return;
            }
            mRegisteredTimeZoneReceiver = false;
            PieFace.this.unregisterReceiver(mTimeZoneReceiver);
        }

        /**
         * Starts/stops the {@link #mUpdateTimeHandler} timer based on the state of the watch face.
         */
        private void updateTimer() {
            mUpdateTimeHandler.removeMessages(MSG_UPDATE_TIME);
            if (shouldTimerBeRunning()) {
                mUpdateTimeHandler.sendEmptyMessage(MSG_UPDATE_TIME);
            }
        }

        /**
         * Returns whether the {@link #mUpdateTimeHandler} timer should be running. The timer
         * should only run in active mode.
         */
        private boolean shouldTimerBeRunning() {
            return isVisible() && !mAmbient;
        }

        /**
         * Handle updating the time periodically in interactive mode.
         */
        private void handleUpdateTimeMessage() {
            invalidate();
            if (shouldTimerBeRunning()) {
                long timeMs = System.currentTimeMillis();
                long delayMs = INTERACTIVE_UPDATE_RATE_MS
                        - (timeMs % INTERACTIVE_UPDATE_RATE_MS);
                mUpdateTimeHandler.sendEmptyMessageDelayed(MSG_UPDATE_TIME, delayMs);
            }
        }
    }
}
