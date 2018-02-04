package com.example.coordinator;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Author: getnway on 2018-01-24.
 * Email: getnway@gmail.com
 */
@CoordinatorLayout.DefaultBehavior(CollapsingLayout.Behavior.class)
public class CollapsingLayout extends LinearLayout {
    private static final String TAG = "CollapsingLayout";
    private View targetView;        // 要压缩的子View
    private int targetMaxHeight;    // 要压缩的子View最大高度
    private int targetMinHeight;    // 要压缩的子View最小高度

    public CollapsingLayout(Context context) {
        this(context, null);
    }

    public CollapsingLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CollapsingLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
    }

    public void setTargetView(View targetView) {
        this.targetView = targetView;
        if (targetView != null) {
            targetMaxHeight = 270;//targetView.getHeight();
            targetMinHeight = targetView.getMinimumHeight();
        } else {
            targetMaxHeight = 0;
            targetMinHeight = 0;
        }
        Log.d(TAG, String.format("call setTargetView(): targetView = [%s] %s-%s", targetView, targetMaxHeight, targetMinHeight));
    }

    public View getTargetView() {
        return targetView;
    }

    public int getTargetMaxHeight() {
        return targetMaxHeight;
    }

    public int getTargetMinHeight() {
        return targetMinHeight;
    }

    @Override
    public void setOrientation(int orientation) {
        if (orientation != VERTICAL) {
            throw new IllegalArgumentException("CollapsingLayout is always vertical and does"
                    + " not support horizontal orientation");
        }
        super.setOrientation(orientation);
    }

    public static class Behavior extends CoordinatorLayout.Behavior<CollapsingLayout> {
        private static final String TAG = "Collapsing-Behavior";

        private boolean mSkipNestedPreScroll;
        private int mCollapsingDelta;

        public Behavior() {
        }

        public Behavior(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, CollapsingLayout child, View directTargetChild, View target, int nestedScrollAxes) {
            boolean started = (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0
                    && child.getTargetView() != null
                    && child.getTargetMaxHeight() > 0;
            Log.d(TAG, String.format("call onStartNestedScroll(): %s %s %s",
                    (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0, child.getTargetView() != null, child.getTargetMaxHeight()));
            return started;
        }

        @Override
        public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, CollapsingLayout child, View target, int dx, int dy, int[] consumed) {
//            if (dy != 0 && !mSkipNestedPreScroll) {
            if (dy != 0) {
                int consumedY = 0;
                int currentHeight = child.getTargetView().getLayoutParams().height;
                if (dy < 0) {
//                    // We're scrolling down
//                    if (child.getTargetMaxHeight() - currentHeight < -dy) {
//                        consumedY = -(child.getTargetMaxHeight() - currentHeight);
//                    } else {
//                        consumedY = dy;
//                    }
//                    if (consumedY != 0) {
//                        LayoutParams layoutParams = (LayoutParams) child.getTargetView().getLayoutParams();
//                        layoutParams.height = layoutParams.height - consumedY;
//                        child.getTargetView().setLayoutParams(layoutParams);
//                        child.getTargetView().invalidate();
//                        mCollapsingDelta = child.getTargetMaxHeight() - layoutParams.height;
//                    }
                } else {
                    // We're scrolling up
                    if (currentHeight - child.getTargetMinHeight() < dy) {
                        consumedY = currentHeight - child.getTargetMinHeight();
                    } else {
                        consumedY = dy;
                    }
                    if (consumedY != 0) {
                        LayoutParams layoutParams = (LayoutParams) child.getTargetView().getLayoutParams();
                        layoutParams.height = layoutParams.height - consumedY;
                        child.getTargetView().setLayoutParams(layoutParams);
                        child.getTargetView().invalidate();
                        mCollapsingDelta = child.getTargetMaxHeight() - layoutParams.height;
                    }
                }
                Log.d(TAG, String.format("call onNestedPreScroll():%s consumedY = [%s] currentHeight = [%s] afterHeight = [%s]",
                        dy, consumedY, currentHeight, child.getTargetView().getLayoutParams().height));
                consumed[1] = consumedY;
            }
        }

        @Override
        public void onNestedScroll(CoordinatorLayout coordinatorLayout, CollapsingLayout child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
            if (dyUnconsumed < 0) {
                // If the scrolling view is scrolling down but not consuming, it's probably be at
                // the top of it's content

                int dy = dyUnconsumed;
                int consumedY;
                int currentHeight = child.getTargetView().getLayoutParams().height;
                if (child.getTargetMaxHeight() - currentHeight < -dy) {
                    consumedY = -(child.getTargetMaxHeight() - currentHeight);
                } else {
                    consumedY = dy;
                }
                if (consumedY != 0) {
                    LayoutParams layoutParams = (LayoutParams) child.getTargetView().getLayoutParams();
                    layoutParams.height = layoutParams.height - consumedY;
                    child.getTargetView().setLayoutParams(layoutParams);
                    child.getTargetView().invalidate();
                    mCollapsingDelta = child.getTargetMaxHeight() - layoutParams.height;
                }
                // Set the expanding flag so that onNestedPreScroll doesn't handle any events
                mSkipNestedPreScroll = true;
                Log.d(TAG, String.format("call onNestedScroll():%s consumedY = [%s] currentHeight = [%s] afterHeight = [%s]",
                        dy, consumedY, currentHeight, child.getTargetView().getLayoutParams().height));
            } else {
                // As we're no longer handling nested scrolls, reset the skip flag
                mSkipNestedPreScroll = false;
                Log.d(TAG, String.format("call onNestedScroll(): %s", dyUnconsumed));
            }
        }

        @Override
        public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, CollapsingLayout child, View target) {
            // Reset the flags
            mSkipNestedPreScroll = false;
        }
    }

    public static class ScrollingViewBehavior extends CoordinatorLayout.Behavior<View> {
        private static final String TAG = "Collapsing-ViewBehavior";

        public ScrollingViewBehavior() {
        }

        public ScrollingViewBehavior(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
            return dependency instanceof CollapsingLayout;
        }

        @Override
        public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
            final CoordinatorLayout.Behavior behavior =
                    ((CoordinatorLayout.LayoutParams) dependency.getLayoutParams()).getBehavior();
            if (behavior instanceof Behavior) {
                int offset = dependency.getHeight() - ((Behavior) behavior).mCollapsingDelta + parent.getTop();
                Log.d(TAG, String.format("call onDependentViewChanged(): %s %s %s",
                        dependency.getHeight(), ((Behavior) behavior).mCollapsingDelta, parent.getTop()));
//                ViewCompat.offsetTopAndBottom(child, offset);
                CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
                layoutParams.topMargin = offset;
                child.setLayoutParams(layoutParams);
                child.invalidate();
            }
            return false;
        }
    }
}
