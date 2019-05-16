package com.writer.dillon;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.push.DeviceRegistrationResult;

import java.util.ArrayList;
import java.util.List;

public class Settings extends AppCompatActivity {
    Context context;
    Switch switchNotification;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        context = getApplicationContext();
        switchNotification = findViewById(R.id.notification_switch);

        final BackendlessUser currentUser = Backendless.UserService.CurrentUser();
        Boolean notificationsEnabled = (Boolean) currentUser.getProperty("notifications");

        if(notificationsEnabled){
            switchNotification.setChecked(true);
        }
        else {
            switchNotification.setChecked(false);
        }

        switchNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> channels = new ArrayList<String>();
                channels.add( "default" );
                if(!switchNotification.isChecked()){
                    Backendless.Messaging.unregisterDevice();
                    Toast.makeText( context, "Notifications Off!",
                                Toast.LENGTH_LONG).show();
                    currentUser.setProperty("notifications", false);
                }
                else {
                    Backendless.Messaging.registerDevice(channels, new AsyncCallback<DeviceRegistrationResult>() {
                        @Override
                        public void handleResponse(DeviceRegistrationResult response) {
                        Toast.makeText( context, "Notifications On!",
                                Toast.LENGTH_LONG).show();
                            currentUser.setProperty("notifications", true);



                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                        Toast.makeText( context, "Error registering " + fault.getMessage(),
                                Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }



}
