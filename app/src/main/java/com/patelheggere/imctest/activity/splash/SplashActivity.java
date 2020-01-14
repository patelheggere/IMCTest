package com.patelheggere.imctest.activity.splash;

import android.Manifest;
import android.content.Intent;
import android.os.Handler;
import android.view.WindowManager;

import com.patelheggere.imctest.R;
import com.patelheggere.imctest.activity.login.RegistrationActivity;
import com.patelheggere.imctest.activity.home.MainActivity;
import com.patelheggere.imctest.base.BaseActivity;
import com.patelheggere.imctest.utils.SharedPrefsHelper;

import static com.patelheggere.imctest.utils.AppUtils.Constants.FIRST_TIME;
import static com.patelheggere.imctest.utils.AppUtils.Constants.THREE_SECOND;

public class SplashActivity extends BaseActivity {

    @Override
    protected int getContentView() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return R.layout.activity_splash2;
    }

    @Override
    protected void initView() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if(SharedPrefsHelper.getInstance().get(FIRST_TIME, true)) {
                    Intent i = new Intent(SplashActivity.this, RegistrationActivity.class);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                    startActivity(i);
                }
                else {
                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                    startActivity(i);
                }
                finish();

            }



        }, THREE_SECOND);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }
}
