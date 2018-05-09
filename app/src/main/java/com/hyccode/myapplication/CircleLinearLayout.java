package com.hyccode.myapplication;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;



/**
 * Created by hyc on 2018/5/6.
 */
public class CircleLinearLayout extends ViewGroup {

    private final String TAG = "CircleLinearLayout";

    private int radius;

    public CircleLinearLayout(Context context) {
        super(context);
    }

    public CircleLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.CircleLinearLayout, defStyleAttr, 0);
        int count = typedArray.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attNameId = typedArray.getIndex(i);
            switch (attNameId) {
                case R.styleable.CircleLinearLayout_circleRadius:
                    radius = typedArray.getDimensionPixelSize(attNameId, 10);
                    break;
            }
        }

        typedArray.recycle();
        Log.d(TAG, "radius = " + radius);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
//        return super.generateLayoutParams(attrs);  
        return new MarginLayoutParams(getContext(), attrs);
    }

    /**
     * 计算所有ChildView的宽度和高度，然后根据ChildView的计算结果设置自己的宽度和高度
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);  

        /**
         * 获取此ViewGroup上级容器为其推荐计算模式 
         */
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        /**
         * 获取此ViewGroup上级容器为其推荐的宽和高 
         */
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        //计算出所有childView的宽和高，（通过ViewGroup的measureChildren方法为其所有的孩子设置宽和高，此行执行完成后，childView的宽和高都已经正确的计算过了）  
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        /**
         * ViewGroup内子控件的宽度和高度 
         */
        int widthContent = 0;
        int heightContent = 0;

        int itemHeight = getChildAt(0).getMeasuredHeight();//单个childView的高度

        heightContent = (itemHeight + radius) * 2;
        widthContent = (itemHeight + radius) * 2;
//        Log.d(TAG + "onMeasure", "heightContent:"+heightContent);

        /**
         * 测量ViewGroup的宽高，如果为wrap_content就按照内容计算得到的宽高 
         */
        setMeasuredDimension((widthMode == MeasureSpec.EXACTLY) ? widthSize : widthContent, (heightMode == MeasureSpec.EXACTLY) ? heightSize : heightContent);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//        drawInHorizontal();  
        drawInCircle();

    }

    /**
     * 按照Circle的方式排列
     */
    private void drawInCircle() {
        int cCount = getChildCount();
        int lastW = 0;

        //圆心坐标  
        float[] circleCentre = {getWidth() / 2 * 1.0f, getHeight() / 2 * 1.0f};

        //每个占多少个弧度  
//        float oItem = 360/cCount*1.0f;  
        float oItem = (float) (2 * Math.PI / cCount * 1.0f);

        //cCount个坐标  
        float[][] xyPosition = new float[cCount][2];
        for (int i = 0; i < cCount; i++) {
            xyPosition[i] = Arith.getXYPoint(circleCentre, radius, oItem * (i));

            //x坐标  
            int xLabel = (int) xyPosition[i][0];
            //y坐标  
            int yLabel = (int) xyPosition[i][1];

            Log.d(TAG, "position : (" + xLabel + "," + yLabel + ")");
            View view = getChildAt(i);
            view.layout((int) (xLabel - view.getMeasuredWidth() / 2 * 1.0f), (int) (yLabel - view.getMeasuredHeight() / 2 * 1.0f), (int) (xLabel + view.getMeasuredWidth() / 2 * 1.0f), (int) (yLabel + view.getMeasuredHeight() / 2 * 1.0f));

        }

    }

    /**
     * 按照horizontal的方式排列
     */
    private void drawInHorizontal() {
        int cCount = getChildCount();
        int lastW = 0;
        for (int i = 0; i < cCount; i++) {
            View view = getChildAt(i);
            view.layout(lastW, 0, view.getWidth(), view.getHeight());
            lastW += view.getWidth();
            Log.i(TAG, "lastW = " + lastW);
        }
    }
}  