package com.darklightning.othello;

import android.content.Context;
import android.graphics.Color;
import android.text.style.BackgroundColorSpan;
import android.widget.Button;
import android.util.Log;
/**
 * Created by rikki on 1/21/17.
 */

public class MySquares extends Button
{
    public int m,n;
    boolean isblack,ischecked;
    public static boolean blackturn;
    private static final String TAG = "rikki";
    public MySquares(Context context)
    {
        super(context);
    }
    public void changeBlackTurn()
    {
        if(blackturn==false)
        {
            blackturn=true;
        }
        else
        {
            blackturn=false;
        }
    }
    public void changeIsBlack()
    {
        if(isblack==true)
        {
            isblack=false;
            setBackgroundColor(Color.parseColor("white"));

        }
        else
        {
            isblack=true;
            setBackgroundColor(Color.parseColor("black"));

        }
    }

    public void setColour()
    {
        if(blackturn==true)
        {
            setBackgroundColor(Color.parseColor("black"));
            isblack=true;
            Log.d(TAG,"black");
        }
        else
        {
            setBackgroundColor(Color.parseColor("white"));
            isblack=false;
            Log.d(TAG,"white");
        }

    }
}
