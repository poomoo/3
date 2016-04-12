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
 * 日历控件
 */
public class CalendarView extends View implements View.OnTouchListener {
    private final static String TAG = "CalendarView";
    private Date curDate; // 当前日历显示的月
    private Date downDate; // 手指按下状态时临时日期
    private int downIndex; // 按下的格子索引
    private Date today; // 今天的日期文字显示红色

    private Calendar calendar;
    private Surface surface;
    private String[] date; // 日历显示数字
    private int column = 7; //日历列数
    private int row = 0; //日历行数(除去星期)
    private int dayInWeek;
    private int curStartIndex, curEndIndex; // 当前显示的日历起始的索引
    private int curMonthDay;//当前月份天数

    //给控件设置监听事件
    private OnItemClickListener onItemClickListener;

    public CalendarView(Context context) {
        super(context);
        init();
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        today = new Date();
        if (curDate == null)
            curDate = today;
        calendar = Calendar.getInstance();
        calendar.setTime(curDate);
        surface = new Surface();
        surface.density = getResources().getDisplayMetrics().density;
        setBackgroundColor(surface.bgColor);
        setOnTouchListener(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        surface.width = getResources().getDisplayMetrics().widthPixels;
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
            calculateRow();
        }
        super.onLayout(changed, left, top, right, bottom);
    }


    private void calculateRow() {
        LogUtils.i(TAG, "calculateRow:" + calendar.getTime());
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        dayInWeek = calendar.get(Calendar.DAY_OF_WEEK);
        LogUtils.i(TAG, "calculateRow dayInWeek:" + dayInWeek);
        if (dayInWeek == 1)
            dayInWeek = 6;
        else
            dayInWeek -= 2;//以日为开头-1，以星期一为开头-2

        calendar.setTime(curDate);
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 0);
        curMonthDay = calendar.get(Calendar.DAY_OF_MONTH);
        LogUtils.i(TAG, "calculateRow dayInWeek2:" + dayInWeek + "curMonthDay:" + curMonthDay);
        if (dayInWeek + curMonthDay > 35)
            row = 6;
        else
            row = 5;
        Log.i(TAG, "calculateRow:" + row + "");
        surface.init(row + 1);
    }

    private void initDate() {
        date = new String[column * row];
        for (int i = 0; i < date.length; i++)
            date[i] = "";
    }

    private void calculateDate() {
        initDate();
        int monthStart = dayInWeek;
        curStartIndex = monthStart;
        date[monthStart] = 1 + "";
        // last month
        if (monthStart > 0) {
            calendar.set(Calendar.DAY_OF_MONTH, 0);
            int dayInmonth = calendar.get(Calendar.DAY_OF_MONTH);
            for (int i = monthStart - 1; i >= 0; i--) {
                date[i] = dayInmonth + "";
                dayInmonth--;
            }
            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date[0]));
        }
        // this month
        for (int i = 1; i < curMonthDay; i++) {
            date[monthStart + i] = i + 1 + "";
        }
        curEndIndex = monthStart + curMonthDay;
        // next month
        for (int i = monthStart + curMonthDay; i < column * row; i++) {
            date[i] = i - (monthStart + curMonthDay) + 1 + "";
        }
