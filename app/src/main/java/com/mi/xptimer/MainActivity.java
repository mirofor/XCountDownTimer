package com.mi.xptimer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.mi.widget.xcountdown.CountDownView;


public class MainActivity extends AppCompatActivity implements CountDownView.OnXcountDownClickListener, CountDownView.OnXcountDownTickListener {

    CountDownView mBtnTimerOne;
    CountDownView mBtnTimerTwo;
    CountDownView mBtnTimerThree;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initEvent();
    }

    private void initView() {
        mBtnTimerOne = (CountDownView) findViewById(R.id.id_button_green);
        mBtnTimerTwo = (CountDownView) findViewById(R.id.id_button_yellow);
        mBtnTimerThree = (CountDownView) findViewById(R.id.id_button_normal);

        mBtnTimerOne.setText("点击获取验证码");
        mBtnTimerTwo.setText("立即获取");
        mBtnTimerThree.setText("获取验证码");
    }

    private void initEvent() {

        mBtnTimerOne.setXcountDownListener(this);
        mBtnTimerTwo.setXcountDownListener(this);
        mBtnTimerThree.setXcountDownListener(this);

        mBtnTimerOne.setXcountDownTickListener(this);

        mBtnTimerOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBtnTimerOne.start();
            }
        });
        mBtnTimerTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBtnTimerTwo.start();
            }
        });
        mBtnTimerThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBtnTimerThree.start();
            }
        });
    }

    /**
     * 倒计时结束后的处理，重新发送...
     */
    @Override
    public void onResendCode(View view) {

        Log.e("倒计时", "点击重发...");
        Toast.makeText(getApplicationContext(), "验证码已发送！", Toast.LENGTH_SHORT).show();
        ((CountDownView) view).start();
    }

    @Override
    public void onInitSendCode(View view) {
        Log.e("倒计时", "第一次点击...");
        ((CountDownView) view).start();
    }

    @Override
    public void onSendCodeTick(long restMills) {
        String show = restMills / 1000 + "秒后重新发送";
        mBtnTimerOne.setText(show);

    }
}
