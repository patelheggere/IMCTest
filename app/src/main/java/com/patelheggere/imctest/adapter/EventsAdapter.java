package com.patelheggere.imctest.adapter;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.net.Uri;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.patelheggere.imctest.R;

import java.text.DecimalFormat;
import java.util.List;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.MyViewHolder> {
    private static final String TAG = "EventsAdapter";
    private Context context;
    private boolean isClicked = false;
    private long nbId=0;
    private DecimalFormat formatter;
    private List<String> dataModelList;
    private boolean playWhenReady = false;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    private String type;

    public EventsAdapter(Context context, List<String> dataList) {
        this.context = context;
        this.dataModelList = dataList;

    }
    

    @Override
    public int getItemCount() {
        return dataModelList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView mTextViewTitle;
        private CardView cardView;

        MyViewHolder(View view) {
            super(view);

            mTextViewTitle = view.findViewById(R.id.time);
            cardView = view.findViewById(R.id.cardMain);
        }
    }
    
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.events_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        try {
            final String dataModel = dataModelList.get(position);
            CharSequence time = DateUtils.getRelativeTimeSpanString(Long.parseLong(dataModel), System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
            holder.mTextViewTitle.setText("Event Scheduled "+time);
            setAnimation(holder.cardView);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void setAnimation(View view){
        try{
            ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(view, "scaleX",0.9f,1.0f);
            ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(view, "scaleY",0.9f,1.0f);
            scaleDownX.setDuration(500);
            scaleDownY.setDuration(500);
            scaleDownX.setInterpolator(new DecelerateInterpolator());
            scaleDownY.setInterpolator(new DecelerateInterpolator());
            AnimatorSet scaleDown = new AnimatorSet();
            scaleDown.play(scaleDownX).with(scaleDownY);
            scaleDown.start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}


