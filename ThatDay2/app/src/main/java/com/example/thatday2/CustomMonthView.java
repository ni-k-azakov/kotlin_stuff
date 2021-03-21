package com.example.thatday2;

import android.content.Context;
import android.graphics.Canvas;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.MonthView;
import com.haibin.calendarview.RangeMonthView;

public class CustomMonthView extends RangeMonthView {

    public CustomMonthView(Context context) {
        super(context);
    }

    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme, boolean isSelectedPre, boolean isSelectedNext) {
        canvas.drawRect(x , y , x + mItemWidth , y + mItemHeight, mSelectedPaint);
        return true;
    }

    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x, int y, boolean isSelected) {
        canvas.drawCircle(x + mItemWidth / 2, y + mItemHeight - 3 * mPadding, mPointRadius, mPointPaint);
    }

    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme, boolean isSelected) {
        float baselineY = mTextBaseLine + y;
        int cx = x + mItemWidth / 2;
        canvas.drawText(String.valueOf(calendar.getDay()),
                cx,
                baselineY,
                calendar.isCurrentDay() ? mCurDayTextPaint :
                        calendar.isCurrentMonth() ? mSchemeTextPaint : mOtherMonthTextPaint);
    }
}
