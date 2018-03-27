package com.example.sagar.myapplication;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class HomeCardAdapter extends RecyclerView.Adapter<HomeCardAdapter.ViewHolder> {

    private ArrayList<RecentDriveCard> mList;

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
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

    public HomeCardAdapter(ArrayList<RecentDriveCard> list) {
        mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        RecentDriveCard currentItem = mList.get(position);

        //Get Id of the drive to pass into the next activity for query
        String driveId = "";

        holder.txtDate.setText(currentItem.getDate().toString());
        holder.txtDuration.setText(currentItem.getDuration().toString());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), RecentDriveInfo.class);
                i.putExtra("driveId", driveId);
                view.getContext().startActivity(i);
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return mList.size();
    }
}
