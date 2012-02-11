package com.viettelcdc.tinngan.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.ViewGroup;

public class DragToggleViewPager extends ViewPager {
	private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	
	private GestureDetector gestureDetector;
	private boolean dragEnabled;

	public DragToggleViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		setDragEnabled(false);
		setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
	}
	
	public boolean isDragEnabled() {
		return dragEnabled;
	}

	public void setDragEnabled(boolean dragEnabled) {
		this.dragEnabled = dragEnabled;
		if(!dragEnabled && gestureDetector == null) {
			gestureDetector = new GestureDetector(new SwingGestureListener(this));
		}
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
		if(dragEnabled) {
			return super.onInterceptTouchEvent(motionEvent);
		}
		super.onInterceptTouchEvent(motionEvent);
		return gestureDetector.onTouchEvent(motionEvent);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent motionEvent) {
		if(dragEnabled) {
			return super.onTouchEvent(motionEvent);
		}
		gestureDetector.onTouchEvent(motionEvent);
		return true;
	}

	public boolean showNext() {
		int currentIndex = getCurrentItem();
		if(currentIndex < getAdapter().getCount()) {
        	setCurrentItem(currentIndex + 1, true);
        	return true;
        }
		return false;
	}

	public boolean showPrevious() {
		int currentIndex = getCurrentItem();
		if(currentIndex > 0) {
        	setCurrentItem(currentIndex-1, true);
        	return true;
        }
		return false;
	}
	
	static class SwingGestureListener extends SimpleOnGestureListener {
		private DragToggleViewPager viewPager;
		
		SwingGestureListener(DragToggleViewPager viewPager) {
			this.viewPager = viewPager;
		}
		
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                    return false;
                
                if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
                		&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                	return viewPager.showNext();
                } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                		&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                	return viewPager.showPrevious();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
    }
}
