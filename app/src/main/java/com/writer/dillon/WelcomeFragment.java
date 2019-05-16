package com.writer.dillon;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;

import java.util.Calendar;


public class WelcomeFragment extends Fragment  {

    private TextView tvWelcomeMessage;
    private String TAG = this.getClass().getSimpleName();

    public WelcomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View  view = inflater.inflate(R.layout.fragment_welcome, container, false);
        tvWelcomeMessage = view.findViewById(R.id.tv_welcome_message);



        if (Backendless.UserService.CurrentUser() != null) {
            BackendlessUser currentUser = Backendless.UserService.CurrentUser();
            String name = currentUser.getProperty("name").toString();


            Calendar rightNow = Calendar.getInstance();
            int time24 = rightNow.get(Calendar.HOUR_OF_DAY); // return the hour in 24 hrs format (ranging from 0-23)


            if (time24 < 12) {
                tvWelcomeMessage.setText("Good Morning, " + name + ". ");
            } else if (time24 >= 12 && time24 < 16) {
                tvWelcomeMessage.setText("Good Afternoon, " + name + ". ");
            } else {
                tvWelcomeMessage.setText("Good Evening, " + name + ". ");
            }


        }
        return view;

    }




}
