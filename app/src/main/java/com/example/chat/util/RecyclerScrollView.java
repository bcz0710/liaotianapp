package com.example.chat.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ScrollView;

/**
 * Custom ScrollView to intercept touch events based on scroll distance.
 */
public class RecyclerScrollView extends ScrollView {

    // Minimum touch slop required for scrolling to begin
    private final int touchSlop;

    // Initial Y position of touch event
    private float initialTouchY;

    public RecyclerScrollView(Context context) {
        super(context);
        this.touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public RecyclerScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public RecyclerScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    /**
     * Intercepts touch events based on vertical scroll distance.
     *
     * @param ev The motion event.
     * @return true if the event is intercepted, false otherwise.
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // Record the initial Y position on ACTION_DOWN
                initialTouchY = ev.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:
                // Calculate the distance moved and intercept if it exceeds touchSlop
                if (Math.abs(ev.getRawY() - initialTouchY) > touchSlop) {
                    return true; // Start intercepting the touch event
                }
                break;
        }

        // Pass the event to the parent if not intercepted
        return super.onInterceptTouchEvent(ev);
    }
}
