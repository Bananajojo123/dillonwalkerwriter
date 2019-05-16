package com.writer.dillon;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
    Button resetPass;
    Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        context = getApplicationContext();
        switchNotification = findViewById(R.id.notification_switch);
        logout = findViewById(R.id.button_logout);
        resetPass = findViewById(R.id.reset_pass);
        final BackendlessUser currentUser = Backendless.UserService.CurrentUser();
        Boolean notificationsEnabled = (Boolean) currentUser.getProperty("notifications");
        //Backendless.initApp(context, getString(R.string.backendless_app_id), getString(R.string.backendless_android_api_key));
        if(notificationsEnabled){
            switchNotification.setChecked(true);
        }
        else {
            switchNotification.setChecked(false);
        }

        resetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                BackendlessUser currentUser = Backendless.UserService.CurrentUser();
                String id = currentUser.getEmail();
                Boolean signingoogle = (Boolean) currentUser.getProperty("googlesignin");
                if(!signingoogle) {

                    Backendless.UserService.restorePassword(id, new AsyncCallback<Void>() {
                        public void handleResponse(Void response) {
                            Toast.makeText(view.getContext(), "Email Sent!", Toast.LENGTH_LONG).show();
                            Intent i2 = new Intent(view.getContext(), LoginActivity.class);
                            startActivity(i2);
                        }

                        public void handleFault(BackendlessFault fault) {

                            Log.i("Settings", fault.toString());
                            Toast.makeText(view.getContext(), getString(R.string.backendless_error) + " Error: " + fault.toString(), Toast.LENGTH_LONG).show();
                        }
                    });
                }

                else {
                    Toast.makeText(context, getString(R.string.cantresetgoogleaccount), Toast.LENGTH_LONG).show();
                }
            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Backendless.UserService.logout(new AsyncCallback<Void>() {
                    @Override
                    public void handleResponse(Void response) {
                        Toast.makeText( context, getString(R.string.logged_out),
                                Toast.LENGTH_LONG).show();
                        Intent i = new Intent(context, LoginActivity.class);
                        startActivity(i);
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Toast.makeText( context, getString(R.string.logged_out_error),
                                Toast.LENGTH_LONG).show();
                    }
                });


            }
        });

        switchNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> channels = new ArrayList<String>();
                channels.add( "default" );
                if(!switchNotification.isChecked()){
                    Backendless.Messaging.unregisterDevice();
                    Toast.makeText( context, "Notifications Off!",
                                Toast.LENGTH_SHORT).show();
                    currentUser.setProperty("notifications", false);
                }
                else {
                    Backendless.Messaging.registerDevice(channels, new AsyncCallback<DeviceRegistrationResult>() {
                        @Override
                        public void handleResponse(DeviceRegistrationResult response) {
                        Toast.makeText( context, "Notifications On!",
                                Toast.LENGTH_SHORT).show();
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
