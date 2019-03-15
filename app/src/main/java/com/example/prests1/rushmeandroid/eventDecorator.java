package com.example.prests1.rushmeandroid;

import android.graphics.Color;
import android.icu.util.LocaleData;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;

/**
 * eventDecorator is used to overlay the number of events below the date that has the events
 */
public class eventDecorator implements DayViewDecorator {

    private final int color;
    private final HashSet<CalendarDay> dates;
    private final int eventNum;
    public eventDecorator(int color, Collection<CalendarDay> dates, int total) {
        this.color = color;
        this.dates = new HashSet<>(dates);
        this.eventNum = total;
    }

    /**
     * Decorate any day that matches the date (should be always true)
     * @param day
     * @return
     */
    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    /**
     * Apply overlay
     */
    public void decorate(DayViewFacade view) {
        CustomSpan numEvents = new CustomSpan(Integer.toString(eventNum), color);
        view.addSpan(numEvents);
        view.addSpan(new ForegroundColorSpan(Color.BLACK)); //Prevents the date from being the color as the number of events that day
    }
}
