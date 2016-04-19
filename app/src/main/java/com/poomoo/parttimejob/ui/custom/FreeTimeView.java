package com.poomoo.parttimejob.ui.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;

import com.poomoo.commlib.LogUtils;
import com.poomoo.parttimejob.R;

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
    private SparseArray<Boolean> sparseArray = new SparseArray<>();

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
        for (int i = 0; i < row * column; i++)
            sparseArray.put(i, false);
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
//        Log.i(TAG, "[onLayout] changed:"
//                + (changed ? "new size" : "not change") + " left:" + left
//                + " top:" + top + " right:" + right + " bottom:" + bottom);
        if (changed) {
            surface.init();
        }
        super.onLayout(changed, left, top, right, bottom);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(0, 0, surface.width, surface.weekHeight, surface.weekBgPaint);//星期背景色
        canvas.drawRect(0, surface.weekHeight, surface.cellWidth, surface.height, surface.weekBgPaint);//时间背景色
        // 画框
        canvas.drawPath(surface.boxPath, surface.borderPaint);//内框
        // 星期
        float weekTextY = surface.weekHeight * 3 / 4f;

        for (int i = 0; i < surface.weekText.length; i++) {
            float weekTextX = i
                    * surface.cellWidth
                    + (surface.cellWidth - surface.weekPaint
                    .measureText(surface.weekText[i])) * 1 / 2f;
            surface.weekPaint.setColor(surface.textColor);
            canvas.drawText(surface.weekText[i], weekTextX, weekTextY,
                    surface.weekPaint);
        }

        for (int i = 8; i < column * row; i++) {
            //文字
            if (i == 8) {
                int x = getXByIndex(i);
                float timeTextX = (x - 1) * surface.cellWidth + (surface.cellWidth - surface.weekPaint.measureText("上午")) * 1 / 2f;
                float timeTextY = surface.weekHeight + surface.cellHeight * 3 / 5f;
                canvas.drawText("上午", timeTextX, timeTextY, surface.weekPaint);

            } else if (i == 16) {
                int x = getXByIndex(i);
                float timeTextX = (x - 1) * surface.cellWidth + (surface.cellWidth - surface.weekPaint.measureText("上午")) * 1 / 2f;
                float timeTextY = surface.weekHeight + surface.cellHeight + surface.cellHeight * 3 / 5f;
                canvas.drawText("下午", timeTextX, timeTextY, surface.weekPaint);
            } else if (i == 24) {
                int x = getXByIndex(i);
                float timeTextX = (x - 1) * surface.cellWidth + (surface.cellWidth - surface.weekPaint.measureText("上午")) * 1 / 2f;
                float timeTextY = surface.weekHeight + 2 * surface.cellHeight + surface.cellHeight * 3 / 5f;
                canvas.drawText("晚上", timeTextX, timeTextY, surface.weekPaint);
            } else {
                Bitmap bmp;
                if (sparseArray.get(i)) {
                    //图标
                    bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_selected);
                    drawCellIcon(canvas, i, bmp);
                } else {//撤销
                    bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_selected);
                    bmp = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), Bitmap.Config.ARGB_8888);
                    drawCellIcon(canvas, i, bmp);
                }
            }

        }
        super.onDraw(canvas);
    }

    /**
     * @param canvas
     * @param index
     * @param bitmap
     */
    private void drawCellIcon(Canvas canvas, int index, Bitmap bitmap) {
        int x = getXByIndex(index);
        int y = getYByIndex(index);
        float cellX = (surface.cellWidth * (x - 1)) + (surface.cellWidth) * 1 / 4f;
        float cellY = surface.weekHeight + (y - 2) * surface.cellHeight + surface.cellHeight * 2 / 5f;
        canvas.drawBitmap(bitmap, cellX, cellY, surface.datePaint);
    }


    private int getXByIndex(int i) {
        return i % 8 + 1; // 1 2 3 4 5 6 7 8
    }

    private int getYByIndex(int i) {
        return i / 8 + 1; // 1 2 3
    }

    private void setSelectedDateByCoor(float x, float y) {
        if (x > surface.cellWidth && y > surface.weekHeight) {
            int m = (int) (Math.floor(x / surface.cellWidth) + 1);
            int n = (int) (Math.floor((y - (surface.weekHeight)) / Float.valueOf(surface.cellHeight)) + 1);
            downIndex = (n - 1) * 8 + m - 1 + 8;//起始下标从9开始
            LogUtils.d(TAG, "downIndex:" + downIndex);
            if (sparseArray.get(downIndex)) {
                sparseArray.put(downIndex, false);
            } else {
                sparseArray.put(downIndex, true);
            }
            invalidate();
        } else if (x > surface.cellWidth) {
            int m = (int) (Math.floor(x / surface.cellWidth) + 1);
            int n = (int) (Math.floor(y / Float.valueOf(surface.weekHeight)) + 1);
            downIndex = (n - 1) * 8 + m - 1;//起始下标从1开始
            if (sparseArray.get(downIndex + n * 8) || sparseArray.get(downIndex + (n + 1) * 8) || sparseArray.get(downIndex + (n + 2) * 8)) {
                sparseArray.put(downIndex + n * 8, false);
                sparseArray.put(downIndex + (n + 1) * 8, false);
                sparseArray.put(downIndex + (n + 2) * 8, false);
            } else {
                sparseArray.put(downIndex + n * 8, true);
                sparseArray.put(downIndex + (n + 1) * 8, true);
                sparseArray.put(downIndex + (n + 2) * 8, true);
            }
            invalidate();
        }
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

        public Path boxPath;
        public Paint borderPaint;
        public float borderWidth;

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
            datePaint.setColor(bgColor2);
            datePaint.setAntiAlias(true);
            datePaint.setStyle(Paint.Style.FILL);

            weekBgPaint = new Paint();
            weekBgPaint.setColor(bgColor1);
            weekBgPaint.setAntiAlias(true);
            weekBgPaint.setStyle(Paint.Style.FILL);

            boxPath = new Path();
            boxPath.rLineTo(width, 0);
            boxPath.moveTo(0, weekHeight);
            boxPath.rLineTo(width, 0);
            for (int i = 0; i < row; i++) {
                //横线
                if (i == row - 1)
                    boxPath.moveTo(0, weekHeight + i * cellHeight - 2);
                else
                    boxPath.moveTo(0, weekHeight + i * cellHeight);
                boxPath.rLineTo(width, 0);
            }
            for (int i = 0; i < column + 1; i++) {
                //竖线
                if (i == column)
                    boxPath.moveTo(i * cellWidth - 2, 0);
                else
                    boxPath.moveTo(i * cellWidth, 0);
                boxPath.rLineTo(0, height);
            }
            //外
            borderPaint = new Paint();
            borderPaint.setColor(borderColor);
            borderPaint.setStyle(Paint.Style.STROKE);
            borderWidth = (float) (0.5 * density);
            borderWidth = borderWidth < 1 ? 1 : borderWidth;
            borderPaint.setStrokeWidth(borderWidth);
        }
    }

    //给控件设置监听事件
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //监听接口
    public interface OnItemClickListener {
        void OnItemClick(int index);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                setSelectedDateByCoor(event.getX(), event.getY());
//                onItemClickListener.OnItemClick(downIndex);
                break;
        }
        return true;
    }

    public SparseArray<Boolean> getSparseArray() {
        return sparseArray;
    }
}
