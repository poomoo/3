package com.poomoo.parttimejob.ui.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.poomoo.commlib.LogUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * 空闲时间选择控件
 */
public class FreeTimeView extends View implements View.OnTouchListener {
    private final static String TAG = "FreeTimeView";
    private int downIndex; // 按下的格子索引

    private Surface surface;
    private int column = 8; //表格列数
    private int row = 4; //表格行数(出去星期)

    //给控件设置监听事件
    private OnItemClickListener onItemClickListener;

    public FreeTimeView(Context context) {
        super(context);
        init();
    }

    public FreeTimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        surface = new Surface();
        surface.density = getResources().getDisplayMetrics().density;
        setBackgroundColor(surface.bgColor2);
        setOnTouchListener(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        surface.width = getResources().getDisplayMetrics().widthPixels * 6 / 7;
        surface.height = getResources().getDisplayMetrics().heightPixels * 2 / 5;
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(surface.width,
                MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(surface.height,
                MeasureSpec.EXACTLY);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        Log.i(TAG, "[onLayout] changed:"
                + (changed ? "new size" : "not change") + " left:" + left
                + " top:" + top + " right:" + right + " bottom:" + bottom);
        if (changed) {
            surface.init();
        }
        super.onLayout(changed, left, top, right, bottom);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        LogUtils.i(TAG, "onDraw:");
        // 画框
        canvas.drawPath(surface.box1Path, surface.border1Paint);//外框
        canvas.drawPath(surface.box2Path, surface.border2Paint);//内框
//        if (1 == 1)
//            return;
        // 星期
        float weekTextY = surface.weekHeight * 3 / 4f;

        for (int i = 0; i < surface.weekText.length; i++) {
            float weekTextX = i
                    * surface.cellWidth
                    + (surface.cellWidth - surface.weekPaint
                    .measureText(surface.weekText[i])) * 1 / 2f;
            surface.weekPaint.setColor(surface.textColor);
            drawWeekBg(canvas, i);
            canvas.drawText(surface.weekText[i], weekTextX, weekTextY,
                    surface.weekPaint);
        }

        for (int i = 8; i < column * row; i++) {
            //文字

            if (i == 8) {
                int x = getXByIndex(i);
//                drawTimeBg(canvas, i);
                float timeTextX = (x - 1) * surface.cellWidth + (surface.cellWidth - surface.weekPaint.measureText("上午")) * 1 / 2f;
                float timeTextY = surface.weekHeight + surface.cellHeight * 3 / 5f;
                canvas.drawText("上午", timeTextX, timeTextY, surface.weekPaint);

            } else if (i == 16) {
                int x = getXByIndex(i);
//                drawTimeBg(canvas, i);
                float timeTextX = (x - 1) * surface.cellWidth + (surface.cellWidth - surface.weekPaint.measureText("上午")) * 1 / 2f;
                float timeTextY = surface.weekHeight + surface.cellHeight + surface.cellHeight * 3 / 5f;
                canvas.drawText("下午", timeTextX, timeTextY, surface.weekPaint);
            } else if (i == 24) {
                int x = getXByIndex(i);
//                drawTimeBg(canvas, i);
                float timeTextX = (x - 1) * surface.cellWidth + (surface.cellWidth - surface.weekPaint.measureText("上午")) * 1 / 2f;
                float timeTextY = surface.weekHeight + 2 * surface.cellHeight + surface.cellHeight * 3 / 5f;
                canvas.drawText("晚上", timeTextX, timeTextY, surface.weekPaint);
            }
//            else drawCellBg(canvas, i);
        }
        super.onDraw(canvas);
    }

    /**
     * @param canvas
     * @param index
     */
    private void drawWeekBg(Canvas canvas, int index) {
        int x = getXByIndex(index);
        float left;
        float top;
        float right;
        float bottom;
        if (x == 1) {
            left = surface.border1Width;
            right = surface.cellWidth - surface.border2Width;
        } else if (x == 8) {
            left = surface.cellWidth * (x - 1) + surface.border2Width;
            right = surface.cellWidth * x - surface.border1Width;
        } else {
            left = surface.cellWidth * (x - 1) + surface.border2Width;
            right = left + surface.cellWidth - 2 * surface.border2Width;
        }
        top = surface.border1Width;
        bottom = surface.weekHeight - surface.border2Width;
        LogUtils.d(TAG, "drawWeekBg" + x + " border1Width:" + surface.border1Width + "border2Width:" + surface.border2Width);
        LogUtils.d(TAG, "left" + left + " top:" + top + "right:" + right + "bottom" + bottom + "surface.cellWidth:" + surface.cellWidth + "surface.weekHeight:" + surface.weekHeight);
        canvas.drawRect(left, top, right, bottom, surface.weekBgPaint);
    }

    /**
     * @param canvas
     * @param index
     */
    private void drawTimeBg(Canvas canvas, int index) {
        int x = getXByIndex(index);
        int y = getYByIndex(index);
        LogUtils.d(TAG, "drawTimeBg x:" + x + "y:" + y);
        float left;
        float top;
        float right;
        float bottom;

        left = surface.cellWidth * (x - 1) + surface.border1Width;
        top = surface.weekHeight + (y - 2) * surface.cellHeight + surface.border2Width;
        right = left + surface.cellWidth - surface.border2Width;
        if (y == 4)
            bottom = top + surface.cellHeight - surface.border1Width;
        else
            bottom = top + surface.cellHeight - surface.border2Width;
        canvas.drawRect(left, top, right, bottom, surface.weekBgPaint);
    }

    /**
     * @param canvas
     * @param index
     * @param text
     */
    private void drawCellText(Canvas canvas, int index, String text) {
        int x = getXByIndex(index);
        int y = getYByIndex(index);

        float cellX = (surface.cellWidth * (x - 1)) + (surface.cellWidth - surface.weekPaint.measureText(text)) * 1 / 5f;
        float cellY = surface.weekHeight + (y - 1) * surface.cellHeight + surface.cellHeight * 2 / 5f;
        canvas.drawText(text, cellX, cellY, surface.weekPaint);
    }

    /**
     * @param canvas
     * @param index
     */
    private void drawCellBg(Canvas canvas, int index) {
        int x = getXByIndex(index);
        int y = getYByIndex(index);
        float radius;

        radius = Math.min(surface.cellWidth, surface.cellHeight) / 3f;

        float cellX = (surface.cellWidth * (x - 1)) + surface.cellWidth / 2f;
        float cellY = surface.weekHeight + (y - 1) * surface.cellHeight + surface.cellHeight / 1.6f;

        canvas.drawCircle(cellX, cellY, radius, surface.datePaint);
    }


    private int getXByIndex(int i) {
        return i % 8 + 1; // 1 2 3 4 5 6 7 8
    }

    private int getYByIndex(int i) {
        return i / 8 + 1; // 1 2 3
    }

    private void setSelectedDateByCoor(float x, float y) {
        if (y > surface.weekHeight) {
            int m = (int) (Math.floor(x / surface.cellWidth) + 1);
            int n = (int) (Math.floor((y - (surface.weekHeight)) / Float.valueOf(surface.cellHeight)) + 1);
            downIndex = (n - 1) * 8 + m - 1;
        }
        invalidate();
    }

    /**
     * 1. 布局尺寸 2. 文字颜色，大小 3. 当前日期的颜色，选择的日期颜色
     */
    private class Surface {
        public float density;
        public int width; // 整个控件的宽度
        public int height; // 整个控件的高度
        public float weekHeight; // 显示星期的高度
        public float cellWidth; // 日期方框宽度
        public float cellHeight; // 日期方框高度

        public int bgColor1 = Color.parseColor("#C2E2DD");//表头颜色
        public int bgColor2 = Color.parseColor("#FFFFFF");//表格颜色
        private int textColor = Color.parseColor("#000000");//当月日期
        private int borderColor = Color.parseColor("#0EB099");//        边框颜色

        public Paint weekPaint;
        public Paint datePaint;
        public Paint weekBgPaint;

        public Path box1Path; // 外边框
        public Path box2Path; // 内边框
        public Paint border1Paint;// 外边框
        public Paint border2Paint;// 内边框
        public float border1Width;
        public float border2Width;

        public final String[] weekText = {"周", "一", "二", "三", "四", "五", "六", "日"};


        public void init() {
            float temp = height / row;
            weekHeight = (float) ((temp) * 0.7);
            cellHeight = (height - weekHeight) / (row - 1);
            cellWidth = width / 8f;

            weekPaint = new Paint();
            weekPaint.setColor(textColor);
            weekPaint.setAntiAlias(true);
            float weekTextSize = weekHeight * 0.4f;
            weekPaint.setTextSize(weekTextSize);

            datePaint = new Paint();
            datePaint.setAntiAlias(true);
            datePaint.setTypeface(Typeface.DEFAULT_BOLD);

            weekBgPaint = new Paint();
            weekBgPaint.setColor(bgColor1);
            weekBgPaint.setAntiAlias(true);
            weekBgPaint.setStyle(Paint.Style.FILL);

            box1Path = new Path();
            box1Path.rLineTo(width, 0);

            box2Path = new Path();
            box2Path.moveTo(0, weekHeight);
            box2Path.rLineTo(width, 0);
            for (int i = 0; i < row; i++) {
                //横线
                if (i == row - 1) {
                    box1Path.moveTo(0, weekHeight + i * cellHeight);
                    box1Path.rLineTo(width, 0);
                } else {
                    box2Path.moveTo(0, weekHeight + i * cellHeight);
                    box2Path.rLineTo(width, 0);
                }

            }
            for (int i = 0; i < column + 1; i++) {
                //竖线
                if (i == 0 || i == column) {
                    box1Path.moveTo(i * cellWidth, 0);
                    box1Path.rLineTo(0, height);
                } else {
                    box2Path.moveTo(i * cellWidth, 0);
                    box2Path.rLineTo(0, height);
                }

            }
            //外
            border1Paint = new Paint();
            border1Paint.setColor(borderColor);
            border1Paint.setStyle(Paint.Style.STROKE);
            border1Width = (float) (1.8 * density);
            border1Width = border1Width < 1 ? 1 : border1Width;
            border1Paint.setStrokeWidth(border1Width);
            //内
            border2Paint = new Paint();
            border2Paint.setColor(borderColor);
            border2Paint.setStyle(Paint.Style.STROKE);
            border2Width = (float) (0.5 * density);
            border2Width = border2Width < 1 ? 1 : border2Width;
            border2Paint.setStrokeWidth(border2Width);
            LogUtils.i(TAG, "init border1Width:" + border1Width + "border2Width:" + border2Width);
        }
    }

    //给控件设置监听事件
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //监听接口
    public interface OnItemClickListener {
        void OnItemClick();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                setSelectedDateByCoor(event.getX(), event.getY());
                onItemClickListener.OnItemClick();
                break;
        }
        return true;
    }
}
