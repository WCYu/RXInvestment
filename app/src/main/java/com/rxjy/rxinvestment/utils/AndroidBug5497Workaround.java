package com.rxjy.rxinvestment.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

/**
 * Created by 阿禹 on 2018/7/3.
 */

public class AndroidBug5497Workaround {
    public static void assistActivity(Activity activity) {
        new AndroidBug5497Workaround(activity);
    }
    private View mChildOfContent;
    /*保存改变高度前的高度*/
    private int usableHeightPrevious;
    private FrameLayout.LayoutParams frameLayoutParams;
    private Activity activity;
    private AndroidBug5497Workaround(final Activity activity) {
        this.activity = activity;
        FrameLayout content = (FrameLayout) activity.findViewById(android.R.id.content);
        mChildOfContent = content.getChildAt(0);
        //整体布局监听：当软键盘打开，关闭，虚拟键盘显示，隐藏时都会调用该方法
        mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                possiblyResizeChildOfContent();
            }
        });
        frameLayoutParams = (FrameLayout.LayoutParams) mChildOfContent.getLayoutParams();
    }

    private void possiblyResizeChildOfContent() {
        //activity有效高度
        int usableHeightNow = computeUsableHeight();
        //有效高度与之前的有效高度不等时，则重新设置高度
        if (usableHeightNow != usableHeightPrevious) {
            //可能是因为用了沉浸式，所以在有效高度上需要加上状态栏的高度
            //如果你发现这样写高度不对，可以去掉状态栏高度试试
            frameLayoutParams.height = usableHeightNow + getStatusHeight(activity);
            mChildOfContent.requestLayout();
            //保存本次有效高度
            usableHeightPrevious = usableHeightNow;
        }
    }

    private int computeUsableHeight() {
        Rect r = new Rect();
        mChildOfContent.getWindowVisibleDisplayFrame(r);
        return r.bottom - r.top;// 全屏模式下： return r.bottom
    }
    /**
     * 获得状态栏的高度
     *
     * @param context
     * @return
     */
    public static int getStatusHeight(Context context)
    {

        int statusHeight = -1;
        try
        {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return statusHeight;
    }
}
