package com.writer.dillon;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.push.DeviceRegistrationResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;


public class LoginActivity extends Activity implements GoogleApiClient.OnConnectionFailedListener {
    private static final int RC_SIGN_IN = 007;
    private EditText email;
    private EditText upassword;
    private EditText upasswordConfirm;
    private EditText uname;
    private Button signUpButton;
    private Button loginButton;
    private TextView signUpText;
    private String screen = "Login";
    private String TAG = this.getClass().getSimpleName();
    private GoogleSignInClient mGoogleSignInClient;
    private SignInButton signinbutton;
    Context context;
    private boolean signingInWithGoogle = false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Backendless.initApp(this, getString(R.string.backendless_app_id), getString(R.string.backendless_android_api_key));
        context = getApplicationContext();

        setContentView(R.layout.activity_login);
        email = findViewById(R.id.enter_email);
        upassword = findViewById(R.id.enter_password);
        upasswordConfirm = findViewById(R.id.re_enter_password);
        uname = findViewById(R.id.enter_name);
        signUpButton = findViewById(R.id.signup_button);
        loginButton = findViewById(R.id.login_button);
        signUpText = findViewById(R.id.sign_up_text);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        signinbutton = findViewById(R.id.gplus_login);

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(screen.equals("Login")) {
                    uname.setVisibility(View.VISIBLE);
                    signUpButton.setVisibility(View.VISIBLE);
                    loginButton.setVisibility(View.GONE);
                    upasswordConfirm.setVisibility(View.VISIBLE);
                    screen = "Sign Up";
                    signUpText.setText("Cancel Sign Up");
                }
                else{
                    uname.setVisibility(View.GONE);
                    signUpButton.setVisibility(View.GONE);
                    upasswordConfirm.setVisibility(View.GONE);
                    loginButton.setVisibility(View.VISIBLE);
                    screen = "Login";
                    signUpText.setText("Sign Up!");
                }
            }
        });
        signinbutton.setOnClickListener(new SignUpWithGoogleClickListener());
        signUpButton.setOnClickListener(new SignUpOnClickListener());
        loginButton.setOnClickListener(new LoginOnClickListener());



    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private class SignUpOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View V){
            signUp(email.getText().toString(), uname.getText().toString(),upassword.getText().toString(),upasswordConfirm.getText().toString(), "");
        }
    }
    private class LoginOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View V){
            String emailaddress = email.getText().toString().trim();
            String password = upassword.getText().toString().trim();
            login(emailaddress,password);
        }}
    private class SignUpWithGoogleClickListener implements View.OnClickListener  {
        @Override
        public void onClick(View V){
            String message = "logging in...";
            signIn();
            Log.i(TAG, message);
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    private void updateUI(GoogleSignInAccount signedIn) {

    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            signUp(account.getEmail(),account.getGivenName(),account.getId(),account.getId(),account.getPhotoUrl().toString());
            signingInWithGoogle = true;
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
            warnUser(getString(R.string.sign_in_with_google_error_title), getString(R.string.sign_in_with_google_error));
        }


    }

    public String signUp(String uemail, String uname, String upassword, String uconfirmPassword, String photourl){
        String message = "";
        final String emailaddress = uemail;
        String name = uname;
        final String password = upassword;
        String confirmPassword = uconfirmPassword;
        final ProgressDialog pDialogSignup = ProgressDialog.show(LoginActivity.this,
                getString(R.string.progress_title),
                getString(R.string.progress_sign_up),
                true);

        if((emailaddress.isEmpty() && name.isEmpty() && password.isEmpty() && confirmPassword.isEmpty())){
            message = "Please fill out all fields";
            pDialogSignup.dismiss();

            warnUser(getString(R.string.empty_field_signup_error), getString(R.string.empty_field_signup_error));
        }
        else if(!password.equals(confirmPassword)){
            message = "Passwords don't match";
            pDialogSignup.dismiss();
            warnUser(getString(R.string.password_general_error_title), getString(R.string.password_matching_error));
        }
        else if(password.equals(emailaddress)){
            pDialogSignup.dismiss();
            warnUser(getString(R.string.password_general_error_title), getString(R.string.password_same_as_email));
        }
        else if(password.length() < 6){
            pDialogSignup.dismiss();
            warnUser(getString(R.string.password_general_error_title), getString(R.string.password_length_too_short));
        }
        else if(!(emailaddress.contains("@") && emailaddress.contains("."))){
            pDialogSignup.dismiss();
            warnUser(getString(R.string.password_general_error_title), getString(R.string.password_not_contain_requirements));

        }
        else{
            pDialogSignup.dismiss();
            BackendlessUser user = new BackendlessUser();
            user.setEmail(emailaddress);
            user.setPassword(password);
            user.setProperty("name", name);
            user.setProperty("profileurl", photourl);

            Backendless.UserService.register(user, new AsyncCallback<BackendlessUser>() {
                @Override
                public void handleResponse(BackendlessUser response) {
                    pDialogSignup.dismiss();
                    login(emailaddress, password);

                }

                @Override
                public void handleFault(BackendlessFault fault) {
                    if(fault.getCode().equals("3033") && signingInWithGoogle){
                        login(emailaddress,password);
                        Log.i(TAG, "Logged In With Google!");

                    }
                    else if(fault.getCode().equals("3033")){
                        Log.i(TAG, fault.toString());
                        warnUser(getString(R.string.backendless_user_exists_error_title), getString(R.string.backendless_user_exists_error));
                        pDialogSignup.dismiss();

                    }
                    else{
                        warnUser(getString(R.string.backendless_error_title), getString(R.string.backendless_error));
                        Log.i(TAG, fault.toString());
                        pDialogSignup.dismiss();
                    }

                    Log.i(TAG, fault.toString());


                }
            });
        }
        return(message);
    }

    public String login(final String uemail, final String upassword){
        String message = "";
        String emailaddress = uemail;
        String password = upassword;
        final ProgressDialog pDialogLogin = ProgressDialog.show(LoginActivity.this,
                getString(R.string.progress_title),
                getString(R.string.progress_logging_in),
                true);

        Backendless.UserService.login(emailaddress, password, new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser response) {
                List<String> channels = new ArrayList<String>();
                channels.add( "default" );
                Backendless.Messaging.registerDevice(channels, new AsyncCallback<DeviceRegistrationResult>() {
                    @Override
                    public void handleResponse(DeviceRegistrationResult response) {
//                        Toast.makeText( context, "Device registered!",
//                                Toast.LENGTH_LONG).show();



                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
//                        Toast.makeText( context, "Error registering " + fault.getMessage(),
//                                Toast.LENGTH_LONG).show();
                    }
                });
                pDialogLogin.dismiss();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                pDialogLogin.dismiss();
                if(fault.getCode().equals("3087")){
                    warnUser(getString(R.string.backendless_verify), getString(R.string.backendless_verify_your_email));
                }
                else {
                    warnUser(getString(R.string.backendless_error_title), getString(R.string.backendless_error));
                    Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        return(message);
    }
    private void warnUser(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setMessage(message);
        builder.setTitle(title);
        builder.setPositiveButton(android.R.string.ok, null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}


