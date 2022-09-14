package com.example.calendar;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Not used
 */

class SwipeListener implements View.OnTouchListener {
    //initialize variable
    GestureDetector gestureDetector;
    //create constructor
    SwipeListener(View view) {
        //initialize threshold value
        int threshold = 100;
        int velocity_threshold = 100;

        //initialize simple gesture listener
        GestureDetector.SimpleOnGestureListener listener = new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                float xDiff = e2.getX() - e1.getX();
                float yDiff = e2.getY() - e1.getY();
                try {
                    //when x is gather than y
                    if (Math.abs(xDiff) > Math.abs(yDiff)) {
                        //when x difference is gather than threshold
                        if (Math.abs(xDiff) > threshold && Math.abs(velocityX) > velocity_threshold) {

                            if (xDiff > 0) {
                                Log.d("myLogF", "right");
                            } else {
                                Log.d("myLogF", "left");
                            }
                            return true;
                        }
                    }
                }catch (Exception e){
                    Log.d("myLog", "Error : " + e.toString());
                }
                return false;
            }
        };
        //initialize gesture detector
        gestureDetector = new GestureDetector(listener);
        view.setOnTouchListener(this);
    }
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return gestureDetector.onTouchEvent(motionEvent);
    }
}