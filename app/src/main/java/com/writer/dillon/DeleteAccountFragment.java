package com.writer.dillon;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.util.Log;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.IDataStore;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeleteAccountFragment extends DialogFragment {
    Context context;
    private String TAG = this.getClass().getSimpleName();
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.delete_account_message)
                .setPositiveButton(R.string.delete_account, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        BackendlessUser user = Backendless.UserService.CurrentUser();

                        final IDataStore<BackendlessUser> dataStore = Backendless.Data.of( BackendlessUser.class );
                        dataStore.findById(user.getObjectId(), new AsyncCallback<BackendlessUser>()
                        {
                            @Override
                            public void handleResponse( BackendlessUser backendlessUser )
                            {
                                dataStore.remove( backendlessUser, new AsyncCallback<Long>()
                                {
                                    @Override
                                    public void handleResponse( Long aLong )
                                    {

                                    }

                                    @Override
                                    public void handleFault( BackendlessFault backendlessFault )
                                    {
                                    }
                                } );
                            }

                            @Override
                            public void handleFault( BackendlessFault backendlessFault )
                            {
                                System.out.println( "Server reported an error " + backendlessFault.getMessage() );
                            }
                        } );
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i(TAG, "cancel");
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
