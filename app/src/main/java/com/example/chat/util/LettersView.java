package com.example.chat.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.example.chat.R;

public class LettersView extends View {

    // Constants
    private static final String TAG = "LettersView";
    private static final int DEFAULT_TEXT_SIZE = 40;
    private static final int SELECTED_TEXT_SIZE = 50;
    private static final int TEXT_COLOR_NORMAL = Color.BLACK;
    private static final int TEXT_COLOR_SELECTED = Color.parseColor("#FF4081");  // colorPrimary (Example)

    private String[] strChars = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
            "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#"};

    private Paint mPaint;
    private int checkIndex = -1;
    private TextView mTextView;
    private OnLettersListViewListener onLettersListViewListener;

    public LettersView(Context context) {
        super(context);
        init();
    }

    public LettersView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LettersView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * Initialize the Paint object for drawing.
     */
    private void init() {
        mPaint = new Paint();
        mPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mPaint.setAntiAlias(true);  // Enable anti-aliasing for smoother text
    }

    /**
     * Draw the letters on the canvas.
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int viewWidth = getWidth();
        int viewHeight = getHeight();
        int singleHeight = viewHeight / strChars.length;

        // Loop through the characters and draw them
        for (int i = 0; i < strChars.length; i++) {
            setTextProperties(i);  // Set text properties (color, size)

            float lettersX = (viewWidth - mPaint.measureText(strChars[i])) / 2;
            float lettersY = singleHeight * i + singleHeight;

            canvas.drawText(strChars[i], lettersX, lettersY, mPaint);
        }
    }

    /**
     * Set text properties like color and size based on whether the letter is selected.
     */
    private void setTextProperties(int index) {
        if (index == checkIndex) {
            mPaint.setColor(TEXT_COLOR_SELECTED);
            mPaint.setTextSize(SELECTED_TEXT_SIZE);
        } else {
            mPaint.setColor(TEXT_COLOR_NORMAL);
            mPaint.setTextSize(DEFAULT_TEXT_SIZE);
        }
    }

    /**
     * Handle touch events (e.g. when user is scrolling and selecting a letter).
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        float y = event.getY();
        int c = (int) (y / getHeight() * strChars.length);  // Calculate the selected letter index

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                if (checkIndex != c) {  // Only update if index changes
                    checkIndex = c;
                    updateLetter(c);
                }
                break;

            case MotionEvent.ACTION_UP:
                checkIndex = -1;
                resetLetterView();
                break;
        }
        return true;
    }

    /**
     * Update the displayed letter in the TextView and notify listener.
     */
    private void updateLetter(int index) {
        if (index >= 0 && index < strChars.length) {
            if (onLettersListViewListener != null) {
                onLettersListViewListener.onLettersListener(strChars[index]);
            }

            if (mTextView != null) {
                mTextView.setVisibility(View.VISIBLE);
                mTextView.setText(strChars[index]);
            }
            invalidate();  // Only invalidate once when the index changes
        }
    }

    /**
     * Reset the visibility of the TextView when user stops touching.
     */
    private void resetLetterView() {
        if (mTextView != null) {
            mTextView.setVisibility(View.INVISIBLE);
        }
        invalidate();  // Invalidate to remove selection highlight
    }

    public void setmTextView(TextView mTextView) {
        this.mTextView = mTextView;
    }

    public TextView getmTextView() {
        return mTextView;
    }

    public void setOnLettersListViewListener(OnLettersListViewListener listener) {
        this.onLettersListViewListener = listener;
    }

    public OnLettersListViewListener getOnLettersListViewListener() {
        return onLettersListViewListener;
    }

    /**
     * Interface for communicating the selected letter to the outside world.
     */
    public interface OnLettersListViewListener {
        void onLettersListener(String letter);
    }
}
