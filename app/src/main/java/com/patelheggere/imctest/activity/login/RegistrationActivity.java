package com.patelheggere.imctest.activity.login;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.core.app.ActivityCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.patelheggere.imctest.R;
import com.patelheggere.imctest.activity.home.MainActivity;
import com.patelheggere.imctest.base.BaseActivity;
import com.patelheggere.imctest.data.DBManager;
import com.patelheggere.imctest.data.DatabaseHelper;
import com.patelheggere.imctest.location.GPSTracker;
import com.patelheggere.imctest.models.UserDetails;
import com.patelheggere.imctest.utils.AppUtils;
import com.patelheggere.imctest.utils.SharedPrefsHelper;


import static com.patelheggere.imctest.utils.AppUtils.Constants.FIRST_TIME;
import static com.patelheggere.imctest.utils.AppUtils.Constants.TWO_SECOND;


public class RegistrationActivity extends BaseActivity {

    private static final String TAG = "RegistrationActivity";
    private ActionBar mActionBar;
    private TextInputEditText mTextInputEditTextFName, mTextInputEditTextLName, mTextInputEditTextEmailLogin, mTextInputEditTextEmail, mTextInputEditTextPwd, mTextInputEditTextPwdLogin;
    private Button mRegisterSubmit, mButtonLoginSubmit, mButtonLogin, mButtonRegister;
    private View mRegisterView, mLoginView;
    private ProgressBar mProgressBar;

    private GPSTracker mGpsTracker;
    private DBManager mDbManager;
    private String[] PERMISSIONS = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    int PERMISSION_ALL = 1;

    @Override
    protected int getContentView() {
        return R.layout.activity_registration;
    }

    @Override
    protected void initView() {
        mActionBar = getSupportActionBar();
        mActionBar.setTitle(getString(R.string.login));
        mTextInputEditTextEmail = findViewById(R.id.et_email);
        mTextInputEditTextFName = findViewById(R.id.et_name);
        mTextInputEditTextLName = findViewById(R.id.et_lname);
        mTextInputEditTextEmailLogin = findViewById(R.id.et_email_login);

        mProgressBar = findViewById(R.id.progress_bar);

        mTextInputEditTextPwd = findViewById(R.id.et_pwd);
        mTextInputEditTextPwdLogin = findViewById(R.id.et_pwd_login);

        mRegisterSubmit = findViewById(R.id.btn_register_submit);
        mButtonRegister = findViewById(R.id.btn_register);
        mButtonLogin = findViewById(R.id.btn_login);
        mButtonLoginSubmit = findViewById(R.id.btn_login_submit);
        mRegisterView = findViewById(R.id.reg_lyt);
        mLoginView = findViewById(R.id.log_lyt);
        mDbManager = new DBManager(context);
    }


    @Override
    protected void initData() {
        if (!AppUtils.hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        } else {
            mGpsTracker = new GPSTracker(RegistrationActivity.this);
        }
    }

