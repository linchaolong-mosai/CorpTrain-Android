package com.mosai.corporatetraining.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * 描述:
 * 作者：周年斌
 * 时间：2016/5/26 0026 23:00
 * 邮箱：nianbin@mosainet.com
 */
public class MyScrollView extends ScrollView{
    public MyScrollView(Context context) {
        super(context,null);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    int x,y,lastx,lasty;
boolean intercepted=false;
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercepted = false;
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                lastx = (int)ev.getX();
                lasty = (int)ev.getY();
                intercepted = false;
                break;
            case MotionEvent.ACTION_MOVE:
                x = (int)ev.getX();
                y = (int)ev.getY();
                if(Math.abs(x-lastx)>Math.abs(y-lasty)){
                    intercepted=false;
                }else{
                    intercepted=true;

                }

                lastx = x;
                lasty = y;

                break;
            case MotionEvent.ACTION_UP:
                intercepted = false;
                break;
        }
        return intercepted;
    }
}
