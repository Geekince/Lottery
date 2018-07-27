package com.wyzk.lottery.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;

import com.wyzk.lottery.R;
import com.wyzk.lottery.constant.IConst;
import com.wyzk.lottery.utils.ACache;
import com.wyzk.lottery.utils.BuildManager;

/**
 * 闪屏页面
 */
public class SplashActivity extends LotteryBaseActivity {

    private final static int ANIMATION_DURATION = 1000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        BuildManager.setStatusTransOther(this);
        loadAnimation();
    }

    private void loadAnimation() {
        RelativeLayout splash = (RelativeLayout) findViewById(R.id.splash);

        AlphaAnimation aa = new AlphaAnimation(0.8f, 1);
        aa.setDuration(ANIMATION_DURATION);
        aa.setInterpolator(new LinearInterpolator());
        aa.setFillAfter(true);

        splash.startAnimation(aa);

        aa.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                String token = ACache.get(SplashActivity.this).getAsString(IConst.TOKEN);
                Log.d("token", "" + token);
                if (TextUtils.isEmpty(token)) {
                    toActivity(LoginActivity.class);
                } else {
                    toActivity(MainActivity.class);
                }
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}