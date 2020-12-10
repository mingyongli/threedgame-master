package com.ws3dm.app.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;

/**
 * Project: CircleProgressView
 * Author: Guoger
 * Mail: 505917699@qq.com
 * Created time: 2016/4/17/16 10:39.
 */
/**
 * Project: CircleProgressView
 * Author: Guoger
 * Mail: 505917699@qq.com
 * Created time: 2016/4/18/16 11:11.
 */
public class CircleConstant {
    //Color
    public static final int DEFAULT_ROUND_COLOR = Color.parseColor("#2E666666");
    public static final int DEFAULT_ROUND_PROGRESS_COLOR = Color.parseColor("#FFFF0000");
    public static final int DEFAULT_DESP_TEXT_COLOR = Color.parseColor("#FF000000");
    public static final int DEFAULT_VALUE_TEXT_COLOR = Color.parseColor("#FFFF0000");

    //Number
    public static final int DEFAULT_CIRCLE_STROKE_WIDTH = 5;
    public static final int DEFAULT_VALUE_TEXT_SIZE = 25;
    public static final int DEFAULT_DESP_TEXT_SIZE = 15;
    public static final int DEFAULT_ANIM_DURATION = 1000 * 1;

    //Style
    public static final int STYLE_STROKE = 0;
    public static final int STYLE_FILL = 1;

    //boolean
    public static final boolean DEFAULT_VALUE_TEXT_IS_DISPLAYABLE = true;
    public static final boolean DEFAULT_DESP_TEXT_IS_DISPLAYABLE = true;

    //String
    public static final String DEFAULT_VALUE_TEXT = "0.0";
    public static final String DEFAULT_DESP_TEXT = "分";
}