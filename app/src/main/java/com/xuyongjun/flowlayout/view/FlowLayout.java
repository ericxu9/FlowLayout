package com.xuyongjun.flowlayout.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * ============================================================
 * 作 者 : XYJ
 * 版 本 ： 1.0
 * 创建日期 ： 2016/8/10 8:57
 * 描 述 ：流式布局
 * 修订历史 ：
 * ============================================================
 **/
public class FlowLayout extends ViewGroup {
    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);

        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        //wrap_content 也就是AT_MOST,需要自己去设置宽高
        int width = 0;
        int height = 0;

        //记录每一行元素的宽度和高度
        int lineWidth = 0;
        int lineHeight = 0;

        //得到布局中的所有元素
        int childCount = getChildCount();

        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            //测量子View的宽和高
            measureChild(child, widthMeasureSpec, heightMeasureSpec);

            //得到LayoutParams，我们指定的是MarginLayoutParams
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

            //获取子view占据的宽和高
            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;

            //换行
            if (lineWidth + childWidth > sizeWidth - getPaddingLeft() - getPaddingRight()) {
                //对比得到最大宽度
                width = Math.max(lineWidth, width);
                //重置lineWidth
                lineWidth = childWidth;

                //记录的是上一行的行高，并没有记录当前行的宽和高，我们需要通过判断最后一个veiw来设置
                height += lineHeight;
                lineHeight = childHeight;
            } else {//未换行
                //叠加行宽
                lineWidth += childWidth;
                //得到当前行最大高度
                lineHeight = Math.max(lineHeight, childHeight);
            }

            //最后一个控件,不管换没换行都没记录当前行的宽和高，我们要记录下
            if (i == childCount - 1) {
                width = Math.max(lineWidth, width);
                height += lineHeight;
            }

        }

        //自己设置宽度和高度
        setMeasuredDimension(modeWidth == MeasureSpec.EXACTLY ? sizeWidth : width + getPaddingRight() + getPaddingLeft(),
                modeHeight == MeasureSpec.EXACTLY ? sizeHeight : height + getPaddingBottom() + getPaddingTop());


        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    /**
     * 存放所有的子View
     */
    private List<List<View>> mAllViews = new ArrayList<List<View>>();

    /**
     * 每一行的高度
     */
    private List<Integer> mLineHeight = new ArrayList<Integer>();

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        //可能会调用多次
        mAllViews.clear();
        mLineHeight.clear();

        //获取当前ViewGroup的宽度
        int width = getWidth();

        int lineWidth = 0;
        int lineHeight = 0;

        //存放每一行的子View
        List<View> lineViews = new ArrayList<View>();

        int childCount = getChildCount();

        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);

            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            if (lineWidth + childWidth + lp.leftMargin + lp.rightMargin > width - getPaddingLeft() -getPaddingRight()) {

                //记录lineHeight
                mLineHeight.add(lineHeight);

                //记录当前行的View
                System.out.println("lineVIews:" + lineViews.size());
                mAllViews.add(lineViews);

                //重置行宽和行高
                lineWidth = 0;
                lineHeight = childHeight + lp.bottomMargin + lp.topMargin;

                //重置View集合
                lineViews = new ArrayList<>();
            }

            lineWidth += childWidth + lp.leftMargin + lp.rightMargin;
            lineHeight = Math.max(lineHeight, childHeight + lp.bottomMargin + lp.topMargin);

            lineViews.add(child);
        }//for end

        //处理最后一行
        mLineHeight.add(lineHeight);
        mAllViews.add(lineViews);

        //记录View的位置
        int left = 0 + getPaddingLeft();
        int top = 0 + getPaddingTop();

        //行数
        int lineNum = mAllViews.size();

        for (int i = 0; i < lineNum; i++) {
            //当前行的所有View
            lineViews = mAllViews.get(i);

            lineHeight = mLineHeight.get(i);

            for (int j = 0; j < lineViews.size(); j++) {
                View child = lineViews.get(j);
                //判断child的状态
                if (child.getVisibility() == View.GONE) {
                    continue;
                }

                MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

                int lc = left + lp.leftMargin;
                int tc = top + lp.topMargin;
                int rc = lc + child.getMeasuredWidth();
                int bc = tc + child.getMeasuredHeight();

                //为子View进行布局
                child.layout(lc, tc, rc, bc);

                left += child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            }
            left = 0 + getPaddingLeft();
            top += lineHeight;
        }

    }

    /**
     * 与当前ViewGroup对应的LayoutParams,也是子View的获取得到的
     *
     * @param attrs
     * @return
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }
}
