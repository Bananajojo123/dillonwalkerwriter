package com.writer.dillon;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Switch;

public class Settings extends AppCompatActivity {

    Switch switchNotification;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        switchNotification = findViewById(R.id.notification_switch);


        
    }
}
