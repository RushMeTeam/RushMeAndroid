package com.example.prests1.rushmeandroid;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.text.style.LineBackgroundSpan;
import android.util.Log;

public class CustomSpan implements LineBackgroundSpan {
    String text;
    public CustomSpan(String str){
        this.text = str;
    }

    @Override
    public void drawBackground(Canvas c, Paint p, int left, int right, int top, int baseline, int bottom, CharSequence text, int start, int end, int lnum){
        p.setColor(Color.RED);
        text = this.text;
        Log.d("SPANNNN", this.text);
        c.drawText(String.valueOf(text), ((left+right)/2)-10, bottom+30, p);
    }

}


