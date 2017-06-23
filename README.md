# XCountDownTimer
android下的简单倒计时组件。

[![License](https://img.shields.io/badge/license-Apache%202-green.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![Download](https://api.bintray.com/packages/brightmi/maven/xcountdown/images/download.svg) ](https://bintray.com/brightmi/maven/xcountdown/_latestVersion)


## 1. 如何添加

#### 在app目录下的build.gradle中添加依赖

```gradle
compile 'com.mi.widget:xcountdown:1.0.1'
```

## 2. XML中配置使用

#### 直接在XML文件中设置各属性信息

```
      <com.mi.widget.xcountdown.CountDownView
           android:id="@+id/id_button_normal"
           android:layout_width="150dp"
           android:layout_height="wrap_content"
           android:layout_marginTop="30dp"
           android:padding="10dp"
           app:disable_background="@drawable/countdown_btn_bg_disable"
           app:enable_background="@drawable/countdown_btn_bg_normal"
           app:gap_time="1"
           app:text_color="@android:color/white"
           app:text_size="16sp"
           app:tip_text="点击重发"
           app:total_time="30" />
```


**属性说明**

| 属性 | 类型 | 说明 |
|---|---|---|
| text_size | dimension | 文字大小 |
| text_color | color | 文字颜色 |
| tip_text | String | 倒计时结束后的提示文字,默认文字是："点击重发" |
| enable_background | reference | 默认背景 |
| disable_background | reference | 验证码View不可点击时的背景 |
| total_time | integer | 倒计时总时间，单位 秒 。默认60秒|
| gap_time | integer | 间隔时间。默认1秒变化一次 |
| anim_in | integer | 文字进入时动画效果,默认 android.R.anim.fade_in |
| anim_out | integer | 文字退出后动画效果，默认 android.R.anim.fade_out|


## 3. 实现CountDownView.OnXcountDownClickListener接口
> mTvSendCode.setXcountDownListener(this);

#### onInitSendCode() //第一次点击发送验证码的业务代码处理
#### onResendCode() //重发验证码处理

## 4. 发送验证码

> CountDownView mTvSendCode;
> mTvSendCode.start();
