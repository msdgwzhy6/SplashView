package com.splashview;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;


/**
 * Created by Administrator on 2017/8/10.
 * 闪屏广告
 *
 * @auther madreain
 */

public class SplashDialog extends Dialog {

    private Context mContext;
    //展示的
    AdvertisingImgModel advertisingImgModel;

    //背景照片
    private ImageView img_background;
    //倒计时
    private CountDownView count_down_view;

    private OnSplashDetailClickListener onSplashDetailClickListener;

    public SplashDialog(Context context, AdvertisingImgModel advertisingImgModel) {
        super(context, R.style.ADDialog);
        mContext = context;
        this.advertisingImgModel = advertisingImgModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_dialog);
        full(true);
        initView();
    }

    /**
     * @param enable false 显示，true 隐藏
     */
    private void full(boolean enable) {
        WindowManager.LayoutParams p = this.getWindow().getAttributes();
        if (enable) {

            p.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;//|=：或等于，取其一

        } else {
            p.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);//&=：与等于，取其二同时满足，     ~ ： 取反

        }
        getWindow().setAttributes(p);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    private void initView() {

        img_background = (ImageView) findViewById(R.id.img_background);
        Glide.with(mContext).load(advertisingImgModel.getImgurl()).into(img_background);
        //广告倒计时
        count_down_view = (CountDownView) findViewById(R.id.count_down_view);
        count_down_view.setCountDownTimerListener(new CountDownView.CountDownTimerListener() {
            @Override
            public void onStartCount() {

            }

            @Override
            public void onChangeCount(int second) {

            }

            @Override
            public void onFinishCount() {
                dismiss();
            }
        });
        //启动倒计时
        count_down_view.start();
        //点击跳过按钮
        count_down_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        //点击事件
        img_background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSplashDetailClickListener != null) {
                    onSplashDetailClickListener.onSplashDetailClick(advertisingImgModel);
                }
                dismiss();
            }
        });

    }


    public void setOnSplashDetailClickListener(OnSplashDetailClickListener onSplashDetailClickListener) {
        this.onSplashDetailClickListener = onSplashDetailClickListener;
    }

    public interface OnSplashDetailClickListener {
        void onSplashDetailClick(AdvertisingImgModel advertisingImgModel);
    }

}