//        if (curEndIndex < 42) {
//            // 显示了下一月的
//            calendar.add(Calendar.DAY_OF_MONTH, 1);
//        }
//        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date[column * row - 1]));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        LogUtils.i(TAG, "onDraw:" + curDate);
        // 星期
        float weekTextY = surface.weekHeight * 3 / 4f;

        for (int i = 0; i < surface.weekText.length; i++) {
            float weekTextX = i
                    * surface.cellWidth
                    + (surface.cellWidth - surface.weekPaint
                    .measureText(surface.weekText[i])) * 1 / 2f;
            if (i == 0)
                surface.weekPaint.setColor(surface.textColor1);
            else
                surface.weekPaint.setColor(surface.textColor1);
            canvas.drawText(surface.weekText[i], weekTextX, weekTextY,
                    surface.weekPaint);
        }
        // 计算日期
        calculateDate();
        calendar.setTime(curDate);
        String curYearAndMonth = calendar.get(Calendar.YEAR) + ""
                + calendar.get(Calendar.MONTH);
        calendar.setTime(today);
        String todayYearAndMonth = calendar.get(Calendar.YEAR) + ""
                + calendar.get(Calendar.MONTH);
        int todayIndex = -1;
        if (curYearAndMonth.equals(todayYearAndMonth)) {
            int todayNumber = calendar.get(Calendar.DAY_OF_MONTH);
            todayIndex = curStartIndex + todayNumber - 1;
        }

        int downIndex = -1;
        if (downDate != null) {
            calendar.setTime(downDate);

            String downYearAndMonth = calendar.get(Calendar.YEAR) + ""
                    + calendar.get(Calendar.MONTH);
            if (curYearAndMonth.equals(downYearAndMonth)) {
                int downNumber = calendar.get(Calendar.DAY_OF_MONTH);
                downIndex = curStartIndex + downNumber - 1;
            }
        }


        for (int i = 0; i < column * row; i++) {
            //文字
            int color = surface.textColor1;
            if (isLastMonth(i)) {
                color = surface.textColor2;
            } else if (isNextMonth(i)) {
                color = surface.textColor2;
            }
            if (todayIndex != -1 && i == todayIndex) {
                color = surface.todayNumberColor;
                drawDateBg(canvas, i, surface.currBgColor);
            }
            if (downIndex == i) {
                color = surface.todayNumberColor;
                drawDateBg(canvas, i, surface.selectedBgColor);
            }

            drawCellText(canvas, i, date[i], color);
        }
        calendar.setTime(curDate);
        super.onDraw(canvas);
    }


    /**
     * @param canvas
     * @param index
     * @param text
     */
    private void drawCellText(Canvas canvas, int index, String text, int color) {
        int x = getXByIndex(index);
        int y = getYByIndex(index);

        surface.datePaint.setColor(color);

        float cellX = (surface.cellWidth * (x - 1)) + (surface.cellWidth - surface.datePaint.measureText(text)) / 2f;
        float cellY = surface.weekHeight + (y - 1) * surface.cellHeight + surface.cellHeight * 3 / 4f;
        canvas.drawText(text, cellX, cellY, surface.datePaint);
    }


    /**
     * @param canvas
     * @param index
     */
    private void drawDateBg(Canvas canvas, int index, int color) {
        int x = getXByIndex(index);
        int y = getYByIndex(index);
        float radius;
        surface.dateBgPaint.setColor(color);

        radius = Math.min(surface.cellWidth, surface.cellHeight) / 3f;

        float cellX = (surface.cellWidth * (x - 1)) + surface.cellWidth / 2f;
        float cellY = surface.weekHeight + (y - 1) * surface.cellHeight + surface.cellHeight / 1.6f;

        canvas.drawCircle(cellX, cellY, radius, surface.dateBgPaint);
    }

    private boolean isLastMonth(int i) {
        if (i < curStartIndex) {
            return true;
        }
        return false;
    }

    private boolean isNextMonth(int i) {
        if (i >= curEndIndex) {
            return true;
        }
        return false;
    }

    private int getXByIndex(int i) {
        return i % 7 + 1; // 1 2 3 4 5 6 7
    }

    private int getYByIndex(int i) {
        return i / 7 + 1; // 1 2 3 4 5 6
    }

    // 获得当前应该显示的年月
    public String getYearAndMonth() {
        calendar.setTime(curDate);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return year + "-" + month + "-" + day;
    }

    //上一月
    public String clickLeftMonth() {
        calendar.setTime(curDate);
        calendar.add(Calendar.MONTH, -1);
        curDate = calendar.getTime();
        calculateRow();
        invalidate();
        return getYearAndMonth();
    }

    //下一月
    public String clickRightMonth() {
        calendar.setTime(curDate);
        calendar.add(Calendar.MONTH, 1);
        curDate = calendar.getTime();
        calculateRow();
        invalidate();
        return getYearAndMonth();
    }

    //设置日历时间
    public void setCalendarData(Date date) {
        calendar.setTime(date);
        curDate = date;
        invalidate();
    }

    public void setDownDate(Date downDate) {
        this.downDate = downDate;
    }

    private void setSelectedDateByCoor(float x, float y) {
        if (y > surface.weekHeight) {
            int m = (int) (Math.floor(x / surface.cellWidth) + 1);
            int n = (int) (Math.floor((y - (surface.weekHeight)) / Float.valueOf(surface.cellHeight)) + 1);
            downIndex = (n - 1) * 7 + m - 1;
            LogUtils.i(TAG, "curDate:" + curDate + "calendar.getTime():" + calendar.getTime());
            LogUtils.i(TAG, "downIndex:" + downIndex + "date:" + date[downIndex] + "curStartIndex:" + curStartIndex + "curEndIndex:" + curEndIndex);
            if (downIndex < curStartIndex) {
                //上个月
                LogUtils.i(TAG, "上个月");
                calendar.add(Calendar.MONTH, -1);
            } else if (downIndex >= curEndIndex) {
                //下个月
                LogUtils.i(TAG, "下个月");
                calendar.add(Calendar.MONTH, 1);
            }

            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date[downIndex]));
            downDate = calendar.getTime();
            calendar.setTime(curDate);//还原到当前日期
            LogUtils.i(TAG, "downDate:" + downDate + "today:" + today);
            if (downDate.before(today)) {
                LogUtils.i(TAG, "今天之前");
                return;
            }

            if (isLastMonth(downIndex)) {
                clickLeftMonth();
                return;
            } else if (isNextMonth(downIndex)) {
                clickRightMonth();
                return;
            }
            curDate = downDate;
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
        public float cellTextSize;//字体大小

        public int bgColor = Color.parseColor("#FFFFFF");
        private int textColor1 = Color.parseColor("#484848");//当月日期
        private int textColor2 = Color.parseColor("#979797");//非当月日期
        private int currBgColor = Color.parseColor("#B5B5B5");
        private int selectedBgColor = Color.parseColor("#0db09b");
        public int todayNumberColor = Color.WHITE;

        public Paint weekPaint;
        public Paint datePaint;
        public Paint dateBgPaint;

        public Path boxPath; // 边框路径
        public Paint borderPaint;
        public float borderWidth;
        private int borderColor = Color.GRAY;

        public final String[] weekText = {"一", "二", "三", "四", "五", "六", "日"};


        public void init(int row) {
            Log.i(TAG, "init:" + row);
            float temp = height / row;
            weekHeight = (float) ((temp) * 0.7);
            cellHeight = (height - weekHeight) / (row - 1);
            cellWidth = width / 7f;

            weekPaint = new Paint();
//            weekPaint.setColor(textColor);
            weekPaint.setAntiAlias(true);
            float weekTextSize = weekHeight * 0.5f;
            weekPaint.setTextSize(weekTextSize);

            datePaint = new Paint();
//            datePaint.setColor(textColor);
            datePaint.setAntiAlias(true);
            cellTextSize = cellHeight * 1 / 3f;
            datePaint.setTextSize(cellTextSize);
            datePaint.setTypeface(Typeface.DEFAULT_BOLD);

            dateBgPaint = new Paint();
            dateBgPaint.setAntiAlias(true);
            dateBgPaint.setStyle(Paint.Style.FILL);

            boxPath = new Path();
            boxPath.rLineTo(width, 0);
            boxPath.moveTo(0, weekHeight);
            boxPath.rLineTo(width, 0);
            for (int i = 1; i < row; i++) {
                //横线
                boxPath.moveTo(0, weekHeight + i * cellHeight);
                boxPath.rLineTo(width, 0);
            }
            for (int i = 1; i < column; i++) {
                //竖线
                boxPath.moveTo(i * cellWidth, 0);
                boxPath.rLineTo(0, height);
            }

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
        void OnItemClick(Date downDate);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                setSelectedDateByCoor(event.getX(), event.getY());
                onItemClickListener.OnItemClick(curDate);
                break;
        }
        return true;
    }
}
