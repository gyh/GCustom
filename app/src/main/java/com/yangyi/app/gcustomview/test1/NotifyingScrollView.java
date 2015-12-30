package com.yangyi.app.gcustomview.test1;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * @author Cyril Mottier with modifications from Manuel Peinado
 */
public class NotifyingScrollView extends ScrollView {
    // Edge-effects don't mix well with the translucent action bar in Android 2.X
    private boolean mDisableEdgeEffects = true;

    /**
     * @author Cyril Mottier with modifications from Manuel Peinado
     */
    public interface OnScrollChangedListener {
        void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt);
        void onScrollIdle();
    }

    private OnScrollChangedListener mOnScrollChangedListener;
    private boolean mScrollChangedSinceLastIdle;

    public NotifyingScrollView(Context context) {
        super(context);
    }

    public NotifyingScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NotifyingScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        mScrollChangedSinceLastIdle = true;
        if (mOnScrollChangedListener != null) {
            mOnScrollChangedListener.onScrollChanged(this, l, t, oldl, oldt);
        }
    }

    public void setOnScrollChangedListener(OnScrollChangedListener listener) {
        mOnScrollChangedListener = listener;
    }

    @Override
    protected float getTopFadingEdgeStrength() {
        // http://stackoverflow.com/a/6894270/244576
        if (mDisableEdgeEffects && Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            return 0.0f;
        }
        return super.getTopFadingEdgeStrength();
    }

    @Override
    protected float getBottomFadingEdgeStrength() {
        // http://stackoverflow.com/a/6894270/244576
        if (mDisableEdgeEffects && Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            return 0.0f;
        }
        return super.getBottomFadingEdgeStrength();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        boolean b = super.onTouchEvent(ev);
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            if (mOnScrollChangedListener != null && mScrollChangedSinceLastIdle) {
                mScrollChangedSinceLastIdle = false;
                mOnScrollChangedListener.onScrollIdle();
            }
        }
        return b;
    }
}
