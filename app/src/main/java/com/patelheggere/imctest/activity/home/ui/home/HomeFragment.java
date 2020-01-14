package com.patelheggere.imctest.activity.home.ui.home;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.patelheggere.imctest.R;
import com.patelheggere.imctest.base.BaseFragment;
import com.patelheggere.imctest.broadcast.MyNotificationPublisher;
import com.patelheggere.imctest.data.DBManager;
import com.patelheggere.imctest.data.DatabaseHelper;
import com.patelheggere.imctest.utils.AppUtils;
import com.patelheggere.imctest.utils.SharedPrefsHelper;

import java.util.Calendar;

import static com.patelheggere.imctest.utils.AppUtils.convertMilliToDate;


public class HomeFragment extends BaseFragment {
    private static final String TAG = "HomeFragment";
    private HomeViewModel homeViewModel;
    private Button mButtonPickTime;
    private DBManager mDbManager;
    private View mRootView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        mRootView = inflater.inflate(R.layout.fragment_home, container, false);
        initView();
        initListener();
        return mRootView;
    }

    private void initView() {
        mButtonPickTime = mRootView.findViewById(R.id.button_time);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                mButtonPickTime.setText(getString(R.string.pick_time));
            }
        });
    }

    private void initListener() {
        mButtonPickTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePick();
            }
        });
    }

    public void timePick(){
        final Calendar beforeTimeSet = Calendar.getInstance();
        final  int minHour = beforeTimeSet.get(Calendar.HOUR_OF_DAY);
        final int minMinute = beforeTimeSet.get(Calendar.MINUTE);

        TimePickerDialog mTimePickerDialog = new TimePickerDialog(mActivity, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                final Calendar mCalendar = Calendar.getInstance();
                Calendar now = Calendar.getInstance();

                boolean validTime = true;
                if (hourOfDay < minHour || (hourOfDay == minHour && minute < minMinute)) {
                    validTime = false;
                }
                mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                mCalendar.set(Calendar.MINUTE, minute);
                mCalendar.set(Calendar.SECOND, 0);
                mCalendar.set(Calendar.MILLISECOND, 0);

                long delay = mCalendar.getTimeInMillis()-now.getTimeInMillis();
                String scheduledTime = convertMilliToDate(mCalendar.getTimeInMillis());
                if(validTime) {
                    mDbManager = new DBManager(mActivity);
                    mDbManager.open();
                   long ins =  mDbManager.insertTime(SharedPrefsHelper.getInstance().get(DatabaseHelper.EMAIL).toString(), mCalendar.getTimeInMillis()+"");
                   if(ins>0) {
                       scheduleNotification(mActivity, delay, 1, scheduledTime);
                   }
                   else {
                       Toast.makeText(mActivity, R.string.not_able_to_store, Toast.LENGTH_LONG).show();
                   }
                }
                else
                {
                    Toast.makeText(mActivity, R.string.time_cant_less, Toast.LENGTH_LONG).show();
                }
            }
        }, minHour, minMinute, false);
        mTimePickerDialog.show();
    }

    public void scheduleNotification(Context context, long delay, int notificationId, String scheduledTime) {
        long futureInMillis = SystemClock.elapsedRealtime() + delay;


        Intent notificationIntent = new Intent(context, MyNotificationPublisher.class);
        notificationIntent.putExtra(AppUtils.Constants.TITLE, "IMC Event");
        notificationIntent.putExtra(AppUtils.Constants.BODY, "Scheduled at "+scheduledTime);
        notificationIntent.putExtra(AppUtils.Constants.IS_NOTIF, true);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        mAlarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }

}