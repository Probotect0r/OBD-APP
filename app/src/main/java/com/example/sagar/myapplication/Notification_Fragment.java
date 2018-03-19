package com.example.sagar.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class Notification_Fragment extends android.support.v4.app.Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notification_, container, false);
        EditText emailField = view.findViewById(R.id.email);
        EditText passField = view.findViewById(R.id.password);
        Button btn = view.findViewById(R.id.login);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailField.getText().toString();
                String pass = passField.getText().toString();
                Log.d("Notifications", "onClick: " + email);
                Log.d("Notifications", "onClick: " + pass);
                //TODO: Send email/pass to backend
                //TODO: Athenticate
                //TODO: Get and display all notifications (historical data)
            }
        });
        return view;
    }

}
