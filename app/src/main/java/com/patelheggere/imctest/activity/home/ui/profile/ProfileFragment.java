package com.patelheggere.imctest.activity.home.ui.profile;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.patelheggere.imctest.R;
import com.patelheggere.imctest.adapter.EventsAdapter;
import com.patelheggere.imctest.base.BaseFragment;
import com.patelheggere.imctest.data.DBManager;
import com.patelheggere.imctest.data.DatabaseHelper;
import com.patelheggere.imctest.utils.SharedPrefsHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ProfileFragment extends BaseFragment {
    private TextView mTextViewName, mTextViewEmail, mTextViewLat, mTextViewLon;
    private View mRootView;
    private RecyclerView mRecyclerViewEvents;
    private DBManager dbManager;
    private EventsAdapter mEventsAdapter;
    private TextView mTextViewNoData;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_profile, container, false);
        initViews();
        initData();
        return mRootView;
    }

    private void initViews() {
        mTextViewName = mRootView.findViewById(R.id.textViewNameValue);
        mTextViewEmail = mRootView.findViewById(R.id.textViewEmailValue);
        mTextViewLat = mRootView.findViewById(R.id.textViewLatValue);
        mTextViewLon = mRootView.findViewById(R.id.textViewLonValue);
        mRecyclerViewEvents = mRootView.findViewById(R.id.timeRecyclerView);
        mTextViewNoData = mRootView.findViewById(R.id.no_data);
        mRecyclerViewEvents.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
    }

    private void initData() {
        mTextViewName.setText(SharedPrefsHelper.getInstance().get(DatabaseHelper.FNAME) + " " + SharedPrefsHelper.getInstance().get(DatabaseHelper.LName));
        mTextViewEmail.setText(SharedPrefsHelper.getInstance().get(DatabaseHelper.EMAIL).toString());
        mTextViewLat.setText(SharedPrefsHelper.getInstance().get(DatabaseHelper.LAT).toString());
        mTextViewLon.setText(SharedPrefsHelper.getInstance().get(DatabaseHelper.LON).toString());
        dbManager = new DBManager(mActivity);
        dbManager.open();
        List<String> mEventsList = new ArrayList<>();
        Cursor cursor = dbManager.fetchAllSchedules(SharedPrefsHelper.getInstance().get(DatabaseHelper.EMAIL).toString());
        if (cursor.getCount() > 0) {
            do {
                mEventsList.add(cursor.getString(cursor.getColumnIndex(DatabaseHelper.TIME)));
            } while (cursor.moveToNext());
            Collections.sort(mEventsList);
            mEventsAdapter = new EventsAdapter(mActivity, mEventsList);
            mRecyclerViewEvents.setAdapter(mEventsAdapter);
            mRecyclerViewEvents.setVisibility(View.VISIBLE);
            mTextViewNoData.setVisibility(View.GONE);
        } else {
            mRecyclerViewEvents.setVisibility(View.GONE);
            mTextViewNoData.setVisibility(View.VISIBLE);
        }
    }
}