    @Override
    protected void initListener() {

        mRegisterSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitDetails();
            }
        });

        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRegisterView.setVisibility(View.GONE);
                mLoginView.setVisibility(View.VISIBLE);
                mActionBar.setTitle(getString(R.string.login));
            }
        });

        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!AppUtils.hasPermissions(RegistrationActivity.this, PERMISSIONS)) {
                    ActivityCompat.requestPermissions(RegistrationActivity.this, PERMISSIONS, PERMISSION_ALL);
                } else {
                    mGpsTracker = new GPSTracker(RegistrationActivity.this);
                    mRegisterView.setVisibility(View.VISIBLE);
                    mLoginView.setVisibility(View.GONE);
                    mActionBar.setTitle(getString(R.string.registration));
                }
            }
        });

        mButtonLoginSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitLoginDetails();
            }
        });


    }

    private void check() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mGpsTracker.getLatitude() != 0) {
                    mRegisterSubmit.setEnabled(true);
                    mRegisterSubmit.setClickable(true);
                } else {
                    check();
                }
            }
        }, TWO_SECOND);
    }

    private void submitLoginDetails() {
        mDbManager = new DBManager(RegistrationActivity.this);
        mDbManager.open();
        if (mTextInputEditTextEmailLogin.getText() == null || mTextInputEditTextEmailLogin.getText().toString().trim().length() == 0) {
            mTextInputEditTextEmailLogin.setError(getString(R.string.email_correct));
            return;
        }
        if (mTextInputEditTextPwdLogin.getText() == null || mTextInputEditTextPwdLogin.getText().toString().length() < 3) {
            mTextInputEditTextPwdLogin.setError(getString(R.string.enter_pwd));
            return;
        }
        if (mTextInputEditTextEmailLogin.getText() != null) {
            if (!(Patterns.EMAIL_ADDRESS.matcher(mTextInputEditTextEmailLogin.getText()).matches())) {
                mTextInputEditTextEmailLogin.setError(getString(R.string.email_correct));
                return;
            }
        }
        mButtonLoginSubmit.setEnabled(false);
        mButtonLoginSubmit.setClickable(false);
        mProgressBar.setVisibility(View.VISIBLE);


        Cursor cursor = mDbManager.fetch(mTextInputEditTextEmailLogin.getText().toString(), mTextInputEditTextPwdLogin.getText().toString());
        if (cursor.getCount() > 0) {
            String email = cursor.getString(cursor.getColumnIndex(DatabaseHelper.EMAIL));
            String lat = cursor.getString(cursor.getColumnIndex(DatabaseHelper.LAT));
            String lon = cursor.getString(cursor.getColumnIndex(DatabaseHelper.LON));
            String fname = cursor.getString(cursor.getColumnIndex(DatabaseHelper.FNAME));
            String lname = cursor.getString(cursor.getColumnIndex(DatabaseHelper.LName));
            SharedPrefsHelper.getInstance().save(DatabaseHelper.EMAIL, email);
            SharedPrefsHelper.getInstance().save(DatabaseHelper.LAT, lat);
            SharedPrefsHelper.getInstance().save(DatabaseHelper.LON, lon);
            SharedPrefsHelper.getInstance().save(DatabaseHelper.FNAME, fname);
            SharedPrefsHelper.getInstance().save(DatabaseHelper.LName, lname);
            SharedPrefsHelper.getInstance().save(FIRST_TIME, false);

            mDbManager.close();
            mGpsTracker.stopUsingGPS();
            mProgressBar.setVisibility(View.GONE);
            startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
            overridePendingTransition(R.anim.enter, R.anim.exit);
            finish();

        } else {
            Toast.makeText(RegistrationActivity.this, getString(R.string.invalid), Toast.LENGTH_LONG).show();
            mButtonLoginSubmit.setEnabled(true);
            mButtonLoginSubmit.setClickable(true);
            mProgressBar.setVisibility(View.GONE);
        }


    }

    private void showToast(String string) {
        Toast.makeText(RegistrationActivity.this, string, Toast.LENGTH_LONG).show();
    }

    private void submitDetails() {
        try {
            if(mGpsTracker.getLatitude()==0)
            {
                mGpsTracker = new GPSTracker(this);
                showToast(getString(R.string.no_gps));
                return;
            }
            if (mTextInputEditTextFName.getText() == null || mTextInputEditTextFName.getText().toString().trim().length() < 3) {
                mTextInputEditTextFName.setError(getString(R.string.name_required));
                return;
            }
            if (mTextInputEditTextLName.getText() == null || mTextInputEditTextLName.getText().toString().trim().length() == 0) {
                mTextInputEditTextLName.setError(getString(R.string.lnmae_correct));
                return;
            }
            if (mTextInputEditTextPwd.getText() == null || mTextInputEditTextPwd.getText().toString().trim().length() < 3) {
                mTextInputEditTextPwd.setError(getString(R.string.enter_pwd));
                return;
            }
            if (mTextInputEditTextEmail.getText() == null || mTextInputEditTextEmail.getText().toString().trim().length() == 0) {
                mTextInputEditTextEmail.setError(getString(R.string.email_correct));
                return;
            }
            if (mTextInputEditTextEmail.getText() != null) {
                if (!(Patterns.EMAIL_ADDRESS.matcher(mTextInputEditTextEmail.getText()).matches())) {
                    mTextInputEditTextEmail.setError(getString(R.string.email_correct));
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        UserDetails userDetails = new UserDetails();
        userDetails.setEmail(mTextInputEditTextEmail.getText().toString());
        userDetails.setFname(mTextInputEditTextFName.getText().toString());
        userDetails.setLname(mTextInputEditTextLName.getText().toString());
        userDetails.setPwd(mTextInputEditTextPwd.getText().toString());
        userDetails.setLat(mGpsTracker.getLatitude() + "");
        userDetails.setLon(mGpsTracker.getLongitude() + "");
        mDbManager.open();
        long val = mDbManager.insert(userDetails);
        if (val > 0) {
            SharedPrefsHelper.getInstance().save(FIRST_TIME, false);
            SharedPrefsHelper.getInstance().save(DatabaseHelper.EMAIL, userDetails.getEmail());
            SharedPrefsHelper.getInstance().save(DatabaseHelper.LAT, userDetails.getLat());
            SharedPrefsHelper.getInstance().save(DatabaseHelper.LON, userDetails.getLon());
            SharedPrefsHelper.getInstance().save(DatabaseHelper.FNAME, userDetails.getFname());
            SharedPrefsHelper.getInstance().save(DatabaseHelper.LName, userDetails.getLname());
            mDbManager.close();
            mGpsTracker.stopUsingGPS();
            startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
            overridePendingTransition(R.anim.enter, R.anim.exit);
            finish();
        }
        else{
            showToast(getString(R.string.unable_to_store_user));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PERMISSION_ALL && resultCode == RESULT_OK) {
            mGpsTracker = new GPSTracker(RegistrationActivity.this);
            mRegisterView.setVisibility(View.VISIBLE);
            mLoginView.setVisibility(View.GONE);
            mActionBar.setTitle(getString(R.string.registration));
            if (mGpsTracker.getLatitude() == 0) {
                mRegisterSubmit.setEnabled(false);
                mRegisterSubmit.setClickable(false);
                check();
            }
        } else {
            Toast.makeText(RegistrationActivity.this, "Permission required", Toast.LENGTH_LONG).show();
        }
    }
}
