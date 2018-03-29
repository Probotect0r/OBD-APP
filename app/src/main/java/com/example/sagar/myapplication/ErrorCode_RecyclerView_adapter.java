package com.example.sagar.myapplication;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by achal on 2018-03-29.
 */

public class ErrorCode_RecyclerView_adapter extends RecyclerView.Adapter<ErrorCode_RecyclerView_adapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView errorCode;
        public TextView errorDesc;
        public View view;

        public ViewHolder(View v) {
            super(v);
            view = v;
            errorCode = v.findViewById(R.id.txtErrorCode);
            errorDesc = v.findViewById(R.id.txtErrorDescription);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.error_card_layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.errorCode.setText("Error: " + "1234");
        holder.errorDesc.setText("Description: " + "Dummy Error");
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
