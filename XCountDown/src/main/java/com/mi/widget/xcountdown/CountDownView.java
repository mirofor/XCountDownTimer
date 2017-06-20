package com.mi.widget.xcountdown;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TextSwitcher;
import android.widget.TextView;


public class CountDownView extends TextSwitcher
        implements TextSwitcher.ViewFactory, View.OnClickListener {

    private int totalSeconds;    //默认60s
    private int gap;             //间隙
    private String tipString;    //提示文字

    private float text_size;     //文字大小
    private int text_color;      //文字颜色

    private int animIn;      //进入时动画
    private int animOut;      //移除时动画

    private int bgEnableResId;   //默认背景
    private int bgDisableResId;  //不可点击时的背景

    private CountDownTimer timer;
    private OnXcountDownClickListener mListener;
    private OnXcountDownTickListener mTickListener;

    private boolean countDownFinish; //倒计时是否结束

    //倒计时结束时默认的提示文字，暂时定义为此常量值
    private static final String DEFAULT_TIP_TEXT = "点击重发";

    public CountDownView(Context context) {
        this(context, null);
    }

    public CountDownView(Context context, AttributeSet attrs) {
        super(context, attrs);

        checkAttrs(attrs);
        setBackgroundResource(bgEnableResId);

        configSwitcher();
        configTimer();
        setOnClickListener(this);
    }

    /**
     * 检查自定义属性
     */
    private void checkAttrs(AttributeSet attrs) {

        float defaultTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14.0f,
                Resources.getSystem().getDisplayMetrics());

        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.XCountDownTextView);

        text_size = ta.getDimension(R.styleable.XCountDownTextView_text_size, defaultTextSize);
        text_color = ta.getColor(R.styleable.XCountDownTextView_text_color, Color.WHITE);

        bgEnableResId = ta.getResourceId(R.styleable.XCountDownTextView_enable_background,
                R.drawable.countdown_btn_bg_normal);
        bgDisableResId = ta.getResourceId(R.styleable.XCountDownTextView_disable_background,
                R.drawable.countdown_btn_bg_disable);

        animIn = ta.getResourceId(R.styleable.XCountDownTextView_anim_in, android.R.anim.fade_in);
        animOut = ta.getResourceId(R.styleable.XCountDownTextView_anim_out,
                android.R.anim.fade_out);

        totalSeconds = ta.getInteger(R.styleable.XCountDownTextView_total_time, 60);
        gap = ta.getInteger(R.styleable.XCountDownTextView_gap_time, 1);
        tipString = ta.getString(R.styleable.XCountDownTextView_tip_text);

        checkAttrs();

        ta.recycle();
    }

    /**
     * 验证 自定义属性有效性
     * 非法将使用默认值
     */
    private void checkAttrs() {
        if (TextUtils.isEmpty(tipString)) {
            tipString = DEFAULT_TIP_TEXT;
        }

        if (gap <= 0) {
            gap = 1;
        }

        if (totalSeconds < gap) {
            totalSeconds = 60;
        }
    }

    /**
     * 开始计时
     */
    public void start() {
        timer.start();
        setBackgroundResource(bgDisableResId);
        setClickable(false);
    }

    /**
     * 取消定時器
     */
    public void cancel() {
        timer.cancel();
        timer.onFinish();
        setClickable(true);
    }

    /**
     * 配置 CountDownTimer
     * 默认60s 间隙 1s
     */
    private void configTimer() {
        if (null == timer) {
            timer = new CountDownTimer(totalSeconds * 1000, gap * 1000) {
                @Override
                public void onTick(long l) {
                    onGapCallback(l);
                }

                @Override
                public void onFinish() {
                    onCountDownFinish();
                }
            };
        }
    }

    /**
     * 计时完毕回调
     */
    private void onCountDownFinish() {

        setClickable(true);
        countDownFinish = true;
        String show = tipString;
        setText(show);
        setBackgroundResource(bgEnableResId);
    }

    /**
     * 间隙的回调
     *
     * @param restMills 剩余时间
     */
    private void onGapCallback(long restMills) {

        setClickable(false);
        countDownFinish = false;
        if (mTickListener != null) {
            mTickListener.onSendCodeTick(restMills);
        } else {
            String show = restMills / (gap * 1000) + "s";
            setText(show);
        }
    }

    /**
     * 配置 TextSwitcher 切换动画
     */
    private void configSwitcher() {
        setFactory(this);
        setInAnimation(getContext(), animIn);
        setOutAnimation(getContext(), animOut);
    }

    public int px2sp(float pxValue) {
        final float fontScale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static int sp2px(float spValue) {
        final float fontScale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    @Override
    public View makeView() {
        TextView textView = new TextView(getContext());
        textView.setMaxLines(1);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(text_color);
        textView.setTextSize(px2sp(text_size));
        int padding =
                TypedValue.complexToDimensionPixelSize(10, Resources.getSystem().getDisplayMetrics());
        textView.setPadding(padding, padding, padding, padding);

        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        textView.setLayoutParams(params);
        return textView;
    }

    @Override
    public void onClick(View view) {
        if (mListener != null) {
            if (countDownFinish) { //重发
                mListener.onResendCode(view);
            } else {//第一次点击发送
                mListener.onInitSendCode(view);
            }
        }
    }

    /**
     * 重新发送验证码
     */
    public interface OnXcountDownClickListener {
        void onResendCode(View view);

        void onInitSendCode(View view);
    }

    public void setXcountDownListener(OnXcountDownClickListener listener) {
        this.mListener = listener;
    }

    public interface OnXcountDownTickListener {
        void onSendCodeTick(long restMills);
    }

    public void setXcountDownTickListener(OnXcountDownTickListener tickListener) {
        this.mTickListener = tickListener;
    }
}
