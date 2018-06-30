package com.dyhdyh.plugin.eleme.service;

import android.accessibilityservice.AccessibilityService;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

/**
 * @author dengyuhan
 *         created 2018/6/30 17:36
 */
public class ElemeAccessibilityService extends AccessibilityService {
    private Handler mHandler = new Handler();

    @Override
    public void onAccessibilityEvent(final AccessibilityEvent event) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return;
        }

        int eventType = event.getEventType();
        //根据事件回调类型进行处理
        switch (eventType) {
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                final AccessibilityNodeInfo window = getRootInActiveWindow();
                if (window == null) {
                    return;
                }
                CharSequence className = event.getClassName();
                Log.d("--------->", className + "-->");
                if (className.equals("me.ele.order.ui.rate.OrderRateActivity")) {

                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {//评价页面
                            List<AccessibilityNodeInfo> awesome = window.findAccessibilityNodeInfosByText("超赞");
                            if (awesome != null && !awesome.isEmpty()) {
                                AccessibilityNodeInfo nodeInfo = awesome.get(0);
                                nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                Log.d("----------->", nodeInfo.getText() + "----->");
                            }
                            List<AccessibilityNodeInfo> anonymous = window.findAccessibilityNodeInfosByText("匿名评价");
                            if (anonymous != null && !anonymous.isEmpty()) {
                                AccessibilityNodeInfo nodeInfo = anonymous.get(0);
                                nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                Log.d("----------->", nodeInfo.getText() + "----->");
                            }
                            final List<AccessibilityNodeInfo> star = window.findAccessibilityNodeInfosByViewId("me.ele:id/five");
                            if (star != null && !star.isEmpty()) {
                                AccessibilityNodeInfo first = star.get(0);
                                first.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                mHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        final List<AccessibilityNodeInfo> newStar = window.findAccessibilityNodeInfosByViewId("me.ele:id/five");
                                        if (newStar != null) {
                                            for (int i = 0; i < newStar.size(); i++) {
                                                newStar.get(i).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                            }
                                            setCommentText();
                                        }
                                    }
                                }, 400);
                            }
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    List<AccessibilityNodeInfo> submit = window.findAccessibilityNodeInfosByText("提交评价");
                                    if (submit != null && !submit.isEmpty()) {
                                        AccessibilityNodeInfo nodeInfo = submit.get(0);
                                        nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                        event.getSource().performAction(AccessibilityService.GLOBAL_ACTION_BACK);
                                        Log.d("----------->", nodeInfo.getText() + "----->");
                                    }
                                }
                            }, 400);
                        }
                    }, 400);
                } else if (className.equals("me.ele.application.ui.home.HomeActivity")) {
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            List<AccessibilityNodeInfo> list = window.findAccessibilityNodeInfosByText("评价得");
                            if (list != null && !list.isEmpty()) {
                                AccessibilityNodeInfo nodeInfo = list.get(0);
                                nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            }
                        }
                    }, 400);
                }
                break;
        }
    }

    @Override
    public void onInterrupt() {

    }

    private void setCommentText() {
        final AccessibilityNodeInfo window = getRootInActiveWindow();
        if (window != null) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    List<AccessibilityNodeInfo> comment = window.findAccessibilityNodeInfosByViewId("me.ele:id/edit_text");
                    if (comment != null && !comment.isEmpty()) {
                        Bundle arguments = new Bundle();
                        arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, "好评");
                        comment.get(0).performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
                    }
                }
            }, 400);
        }
    }

}
