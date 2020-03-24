package com.example.blu_main_test1.amount_change_viewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.lang.reflect.Field;


/**
 * Created by Deepak Mishra on 10/16/2017.
 */

public class VerticalViewPager extends ViewPager {


    public VerticalViewPager(Context context) {
        super(context);
        init(context);


    }

    public VerticalViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        // The majority of the magic happens here
        setPageTransformer(false, new VerticalPageTransformer());
        // The easiest way to get rid of the overscroll drawing that happens on the left and right
        setOverScrollMode(OVER_SCROLL_NEVER);

        try {
            Class cls = this.getClass().getSuperclass();
            Field distanceField = cls.getDeclaredField("mFlingDistance");
            distanceField.setAccessible(true);
            distanceField.setInt(this, distanceField.getInt(this) );
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        try {
            Class cls = this.getClass().getSuperclass();
            Field minVelocityField = cls.getDeclaredField("mMinimumVelocity");
            minVelocityField.setAccessible(true);
            minVelocityField.setInt(this, minVelocityField.getInt(this) );
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        try {
            Class cls = this.getClass().getSuperclass();
            Field maxVelocityField = cls.getDeclaredField("mMaximumVelocity");
            maxVelocityField.setAccessible(true);
            maxVelocityField.setInt(this, maxVelocityField.getInt(this) );
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        try {
            Class cls = this.getClass().getSuperclass();
            Field slopField = cls.getDeclaredField("mTouchSlop");
            slopField.setAccessible(true);
            slopField.setInt(this, slopField.getInt(this)/10 );
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        try {
            Class cls = this.getClass().getSuperclass();
            Field minHeightWidthRatioField = cls.getDeclaredField("minYXRatioForIntercept");
            minHeightWidthRatioField.setAccessible(true);
            minHeightWidthRatioField.setFloat(this, minHeightWidthRatioField.getFloat(this) );
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        try {
            Class cls = this.getClass().getSuperclass();
            Field minHeightWidthRatioField = cls.getDeclaredField("minYXRatioForTouch");
            minHeightWidthRatioField.setAccessible(true);
            minHeightWidthRatioField.setInt(this, minHeightWidthRatioField.getInt(this) );
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Swaps the X and Y coordinates of your touch event.
     */
    private MotionEvent swapXY(MotionEvent ev) {
        float width = getWidth();
        float height = getHeight();

        float y = ev.getY();
        float x = ev.getX();

        float newX = (y / height) * width;
        float newY = (x / width) * height;

        ev.setLocation(newX, newY);

        return ev;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercepted = super.onInterceptTouchEvent(swapXY(ev));
        swapXY(ev); // return touch coordinates to original reference frame for any child views
        return intercepted;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(swapXY(ev));
    }

    private class VerticalPageTransformer implements PageTransformer {
        private static final float MIN_SCALE = 0.75f;
        private int maxTranslateOffsetX;


        @Override
        public void transformPage(View view, float position) {

            if (position < -1) {
                view.setAlpha(1);

            } else if (position <= 1) {
                view.setAlpha(1);
                view.setTranslationX(view.getWidth() * -position);
                view.setTranslationY(view.getHeight()* position);
            } else {
                view.setAlpha(1);
            }



        }
        private int dp2px(Context context, float dipValue) {
            float m = context.getResources().getDisplayMetrics().density;
            return (int) (dipValue * m + 0.3f);
        }
    }

}