package com.example.prests1.rushmeandroid;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.text.style.LineBackgroundSpan;
import android.util.Log;


/**
 * Setup for the text of # of events on a given day
 */
public class CustomSpan implements LineBackgroundSpan {
    String text;
    int color;
    public CustomSpan(String str, int newColor){
        this.color = newColor;
        this.text = str;
    }

    @Override
    public void drawBackground(Canvas c, Paint p, int left, int right, int top, int baseline, int bottom, CharSequence text, int start, int end, int lnum){
        p.setColor(color);
        text = this.text;
        c.drawText(String.valueOf(text), ((left+right)/2)-10, bottom+30, p);
    }

}


