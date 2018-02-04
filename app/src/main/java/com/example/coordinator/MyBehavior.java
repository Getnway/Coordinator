package com.example.coordinator;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Author: getnway on 2018-01-23.
 * Email: getnway@gmail.com
 */
public class MyBehavior extends CoordinatorLayout.Behavior<View> {
    private static final String TAG = "MyBehavior";

    public MyBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        Log.d(TAG, String.format("call layoutDependsOn(): child = [%s], dependency = [%s]", child, dependency));
        return true;//super.layoutDependsOn(parent, child, dependency);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        Log.d(TAG, String.format("call onDependentViewChanged(): child = [%s], dependency = [%s]", child, dependency));
        return super.onDependentViewChanged(parent, child, dependency);
    }


    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        Log.e(TAG, String.format("call onStartNestedScroll(): directTargetChild = [%s], target = [%s], nestedScrollAxes = [%s]",
                directTargetChild, target, nestedScrollAxes));
        return true;
//        return super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
//        consumed[1] = dy;
        Log.e(TAG, String.format("call onNestedPreScroll(): target = [%s], dx = [%s], dy = [%s], consumed = [%s %s]", target, dx, dy, consumed[0], consumed[1]));
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        Log.e(TAG, String.format("call onNestedScroll(): target = [%s], dxConsumed = [%s], dyConsumed = [%s], dxUnconsumed = [%s], dyUnconsumed = [%s]",
                target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed));
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target) {
        Log.e(TAG, String.format("call onStopNestedScroll(): child = [%s], target = [%s]", child, target));
        super.onStopNestedScroll(coordinatorLayout, child, target);
    }
}
