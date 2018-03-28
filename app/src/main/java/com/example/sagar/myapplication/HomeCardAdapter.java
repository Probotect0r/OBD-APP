package com.example.sagar.myapplication;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class HomeCardAdapter extends RecyclerView.Adapter<HomeCardAdapter.ViewHolder> {

    private List<Drive> recentDrives;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtDate;
        public TextView txtDuration;
        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            txtDate = (TextView) v.findViewById(R.id.date);
            txtDuration = (TextView) v.findViewById(R.id.duration);
        }
    }

    public HomeCardAdapter(List<Drive> list) {
        recentDrives = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Drive currentItem = recentDrives.get(position);

        String driveId = currentItem.getId();
        Date start = currentItem.getStart();
        Date end = currentItem.getEnd();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM dd, yyyy");
        String dateString = simpleDateFormat.format(start);

        holder.txtDate.setText(dateString);
        holder.txtDuration.setText("30 seconds");

        holder.itemView.setOnClickListener(view -> {
            Intent i = new Intent(view.getContext(), RecentDriveInfo.class);
            i.putExtra("driveId", driveId);
            view.getContext().startActivity(i);
        });
    }
    
    @Override
    public int getItemCount() {
        return recentDrives.size();
    }
}
