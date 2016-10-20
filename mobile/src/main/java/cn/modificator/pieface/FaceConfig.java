package cn.modificator.pieface;

import android.graphics.Bitmap;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;

/**
 * Created by Modificator
 * time: 16-10-20.上午11:05
 * des:create file and achieve model
 */

public class FaceConfig {
    //表盘背景颜色1进制值
    @ColorInt
    private int backgroundColor;
    //表盘背景切色
    @ColorInt
    private int backgroundClipColor;
    //小时字体颜色
    @ColorInt
    private int hourColor;
    //小时字体切色
    @ColorInt
    private int hourClipColor;
    //分钟字体颜色
    @ColorInt
    private int minuteColor;
    //分钟字体切色
    @ColorInt
    private int minuteClipColor;
    //秒针宽度百分比 0-1
    @FloatRange(from = 0f, to = 1f)
    private float secondsWidth;

    //表盘背景图片
    private Bitmap backgroundImage;
    //是否使用背景图片
    private boolean useBgImage = false;
    //秒针填充
    private boolean fullSeconds = false;
    //单独属性或就这样? 第二页的属性考虑单独的配置
    private boolean useSecondPage = false;

    public int getBackgroundClipColor() {
        return backgroundClipColor;
    }

    public void setBackgroundClipColor(int backgroundClipColor) {
        this.backgroundClipColor = backgroundClipColor;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public Bitmap getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(Bitmap backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public boolean isFullSeconds() {
        return fullSeconds;
    }

    public void setFullSeconds(boolean fillSeconds) {
        this.fullSeconds = fillSeconds;
    }

    public int getHourClipColor() {
        return hourClipColor;
    }

    public void setHourClipColor(int hourClipColor) {
        this.hourClipColor = hourClipColor;
    }

    public int getHourColor() {
        return hourColor;
    }

    public void setHourColor(int hourColor) {
        this.hourColor = hourColor;
    }

    public int getMinuteClipColor() {
        return minuteClipColor;
    }

    public void setMinuteClipColor(int minuteClipColor) {
        this.minuteClipColor = minuteClipColor;
    }

    public int getMinuteColor() {
        return minuteColor;
    }

    public void setMinuteColor(int minuteColor) {
        this.minuteColor = minuteColor;
    }

    public float getSecondsWidth() {
        return secondsWidth;
    }

    public void setSecondsWidth(float secondsWidth) {
        this.secondsWidth = secondsWidth;
    }

    public boolean isUseBgImage() {
        return useBgImage;
    }

    public void setUseBgImage(boolean useBgImage) {
        this.useBgImage = useBgImage;
    }

    public boolean isUseSecondPage() {
        return useSecondPage;
    }

    public void setUseSecondPage(boolean useSecondPage) {
        this.useSecondPage = useSecondPage;
    }
}
