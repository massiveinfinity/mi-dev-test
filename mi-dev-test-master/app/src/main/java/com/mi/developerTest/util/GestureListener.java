package com.winjit.util;

import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

public class GestureListener extends SimpleOnGestureListener{
    
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
   
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, 
                                    float velocityX, float velocityY) {
       
        if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && 
                     Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
            //From Right to Left
            return true;
        }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE &&
                     Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
            //From Left to Right
            return true;
        }
       
        if(e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && 
                    Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
            //From Bottom to Top
            return true;
        }  else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && 
                    Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
            //From Top to Bottom
            return true;
        }
        return false;
    }
    @Override
    public boolean onDown(MotionEvent e) {
        //always return true since all gestures always begin with onDown and<br>
        //if this returns false, the framework won't try to pick up onFling for example.
        return true;
    }
}
