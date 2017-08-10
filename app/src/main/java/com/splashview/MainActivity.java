package com.splashview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView txt;
    SplashDialog splashDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        txt = (TextView) findViewById(R.id.txt);

        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdvertisingImgModel advertisingImgModel = new AdvertisingImgModel(1, "http://bmob-cdn-10899.b0.upaiyun.com/2017/05/09/34b6d85c406894f3803d949a78c4546e.jpg");
                splashDialog = new SplashDialog(MainActivity.this, advertisingImgModel);
                splashDialog.setOnSplashDetailClickListener(new SplashDialog.OnSplashDetailClickListener() {
                    @Override
                    public void onSplashDetailClick(AdvertisingImgModel advertisingImgModel) {
                        Toast.makeText(MainActivity.this, "跳转到广告的详情页", Toast.LENGTH_SHORT).show();
                    }
                });
                splashDialog.show();
            }
        });
    }
}
