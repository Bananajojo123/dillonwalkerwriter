package com.writer.dillon;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.push.DeviceRegistrationResult;

import java.util.ArrayList;
import java.util.List;

public class UserSettings extends AppCompatActivity {
    Context context;
    Switch switchNotification;
    Button resetPass;
    Button logout;
    Button delaccount;


    // Report Problem
    EditText reportProblemEmail;
    EditText reportProblemMessage;
    Button reportProblemSubmit;
    Button reportProblemShow;
    Boolean hidden = true;

    // Auto login
    SharedPreferences pref; // 0 - for private mode
    SharedPreferences.Editor editor;
    Switch autologin;

    // Book testing
    BackendlessUser user;
    private String TAG = this.getClass().getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        pref = getApplicationContext().getSharedPreferences("login", 0);
        editor = pref.edit();


        final BackendlessUser currentUser = Backendless.UserService.CurrentUser();
        context = getApplicationContext();
        switchNotification = findViewById(R.id.notification_switch);
        logout = findViewById(R.id.button_logout);
        resetPass = findViewById(R.id.reset_pass);
        delaccount = findViewById(R.id.button_delete_account);
        Boolean notificationsEnabled = (Boolean) currentUser.getProperty("notifications");
        //Backendless.initApp(context, getString(R.string.backendless_app_id), getString(R.string.backendless_android_api_key));

        switchNotification.setChecked(notificationsEnabled);

        // Report Problems
        reportProblemEmail = findViewById(R.id.report_email);
        reportProblemMessage = findViewById(R.id.report_message);
        reportProblemSubmit = findViewById(R.id.send_report_email_button);
        reportProblemShow = findViewById(R.id.report_problem);

        reportProblemEmail.setVisibility(View.GONE);
        reportProblemMessage.setVisibility(View.GONE);
        reportProblemSubmit.setVisibility(View.GONE);
        reportProblemEmail.setText(currentUser.getEmail());

        delaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDelete();
            }

        });

        // Auto Login
        autologin = findViewById(R.id.auto_login_switch);
        autologin.setChecked((pref.getBoolean("enabled", true)));

        resetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

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

                            Log.i("UserSettings", fault.toString());
                            Toast.makeText(view.getContext(), getString(R.string.backendless_error) + " Error: " + fault.toString(), Toast.LENGTH_LONG).show();
                        }
                    });
                }

                else {
                    Toast.makeText(context, getString(R.string.cantresetgoogleaccount), Toast.LENGTH_LONG).show();
                }
            }
        });

        autologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pref.getBoolean("enabled", false)){
                    editor.putBoolean("enabled", false);
                    editor.apply();
                    autologin.setChecked(false);
                    Toast.makeText(getApplicationContext(), getString(R.string.auto_login_disabled), Toast.LENGTH_SHORT).show();
               }
                else{
                    editor.putBoolean("enabled", true);
                    editor.apply();
                    autologin.setChecked(true);
                    Toast.makeText(getApplicationContext(), getString(R.string.auto_login_enabled), Toast.LENGTH_SHORT).show();
                }
            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Backendless.UserService.logout(new AsyncCallback<Void>() {
                    @Override
                    public void handleResponse(Void response) {
                        editor.putBoolean("enabled", true);
                        editor.putString("email", null);
                        editor.putString("password", null);
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
                if((Boolean) currentUser.getProperty("notifications")){
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

        reportProblemShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(hidden) {
                    reportProblemEmail.setVisibility(View.VISIBLE);
                    reportProblemMessage.setVisibility(View.VISIBLE);
                    reportProblemSubmit.setVisibility(View.VISIBLE);
                    hidden = false;
                }
                else {
                    reportProblemEmail.setVisibility(View.GONE);
                    reportProblemMessage.setVisibility(View.GONE);
                    reportProblemSubmit.setVisibility(View.GONE);
                    hidden = true;
                }
            }
        });

        reportProblemSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","aaron.santacruz03@gmail.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "App Error From " + currentUser.getEmail());
                emailIntent.putExtra(Intent.EXTRA_TEXT, reportProblemMessage.getText());
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });

    }


    public void confirmDelete() {
        DialogFragment newFragment = new DeleteAccountFragment();
        newFragment.show(getSupportFragmentManager(), "del account");
    }


}
